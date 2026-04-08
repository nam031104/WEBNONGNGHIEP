// =============================================
// API Service - kết nối FE với Spring Boot BE
// =============================================

const BASE_URL = 'http://localhost:8080/api';

/** Lấy dữ liệu cảm biến mới nhất */
async function fetchLatestSensorData() {
    const res = await fetch(`${BASE_URL}/data/latest`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Gửi lệnh điều khiển thiết bị */
async function sendControlCommand(payload) {
    const res = await fetch(`${BASE_URL}/control/command`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Lấy danh sách thiết bị + trạng thái */
async function fetchDevices() {
    const userStr = localStorage.getItem('agrismart_user');
    let url = `${BASE_URL}/devices`;
    if (userStr) {
        try {
            const user = JSON.parse(userStr);
            if (user.role !== 'ADMIN') {
                url = `${BASE_URL}/devices/account/${user.idAccount}`;
            }
        } catch (e) {}
    }
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Cập nhật trạng thái thiết bị */
async function fetchDeviceStatus(deviceId) {
    const res = await fetch(`${BASE_URL}/control/status/${deviceId}`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Lấy danh sách user */
async function fetchUsers() {
    const res = await fetch(`${BASE_URL}/users`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Lấy danh sách schedule */
async function fetchSchedules() {
    const res = await fetch(`${BASE_URL}/schedules`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Tạo schedule mới */
async function createSchedule(payload) {
    const res = await fetch(`${BASE_URL}/schedules`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}

/** Xóa schedule */
async function deleteSchedule(id) {
    const res = await fetch(`${BASE_URL}/schedules/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    if (res.status !== 204) {
        return await res.json();
    }
}

/** Cập nhật schedule */
async function updateSchedule(id, payload) {
    const res = await fetch(`${BASE_URL}/schedules/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
}
