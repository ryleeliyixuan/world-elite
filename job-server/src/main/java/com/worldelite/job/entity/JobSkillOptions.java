package com.worldelite.job.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class JobSkillOptions extends JobSkill{
    private Integer categoryId;
}
