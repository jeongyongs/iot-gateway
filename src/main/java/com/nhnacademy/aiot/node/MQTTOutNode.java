package com.nhnacademy.aiot.node;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTOutNode extends InOutputNode {
    private MqttClient client;
    Thread therad;

    /**
     * broker connect 및 option 설정
     */
    void connectClient() {
        try (IMqttClient client = new MqttClient("tcp://localhost:1883", "myId")) {
            log.info("connect");

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);
            client.connect(options);

            // 종료
            client.disconnect();
            log.info("disconenct");
        } catch (MqttException e) {
            e.printStackTrace();
            countFailedPacket();
            log.info("connect fail");
        }
    }

    /**
     * packet 꺼내기
     * <p>
     * JSONObject로 되어있는 packet을 꺼내와서 String 형식으로
     */
    String putPacket() {

        countReceivedPacket();
        return "";
    }

    /**
     * msg 전송
     * 
     * @param topic
     * @param msg
     * @return
     */
    public boolean send(String topic, String msg) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            client.publish(topic, message);
            countSentPacket();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * thread로 동작
     */
    @Override
    protected void process() {
        try {
            send("topic", "msg");
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
