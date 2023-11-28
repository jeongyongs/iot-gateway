package com.nhnacademy.aiot.node.inpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import com.nhnacademy.aiot.node.InOutNode;
import com.nhnacademy.aiot.node.Info;
import com.nhnacademy.aiot.pipe.Packet;
import com.nhnacademy.aiot.pipe.Pipe;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FunctionNode implements InOutNode, Runnable {
    private final Thread thread;
    private final List<Pipe> outPipes;
    private final List<Pipe> inPipes;
    private final UnaryOperator<Packet> function;
    private static FunctionNode instance;
    private static Info info;

    private FunctionNode(UnaryOperator<Packet> function) {
        thread = new Thread(this);
        outPipes = new ArrayList<>();
        inPipes = new ArrayList<>();
        this.function = function;
    }

    public static FunctionNode getInstance(UnaryOperator<Packet> function) {
        if (instance == null) {
            synchronized (FunctionNode.class) {
                instance = new FunctionNode(function);
            }
        }
        return instance;
    }
       
    @Override
    public void connectOutPipe(Pipe inPipe) {
        this.inPipes.add(inPipe);
    }

    @Override
    public void connectInPipe(Pipe outPipe) {
        this.outPipes.add(outPipe);
    }

    @Override
    public Info getInfo() {
        return info;
    }

    @Override
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

    private void postprocess() {
        log.debug("FunctionNode thread start");
    }

    private void process() {
        for (Pipe inPipe : inPipes) {
            Packet inputPacket = inPipe.take();
            Packet outputPacket = function.apply(inputPacket);
            info.increase("recieve");
            for (Pipe outPipe : outPipes) {
                outPipe.put(outputPacket);
                info.increase("send");
            }
        }
    }

    private void preprocess() {
        inPipes.clear();
        outPipes.clear();
        log.info("FunctionNode thread stop");
    }
}
