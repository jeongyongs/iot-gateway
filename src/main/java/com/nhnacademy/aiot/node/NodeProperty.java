package com.nhnacademy.aiot.node;

import org.json.JSONObject;

/**
 * 클래스를 통해 JSONObject으로 입력받은 설정들을
 * 각 노드의 파라미터로 설정할 수 있습니다.
 * <p>
 * 각 노드의 매개변수를 해당 클래스를 통해 통일된 방식으로 관리할수 있습니다.
 */
public class NodeProperty extends JSONObject {

    /**
     * 입력된 JSONObject를 기반으로 새로운 NodeProperty 객체를 생성합니다.
     * 
     * @param jsonObject NodeProperty에 추가할 키-값을 포함하는 JSONObject입니다.
     */
    public NodeProperty(JSONObject jsonObject) {
        jsonObject.keySet()
                .stream()
                .forEach(key -> this.put(key, jsonObject.get(key)));
    }
}
