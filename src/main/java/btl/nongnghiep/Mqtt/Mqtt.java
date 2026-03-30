package btl.nongnghiep.Mqtt;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class Mqtt {

    private MqttClient client;

    @Value("${mqtt.uri}")
    private String uri;

    @Autowired
    private MqttService mqttService;

    @PostConstruct
    public void init() {
        try {

            client = new MqttClient(
                    uri,
                    MqttClient.generateClientId(),
                    new MemoryPersistence()
            );

            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost, reconnecting...");
                    reconnect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String msg = new String(message.getPayload(), UTF_8);

                    System.out.println("Received [" + topic + "]: " + msg);

                    mqttService.Mqtthandle(topic, msg);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // optional
                }
            });


            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setAutomaticReconnect(true); //
            options.setCleanSession(true);

            client.connect(options);

            System.out.println("MQTT connected to: " + uri);


            client.subscribe("test/topic", 0);
            client.publish("test","Hello Word".getBytes(UTF_8),0,false);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    private void reconnect() {
        int retry = 0;
        while (retry < 5) {
            try {
                Thread.sleep(5000);
                client.reconnect();
                System.out.println("Reconnected!");
                return;
            } catch (Exception e) {
                retry++;
                System.out.println("Retry " + retry);
            }
        }
        System.out.println("Reconnect failed!");
    }


    public void MqttPub(String topic, String message, int qos) {
        try {
            client.publish(
                    topic,
                    message.getBytes(UTF_8),
                    qos,
                    false
            );
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void MqttSub(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
