package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.port.Packet;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.port.Port;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class InOutPutNode extends Node {
    private Port inPorts;
    private Map<Integer, Port> outPorts;

    protected InOutPutNode() {
        this.inPorts = new Port() {

            @Override
            public void add(Pipe pipe) {
                // TODO Auto-generated method stubPort inPort, Map<Integer, Port> outPorts
                throw new UnsupportedOperationException("Unimplemented method 'add'");
            }

            @Override
            public boolean put(Packet packet) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'put'");
            }

            @Override
            public Optional<Packet> take() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'take'");
            }

        };
        this.outPorts = new HashMap<>();
    }

    /**
     * Port
     * 
     * @return
     * @throws NoSuchElementException
     */
    protected Packet receive() throws NoSuchElementException {
        Optional<Packet> packet = inPorts.take();

        if (packet.isPresent()) {
            return packet.get();
        }
        throw new NoSuchElementException();
    }

    protected void send(int portNum, Packet packet) {
        Port outPort = outPorts.get(portNum);                
        if (outPort != null) {
            outPort.put(packet);
        }
    }

    public void connectInPipe(Pipe pipe) {
        inPorts.add(pipe);
    }

    public void connectOutPipe(int portNum, Pipe pipe) {
        Port outPort = outPorts.get(portNum);
        if (outPort != null) {
            outPort.add(pipe);
        }
    }
}