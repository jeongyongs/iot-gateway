package com.nhnacademy.aiot.port;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;

/*
 * pipe들의 packet을 보내거나 받도록 하는 port 클래스입니다.
 */
@Slf4j
public class Port implements Runnable {
    private final Thread thread;
    private final CopyOnWriteArraySet<Pipe> pipes;
    private final BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    /**
     * @param inPort port의 종류가 inPort이면 PortType.IN, outPort라면 PortType.OUT
     */
    public Port(PortType portType) {
        pipes = new CopyOnWriteArraySet<>();
        thread = new Thread(this);
        if (portType == PortType.IN) {
            thread.start();
        }
    }

    /**
     * port에 연결할 pipe를 추가합니다.
     * 
     * @param pipe port와 연결할 pipe
     */
    public void add(Pipe pipe) {
        pipes.add(pipe);
    }

    /**
     * port와 연결된 모든 pipe에 packet을 보냅니다.
     * 
     * @param packet pipe로 보낼 packet
     * @return 정상적으로 packet이 보내졌다면 true, 아니면 false
     */
    public boolean put(Packet packet) {
        boolean isPacketSent = false;
        for (Pipe pipe : pipes) {
            isPacketSent = pipe.put(packet);
        }
        return isPacketSent;
    }

    /**
     * port와 연결된 pipe들의 packet을 가져옵니다.
     * 
     * @return 정상적으로 가져왔다면 packet, InterruptedException이 발생하면 null
     */
    public Packet take() {
        try {
            return queue.take();
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
            log.debug("InterruptedException 발생");
            return null;
        }
    }

    @Override
    public void run() {
        store();
    }

    /**
     * 스레드가 실행되는 동안 pipe에 packet이 들어오면 port의 queue로 옮깁니다.
     * <li>port가 inPort인 경우에만 실행됩니다.
     */
    private void store() {
        while (!Thread.currentThread().isInterrupted()) {
            pipes.stream().filter(Predicate.not(Pipe::isEmpty)).forEach(this::putToQueue);
        }
    }

    /**
     * pipe에서 packet을 꺼내 port의 queue로 넣습니다.
     * 
     * @param pipe packet을 꺼내올 pipe
     */
    private void putToQueue(Pipe pipe) {
        try {
            queue.put(pipe.take());
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
