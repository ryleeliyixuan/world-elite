package com.worldelite.job.service.resume;

import com.worldelite.job.constants.ResumeType;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.BaseService;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeVo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 简历服务类接口
 * 提供系统必需的简历模块功能
 */
public abstract class ResumeService extends BaseService{

    @Autowired
    private ResumeMapper resumeMapper;

    /**
     * 获取登录用户默认的简历
     * @return 简历详细数据
     */
    public abstract ResumeDetail getDefaultOrCreate();

    /**
     * 保存简历基础信息并返回简历详细数据
     * @param resumeForm 简历表单
     * @return 简历详细数据
     */
    public abstract ResumeDetail saveBasic(ResumeForm resumeForm);

    /**
     * 删除简历附件
     * @param resumeId 需要删除附件的简历ID
     * @return 删除附件后的简历详细数据
     */
    public abstract ResumeDetail deleteResumeAttachment(Long resumeId);

    /**
     * 删除简历
     * @param resumeId 需要删除的简历ID
     */
    public abstract void deleteResume(Long resumeId);

    /**
     * 通过简历ID获取简历详情
     * @param resumeId 简历ID
     * @return 简历详情
     */
    public abstract ResumeDetail getResumeDetail(Long resumeId);

    /**
     * 通过过滤条件从数据库分页检索简历
     * @param resumeListForm 过滤条件
     * @return 简历详情分页数据
     */
    public abstract PageResult<ResumeDetail> list(ResumeListForm resumeListForm);

    /**
     * 通过过滤条件从索引文件分页检索简历
     * @param resumeListForm 过滤条件
     * @return 简历详情分页数据
     */
    public abstract PageResult<ResumeDetail> search(ResumeListForm resumeListForm);

    /**
     * 将简历详情转换为视图对象
     * 简历详情对象供后台系统内部使用
     * 视图对象供前端使用
     * 为了避免json带来的精度损失
     * Long类型数据需要转换成字符串
     * @param resumeDetail
     * @return
     */
    public abstract ResumeVo toResumeVo(ResumeDetail resumeDetail);

    /**
     * 分页后简历详情转换为视图对象
     * @param pageResult 分页后简历详情数据
     * @return 分页简历详情视图对象
     */
    public abstract PageResult<ResumeVo> toResumeVo(PageResult<ResumeDetail> pageResult);

    /**
     * 获取简历基础信息
     * @param resumeId
     * @return
     */
    public Resume getResumeBasic(Long resumeId){
        return resumeMapper.selectByPrimaryKey(resumeId);
    }
}
