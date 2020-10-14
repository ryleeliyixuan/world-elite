package com.worldelite.job.service;

import com.worldelite.job.entity.ResumeLink;
import com.worldelite.job.form.ResumeLinkForm;
import com.worldelite.job.mapper.ResumeLinkMapper;
import com.worldelite.job.util.AppUtils;
import com.worldelite.job.vo.ResumeLinkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Service
public class ResumeLinkService {

    @Autowired
    private ResumeLinkMapper resumeLinkMapper;

    @Autowired
    @Lazy
    private ResumeService resumeService;

    /**
     * 获取简历链接列表
     * @param resumeId
     * @return
     */
    public List<ResumeLinkVo> getResumeLinkVoList(Long resumeId){
        ResumeLink options = new ResumeLink();
        options.setResumeId(resumeId);
        List<ResumeLink> resumeLinkList = resumeLinkMapper.selectAndList(options);
        return AppUtils.asVoList(resumeLinkList, ResumeLinkVo.class);
    }

    public List<ResumeLink> getResumeLinkList(Long resumeId){
        ResumeLink options = new ResumeLink();
        options.setResumeId(resumeId);
        List<ResumeLink> resumeLinkList = resumeLinkMapper.selectAndList(options);
        return resumeLinkMapper.selectAndList(options);
    }

    /**
     * 保存简历链接
     *
     * @param resumeLinkForm
     * @return
     */
    public ResumeLinkVo saveResumeLink(ResumeLinkForm resumeLinkForm){
//        resumeService.checkResumeCreator(resumeLinkForm.getResumeId());

        ResumeLink resumeLink = null;
        if(resumeLinkForm.getId() != null){
            resumeLink = resumeLinkMapper.selectByPrimaryKey(resumeLinkForm.getId());
        }

        if(resumeLink == null){
            resumeLink = new ResumeLink();
            resumeLink.setResumeId(resumeLinkForm.getResumeId());
        }

        resumeLink.setName(resumeLinkForm.getName());
        resumeLink.setLink(resumeLinkForm.getLink());

        if(resumeLink.getId() == null){
            resumeLinkMapper.insertSelective(resumeLink);
        }else{
            resumeLinkMapper.updateByPrimaryKey(resumeLink);
        }

        return new ResumeLinkVo().asVo(resumeLink);
    }

    /**
     * 删除简历链接
     *
     * @param id
     */
    public void deleteResumeLink(Integer id){
        ResumeLink resumeLink = resumeLinkMapper.selectByPrimaryKey(id);
        if(resumeLink != null){
//            resumeService.checkResumeCreator(resumeLink.getResumeId());
            resumeLinkMapper.deleteByPrimaryKey(id);
        }
    }
}
