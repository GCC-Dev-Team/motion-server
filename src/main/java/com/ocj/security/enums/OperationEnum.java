package com.ocj.security.enums;


public enum OperationEnum {
    Video_Screenshot("vframe/jpg/offset/1","视频截第一秒帧");

    private  String operationOrder;

    private  String message;


    OperationEnum(String operationOrder, String message) {
        this.operationOrder = operationOrder;
        this.message = message;
    }

    public String getOperationOrder() {
        return operationOrder;
    }


    public String getMessage() {
        return message;
    }


}
