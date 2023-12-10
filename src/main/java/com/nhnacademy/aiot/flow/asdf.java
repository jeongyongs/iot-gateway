package com.nhnacademy.aiot.flow;

import java.util.List;

import org.json.JSONObject;

import com.nhnacademy.aiot.port.Packet;

public class asdf {

    public static List<Packet> asd(Packet packet) {
        if (!packet.getJSONObject("payload").has("object")) {
            Packet p = new Packet();
            p.put("topic", "test/fail");
            p.put("payload", "fail");
            return List.of(p);
        }
        packet.put("topic",
                "test/d/" + packet.getJSONObject("payload").getJSONObject("deviceInfo").getString("devEui"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", System.currentTimeMillis());
        jsonObject.put("value", packet.getJSONObject("payload").getJSONObject("object"));
        packet.put("payload", jsonObject);
        return List.of(packet);
    }
}