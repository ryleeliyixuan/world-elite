package com.worldelite.job.vo;

import com.worldelite.job.entity.Activity;
import lombok.Data;

import java.util.List;

/**
 * Nuo Xu
 */
@Data
public class SearchActivityTipsVo implements VoConvertable<SearchActivityTipsVo, Activity> {

    private String title;

    @Override
    public SearchActivityTipsVo asVo(Activity activity) {
        if (activity == null) {
            return null;
        }
        setTitle(activity.getTitle());
        return this;
    }
}
