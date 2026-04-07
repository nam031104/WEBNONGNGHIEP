const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function () {
    stompClient.subscribe('/user/topic/devices', function (message) {
        const device = JSON.parse(message.body);
        const div = document.getElementById("devices");

        // tạo HTML card đẹp
        let html = `
        <div id="${device.deviceId}" class="device-card">
            <div class="device-name"><b>${device.name}</b></div>
        `;

        if (device.sensors) {
            html += `<div class="device-sensors">`;
            device.sensors.forEach(s => {
                html += `<span class="sensor-item">${s.typeSensor}</span>`;
            });
            html += `</div>`;
        }

        html += `<button class="btn-confirm" onclick="confirmDevice('${device.deviceId}')">Xác nhận</button>`;
        html += `</div>`;

        // nếu đã tồn tại thì replace
        const old = document.getElementById(device.deviceId);
        if (old) old.remove();

        div.innerHTML += html;
    });
});

function confirmDevice(deviceId) {
    fetch('/user/device/create/confirm', {
        method: 'POST'
    }).then(() => {
        document.getElementById(deviceId).remove();
        alert("Đã lưu device!");
    });
}