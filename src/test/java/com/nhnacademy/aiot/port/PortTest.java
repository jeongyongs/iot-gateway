package com.nhnacademy.aiot.port;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PortTest {
    @Test
    void take() {
        Port port = new Port(true);
        Pipe pipe1 = new Pipe();

        port.add(pipe1);
        port.start();
        pipe1.put((Packet) new Packet().put("1", "one"));

        assertAll(
                () -> assertEquals("one", port.take().get("1")),
                () -> assertTrue(pipe1.isEmpty()));
    }

    @Test
    void put() {
        Port port = new Port(false);
        Pipe pipe1 = new Pipe();
        Pipe pipe2 = new Pipe();

        port.add(pipe1);
        port.add(pipe2);
        port.start();

        assertAll(
                () -> assertTrue(port.put((Packet) new Packet().put("test", "value"))),
                () -> assertTrue(!pipe1.isEmpty()),
                () -> assertEquals("value", pipe1.take().getString("test")),
                () -> assertEquals("value", pipe2.take().getString("test")));
    }
}
