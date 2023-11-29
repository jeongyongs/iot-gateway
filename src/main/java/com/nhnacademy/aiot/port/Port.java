package com.nhnacademy.aiot.port;

public interface Port extends Iterable<Pipe> {

    void add(Pipe pipe);
}
