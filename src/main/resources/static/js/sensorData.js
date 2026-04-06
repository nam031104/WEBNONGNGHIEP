const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function () {
    stompClient.subscribe('/user/topic/sensordata', function (message) {

        const data = JSON.parse(message.body);
        console.log("DATA:", data);

        const div = document.getElementById("Sensors");

        let html = `<div id="sensor"><br>`;
        html += `<h2>Du lieu dang day len</h2>`;

        data.forEach(s => {
            console.log(s);
            html += `- ${s.typeSensor} - ${s.value} - ${s.unit}<br/>`;
        });

        html += `</div>`;

        div.innerHTML = html;
    });
});