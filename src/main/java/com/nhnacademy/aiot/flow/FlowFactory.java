package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.FunctionFactory;
import com.nhnacademy.aiot.util.JSONFileReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FlowFactory {
    private static final String ID = "id";
    private static final String AN = "an";
    private static final String FUNC = "func";
    private static final String FLOW = "flow";
    private static final String NODES = "nodes";
    private static final String WIRES = "wires";
    private static final String CLASS = "class";
    private static final String BROKER = "broker";
    private static final String BROKERS = "brokers";
    private static final String MQTT_IN_NODE = "MQTTInNode";
    private static final String FUNCTION_NODE = "FunctionNode";
    private static final String CONNECT_IN_PORT = "connectInPort";
    private static final String CONNECT_OUT_PORT = "connectOutPort";
    private static final String CLASS_NAME = "com.nhnacademy.aiot.node.impl.";

    private FlowFactory() {
    }

    /**
     * setting 파일의 요구사항대로 flow를 생성합니다.
     * 
     * @param setting
     * @return setting 파일에 따라 만들어진 flow
     */
    public static Flow getInstance(Setting setting) {
        Flow flow = new Flow();
        Map<String, Pipe> pipeMap = new HashMap<>();
        JSONArray nodes = JSONFileReader.read(setting.getString(FLOW)).getJSONArray(NODES);

        try {
            for (int i = 0; i < nodes.length(); i++) {
                NodeProperty nodeProperty = setProperty(i, nodes, setting);
                Node node = addNodetoFlow(nodeProperty, flow);

                String nodeId = nodeProperty.getString(ID);
                if (pipeMap.containsKey(nodeId)) {
                    connectIn(node, pipeMap, nodeId);
                }

                if (nodeProperty.has(WIRES)) {
                    connectOut(nodeProperty, node, pipeMap);
                }
            }
        } catch (ReflectiveOperationException e) {
            log.error("FlowFactory: ReflectiveOperationException 발생", e);
        }
        return flow;
    }

    /**
     * 각각의 노드에 맞는 NodeProperty를 만듭니다.
     * 
     * @param i       flow.json의 nodes 배열에 있는 node의 index
     * @param nodes   flow.json의 nodes 배열
     * @param setting flow.json이 담긴 setting 파일
     * @return i번째 node의 NodeProperty
     */
    private static NodeProperty setProperty(int i, JSONArray nodes, Setting setting) {
        JSONObject nodeObj = nodes.getJSONObject(i);
        NodeProperty nodeProperty = new NodeProperty(nodeObj.toString());

        if (nodeObj.has(BROKER)) {
            putBroker(nodeProperty, JSONFileReader.read(setting.getString(FLOW)).getJSONArray(BROKERS));
        }

        if (nodeObj.getString(CLASS).equalsIgnoreCase(MQTT_IN_NODE)) {
            nodeProperty.put(AN, setting.getString(AN));
        }
        if (nodeObj.getString(CLASS).equalsIgnoreCase(FUNCTION_NODE)) {
            nodeProperty.put(FUNC, FunctionFactory.getInstance(nodeObj.getString(FUNC)));
        }

        return nodeProperty;
    }

    /**
     * nodeProperty를 받는 node를 만들어 flow에 추가합니다.
     * 
     * @param nodeProperty node를 생성할 때 입력할 nodeProperty
     * @param flow         node를 추가할 flow
     * @return 생성된 node
     * @throws ReflectiveOperationException Reflection 오류 발생
     */
    private static Node addNodetoFlow(NodeProperty nodeProperty, Flow flow) throws ReflectiveOperationException {
        Node node = (Node) Class
                .forName(CLASS_NAME + nodeProperty.getString(CLASS))
                .getConstructor(NodeProperty.class)
                .newInstance(nodeProperty);
        flow.add(nodeProperty.getString(ID), node);
        return node;
    }

    /**
     * 노드의 inPort에 pipe를 추가합니다.
     * 
     * @param node    pipe를 추가할 node
     * @param pipeMap 추가할 pipe가 담긴 map
     * @param nodeId  node의 id
     * @throws ReflectiveOperationException Reflection 오류 발생
     */
    private static void connectIn(Node node, Map<String, Pipe> pipeMap, String nodeId)
            throws ReflectiveOperationException {
        node.getClass().getMethod(CONNECT_IN_PORT, Pipe.class).invoke(node, pipeMap.get(nodeId));
    }

    /**
     * nodeProperty에 broker object를 넣습니다.
     * 
     * @param nodeProperty broker를 추가할 nodeProperty
     * @param brokers      broker 정보가 담긴 JSON Object
     */
    private static void putBroker(NodeProperty nodeProperty, JSONArray brokers) {
        String brokerId = nodeProperty.getString(BROKER);
        for (int j = 0; j < brokers.length(); j++) {
            if (brokers.getJSONObject(j).getString(ID).equals(brokerId)) {
                nodeProperty.remove(BROKER);
                nodeProperty.put(BROKER, brokers.getJSONObject(j));
                return;
            }
        }
    }

    /**
     * node의 outPort에 pipe를 연결합니다.
     * 
     * @param nodeProperty wire 정보가 담긴 nodeProperty
     * @param node         pipe를 연결할 node
     * @param pipeMap      pipe 정보를 담을 map
     * @throws ReflectiveOperationException Reflection 오류 발생
     */
    private static void connectOut(NodeProperty nodeProperty, Node node, Map<String, Pipe> pipeMap)
            throws ReflectiveOperationException {
        JSONArray ports = nodeProperty.getJSONArray(WIRES);
        for (int j = 0; j < ports.length(); j++) {
            JSONArray pipes = ports.getJSONArray(j);
            for (int k = 0; k < pipes.length(); k++) {
                Pipe pipe = new Pipe();
                node.getClass().getMethod(CONNECT_OUT_PORT, int.class, Pipe.class).invoke(node, j, pipe);
                pipeMap.put(pipes.getString(k), pipe);
            }
        }
    }
}
