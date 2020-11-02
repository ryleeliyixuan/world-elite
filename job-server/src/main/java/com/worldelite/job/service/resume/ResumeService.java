package com.worldelite.job.service.resume;

import com.github.pagehelper.PageHelper;
import com.worldelite.job.constants.BusinessType;
import com.worldelite.job.constants.OperationType;
import com.worldelite.job.constants.ResumeIndexFields;
import com.worldelite.job.dto.LuceneIndexCmdDto;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.entity.ResumeOptions;
import com.worldelite.job.entity.UserApplicant;
import com.worldelite.job.form.ResumeForm;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.service.BaseService;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.service.search.SearchService;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.lucene.document.Document;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;

/**
 * 简历服务类接口
 * 提供系统必需的简历模块功能
 */
public abstract class ResumeService extends BaseService{

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private IndexService indexService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource(name = "luceneIndexCmdFanoutExchange")
    private FanoutExchange exchange;

    @Value("${search.index.resumeindex2}")
    private String folder;

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
     * 解析简历文件
     * @param attachmentName 存在OSS上的简历文件名
     * @return 解析后的简历详情
     */
    public abstract ResumeDetail parseAttachment(String attachmentName);

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


    /**
     * 给简历添加索引
     * @param resumeId 简历ID
     */
    public void saveResumeItem(Long resumeId){
        ResumeDetail resumeDetail = getResumeDetail(resumeId);
        saveResumeItem(resumeDetail);
    }

    /**
     * 给简历添加索引
     * @param resumeDetail
     */
    public void saveResumeItem(ResumeDetail resumeDetail){
        Document document = indexService.saveResumeItem(resumeDetail,folder);
        //MQ广播索引更新指令
        rabbitTemplate.convertAndSend(exchange.getName(), "", new LuceneIndexCmdDto(document, OperationType.CreateOrUpdate, BusinessType.Resume));
    }

    /**
     * 给简历添加索引
     * @param document 简历Document
     */
    public void saveResumeItem(Document document){
        Long resumeId = Long.valueOf(document.get(ResumeIndexFields.RESUME_ID));
        saveResumeItem(resumeId);
    }

    public PageResult<ResumeDetail> searchDefault(ResumeListForm resumeListForm){
        return searchService.searchResume(resumeListForm, folder);
    }

    /**
     * 从t_resume表重建所有简历索引
     */
    public abstract void rebuildAllIndex();

    /**
     * Todo 获取重建索引进度
     */
    public abstract void getRebuildProcess();
}
