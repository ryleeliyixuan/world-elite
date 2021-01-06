<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <title>活动报名详情.${registrationWithTemplate.template.templateName!""}</title>
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
        <p class="registration-number">No.${registrationWithTemplate.registration.number!""}</p>
        <h3>${registrationWithTemplate.template.title}</h3>
        <div class="row">
            <#if registrationWithTemplate.template.nameFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.nameFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    姓名：
                </div>
                <div class="col-md-3 registration-value">${registrationWithTemplate.registration.name!""}</div>
            </#if>

            <#if registrationWithTemplate.template.genderFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.genderFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    性别：
                </div>
                <div class="col-md-3 registration-value">${registrationWithTemplate.registration.gender!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registrationWithTemplate.template.phoneFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.phoneFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    手机号：
                </div>
                <div class="col-md-9 registration-value">${registrationWithTemplate.registration.phone!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registrationWithTemplate.template.emailFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.emailFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    邮箱：
                </div>
                <div class="col-md-9 registration-value">${registrationWithTemplate.registration.email!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registrationWithTemplate.template.schoolFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.schoolFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    学校：
                </div>
                <div class="col-md-9 registration-value">${registrationWithTemplate.registration.school!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registrationWithTemplate.template.professionFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.professionFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    专业：
                </div>
                <div class="col-md-9 registration-value">${registrationWithTemplate.registration.profession!""}</div>
            </#if>
        </div>
        <div class="row">
            <#if registrationWithTemplate.template.gradeFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.gradeFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    年级：
                </div>
                <div class="col-md-3 registration-value">${registrationWithTemplate.registration.grade!""}</div>
            </#if>

            <#if registrationWithTemplate.template.educationFlag != "2">
                <div class="col-md-3 registration-label">
                    <#if registrationWithTemplate.template.educationFlag == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    学历：
                </div>
                <div class="col-md-3 registration-value">${registrationWithTemplate.registration.education!""}</div>
            </#if>
        </div>

        <#list registrationWithTemplate.template.questionnaireList as questionnaire>
            <div class="row">
                <div class="col-md-3 registration-label">
                    <#if questionnaire.mustAnswer == "0">
                        <font style="color:red;">*</font>
                    </#if>
                    ${questionnaire.title!""}：
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">回答</div>
            </div>
        </#list>
    </div>
</body>
</html>