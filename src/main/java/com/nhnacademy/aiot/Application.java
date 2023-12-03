package com.nhnacademy.aiot;

import com.nhnacademy.aiot.flow.Flow;
import com.nhnacademy.aiot.flow.FlowFactory;
import com.nhnacademy.aiot.setting.Setting;
import com.nhnacademy.aiot.util.SettingLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * 게이트웨이 소프트웨어입니다.
 */
@Slf4j
public class Application {

    private static final String ERROR_MESSAGE = "애플리케이션 실행 중 문제가 발생했습니다.";

    private Application() {}

    /**
     * 게이트웨이를 시작합니다.
     * 
     * @param args 커맨드라인 인자입니다.
     */
    public static void run(String[] args) {
        try {
            Setting setting = SettingLoader.load(args);
            Flow flow = FlowFactory.getInstance(setting);
            flow.start();
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e);
        }
    }
}
