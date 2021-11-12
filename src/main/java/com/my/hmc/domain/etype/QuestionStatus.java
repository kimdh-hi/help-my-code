package com.my.hmc.domain.etype;

public enum QuestionStatus {

    REQUESTED("요청"), VIEWED("확인"), DONE("완료"), REJECT("거절"), CANCELD("취소");

    private String description;

    QuestionStatus(String description) {
        this.description = description;
    }
}
