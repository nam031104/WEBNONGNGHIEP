const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function () {
    stompClient.subscribe('/user/topic/sensordata', function (message) {
        const deviceData = JSON.parse(message.body);
        console.log("DEVICE DATA:", deviceData);

        const container = document.getElementById("Sensors");

        const deviceId = deviceData.idDevice;
        const sensors = deviceData.dataWebsocketDtos;

        let deviceDiv = document.getElementById("device-" + deviceId);

        // tạo card nếu chưa có
        if (!deviceDiv) {
            deviceDiv = document.createElement("div");
            deviceDiv.id = "device-" + deviceId;
            deviceDiv.className = "device-block";
            container.appendChild(deviceDiv);
        }

        let html = `<h3>Device: ${deviceId}</h3>`;
        html += `<div class="sensor-list">`;

        sensors.forEach(s => {
            html += `<div class="sensor-item">
                        ${s.typeSensor} (${s.idSensor}) : ${s.value} ${s.unit}
                     </div>`;
        });

        html += `</div>`;

        deviceDiv.innerHTML = html;
    });
});