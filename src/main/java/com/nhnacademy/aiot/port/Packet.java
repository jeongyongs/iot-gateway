package com.nhnacademy.aiot.port;

import java.util.Date;

import org.json.JSONObject;

/*
 * 데이터를 담을 클래스입니다.
 */
public class Packet extends JSONObject {
    private Date creationDate;

    public Packet() {
        super();
        creationDate = new Date();
    }

    public Packet(String json) {
        super(json);
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
