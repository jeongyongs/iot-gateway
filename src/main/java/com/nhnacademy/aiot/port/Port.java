package com.nhnacademy.aiot.port;

import java.util.Optional;

public interface Port {

    void add(Pipe pipe);

    boolean put(Packet packet);

    Optional<Packet> take();
}
