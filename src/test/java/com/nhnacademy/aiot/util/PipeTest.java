package com.nhnacademy.aiot.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;

class PipeTest {
    @Test
    void pipeTest() {
        Packet packet = new Packet();
        packet.put("date", packet.getCreationDate());
        packet.put("topic", "data/#");
        packet.put("payload", "test");

        Pipe pipe = new Pipe();
        pipe.put(packet);

        assertAll(() -> assertTrue(packet instanceof JSONObject), () -> assertTrue(!pipe.isEmpty()),
                () -> assertTrue(pipe.put(packet)),
                () -> assertEquals("test", pipe.take().get().getString("payload")),
                () -> assertEquals("data/#", pipe.take().get().getString("topic")));
    }
}