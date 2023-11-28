package com.nhnacademy.aiot.pipe;

import java.util.Date;

import org.json.JSONObject;

/*
 * 데이터를 담을 클래스입니다.
 */
public class Packet extends JSONObject {
    Date creationDate;

    public Packet() {
        super();
        creationDate = new Date();
    }

    /**
     * 인스턴스가 생성된 시간을 반환합니다.
     * 
     * @return creationTime 인스턴스가 생성된 시간
     */
    public Date getCreationDate() {
        return creationDate;
    }
}
