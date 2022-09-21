package com.kh.heallo.domain.uploadfile;

public enum AttachCode {
    RV_CODE("RV000"),BD_CODE("BD000"),CD_CODE("CD000");

    private String code;

    AttachCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
