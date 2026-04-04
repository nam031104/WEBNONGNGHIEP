const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function () {
//khi nào lam dang ki dang nhap xong thì sub vào user/topic/devices
 stompClient.subscribe('/topic/devices', function (message) {
  const device = JSON.parse(message.body);

  const div = document.getElementById("devices");

  let html = `<div id="${device.deviceId}" style="border:1px solid black; margin:10px; padding:10px;">
    <b>${device.name}</b><br/>`;

  if (device.sensors) {
    device.sensors.forEach(s => {
      html += `- ${s.typeSensor}<br/>`;
    });
  }

  html += `<button onclick="confirmDevice('${device.deviceId}')">Xác nhận</button>`;
  html += `</div>`;

  // nếu đã tồn tại thì replace
  const old = document.getElementById(device.deviceId);
  if (old) old.remove();

    div.innerHTML += html;
  });

 });

 function confirmDevice(deviceId) {
  fetch('/device/create/confirm', {
    method: 'POST'
  }).then(() => {
  document.getElementById(deviceId).remove();
  alert("Đã lưu device!");
  });
 }


//const stompClient = new StompJs.Client({
//    brokerURL: 'ws://localhost:8080/gs-guide-websocket'
//});
//
//stompClient.onConnect = (frame) => {
//    setConnected(true);
//    console.log('Connected: ' + frame);
//    stompClient.subscribe('/topic/greetings', (greeting) => {
//        showGreeting(JSON.parse(greeting.body).content);
//    });
//};
//
//stompClient.onWebSocketError = (error) => {
//    console.error('Error with websocket', error);
//};
//
//stompClient.onStompError = (frame) => {
//    console.error('Broker reported error: ' + frame.headers['message']);
//    console.error('Additional details: ' + frame.body);
//};
//
//function setConnected(connected) {
//    $("#connect").prop("disabled", connected);
//    $("#disconnect").prop("disabled", !connected);
//    if (connected) {
//        $("#conversation").show();
//    }
//    else {
//        $("#conversation").hide();
//    }
//    $("#greetings").html("");
//}
//
//function connect() {
//    stompClient.activate();
//}
//
//function disconnect() {
//    stompClient.deactivate();
//    setConnected(false);
//    console.log("Disconnected");
//}
//
//function sendName() {
//    stompClient.publish({
//        destination: "/app/hello",
//        body: JSON.stringify({'name': $("#name").val()})
//    });
//}
//
//function showGreeting(message) {
//    $("#greetings").append("<tr><td>" + message + "</td></tr>");
//}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});