package com.worldelite.job.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Xiang Chao
 **/
@Data
public class JobRecruitDto implements Serializable {
    private int recruitType;
    private int count;
}
