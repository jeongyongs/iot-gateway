package com.nhnacademy.aiot.pipe;

public interface Pipe {

    boolean put(Packet packet);

    Packet take();
}
