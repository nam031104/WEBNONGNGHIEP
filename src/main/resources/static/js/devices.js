// devices.js – Dynamic Actor Control Logic

let allNodes = [];
let selectedNodeId = null;
let uiPollInterval = null;

async function initDevices() {
    await loadNodes();

    const selectEl = document.getElementById('node-select');
    if (selectEl) {
        selectEl.addEventListener('change', (e) => {
            selectedNodeId = e.target.value || null;
            applyInitialNodeState();
        });
    }

    if (uiPollInterval) clearInterval(uiPollInterval);
    uiPollInterval = setInterval(pollNodeStatus, 3000);
}

async function loadNodes() {
    try {
        allNodes = await fetchDevices();
        const selectEl = document.getElementById('node-select');
        const modalSelectEl = document.getElementById('modal-device-select');
        
        if (!selectEl) return;

        if (allNodes.length === 0) {
            selectEl.innerHTML = '<option value="">Không có Node nào</option>';
            if(modalSelectEl) modalSelectEl.innerHTML = '<option value="">Không có thiết bị</option>';
            return;
        }

        let options = '<option value="">-- Chọn Node --</option>';
        allNodes.forEach(node => {
            options += `<option value="${node.idDevice}">${node.name} (ID: ${node.idDevice})</option>`;
        });
        
        selectEl.innerHTML = options;
        if(modalSelectEl) {
            modalSelectEl.innerHTML = options;
            modalSelectEl.addEventListener('change', populateModalActors);
        }

        if (allNodes.length > 0) {
            selectEl.value = allNodes[0].idDevice;
            selectedNodeId = allNodes[0].idDevice;
            applyInitialNodeState();
        }
    } catch (err) {
        console.warn('Could not load nodes:', err.message);
    }
}

async function populateModalActors() {
    const modalSelectEl = document.getElementById('modal-device-select');
    const actorSelectEl = document.getElementById('modal-actor-select');
    actorSelectEl.innerHTML = '<option value="">-- Chọn Actor --</option>';
    
    if(!modalSelectEl.value) return;
    
    try {
        const nodeStatus = await fetchDeviceStatus(modalSelectEl.value);
        if(nodeStatus && nodeStatus.actors) {
            nodeStatus.actors.forEach(actor => {
                actorSelectEl.innerHTML += `<option value="${actor.idActor}">${actor.typeActor.toUpperCase()} - ${actor.idActor}</option>`;
            });
        }
    } catch(e) {}
}

async function pollNodeStatus() {
    if (!selectedNodeId) return;
    try {
        const nodeStatus = await fetchDeviceStatus(selectedNodeId);
        applyPolledNodeState(nodeStatus);
    } catch (err) {}
}

async function applyInitialNodeState() {
    if (!selectedNodeId) return;
    try {
        document.getElementById('devices-control-grid').innerHTML = '<p style="color:#aaa; font-style:italic">Đang tải cấu kiện...</p>';
        const nodeStatus = await fetchDeviceStatus(selectedNodeId);
        document.getElementById('devices-control-grid').innerHTML = ''; // Xóa chữ loading
        applyPolledNodeState(nodeStatus);
    } catch (err) {}
}

// Map icons
const getIconForType = (type) => {
    if(!type) return 'ph-cpu';
    const t = type.toLowerCase();
    if(t.includes('fan')) return 'ph-fan';
    if(t.includes('light') || t.includes('led')) return 'ph-lightbulb';
    if(t.includes('pump')) return 'ph-waves';
    return 'ph-cpu';
};

function applyPolledNodeState(nodeStatus) {
    const actors = nodeStatus.actors || [];
    const grid = document.getElementById('devices-control-grid');
    
    if(actors.length === 0) {
        grid.innerHTML = '<p style="color:#aaa">Thiết bị này chưa báo cáo Actor nào về máy chủ.</p>';
        return;
    }

    actors.forEach(actor => {
        let card = document.getElementById(`actor-card-${actor.idActor}`);
        
        if (!card) {
            // Thêm card mới nếu chưa có
            const cardHtml = `
                <div class="device-control-card glass" id="actor-card-${actor.idActor}">
                    <i class="ph ${getIconForType(actor.typeActor)} icon-large"></i>
                    <h3 style="margin-bottom: 2px;">${actor.typeActor ? actor.typeActor.toUpperCase() : 'Actor'}</h3>
                    <p style="font-size:0.75rem; color:var(--primary); margin-bottom: 20px;">ID: ${actor.idActor}</p>
                    
                    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 12px; background: rgba(0,0,0,0.1); padding: 8px 12px; border-radius: 8px;">
                        <span style="font-size: 0.85rem;">Chế độ (AUTO)</span>
                        <label class="switch">
                            <input type="checkbox" id="mode-toggle-${actor.idActor}" onchange="sendActorParams('${actor.idActor}', this.checked ? 1 : 0, null)">
                            <span class="slider round"></span>
                        </label>
                    </div>
                    
                    <div style="display:flex; justify-content:space-between; align-items:center;">
                        <p class="status-indicator"><span id="dot-${actor.idActor}" class="dot off"></span> <span id="status-text-${actor.idActor}">OFF</span></p>
                        <label class="switch">
                            <input type="checkbox" id="status-toggle-${actor.idActor}" onchange="sendActorParams('${actor.idActor}', null, this.checked ? 1 : 0)">
                            <span class="slider round"></span>
                        </label>
                    </div>
                </div>
            `;
            grid.insertAdjacentHTML('beforeend', cardHtml);
            card = document.getElementById(`actor-card-${actor.idActor}`);
        }

        // Cập nhật DOM element nếu card đã tồn tại
        const modeToggle = document.getElementById(`mode-toggle-${actor.idActor}`);
        const statusToggle = document.getElementById(`status-toggle-${actor.idActor}`);
        const dot = document.getElementById(`dot-${actor.idActor}`);
        const text = document.getElementById(`status-text-${actor.idActor}`);

        // Update dữ liệu mà không làm nháy trang
        if (modeToggle) modeToggle.checked = (actor.mode === 1);
        if (statusToggle) {
            statusToggle.checked = (actor.status === 1);
            // Disabled trạng thái nếu đang Auto
            statusToggle.disabled = (actor.mode === 1); 
        }

        if (text) text.innerText = (actor.status === 1) ? 'ON' : 'OFF';
        if (dot) {
            if (actor.status === 1) {
                dot.className = 'dot on';
                card.classList.add('active');
            } else {
                dot.className = 'dot off';
                card.classList.remove('active');
            }
        }
    });
}

async function sendActorParams(idActor, modeVal, statusVal) {
    if (!selectedNodeId) return;

    // Lấy state hiện thời trên UI để truyền xuống cho đủ
    let currentMode = modeVal;
    let currentStatus = statusVal;

    const modeTgl = document.getElementById(`mode-toggle-${idActor}`);
    const statusTgl = document.getElementById(`status-toggle-${idActor}`);

    if (currentMode === null && modeTgl) currentMode = modeTgl.checked ? 1 : 0;
    if (currentStatus === null && statusTgl) currentStatus = statusTgl.checked ? 1 : 0;

    const payload = {
        idDevice: selectedNodeId,
        idActor: idActor,
        mode: currentMode,
        status: currentStatus
    };

    try {
        await sendControlCommand(payload);
        console.log("MQTT Sent Specific Actor State:", payload);
        
        // Disable luôn statusTgl nếu Mode là Auto
        if (statusTgl) statusTgl.disabled = (currentMode === 1);
    } catch (err) {
        console.error("MQTT Specific Actor State send failed:", err.message);
    }
}
