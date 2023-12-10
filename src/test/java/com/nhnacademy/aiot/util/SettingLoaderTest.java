package com.nhnacademy.aiot.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class SettingLoaderTest {
    @Test
    void ArgsTest() {
        JSONObject setting = SettingLoader.load(new String[] { "--an", "data/", "-s", "humidity,temperatrue" });

        assertAll(
                () -> assertEquals("data/", setting.getString("an")),
                () -> assertEquals("humidity", setting.getJSONArray("s").get(0)),
                () -> assertEquals("temperatrue", setting.getJSONArray("s").get(1)));
    }

    @Test
    void ArgsWithNullTest() {
        JSONObject setting = SettingLoader.load(new String[] {});

        assertAll(
                () -> assertEquals("#/", setting.getString("an")),
                () -> assertEquals("temperature", setting.getJSONArray("s").get(0)));
    }

    @Test
    void SettingJSONTest() {
        JSONObject setting = SettingLoader.load(new String[] { "--an", "data/", "-c", "./src/test/java/com/nhnacademy/aiot/util/test2.json" });

        assertAll(
                () -> assertEquals("data/", setting.getString("an")),
                () -> assertEquals("pressure", setting.getJSONArray("s").get(1)));
    }
}
