<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <title>简历详情.${resume.name}</title>
    <style>
        .resume-body {
            width: 1000px;
            height: 1445px;
            padding: 20px;
            font-size: 20px;
        }

        .text-name {
            font-size: 30px;
            font-weight: bold;
        }

        .avatar {
            border-radius: 5px;
            color: #8c939d;
            width: 160px;
            line-height: 160px;
            text-align: center;
        }

        .desc-text {
            line-height: 30px;
        }

        .resume-title {
            font-size: 28px;
            font-weight: bold;
            padding-bottom: 10px;
            border-bottom: #CCC solid 1px;
        }

        .tag {
            padding: 3px 12px;
            border: #ccc solid 1px;
            border-radius: 10px;
            font-size: 18px;
            color: #888;
        }

        .tag:not(:first-child) {
            margin-left: 15px;
        }

        .text-post {
            font-size: 18px;
            font-weight: bold;
        }

        .ml-2 {
            margin-left: 15px;
        }

        .ml-3 {
            margin-left: 20px;
        }

        .mt-1 {
            margin-top: 10px;
        }

        .mt-2 {
            margin-top: 15px;
        }

        .mt-3 {
            margin-top: 20px;
        }

        .mb-2 {
            margin-bottom: 15px;
        }

        .mr-3 {
            margin-right: 20px;
        }
    </style>
</head>
<body>
<div class="resume-body">
    <div class="media">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-3">
                    <img class="avatar" src="${(resume.avatar)!'暂无照片'}">
                </div>
                <div class="col-md-9">
                    <div class="container-fluid">
                        <div class="row mb-2">
                            <div class="col-md-12"><h3 class="media-heading text-name">${resume.name}</h3></div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <span>性别： ${(resume.gender == 1)?string("男","女")}</span>
                            </div>
                            <div class="col-md-6">
                        <span>政治面貌：
                            <#if resume.maritalStatus == 0>中共党员（含预备党员）</#if>
                            <#if resume.maritalStatus == 1>民主党派</#if>
                            <#if resume.maritalStatus == 2>无党派人士</#if>
                            <#if resume.maritalStatus == 3>团员</#if>
                            <#if resume.maritalStatus == 4>群众</#if>
                        </span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <span>年龄：<#if resume.age??>${resume.age}岁</#if></span>
                            </div>
                            <div class="col-md-6">
                                <span>现居地址：<#if resume.curPlace??>${resume.curPlace}</#if></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <span>邮箱：<#if resume.email??>${resume.email}</#if></span>
                            </div>
                            <div class="col-md-6">
                                <span>入职时间：<#if resume.returnTime??>${resume.returnTime?string("yyyy-MM")}</#if></span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12"><span>手机：<#if resume.phone??>${resume.phone}</#if></span></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if resume.resumeEduList?? && resume.resumeEduList?size != 0>
        <div class="edu-list mt-3">
            <h4 class="resume-title">教育经历</h4>
            <#list resume.resumeEduList as edu>
                <div class="container-fluid mt-3">
                    <div class="row">
                        <div class="col-md-6">
                            <span><#if edu.schoolName??><b>${edu.schoolName}</b></#if></span>
                        </div>
                        <div class="col-md-6">
                            <span>
                                <#if edu.startTime?? && edu.finishTime??>
                                    <#if edu.startTime?string("yyyy-MM") == edu.finishTime?string("yyyy-MM")>
                                        在读
                                    <#else >
                                        ${edu.startTime?string("yyyy-MM")} 至 ${edu.finishTime?string("yyyy-MM")}
                                    </#if>
                                </#if>
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <span><b>学历：</b><#if edu.degree??>${(edu.degree.name)!''}</#if></span>
                        </div>
                        <div class="col-md-6">
                            <span><b>GPA：</b>${(edu.gpa)!''}</span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <span><b>专业：</b>${(edu.majorName)!''}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

    <#if resume.userExpectJob?? && resume.userExpectJob.expectPosition?? >
        <div class="edu-list mt-3">
            <h4 class="resume-title">求职意向</h4>
            <div class="container-fluid mt-3">
                <div class="row">
                    <div class="col-md-12">
                        <span><b>${resume.userExpectJob.expectPosition}</b></span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <span><b>预期薪资：</b>
                        <#if resume.userExpectJob.salaryId??>
                            <#list resume.userExpectJob.salaryId as salary>
                                <#if salary=='1028'>2K以下</#if>
                                <#if salary=='1029'>2K-5K</#if>
                                <#if salary=='1031'>5K-8K</#if>
                                <#if salary=='1032'>8K-10K</#if>
                                <#if salary=='1033'>10K-15K</#if>
                                <#if salary=='1034'>15K-20K</#if>
                                <#if salary=='1035'>20K-30K</#if>
                                <#if salary=='1036'>30K-50K</#if>
                                <#if salary=='1037'>50K以上</#if>
                                <#if salary=='1038'>面议</#if>
                            </#list>
                        </#if>
                        </span>
                    </div>
                    <div class="col-md-6">
                        <span><b>工作城市：</b>${(resume.userExpectJob.expectCity)!''}</span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <span><b>工作类型：</b>${(resume.userExpectJob.expectWorkType)!''}</span>
                    </div>
                    <div class="col-md-6">
                        <span><b>期望行业：</b><#if resume.userExpectJob.category??>${(resume.userExpectJob.category.name)!''}</#if></span>
                    </div>
                </div>
            </div>
        </div>
    </#if>

    <#if resume.resumeExpList?? && resume.resumeExpList?size != 0>
        <div class="exp-list mt-3">
            <h4 class="resume-title">工作/实习经历</h4>
            <#list resume.resumeExpList as exp>
                <div class="container-fluid mt-3">
                    <div class="row">
                        <div class="col-md-12">
                            <span class="mr-3"><b>${(exp.company)!''}</b></span>
                            <span><b>${(exp.workType)!''}.${(exp.post)!''}</b></span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <span><b>行业：</b>${(exp.industry)!''}</span>
                        </div>
                        <div class="col-md-6">
                            <span><b>在职时间：</b>
                                 <#if exp.onWork?? && exp.onWork == 1>
                                     在职
                                 </#if>
                                <#if exp.startTime?? && exp.finishTime??>
                                    ${exp.startTime?string("yyyy-MM")} 至 ${exp.finishTime?string("yyyy-MM")}
                                </#if>
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <span><b>工作描述：</b>${(exp.description)!''}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

    <#if resume.resumePracticeList?? && resume.resumePracticeList?size != 0>
        <div class="practice-list mt-3">
            <h4 class="resume-title">项目/其它经历</h4>
            <#list resume.resumePracticeList as practice>
                <div class="container-fluid mt-3">
                    <div class="row">
                        <div class="col-md-12">
                            <span class="mr-3"><b>${(practice.title)!''}</b></span>
                            <span>
                                <b>
                                    <#if practice.onWork?? && practice.onWork == 1>
                                        项目进行中
                                    </#if>
                                    <#if practice.startTime?? && practice.finishTime??>
                                        ${practice.startTime?string("yyyy-MM")} 至 ${practice.finishTime?string("yyyy-MM")}
                                    </#if>
                                </b>
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <span><b>介绍：</b>${(practice.description)!''}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

    <#if resume.resumeLanguageList?? && resume.resumeLanguageList?size != 0>
        <div class="practice-list mt-3">
            <h4 class="resume-title">语言能力</h4>
            <#list resume.resumeLanguageList as language>
                <div class="container-fluid mt-3">
                    <div class="row">
                        <div class="col-md-6">
                            <span><b>语种${language_index+1}：</b>${(language.title)!''}</span>
                        </div>
                        <div class="col-md-6">
                            <span><b>证书或分数：</b>${(language.description)!''}</span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

    <#if resume.resumeCertificateList?? && resume.resumeCertificateList?size != 0>
        <div class="practice-list mt-3">
            <h4 class="resume-title">证书/奖项</h4>
            <#list resume.resumeCertificateList as awards>
                <div class="container-fluid mt-3">
                    <div class="row">
                        <div class="col-md-6">
                            <span><b>证书/奖项名称：</b>${(awards.title)!''}</span>
                        </div>
                        <div class="col-md-6">
                            <span><b>获得时间：</b><#if awards.time??>${awards.time?string("yyyy-MM")}</#if></span>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </#if>

    <div class="introdcution mt-3">
        <h4 class="resume-title">个人介绍</h4>
        <div class="container-fluid mt-3">
            <div class="row">
                <div class="col-md-12">
                    <#if resume.resumeSkillList?? && resume.resumeSkillList?size != 0>
                        <div>
                            <#list resume.resumeSkillList as skill>
                                <span class="tag">${(skill.name)!''}</span>
                            </#list>
                        </div>
                    </#if>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p class="desc-text mt-2">${(resume.introduction)!''}</p>
                </div>
            </div>
        </div>
    </div>


    <#--<#if resume.resumeLinkList?? && resume.resumeLinkList?size != 0>
        <p class="mt-1"><b>社交平台：</b>
            <#list resume.resumeLinkList as link>
                <a href="${(link.link)!''}" class="ml-2">${(link.name)!''}</a>
            </#list>
        </p>
    </#if>-->
</div>
</body>
</html>