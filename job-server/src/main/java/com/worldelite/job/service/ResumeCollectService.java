package com.worldelite.job.service;

import com.worldelite.job.constants.FavoriteType;
import com.worldelite.job.constants.ResumeStatus;
import com.worldelite.job.entity.Favorite;
import com.worldelite.job.entity.ResumeDetail;
import com.worldelite.job.form.ResumeListForm;
import com.worldelite.job.service.resume.ResumeServiceFactory;
import com.worldelite.job.service.resume.ResumeService;
import com.worldelite.job.service.search.IndexService;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.ResumeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 简历收藏服务类
 */
@Slf4j
@Service
public class ResumeCollectService extends BaseService{

    @Value("${search.index.resumeindex2}")
    private String folder;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private IndexService indexService;

    /**
     * 收藏/取消收藏
     * @param resumeId
     */
    public ResumeVo collect(Long resumeId){
        //收藏/取消收藏简历
        List<Favorite> favoriteList = listByResumeId(resumeId);
        if(CollectionUtils.isEmpty(favoriteList)){
            //收藏
            save(resumeId);
        }else{
            //取消收藏
            for(Favorite favorite:favoriteList){
                favoriteService.delete(favorite.getId());
            }
        }
        //获取简历信息
        ResumeService resumeService = ResumeServiceFactory.getResumeService(resumeId);
        ResumeDetail resumeDetail = resumeService.getResumeDetail(resumeId);
        //重建索引
        resumeService.saveResumeItem(resumeDetail);
        //返回简历信息
        return resumeService.toResumeVo(resumeDetail);
    }

    public PageResult<ResumeVo> list(ResumeListForm resumeListForm){
        resumeListForm.setCollectorId(curUser().getId());
        resumeListForm.setStatus(ResumeStatus.PUBLISH.value);
        ResumeService resumeService = ResumeServiceFactory.getDefaultService();
        PageResult<ResumeDetail> pageResult = resumeService.searchDefault(resumeListForm);
        return resumeService.toResumeVo(pageResult);
    }

    private void save(Long resumeId){
        Favorite favorite = new Favorite();
        favorite.setObjectId(resumeId);
        favorite.setUserId(curUser().getId());
        favorite.setType(FavoriteType.RESUME.value);
        favoriteService.save(favorite);
    }

    private List<Favorite> listByResumeId(Long resumeId){
        return favoriteService.listByObjectId(resumeId,FavoriteType.RESUME);
    }
}
