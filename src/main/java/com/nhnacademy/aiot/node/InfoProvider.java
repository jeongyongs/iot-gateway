package com.nhnacademy.aiot.node;

/**
 * 상태 정보를 가져올 수 있는 인터페이스입니다.
 */
public interface InfoProvider {

    /**
     * 상태 정보를 가져옵니다.
     * 
     * @return 상태 정보가 담긴 Info 객체를 반환합니다.
     */
    Info getInfo();
}
