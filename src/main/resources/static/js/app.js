document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initDevices();

    console.log("AgriSmart Ready (Control & Schedule)");

    // Tự động tải lịch mỗi 5 giây
    setInterval(() => {
        if (document.getElementById('schedule-modal').style.display !== 'flex') {
            loadSchedules();
        }
    }, 5000);

    let currentEditId = null;
    let allSchedules = [];

    async function loadSchedules() {
        try {
            allSchedules = await fetchSchedules();
            const tbody = document.getElementById('schedule-tbody');
            if (!tbody) return;

            tbody.innerHTML = allSchedules.length === 0
                ? '<tr><td colspan="6" style="text-align:center; opacity:.5">Chưa có lịch hẹn nào</td></tr>'
                : allSchedules.map(s => `
                    <tr>
                        <td>${s.idActor}</td>
                        <td>${s.mode === 1 ? 'AUTO' : 'MANUAL'} (${s.status === 1 ? 'BẬT' : 'TẮT'})</td>
                        <td>${new Date(s.date).toLocaleString('vi-VN')}</td>
                        <td>${s.note || '-'}</td>
                        <td><span class="badge ${s.isExecuted ? 'blue' : 'gray'}">${s.isExecuted ? 'Xong' : 'Chờ'}</span></td>
                        <td>
                            <button onclick="editSchedule('${s.idSchedule}')" class="btn-action edit">Sửa</button>
                            <button onclick="removeSchedule('${s.idSchedule}')" class="btn-action del">Xóa</button>
                        </td>
                    </tr>
                `).join('');
        } catch (err) {
            console.error('Lỗi tải lịch:', err);
        }
    }
    loadSchedules();

    window.removeSchedule = async (id) => {
        if (!confirm('Xóa lịch này?')) return;
        try {
            await deleteSchedule(id);
            loadSchedules();
        } catch (err) {
            alert(err.message);
        }
    };

    window.openScheduleModal = async () => {
        currentEditId = null;
        document.getElementById('modal-title').innerText = 'Thêm Lịch Mới';
        
        // Clear inputs
        document.getElementById('modal-datetime').value = '';
        document.getElementById('modal-note').value = '';
        document.getElementById('modal-mode').value = '';
        document.getElementById('modal-status').value = '';
        document.getElementById('modal-status').disabled = false;

        // Load devices for dropdown
        try {
            const devices = await fetchDevices();
            const devSelect = document.getElementById('modal-device-select');
            devSelect.innerHTML = '<option value="">-- Chọn thiết bị --</option>' + 
                devices.map(d => `<option value="${d.idDevice}">${d.name}</option>`).join('');
            
            document.getElementById('modal-actor-select').innerHTML = '<option value="">-- Chọn Actor --</option>';
            devSelect.onchange = async (e) => {
                const status = await fetchDeviceStatus(e.target.value);
                document.getElementById('modal-actor-select').innerHTML = '<option value="">-- Chọn Actor --</option>' + 
                    status.actors.map(a => `<option value="${a.idActor}">${a.typeActor} (${a.idActor})</option>`).join('');
            };
        } catch (e) {}

        document.getElementById('schedule-modal').style.display = 'flex';
    };

    window.closeScheduleModal = () => {
        document.getElementById('schedule-modal').style.display = 'none';
    };

    window.editSchedule = async (id) => {
        const s = allSchedules.find(x => x.idSchedule === id);
        if (!s) return;
        currentEditId = id;
        document.getElementById('modal-title').innerText = 'Sửa Lịch Hẹn';
        
        // Fill base info
        document.getElementById('modal-datetime').value = s.date.substring(0, 16);
        document.getElementById('modal-note').value = s.note || '';
        document.getElementById('modal-mode').value = s.mode;
        document.getElementById('modal-status').value = s.status !== null ? s.status : '';
        document.getElementById('modal-status').disabled = (s.mode === 1);

        // Load devices and set selected
        try {
            const devices = await fetchDevices();
            const devSelect = document.getElementById('modal-device-select');
            
            // Find which device this actor belongs to
            // Note: This requires knowing the deviceId from the actor or searching all devices
            // Since s (ScheduleDto) only has idActor, we have to find the device
            let parentDeviceId = "";
            for (const d of devices) {
                const status = await fetchDeviceStatus(d.idDevice);
                if (status.actors.some(a => a.idActor === s.idActor)) {
                    parentDeviceId = d.idDevice;
                    break;
                }
            }

            devSelect.innerHTML = '<option value="">-- Chọn thiết bị --</option>' + 
                devices.map(d => `<option value="${d.idDevice}" ${d.idDevice === parentDeviceId ? 'selected' : ''}>${d.name}</option>`).join('');
            
            const actorSelect = document.getElementById('modal-actor-select');
            if (parentDeviceId) {
                const status = await fetchDeviceStatus(parentDeviceId);
                actorSelect.innerHTML = '<option value="">-- Chọn Actor --</option>' + 
                    status.actors.map(a => `<option value="${a.idActor}" ${a.idActor === s.idActor ? 'selected' : ''}>${a.typeActor} (${a.idActor})</option>`).join('');
            }
            
            devSelect.onchange = async (e) => {
                const status = await fetchDeviceStatus(e.target.value);
                document.getElementById('modal-actor-select').innerHTML = '<option value="">-- Chọn Actor --</option>' + 
                    status.actors.map(a => `<option value="${a.idActor}">${a.typeActor} (${a.idActor})</option>`).join('');
            };
        } catch (e) {}

        document.getElementById('schedule-modal').style.display = 'flex';
    };

    window.submitSchedule = async () => {
        const actorId = document.getElementById('modal-actor-select').value;
        const dateVal = document.getElementById('modal-datetime').value;
        const modeVal = document.getElementById('modal-mode').value;
        const statusVal = document.getElementById('modal-status').value;

        if (!actorId || !dateVal || modeVal === "") {
            alert("Vui lòng chọn Actor, Thời gian và Chế độ!");
            return;
        }

        const mode = parseInt(modeVal);
        let status = parseInt(statusVal);

        if (mode === 0 && isNaN(status)) {
            alert("Vui lòng chọn trạng thái (Bật/Tắt) cho chế độ MANUAL!");
            return;
        }

        const payload = {
            idActor: actorId,
            date: dateVal + ':00',
            mode: mode,
            status: isNaN(status) ? null : status,
            note: document.getElementById('modal-note').value
        };

        try {
            const url = currentEditId ? `/user/api/schedules/${currentEditId}` : '/user/api/schedules';
            const method = currentEditId ? 'PUT' : 'POST';
            
            const res = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                const errorData = await res.json().catch(() => null);
                const msg = errorData ? errorData.message : `Mã lỗi: ${res.status}`;
                alert("Lỗi server: " + msg);
                return;
            }
            
            document.getElementById('schedule-modal').style.display = 'none';
            loadSchedules();
        } catch (err) {
            console.error(err);
            alert("Lỗi kết nối hoặc xử lý: " + err.message);
        }
    };
});
