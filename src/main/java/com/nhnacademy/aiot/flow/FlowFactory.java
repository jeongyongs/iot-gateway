package com.nhnacademy.aiot.flow;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import com.nhnacademy.aiot.node.Node;
import com.nhnacademy.aiot.node.NodeProperty;
import com.nhnacademy.aiot.port.Pipe;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.FunctionFactory;
import com.nhnacademy.aiot.util.JSONFileReader;

public class FlowFactory {

    private static final String FLOW = "flow";
    private static final String NODES = "nodes";
    private static final String COM_NHNACADEMY_AIOT_NODE_IMPL = "com.nhnacademy.aiot.node.impl.";
    private static final String ID = "id";
    private static final String CONNECT_IN = "connectIn";
    private static final String WIRES = "wires";
    private static final String CONNECT_OUT = "connectOut";
    private static final String MQTT_IN_NODE = "MQTTInNode";
    private static final String APPLICATION_NAME = "applicationName";
    private static final String AN = "an";
    private static final String CLASS = "class";
    private static final String FUNCTION_NODE = "FunctionNode";
    private static final String FUNC = "func";

    private FlowFactory() {}

    public static Flow getInstance(Setting setting) throws Exception {
        Flow flow = new Flow();
        JSONArray nodes = JSONFileReader.read(setting.getString(FLOW)).getJSONArray(NODES);
        Map<String, Pipe> pipeMap = new HashMap<>();

        for (int i = 0; i < nodes.length(); i++) {
            NodeProperty nodeProperty = getNodeProperties(nodes, i, setting);
            Node node = (Node) Class //
                    .forName(COM_NHNACADEMY_AIOT_NODE_IMPL + nodeProperty.getString(CLASS)) //
                    .getConstructor(NodeProperty.class) //
                    .newInstance(nodeProperty);

            String id = nodeProperty.getString(ID);
            flow.add(id, node);

            if (pipeMap.containsKey(id)) {
                node.getClass().getMethod(CONNECT_IN, Pipe.class).invoke(node, pipeMap.remove(id));
            }

            if (!nodeProperty.has(WIRES)) {
                continue;
            }

            JSONArray ports = nodeProperty.getJSONArray(WIRES);
            for (int j = 0; j < ports.length(); j++) {
                JSONArray pipes = ports.getJSONArray(j);
                for (int k = 0; k < pipes.length(); k++) {
                    Pipe pipe = new Pipe();
                    node.getClass().getMethod(CONNECT_OUT, int.class, Pipe.class).invoke(node, j,
                            pipe);
                    pipeMap.put(pipes.getString(k), pipe);
                }
            }
        }

        return flow;
    }

    private static NodeProperty getNodeProperties(JSONArray nodes, int i, Setting setting) {
        NodeProperty nodeProperty = new NodeProperty(nodes.getJSONObject(i));

        if (nodeProperty.getString(CLASS).equals(MQTT_IN_NODE)) {
            nodeProperty.put(APPLICATION_NAME, setting.getString(AN));
        }
        if (nodeProperty.getString(CLASS).equals(FUNCTION_NODE)) {
            nodeProperty.put(FUNC, FunctionFactory.getInstance(nodeProperty.getString(FUNC)));
        }
        return nodeProperty;
    }
}
