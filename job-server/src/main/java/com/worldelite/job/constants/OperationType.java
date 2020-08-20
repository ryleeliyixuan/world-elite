package com.worldelite.job.constants;

/**
 * CRUD操作类型
 **/
public enum OperationType {
    CreateOrUpdate("CU", "添加/更新"),
    Delete("D", "删除");


    private String operation;
    private String name;

    OperationType(String operation, String name) {
        this.operation = operation;
        this.name = name;
    }
}
