package com.nhnacademy.aiot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;

/*
 * JSON 포맷 파일을 불러오는 유틸 클래스입니다.
 */
public class JSONFileReader {

    private JSONFileReader() {}

    /**
     * JSON 파일을 불러옵니다.
     * 
     * @param filename 불러올 확장자를 제외한 JSON 파일명입니다.
     * @return 불러온 JSON 내용을 JSONObject 객체로 반환합니다.
     *         <li>파일이 존재하지 않을 경우 <code>null</code>을 반환합니다.
     */
    public static JSONObject read(String filename) {
        JSONObject o = null;

        try (BufferedReader f = new BufferedReader(new InputStreamReader(
                JSONFileReader.class.getClassLoader().getResourceAsStream(filename)))) {

            String body = f.lines().reduce("", String::concat);
            o = new JSONObject(body);

        } catch (IOException ignore) {
            // ignore
        }

        return o;
    }
}
