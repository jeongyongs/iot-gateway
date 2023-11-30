package com.nhnacademy.aiot.node;

import java.util.HashMap;
import java.util.Map;

/**
 * 정보 수집을 위한 클래스입니다.
 */
public class Info {

    private final Map<String, Long> infos;

    /**
     * 정보 수집을 위한 객체를 생성합니다.
     */
    public Info() {
        infos = new HashMap<>();
    }

    /**
     * 수집한 정보를 확인합니다.
     * 
     * @param key 확인할 정보의 키 값입니다.
     * @return 해당 키의 수집된 정보를 반환합니다.
     *         <li>해당 키가 없으면 <code>0</code>을 반환합니다.
     */
    public long get(String key) {
        if (!infos.containsKey(key)) {
            return 0;
        }
        return infos.get(key);
    }

    /**
     * 해당 키의 값을 증가시킵니다.
     * 
     * @param key 증가시킬 정보의 키 값입니다.
     */
    public void increase(String key) {
        infos.computeIfPresent(key, (k, v) -> v + 1);
        infos.computeIfAbsent(key, e -> 1L);
    }
}
