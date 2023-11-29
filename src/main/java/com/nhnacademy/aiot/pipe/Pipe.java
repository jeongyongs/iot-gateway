package com.nhnacademy.aiot.pipe;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * packet이 node간에 이동할 수 있도록 하는 클래스입니다.
 */
public class Pipe {

    BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    /**
     * pipe에 packet을 넣습니다.
     * 
     * @param packet pipe에 넣을 packet
     * @return packet을 정상적으로 넣었으면 true, 아니면 flase
     */
    public boolean put(Packet packet) {
        try {
            queue.put(packet);
            return true;
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * pipe에서 packet을 꺼냅니다.
     * 
     * @return pipe에서 정상적으로 메세지를 꺼냈다면 value가 packet인 optional, 아니면 value가 null인
     *         optional
     * @throws InterruptedException
     */
    public Optional<Packet> take() {
        Packet packet = null;
        try {
            packet = queue.take();
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
        return Optional.ofNullable(packet);
    }

    /**
     * pipe가 비어있는지 확인합니다.
     * 
     * @return pipe가 비어있다면 true, 아니면 false
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
