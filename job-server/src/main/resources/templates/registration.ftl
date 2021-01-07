<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <title>活动报名详情.${registration.templateName!""}</title>
    <style>
        .registration-number{
            color: #ccc;
            text-align: left;
        }
        .registration-body{
            width: 1000px;
            height: 1445px;
            padding: 20px;
            font-size: 20px;
            text-align: center;
        }
        .registration-label{
            text-align: right;
        }
        .registration-value{
            text-align: left;
        }
        .questionnaire{
            text-align: left;
        }
    </style>
</head>
<body>
    <div class="registration-body">
        <p class="registration-number">No.${registration.number!""}</p>
        <h3>${registration.title}</h3>
        <div class="row">
            <#if registration.nameFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.nameFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    姓名：
                </div>
                <div class="col-md-3 registration-value">${registration.name!""}</div>
            </#if>

            <#if registration.genderFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.genderFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    性别：
                </div>
                <div class="col-md-3 registration-value">${registration.gender!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registration.phoneFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.phoneFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    手机号：
                </div>
                <div class="col-md-9 registration-value">${registration.phone!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registration.emailFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.emailFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    邮箱：
                </div>
                <div class="col-md-9 registration-value">${registration.email!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registration.schoolFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.schoolFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    学校：
                </div>
                <div class="col-md-9 registration-value">${registration.school!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registration.professionFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.professionFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    专业：
                </div>
                <div class="col-md-9 registration-value">${registration.profession!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registration.gradeFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.gradeFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    年级：
                </div>
                <div class="col-md-3 registration-value">${registration.grade!""}</div>
            </#if>

            <#if registration.educationFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registration.educationFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    学历：
                </div>
                <div class="col-md-3 registration-value">${registration.education!""}</div>
            </#if>
        </div>

        <#list registration.questionnaireList as questionnaire>
            <div class="row">
                <div class="col-md-3 registration-label">
                    <#if questionnaire.mustAnswer == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    ${questionnaire.title!""}：
                </div>
            </div>
            <#list questionnaire.answerContent as content>
                <div class="row">
                    <div class="col-md-12">${content}</div>
                </div>
            </#list>
            <#list questionnaire.answerOptions as options>
                <div class="row">
                    <div class="col-md-12">${options.options}</div>
                </div>
            </#list>
        </#list>
    </div>
</body>
</html>