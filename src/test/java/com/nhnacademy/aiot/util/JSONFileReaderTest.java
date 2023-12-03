package com.nhnacademy.aiot.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class JSONFileReaderTest {

    @Test
    // 어떻게 mock으로 파일 입출력 테스트를 할 수 있을까?
    void readTest() {
        // given
        String filename = "settings.json";

        // when
        JSONObject actual = JSONFileReader.read(filename);

        // then
        assertAll( //
                () -> assertTrue(actual.has("an")), //
                () -> assertEquals("application/#", actual.getString("an")) //
        );
    }
}
