package com.nhnacademy.aiot.port;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

/*
 * packet이 node간에 이동할 수 있도록 하는 클래스입니다.
 */

 @Slf4j
public class Pipe {

    private final BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    /**
     * pipe에 packet을 넣습니다.
     * 
     * @param packet pipe에 넣을 packet
     * @return packet을 정상적으로 넣었으면 true, 아니면 flase
     */
    public boolean put(Packet packet) {
        boolean isPacketSent = false;
        try {
            queue.put(packet);
            isPacketSent = true;
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
            log.debug("InterruptedException 발생");
        }
        return isPacketSent;
    }
    
    /**
     * pipe에서 packet을 꺼냅니다.
     * 
     * @return pipe에서 정상적으로 메세지를 꺼냈다면 value가 packet인 optional, InterruptedException가 발생하면 null
     */
    public Packet take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.debug("InterruptedException 발생");
            return null;
        }
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
