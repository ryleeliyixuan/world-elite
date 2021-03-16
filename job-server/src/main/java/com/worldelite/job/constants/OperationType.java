package com.worldelite.job.constants;

/**
 * CRUD操作类型
 **/
public enum OperationType {
    CREATE_OR_UPDATE("CU", "添加/更新"),
    DELETE("D", "删除");

    OperationType(String operation, String name) {
    }
}
