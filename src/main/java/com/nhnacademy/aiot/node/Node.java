package com.nhnacademy.aiot.node;

import lombok.extern.slf4j.Slf4j;

/**
 * 스레드로 동작하는 노드 추상 클래스입니다.
 */
@Slf4j
public abstract class Node implements Runnable, InfoProvider {

    private static final String NODE_STARTED_LOG = "노드가 시작되었습니다.";
    private static final String NODE_STOPPED_LOG = "노드가 종료되었습니다.";
    private static final String SENT_PACKET_KEY = "sentPacket";
    private static final String RECEIVED_PACKET_KEY = "receivedPacket";
    private static final String FAILED_PACKET_KEY = "failedPacket";

    private final Thread thread;
    private final Info info;

    protected Node() {
        thread = new Thread(this);
        info = new Info();
    }

    /**
     * 노드를 실행합니다.
     */
    public void start() {
        thread.start();
    }

    @Override
    public void run() {
        preprocess();
        while (!Thread.interrupted()) {
            process();
        }
        postprocess();
    }

    protected void preprocess() {
        log.debug(NODE_STARTED_LOG);
    }

    /**
     * 노드의 로직을 구현합니다.
     */
    protected abstract void process();

    protected void postprocess() {
        log.debug(NODE_STOPPED_LOG);
    }

    @Override
    public Info getInfo() {
        return info;
    }

    /**
     * 성공적으로 보낸 패킷의 수를 카운트합니다.
     */
    protected void countSentPacket() {
        info.increase(SENT_PACKET_KEY);
    }

    /**
     * 성공적으로 받은 패킷의 수를 카운트합니다.
     */
    protected void countReceivedPacket() {
        info.increase(RECEIVED_PACKET_KEY);
    }

    /**
     * 비정상적으로 처리된 패킷의 수를 카운트합니다.
     */
    protected void countFailedPacket() {
        info.increase(FAILED_PACKET_KEY);
    }
}
