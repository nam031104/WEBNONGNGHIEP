// devices.js – Logic điều khiển Actor
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
        const nodes = await fetchDevices();
        const selectEl = document.getElementById('node-select');
        if (!selectEl) return;

        if (nodes.length === 0) {
            selectEl.innerHTML = '<option value="">Không có thiết bị nào</option>';
            return;
        }

        selectEl.innerHTML = '<option value="">-- Chọn thiết bị --</option>' + 
            nodes.map(n => `<option value="${n.idDevice}">${n.name} (${n.idDevice})</option>`).join('');

        if (nodes.length > 0) {
            selectEl.value = nodes[0].idDevice;
            selectedNodeId = nodes[0].idDevice;
            applyInitialNodeState();
        }
    } catch (err) {
        console.warn('Lỗi tải danh sách node:', err.message);
    }
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
    const grid = document.getElementById('devices-control-grid');
    if (grid) grid.innerHTML = '<p style="color:#aaa; font-style:italic">Đang tải...</p>';
    
    try {
        const nodeStatus = await fetchDeviceStatus(selectedNodeId);
        if (grid) grid.innerHTML = '';
        applyPolledNodeState(nodeStatus);
    } catch (err) {}
}

function applyPolledNodeState(nodeStatus) {
    const actors = nodeStatus.actors || [];
    const grid = document.getElementById('devices-control-grid');
    if (!grid) return;

    if (actors.length === 0) {
        grid.innerHTML = '<p style="color:#aaa">Thiết bị này không có bộ điều khiển.</p>';
        return;
    }

    actors.forEach(actor => {
        let card = document.getElementById(`actor-card-${actor.idActor}`);
        if (!card) {
            const cardHtml = `
                <div class="device-control-card glass" id="actor-card-${actor.idActor}">
                    <i class="ph ph-cpu icon-large"></i>
                    <h3>${actor.typeActor.toUpperCase()}</h3>
                    <p style="font-size:0.75rem; color:var(--accent-blue); margin-bottom: 20px;">ID: ${actor.idActor}</p>
                    
                    <div class="control-row">
                        <span>Tự động (AUTO)</span>
                        <label class="switch">
                            <input type="checkbox" id="mode-toggle-${actor.idActor}" onchange="sendActorParams('${actor.idActor}', this.checked ? 1 : 0, null)">
                            <span class="slider round"></span>
                        </label>
                    </div>
                    
                    <div class="control-row">
                        <span id="status-text-${actor.idActor}">${actor.status === 1 ? 'BẬT' : 'TẮT'}</span>
                        <label class="switch">
                            <input type="checkbox" id="status-toggle-${actor.idActor}" onchange="sendActorParams('${actor.idActor}', null, this.checked ? 1 : 0)">
                            <span class="slider round"></span>
                        </label>
                    </div>
                </div>`;
            grid.insertAdjacentHTML('beforeend', cardHtml);
            card = document.getElementById(`actor-card-${actor.idActor}`);
        }

        const modeTgl = document.getElementById(`mode-toggle-${actor.idActor}`);
        const statusTgl = document.getElementById(`status-toggle-${actor.idActor}`);
        const text = document.getElementById(`status-text-${actor.idActor}`);

        if (modeTgl) modeTgl.checked = (actor.mode === 1);
        if (statusTgl) {
            statusTgl.checked = (actor.status === 1);
            statusTgl.disabled = (actor.mode === 1); 
        }
        if (text) text.innerText = (actor.status === 1) ? 'BẬT' : 'TẮT';
        
        if (actor.status === 1) card.classList.add('active');
        else card.classList.remove('active');
    });
}

async function sendActorParams(idActor, modeVal, statusVal) {
    if (!selectedNodeId) return;

    const modeTgl = document.getElementById(`mode-toggle-${idActor}`);
    const statusTgl = document.getElementById(`status-toggle-${idActor}`);

    const payload = {
        idDevice: selectedNodeId,
        idActor: idActor,
        mode: modeVal !== null ? modeVal : (modeTgl.checked ? 1 : 0),
        status: statusVal !== null ? statusVal : (statusTgl.checked ? 1 : 0)
    };

    try {
        await sendControlCommand(payload);
        if (statusTgl && modeVal !== null) {
            statusTgl.disabled = (modeVal === 1);
        }
    } catch (err) {
        alert("Lỗi điều khiển: " + err.message);
    }
}
