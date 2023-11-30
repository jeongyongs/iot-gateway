package com.nhnacademy.aiot.port;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

/*
 * pipe들의 packet을 보내거나 받도록 하는 port 클래스입니다.
 */
public class Port implements Runnable {
    private Thread thread;
    private boolean inPort = true;
    private Set<Pipe> pipes;
    private BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

    /**
     * @param inPort port의 종류가 inPort이면 true, outPort라면 false
     */
    public Port(boolean inPort) {
        pipes = new HashSet<>();
        this.inPort = inPort;
        thread = new Thread(this);
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
        boolean flag = false;
        for (Pipe pipe : pipes) {
            flag = pipe.put(packet);
        }
        return flag;
    }

    /**
     * port와 연결된 pipe들의 packet을 가져옵니다.
     * 
     * @return 정상정으로 가져왔다면 packet, 아니면 null
     */
    public Packet take() {
        try {
            return queue.take();
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        if (inPort) {
            store();
        }
    }

    /**
     * 스레드가 실행되는 동안 pipe에 packet이 들어오면 port의 queue로 옮깁니다.
     * <li>port가 inPort인 경우에만 실행됩니다.
     */
    private void store() {
        while (!Thread.currentThread().isInterrupted()) {
            pipes.stream()
                    .filter(Predicate.not(Pipe::isEmpty))
                    .forEach(this::putToQueue);
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
