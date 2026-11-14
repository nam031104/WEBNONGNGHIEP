// api.js - Chỉ giữ lại Control và Schedule
const BASE_URL = '/user/api';

/** Lấy danh sách thiết bị (để lấy danh sách actor) */
async function fetchDevices() {
    const res = await fetch(`${BASE_URL}/devices`);
    if (!res.ok) throw new Error(`Lỗi lấy danh sách thiết bị: ${res.status}`);
    return await res.json();
}

/** Lấy trạng thái của một thiết bị (bao gồm danh sách actor bên trong) */
async function fetchDeviceStatus(deviceId) {
    const res = await fetch(`${BASE_URL}/control/status/${deviceId}`);
    if (!res.ok) throw new Error(`Lỗi lấy trạng thái: ${res.status}`);
    return await res.json();
}

/** Gửi lệnh điều khiển Actor */
async function sendControlCommand(payload) {
    const res = await fetch(`${BASE_URL}/control/command`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`Lỗi gửi lệnh: ${res.status}`);
    return await res.json();
}

/** Lấy danh sách lịch trình của User */
async function fetchSchedules() {
    const res = await fetch(`${BASE_URL}/schedules`);
    if (!res.ok) throw new Error(`Lỗi lấy lịch trình: ${res.status}`);
    return await res.json();
}

/** Tạo lịch mới */
async function createSchedule(payload) {
    const res = await fetch(`${BASE_URL}/schedules`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`Lỗi tạo lịch: ${res.status}`);
    return await res.json();
}

/** Xóa lịch */
async function deleteSchedule(id) {
    const res = await fetch(`${BASE_URL}/schedules/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error(`Lỗi xóa lịch: ${res.status}`);
}

/** Cập nhật lịch */
async function updateSchedule(id, payload) {
    const res = await fetch(`${BASE_URL}/schedules/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`Lỗi cập nhật lịch: ${res.status}`);
    return await res.json();
}
