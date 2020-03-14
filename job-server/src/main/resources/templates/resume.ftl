<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <title>简历详情.${resume.name}</title>
    <style>
        .resume-body{
            width: 1000px;
            height: 1445px;
            padding: 20px;
            font-size: 20px;
        }
        .text-name{
            font-size: 30px;
            font-weight: bold;
        }
        .user-avatar{
            width: 120px;
            height: 120px;
        }
        .desc-text{
            line-height: 30px;
        }
        .resume-title{
            font-size: 28px;
            font-weight: bold;
            padding-bottom: 10px;
            border-bottom: #CCC solid 1px;
        }
        .tag{padding: 3px 12px; border: #ccc solid 1px; border-radius: 10px; font-size: 18px; color: #888;}
        .tag:not(:first-child){margin-left: 15px;}
        .text-post{font-size: 18px; font-weight: bold;}
        .ml-2{margin-left: 15px;}
        .ml-3{margin-left: 20px;}
        .mt-1{margin-top: 10px;}
        .mt-2{margin-top: 15px;}
        .mt-3{margin-top: 20px;}
        .mb-2{margin-bottom: 15px;}
    </style>
</head>
<body>
<div class="resume-body">
    <div class="media">
        <div class="media-body">
            <h3 class="media-heading text-name">${resume.name}</h3>
            <p>${resume.graduateTime?string("yyyy-MM")}毕业 / 现居${resume.curPlace} / ${resume.returnTime?string("yyyy-MM")}回国 / ${resume.age}岁 </p>
            <p><i class="glyphicon glyphicon-phone-alt"></i> (+${resume.phoneCode})${resume.phone} <span class="ml-3">${resume.email}</span></p>
        </div>
        <div class="media-right">
            <img class="media-object user-avatar" src="${resume.avatar}">
        </div>
    </div>
    <#if resume.resumeEduList?? && resume.resumeEduList?size != 0>
    <div class="edu-list mt-3">
        <h4 class="resume-title">教育经历</h4>
        <#list resume.resumeEduList as edu>
        <div class="mt-3">
            <p><b>${edu.schoolName}</b><em class="ml-3 pull-right">${edu.startTime?string("yyyy-MM")} 到 ${edu.finishTime?string("yyyy-MM")}</em></p>
            <p>${edu.majorName} / ${(edu.degree.name)!''} / GPA ${edu.gpa}</p>
        </div>
        </#list>
    </div>
    </#if>
    <#if resume.resumeExpList?? && resume.resumeExpList?size != 0>
    <div class="exp-list mt-3">
        <h4 class="resume-title">工作经验</h4>
        <#list resume.resumeExpList as exp>
        <div class="mt-3">
            <p><b>${exp.company}</b><em class="ml-3 pull-right">${exp.startTime?string("yyyy-MM")} 到 ${exp.finishTime?string("yyyy-MM")}</em></p>
            <p class="text-post">${exp.depart} . ${exp.post}</p>
            <p class="desc-text">${exp.description}</p>
        </div>
        </#list>
    </div>
    </#if>
    <#if resume.resumePracticeList?? && resume.resumePracticeList?size != 0>
    <div class="practice-list mt-3">
        <h4 class="resume-title">实践经验</h4>
        <#list resume.resumePracticeList as practice>
        <div class="mt-3">
            <p><b>${practice.title}</b><em class="ml-3 pull-right">${practice.startTime?string("yyyy-MM")} 到 ${practice.finishTime?string("yyyy-MM")}</em></p>
            <p class="desc-text">${practice.description}</p>
        </div>
        </#list>
    </div>
    </#if>
    <div class="introdcution mt-3">
        <h4 class="resume-title">个人介绍</h4>
        <#if resume.resumeSkillList?? && resume.resumeSkillList?size != 0>
        <div>
            <#list resume.resumeSkillList as skill>
            <span class="tag">${skill.name}</span>
            </#list>
        </div>
        </#if>
        <p class="desc-text mt-2">${resume.introduction}</p>
        <#if resume.resumeLinkList?? && resume.resumeLinkList?size != 0>
        <p class="mt-1"><b>社交平台：</b>
            <#list resume.resumeLinkList as link>
            <a href="${link.link}" class="ml-2">${link.name}</a>
            </#list>
        </p>
        </#if>
    </div>
</div>
</body>
</html>