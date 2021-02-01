package com.worldelite.job.service;

import com.worldelite.job.constants.*;
import com.worldelite.job.entity.Activity;
import com.worldelite.job.entity.ActivityOptions;
import com.worldelite.job.entity.Dict;
import com.worldelite.job.event.ActivityInfoRefreshEvent;
import com.worldelite.job.form.ActivityListForm;
import com.worldelite.job.form.SearchNameForm;
import com.worldelite.job.mapper.ActivityMapper;
import com.worldelite.job.vo.ActivityVo;
import com.worldelite.job.vo.PageResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Nuo Xu
 */
@Service
@Slf4j
public class ActivitySearchService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private Analyzer analyzer;

    @Resource(name = "activityIndexWriter")
    private IndexWriter indexWriter;

    @Resource(name = "activitySearcherManager")
    private SearcherManager searcherManager;

    @SneakyThrows
    public PageResult<String> searchActivityTitle(SearchNameForm searchNameForm) {
        return searchActivityTitle(searchNameForm.getKeyWords(), searchNameForm.getPage(), searchNameForm.getSize());
    }

    @SneakyThrows
    public PageResult<String> searchActivityTitle(String keyWords, int page, int size) {
        List<String> arrayList = new ArrayList<>();
        IndexSearcher indexSearcher = null;
        try {
            searcherManager.maybeRefresh();
            indexSearcher = searcherManager.acquire();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new QueryParser(ActivityIndexFields.TITLE, analyzer).parse(keyWords), BooleanClause.Occur.MUST);

            ScoreDoc lastScoreDoc = getLastScoreDoc(builder.build(), indexSearcher, page, size);
            final TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, builder.build(), size);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final Document doc = indexSearcher.doc(scoreDoc.doc);

                arrayList.add(doc.get(ActivityIndexFields.TITLE));
            }

            PageResult<String> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(page);
            pageResult.setPageSize(size);
            pageResult.setList(removeDuplicate(arrayList));
            pageResult.calPageCountAndHasMore(arrayList);

            return pageResult;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (indexSearcher != null)
                searcherManager.release(indexSearcher);
        }
        return new PageResult<String>().emptyResult();
    }


    private List<String> removeDuplicate(List<String> list) {
        Set<String> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }


    /**
     * 获取指定搜索页的上一页ScoreDoc,以实现深度分页查询
     */
    private ScoreDoc getLastScoreDoc(Query query, IndexSearcher indexSearcher, int page, int size) throws IOException {
        if (page == 1) {
            return null;//如果是第一页返回空
        }
        int num = size * (page - 1);
        TopDocs tds = indexSearcher.search(query, num);
        if (tds.scoreDocs.length == 0) {
            return null;
        }
        return num <= tds.scoreDocs.length ? tds.scoreDocs[num - 1] : tds.scoreDocs[tds.scoreDocs.length - 1];
    }

    private ScoreDoc getLastScoreDoc(Query query, IndexSearcher indexSearcher, int page, int size, Sort sort) throws IOException {
        if (page == 1) {
            return null;//如果是第一页返回空
        }
        int num = size * (page - 1);
        TopDocs tds = indexSearcher.search(query, num, sort);
        if (tds.scoreDocs.length == 0) {
            return null;
        }
        return num <= tds.scoreDocs.length ? tds.scoreDocs[num - 1] : tds.scoreDocs[tds.scoreDocs.length - 1];
    }

    @EventListener
    public void createOrRefreshActivityListIndex(ActivityInfoRefreshEvent event) throws IOException {
        log.info("create or refresh activity info index. {}", event.toString());

        //更新指定面试官的信息
        if (event.getActivityId() != null) {
            final Activity activity = activityMapper.selectByPrimaryKey(event.getActivityId());
            //活动被删除
            if (activity == null) {
                indexWriter.deleteDocuments(new Term(ActivityIndexFields.ACTIVITY_ID, String.valueOf(event.getActivityId())));
                indexWriter.commit();

                return;
            }

            buildActivityIndex(activity);
        }
        //更新全部
        else {
            try {
                //在重建索引前执行全部删除
                indexWriter.deleteAll();

                ActivityOptions options = new ActivityOptions();
                options.setDelFlag(Bool.FALSE);
                final List<Activity> activities = activityMapper.selectAndList(options);
                if (activities != null && activities.size() > 0)
                    activities.forEach(this::buildActivityIndex);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                //重建索引出现异常,回滚deleteAll操作,保证搜索有结果可用
                indexWriter.rollback();
            }
        }

        //提交所有更新
        indexWriter.commit();
    }

    private void buildActivityIndex(Activity activity) {
        //仅对有效的活动创建索引
        if (activity.getStatus() == ActivityStatus.REVIEWING.value
                || activity.getStatus() == ActivityStatus.REVIEW_FAILURE.value
                || activity.getStatus() == ActivityStatus.DRAFT.value
                || activity.getStatus() == ActivityStatus.OFFLINE.value
                || activity.getDelFlag() == Bool.TRUE) return;

        Document doc = new Document();

        doc.add(new StringField(ActivityIndexFields.ACTIVITY_ID, String.valueOf(activity.getId()), Field.Store.YES));

        doc.add(new StringField(ActivityIndexFields.FORM, String.valueOf(activity.getForm()), Field.Store.YES));
        doc.add(new StringField(ActivityIndexFields.CITY_ID, String.valueOf(activity.getCityId()), Field.Store.YES));
        doc.add(new StringField(ActivityIndexFields.STATUS, String.valueOf(activity.getStatus()), Field.Store.YES));

        doc.add(new LongPoint(ActivityIndexFields.REGISTRATION_START_TIME, activity.getRegistrationStartTime().getTime()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.REGISTRATION_START_TIME, activity.getRegistrationStartTime().getTime()));
        doc.add(new LongPoint(ActivityIndexFields.REGISTRATION_FINISH_TIME, activity.getRegistrationFinishTime().getTime()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.REGISTRATION_FINISH_TIME, activity.getRegistrationFinishTime().getTime()));

        doc.add(new LongPoint(ActivityIndexFields.ACTIVITY_START_TIME, activity.getActivityStartTime().getTime()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.ACTIVITY_START_TIME, activity.getActivityStartTime().getTime()));
        doc.add(new LongPoint(ActivityIndexFields.ACTIVITY_FINISH_TIME, activity.getActivityFinishTime().getTime()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.ACTIVITY_FINISH_TIME, activity.getActivityFinishTime().getTime()));

        doc.add(new LongPoint(ActivityIndexFields.ACTIVITY_PUBLISH_TIME, activity.getCreateTime().getTime()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.ACTIVITY_PUBLISH_TIME, activity.getCreateTime().getTime()));

        doc.add(new IntPoint(ActivityIndexFields.FOLLOWER, activity.getFollower()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.FOLLOWER, activity.getFollower()));

        doc.add(new IntPoint(ActivityIndexFields.WEIGHT, activity.getWeight()));
        doc.add(new NumericDocValuesField(ActivityIndexFields.WEIGHT, activity.getWeight()));

        doc.add(new TextField(ActivityIndexFields.TITLE, activity.getTitle(), Field.Store.YES));

        doc.add(new StringField(ActivityIndexFields.USER_TYPE, String.valueOf(activity.getUserType()), Field.Store.YES));
        doc.add(new StringField(ActivityIndexFields.ONLY_OVERSEAS, String.valueOf(activity.getOnlyOverseasStudent()), Field.Store.YES));

        try {
            indexWriter.updateDocument(new Term(ActivityIndexFields.ACTIVITY_ID, String.valueOf(activity.getId())), doc);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public PageResult<ActivityVo> searchActivityList(ActivityListForm form) throws IOException {
        List<ActivityVo> activityVos = new ArrayList<>();
        IndexSearcher indexSearcher = null;
        try {
            searcherManager.maybeRefresh();
            indexSearcher = searcherManager.acquire();

            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            if (StringUtils.isNotBlank(form.getKeyword()))
                builder.add(new QueryParser(ActivityIndexFields.TITLE, analyzer).parse(form.getKeyword().trim()), BooleanClause.Occur.MUST);

            if (form.getForm() != null)
                builder.add(new TermQuery(new Term(ActivityIndexFields.FORM, String.valueOf(form.getForm()))), BooleanClause.Occur.MUST);

            if (form.getCityIds() != null) {
                BooleanQuery.Builder query = new BooleanQuery.Builder();
                for (String cityId : form.getCityIds()) {
                    query.add(new TermQuery(new Term(ActivityIndexFields.CITY_ID, cityId)), BooleanClause.Occur.SHOULD);
                }

                builder.add(query.build(), BooleanClause.Occur.MUST);
            }

            if (form.getStatus() != null && form.getStatus().length > 0) {
                for (String status : form.getStatus()) {
                    builder.add(new TermQuery(new Term(ActivityIndexFields.STATUS, status)), BooleanClause.Occur.MUST);
                }
            }

            if (form.getTimeId() != null) {
                final Dict dict = dictService.getDict(form.getTimeId());

                if (dict != null) {
                    int value = Integer.parseInt(dict.getValue());
                    final long endTime = TimeUnit.DAYS.toMillis(value);

                    builder.add(LongPoint.newRangeQuery(ActivityIndexFields.ACTIVITY_START_TIME, System.currentTimeMillis(), System.currentTimeMillis() + endTime), BooleanClause.Occur.MUST);
                }
            }

            if (StringUtils.isNotBlank(form.getPublisherType())) {
                if (form.getPublisherType().equals(String.valueOf(PublisherType.OVERSEAS.value))) {
                    builder.add(new TermQuery(new Term(ActivityIndexFields.ONLY_OVERSEAS, String.valueOf(Bool.TRUE))), BooleanClause.Occur.MUST);
                } else {
                    builder.add(new TermQuery(new Term(ActivityIndexFields.USER_TYPE, form.getPublisherType())), BooleanClause.Occur.MUST);
                }
            }

            Sort sort = new Sort();
            List<SortField> sortFieldList = new ArrayList<>();
            //排序
            if (StringUtils.isNotBlank(form.getSortField())) {

                if (ActivitySort.PUBLISH_TIME == ActivitySort.valueOf(form.getSortField().toUpperCase())) {
                    sortFieldList.add(new SortField(ActivityIndexFields.ACTIVITY_PUBLISH_TIME, SortField.Type.LONG, true));
                }
                if (ActivitySort.FOLLOWER == ActivitySort.valueOf(form.getSortField().toUpperCase())) {
                    sortFieldList.add(new SortField(ActivityIndexFields.FOLLOWER, SortField.Type.INT, true));
                }
                //活动报名结束时间大于当前时间，按活动开始报名时间排序
                if (ActivitySort.REGISTRATION_TIME == ActivitySort.valueOf(form.getSortField().toUpperCase())) {
                    sortFieldList.add(new SortField(ActivityIndexFields.REGISTRATION_START_TIME, SortField.Type.LONG, false));
                    builder.add(LongPoint.newRangeQuery(ActivityIndexFields.REGISTRATION_FINISH_TIME, System.currentTimeMillis(), Long.MAX_VALUE), BooleanClause.Occur.MUST);
                }

                //活动结束时间大于当前时间，按活动开始时间排序
                if (ActivitySort.ACTIVITY_TIME == ActivitySort.valueOf(form.getSortField().toUpperCase())) {
                    sortFieldList.add(new SortField(ActivityIndexFields.ACTIVITY_START_TIME, SortField.Type.LONG, false));
                    builder.add(LongPoint.newRangeQuery(ActivityIndexFields.ACTIVITY_FINISH_TIME, System.currentTimeMillis(), Long.MAX_VALUE), BooleanClause.Occur.MUST);
                }
            }

            sortFieldList.add(new SortField(ActivityIndexFields.WEIGHT, SortField.Type.INT, true));

            sort.setSort(sortFieldList.toArray(new SortField[0]));

            //如果没有查询条件时构造一个通配符查询全部
            if (CollectionUtils.isEmpty(builder.build().clauses())) {
                builder.add(new WildcardQuery(new Term(ActivityIndexFields.ACTIVITY_ID, "*")), BooleanClause.Occur.SHOULD);
            }

            ScoreDoc lastScoreDoc = getLastScoreDoc(builder.build(), indexSearcher, form.getPage(), form.getLimit(), sort);
            final TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc, builder.build(), form.getLimit(), sort);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                final Document doc = indexSearcher.doc(scoreDoc.doc);

                final String activityId = doc.get(ActivityIndexFields.ACTIVITY_ID);
                final ActivityVo activityInfo = activityService.getActivityInfo(Integer.valueOf(activityId));
                if (activityInfo == null) {
                    log.warn("数据库中没有{}的活动信息,索引可能需要刷新", activityId);
                    continue;
                }

                activityVos.add(activityInfo);
            }

            PageResult<ActivityVo> pageResult = new PageResult<>();
            pageResult.setTotal(topDocs.totalHits);
            pageResult.setCurrentPage(form.getPage());
            pageResult.setPageSize(form.getLimit());
            pageResult.setList(activityVos);
            pageResult.calPageCountAndHasMore(activityVos);

            return pageResult;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (indexSearcher != null)
                searcherManager.release(indexSearcher);
        }
        return new PageResult<ActivityVo>().emptyResult();
    }
}
