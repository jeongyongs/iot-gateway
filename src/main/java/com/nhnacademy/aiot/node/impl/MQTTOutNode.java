package com.nhnacademy.aiot.node.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.nhnacademy.aiot.node.OutputNode;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTOutNode extends OutputNode {
    private MqttClient client;

    /**
     * broker connect 및 option 설정
     */
    public MQTTOutNode() {
        try {
            client = new MqttClient("tcp://localhost", "publisherID");
            log.info("connect");

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setWill("test/will", "Disconnected".getBytes(), 1, true);

            client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
            countFailedPacket();
            log.info("connect fail");
        }
    }

    /**
     * JSONObject로 되어있는 packet을 꺼내와서 String 형식으로 변환
     * <p>
     * thread로 동작
     */
    @Override
    protected void process() {
        try {
            Packet packet = receive();
            String pTopic = packet.get("topic").toString();
            String pMsg = packet.get("payload").toString();

            MqttMessage stringPacket = new MqttMessage();
            stringPacket.setPayload(pMsg.getBytes());
            client.publish(pTopic, stringPacket);
            countSentPacket();

            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(100);
            }

            // 연결 종료
            client.disconnect();
            log.info("disconenct");
        } catch (MqttException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // publish(packet.getString("topic")
    // new Message(packet.getJSONObject("payload").toString().getBytes()));

    /**
     * packet 꺼내기
     * <p>
     * JSONObject로 되어있는 packet을 꺼내와서 String 형식으로
     */
    // String putPacket() {

    // String pTopic = packet.get("topic").toString();
    // String pMsg = packet.get("payload").toString();

    // // String stringPacket = receive().toString();
    // // countReceivedPacket();
    // // return stringPacket;
    // }

    /**
     * msg(topic,value) 전송
     * 
     * @param topic
     * @param msg
     * @return
     */
    // public boolean send(String topic, String msg) {
    // try {
    // // Packet packet = receive();
    // // String pTopic = packet.get("topic").toString();
    // // String pMsg = packet.get("payload").toString();

    // // MqttMessage stringPacket = new MqttMessage();
    // // stringPacket.setPayload(pMsg.getBytes());
    // // client.publish(pTopic, stringPacket);
    // // countSentPacket();
    // } catch (MqttException e) {
    // e.printStackTrace();
    // }
    // return true;
    // }

}
