package com.nhnacademy.aiot.node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InfoTest {

    Info info;

    @BeforeEach
    void init() {
        info = new Info();
    }

    @Test
    void getTest() {
        // given
        String key = "send";
        info.increase(key);

        // when
        long actual = info.get(key);

        // then
        assertEquals(1, actual);
    }

    @Test
    void increaseTest() {
        // given
        String key = "recieve";

        // when
        info.increase(key);
        info.increase(key);
        info.increase(key);
        long actual = info.get(key);

        // then
        assertEquals(3, actual);
    }
}
