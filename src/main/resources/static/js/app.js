document.addEventListener('DOMContentLoaded', () => {
    if (typeof initNavigation === 'function') initNavigation();
    if (typeof initDevices === 'function') initDevices();

    console.log("AgriSmart Frontend App Initialized (Control & Schedule Only)");

    // Only Schedule loop
    setInterval(() => {
        if (typeof loadSchedules === 'function' && document.getElementById('schedule-modal').style.display !== 'flex') {
            loadSchedules();
        }
    }, 3000);

    let currentEditScheduleId = null;
    let allSchedulesData = [];

    // ── Schedule Table & Modal (Part 2) ────────────────────
    async function loadSchedules() {
        try {
            const schedules = await fetchSchedules();
            allSchedulesData = schedules;
            
            let deviceMap = {};
            try {
                const devs = await fetchDevices();
                devs.forEach(d => deviceMap[d.idDevice] = d.name || d.typeDevice);
            } catch(e) {}

            const tbody = document.getElementById('schedule-tbody');
            if (!tbody) return;
            
            const getCmdText = (val, name) => val === null ? '' : (val === 1 ? `<span style="color:#4ade80">${name} Bật</span>` : `<span style="color:#f87171">${name} Tắt</span>`);

            tbody.innerHTML = schedules.length === 0
                ? '<tr><td colspan="6" style="text-align:center;opacity:.5">Chưa có lịch nào</td></tr>'
                : schedules.map(s => {
                    const devName = deviceMap[s.idDevice] ? `${deviceMap[s.idDevice]} (ID: ${s.idDevice})` : (s.idDevice || 'Tất cả thiết bị');
                    const actorName = s.idActor ? `<span style="color:#60a5fa">${s.idActor}</span>` : '-';
                    const modeTxt = s.mode === 1 ? '<span style="color:#22c55e">AUTO</span>' : '<span style="color:#f59e0b">MANUAL</span>';
                    const stTxt = s.status === 1 ? 'BẬT' : 'TẮT';
                    const cmds = `${modeTxt} - ${stTxt}`;
                    return `
                    <tr>
                        <td>${actorName}</td>
                        <td>${cmds}</td>
                        <td>${formatDate(s.date)}</td>
                        <td>${s.note || '-'}</td>
                        <td><span style="padding:4px 8px;border-radius:6px;font-size:.85rem;color:#fff;background:${s.isExecuted === 1 ? 'rgba(59,130,246,.8)' : 'rgba(156,163,175,.8)'}">${s.isExecuted === 1 ? 'Hoàn thành' : 'Chưa chạy'}</span></td>
                        <td>
                            <button onclick="editScheduleById('${s.idSchedule}')"
                                style="background:rgba(59,130,246,.15);border:1px solid rgba(59,130,246,.4);color:#60a5fa;
                                       padding:4px 10px;border-radius:6px;cursor:pointer;font-size:.75rem;margin-right:4px;">
                                Sửa
                            </button>
                            <button onclick="deleteScheduleById('${s.idSchedule}')"
                                style="background:rgba(239,68,68,.15);border:1px solid rgba(239,68,68,.4);color:#fca5a5;
                                       padding:4px 10px;border-radius:6px;cursor:pointer;font-size:.75rem">
                                Xóa
                            </button>
                        </td>
                    </tr>`;
                }).join('');
        } catch (err) {
            console.warn('Could not load schedules:', err.message);
        }
    }
    loadSchedules();

    window.deleteScheduleById = async (id) => {
        if (!confirm('Xóa lịch này?')) return;
        try {
            await deleteSchedule(id);
            loadSchedules();
        } catch (err) {
            alert('Lỗi xóa lịch: ' + err.message);
        }
    };

    // ── Add Schedule Button → open modal ───────────────────
    const addBtn = document.getElementById('add-schedule-btn');
    if (addBtn) addBtn.addEventListener('click', () => openScheduleModal());

    window.openScheduleModal = async function() {
        currentEditScheduleId = null;
        document.getElementById('modal-title').innerHTML = '&#x1F4C5; Thêm Lịch Mới';

        // Populate device select
        let deviceOptions = '<option value="">-- Chọn thiết bị --</option>';
        try {
            const devices = await fetchDevices();
            deviceOptions += devices.map(d =>
                `<option value="${d.idDevice}">${d.name || d.typeDevice} (ID: ${d.idDevice})</option>`
            ).join('');
        } catch {}

        // Set min datetime to now
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        const minDt = now.toISOString().slice(0, 16);

        const modal = document.getElementById('schedule-modal');
        document.getElementById('modal-device-select').innerHTML = deviceOptions;
        document.getElementById('modal-device-select').value = '';
        document.getElementById('modal-datetime').min = minDt;
        document.getElementById('modal-datetime').value = '';
        document.getElementById('modal-note').value = '';
        
        document.getElementById('modal-actor-select').innerHTML = '<option value="">-- Chọn thiết bị trước --</option>';

        document.getElementById('modal-mode').value = '';
        document.getElementById('modal-status').value = '';

        modal.style.display = 'flex';
    };

    window.editScheduleById = async function(id) {
        const schedule = allSchedulesData.find(s => s.idSchedule === id);
        if (!schedule) return;

        currentEditScheduleId = id;
        document.getElementById('modal-title').innerHTML = '&#x270E; Sửa Lịch (ID: ' + id.substring(0,6) + '...)';

        // Populate device select
        let deviceOptions = '<option value="">-- Chọn thiết bị --</option>';
        try {
            const devices = await fetchDevices();
            deviceOptions += devices.map(d =>
                `<option value="${d.idDevice}">${d.name || d.typeDevice} (ID: ${d.idDevice})</option>`
            ).join('');
        } catch {}
        document.getElementById('modal-device-select').innerHTML = deviceOptions;

        document.getElementById('modal-device-select').value = schedule.idDevice || '';
        document.getElementById('modal-datetime').value = schedule.date ? schedule.date.substring(0, 16) : '';
        document.getElementById('modal-note').value = schedule.note || '';
        
        // Trick to load actors for this device
        try {
            const nodeStatus = await fetchDeviceStatus(schedule.idDevice || schedule.idActor);
            if(nodeStatus && nodeStatus.actors) {
                let actOpt = '<option value="">-- Chọn Actor --</option>';
                nodeStatus.actors.forEach(actor => {
                    actOpt += `<option value="${actor.idActor}">${actor.typeActor.toUpperCase()} - ${actor.idActor}</option>`;
                });
                document.getElementById('modal-actor-select').innerHTML = actOpt;
            }
        } catch(e) {}
        
        document.getElementById('modal-actor-select').value = schedule.idActor || '';
        document.getElementById('modal-mode').value = schedule.mode === null ? '' : schedule.mode;
        document.getElementById('modal-status').value = schedule.status === null ? '' : schedule.status;

        document.getElementById('schedule-modal').style.display = 'flex';
    };

    window.closeScheduleModal = function() {
        document.getElementById('schedule-modal').style.display = 'none';
    };

    window.submitSchedule = async function() {
        const deviceId = document.getElementById('modal-device-select').value;
        const dateStr  = document.getElementById('modal-datetime').value;
        const note     = document.getElementById('modal-note').value;
        
        const actorId  = document.getElementById('modal-actor-select').value;
        const modeStr  = document.getElementById('modal-mode').value;
        const statusStr= document.getElementById('modal-status').value;

        if (!actorId) { alert('Vui lòng chọn Actor!'); return; }
        if (!dateStr) { alert('Vui lòng chọn thời gian!'); return; }

        const payload = { 
            idActor: actorId, 
            date: dateStr + ':00', 
            note: note,
            mode: modeStr === "" ? null : parseInt(modeStr),
            status: statusStr === "" ? null : parseInt(statusStr)
        };

        try {
            if (currentEditScheduleId) {
                await updateSchedule(currentEditScheduleId, payload);
            } else {
                await createSchedule(payload);
            }
            closeScheduleModal();
            loadSchedules();
        } catch (err) {
            alert('Lỗi lưu lịch: ' + err.message);
        }
    };
});

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return d.toLocaleString('vi-VN');
}
