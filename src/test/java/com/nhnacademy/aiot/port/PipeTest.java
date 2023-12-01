package com.nhnacademy.aiot.port;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class PipeTest {
    @Test
    void pipeTest() throws InterruptedException {
        Pipe pipe = new Pipe();
        Packet packet = new Packet();
        packet.put("date", packet.getCreationDate());
        packet.put("topic", "data/#");
        packet.put("payload", "test");

        pipe.put(packet);

        assertAll(() -> assertTrue(packet instanceof JSONObject),
                () -> assertTrue(pipe.put(packet)),
                () -> assertTrue(!pipe.isEmpty()),
                () -> assertEquals("test", pipe.take().getString("payload")),
                () -> assertEquals("data/#", pipe.take().getString("topic")),
                () -> assertTrue(pipe.isEmpty()));

    }
}
