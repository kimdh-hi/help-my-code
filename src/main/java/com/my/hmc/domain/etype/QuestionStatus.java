package com.my.hmc.domain.etype;

public enum QuestionStatus {

    ALL("전체"), REQUESTED("요청"),  DONE("완료"), REJECT("거절");

    private String description;

    QuestionStatus(String description) {
        this.description = description;
    }
}
