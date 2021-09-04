package com.cos.quizapp.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfo {
    private String name;
    private String phoneNumber;
    private String birthDay;
    private String address;
    private Object rank;
    private Object score;

    public UserInfo(String name, String phoneNumber, String birthDay, String address, int rank, int score){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.rank = rank;
        this.score = score;
    }

    public UserInfo(String name, String phoneNumber, String birthDay, String address, Object rank, Object score) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.address = address;
        this.rank = rank;
        this.score = score;
    }
}

