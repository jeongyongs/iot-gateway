package com.nhnacademy.aiot.node.impl;

import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import com.nhnacademy.aiot.node.InputNode;
import com.nhnacademy.aiot.port.Packet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQTTInNode extends InputNode {

    private static final String ERROR = "에러";


    private static final String SUBSCRIBER_ID = UUID.randomUUID().toString();


    private MqttClient subscriber;

    private final String applicationName;


    // 생성자에서는
    // 연결할 브로커의 주소, 클라이언트의 아이디, 받아낼 메시지 구분 3가지를 매개변수로 받는다
    // 브로커의 주소를 받아 브로커와 연결해준다
    // 랜덤으로 클라이언트의 아이디를 생성해 담아준다
    // 받아낼 메시지를 구분해주는 APPLICATION NAME을 받아준다


    public MQTTInNode(String brokerId, String applicationName) {

        this.applicationName = applicationName;
        try {
            subscriber = new MqttClient(brokerId, SUBSCRIBER_ID);
            subscriber.connect();
            log.info("connect");

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Mqtt message를 받는다 (APPLICATION NAME을 토픽으로 가진애들만)
    // Mqtt message에서 topic과 msg를 뽑아 Packet에 담아준다 (Pakcet = JSON OBJECT (key(String),
    // 패킷을 다음 노드로 전송한다

    @Override
    protected void process() {

        try {
            Packet packet = new Packet();
            subscriber.subscribe(applicationName, (topic, msg) -> {
                packet.append("topic", topic);
                packet.append("payload", msg.getPayload());
            });
            send(packet);
        } catch (MqttException e) {
            log.error(ERROR, e);
        }



    }

}
