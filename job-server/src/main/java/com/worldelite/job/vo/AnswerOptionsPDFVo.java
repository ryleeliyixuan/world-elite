package com.worldelite.job.vo;

import lombok.Data;

import java.util.List;

/**
 * 导出报名表PDF选项视图
 */
@Data
public class AnswerOptionsPDFVo {
    private String options; //选项内容
    private Boolean selected; //是否选中
}
