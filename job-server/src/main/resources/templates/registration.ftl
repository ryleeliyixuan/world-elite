<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css">
    <title>活动报名详情.${registration.title!""}</title>
    <style>
        .el-dialog {
            border-radius: 13px;
            padding: 20px 17px;
        }

        .content-container {
            width: 100%;
            padding: 12px 47px 35px;
        }

        .content-container .title-container {
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        .content-container .title-container .number {
            position: absolute;
            left: 0;
            transform: translateY(-50%);
            top: 50%;
            font-size: 14px;
            color: #333333;
        }

        .content-container .title-container .title {
            font-size: 21px;
            color: #333333;
        }

        .content-container .form-container .item {
            display: flex;
            align-items: center;
            margin-top: 10px;
        }

        .content-container .form-container .item .required {
            color: red;
            width: 12px;
        }

        .content-container .form-container .item .name {
            font-size: 16px;
            font-weight: 400;
            color: #666666;
            line-height: 22px;
        }

        .content-container .form-container .item .value {
            font-size: 16px;
            font-weight: 400;
            color: #333333;
            line-height: 22px;
        }

        .question1-container {
            margin-top: 18px;
        }

        .question1-line1 {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
        }

        .title {
            font-size: 16px;
            color: #666666;
            line-height: 22px;
        }

        .must {
            color: red;
            margin-right: 5px;
        }

        .question1-line2 {
            margin-top: 5px;
            margin-left: 12px;
        }

        .answer {
            font-size: 18px;
            color: #333333;
            line-height: 25px;
        }

        .question2-container {
            margin-top: 18px;
        }

        .question2-line1 {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
        }

        .title {
            font-size: 18px;
            color: #666666;
            line-height: 25px;
        }

        .must {
            color: red;
            margin-right: 5px;
        }

        .question2-line2 {
            display: flex;
            flex-wrap: wrap;
            margin-top: -4px;
            width: calc(100% + 70px);
        }

        .option-container {
            display: flex;
            align-items: flex-start;
            margin: 4px 70px 4px 0;
        }

        .icon {
            display: flex;
            align-items: center;
            height: 25px;
            flex-shrink: 0;
        }

        .options {
            margin-left: 10px;
            font-size: 18px;
            font-weight: 500;
            color: #666666;
            line-height: 25px;
        }

        .question3-container {
            margin-top: 18px;
        }

        .question3-line1 {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
        }

        .title {
            font-size: 18px;
            color: #666666;
            line-height: 25px;
        }

        .must {
            color: red;
            margin-right: 5px;
        }

        .question3-line2 {
            display: flex;
            flex-wrap: wrap;
            margin-top: -4px;
            width: calc(100% + 70px);
        }

        .option-container {
            display: flex;
            align-items: flex-start;
            margin: 4px 70px 4px 0;
        }

        .icon {
            display: flex;
            align-items: center;
            height: 25px;
            flex-shrink: 0;
        }

        .options {
            margin-left: 10px;
            font-size: 18px;
            font-weight: 500;
            color: #666666;
            line-height: 25px;
        }

        .question4-container {
            margin-top: 18px;
        }

        .question4-container .question4-line1 {
            display: flex;
            align-items: flex-start;
            justify-content: space-between;
        }

        .question4-container .question4-line1 .title {
            font-size: 18px;
            color: #666666;
            line-height: 25px;
        }

        .question4-container .question4-line1 .title  .must {
            color: red;
            margin-right: 5px;
        }

        .question4-line2 {
            margin-top: 4px;
            margin-left: 12px;
        }

        .question4-line2 .image {
            width: 200px;
            display: block;
        }

        .question4-line2 .link {
            font-size: 18px;
        }
    </style>
</head>
<body>
    <div class="dialog">
        <div class="content-container">
            <div class="title-container">
                <div class="number">No.${"0000"+registration.number}</div>
                <div class="title">${registration.title}</div>
            </div>
            <div class="form-container">
                <div class="item"><span class="required">*</span> <span class="name">姓名：</span> <span class="value">${registration.name!""}</span></div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">性别：</span>
                    <#if registration.gender == "1">
                        <span class="value">男</span>
                    </#if>
                    <#if registration.gender == "2">
                        <span class="value">女</span>
                    </#if>
                </div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">手机：</span>
                    <span class="value">${registration.phone!""}</span>
                </div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">邮箱：</span>
                    <span class="value">${registration.email!""}</span>
                </div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">学校：</span>
                    <span class="value">${registration.school!""}</span>
                </div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">专业：</span>
                    <span class="value">${registration.profession!""}</span>
                </div>
                <div class="item">
                    <span class="required">*</span>
                    <span class="name">学历：</span>
                    <span class="value">${registration.education.name!""}</span>
                </div>
                <div class="question-container">
                    <#list registration.questionnaireList as questionnaire>
                        <#if questionnaire == "1">
                            <div class="question1-container">
                                <div class="question1-line1">
                                    <div class="title"><span class="must">*</span>${questionnaire.title!""}</div>
                                </div>
                                <div class="question1-line2">
                                    <div class="answer">{{question.answer}}</div>
                                </div>
                            </div>
                        </#if>

                        <#if questionnaire == "2">
                            <div class="question2-container">
                                <div class="question2-line1">
                                    <div class="title"><span class="must">*</span>${questionnaire.title!""}</div>
                                </div>
                                <div class="question2-line2">
                                    <div class="option-container">
                                        <img v-if="option.checked" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iMTZweCIgaGVpZ2h0PSIxNnB4IiB2aWV3Qm94PSIwIDAgMTYgMTYiIHZlcnNpb249IjEuMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayI+CiAgICA8IS0tIEdlbmVyYXRvcjogU2tldGNoIDU4ICg4NDY2MykgLSBodHRwczovL3NrZXRjaC5jb20gLS0+CiAgICA8dGl0bGU+5Y2V6YCJIOmAieS4reWkh+S7vSA0PC90aXRsZT4KICAgIDxkZXNjPkNyZWF0ZWQgd2l0aCBTa2V0Y2guPC9kZXNjPgogICAgPGRlZnM+CiAgICAgICAgPHJlY3QgaWQ9InBhdGgtMSIgeD0iMzYwIiB5PSIxMTciIHdpZHRoPSIxMjAwIiBoZWlnaHQ9IjcwMCI+PC9yZWN0PgogICAgICAgIDxmaWx0ZXIgeD0iLTIuNSUiIHk9Ii0zLjglIiB3aWR0aD0iMTA1LjElIiBoZWlnaHQ9IjEwOC43JSIgZmlsdGVyVW5pdHM9Im9iamVjdEJvdW5kaW5nQm94IiBpZD0iZmlsdGVyLTIiPgogICAgICAgICAgICA8ZmVNb3JwaG9sb2d5IHJhZGl1cz0iMS41IiBvcGVyYXRvcj0iZGlsYXRlIiBpbj0iU291cmNlQWxwaGEiIHJlc3VsdD0ic2hhZG93U3ByZWFkT3V0ZXIxIj48L2ZlTW9ycGhvbG9neT4KICAgICAgICAgICAgPGZlT2Zmc2V0IGR4PSIwIiBkeT0iNCIgaW49InNoYWRvd1NwcmVhZE91dGVyMSIgcmVzdWx0PSJzaGFkb3dPZmZzZXRPdXRlcjEiPjwvZmVPZmZzZXQ+CiAgICAgICAgICAgIDxmZUdhdXNzaWFuQmx1ciBzdGREZXZpYXRpb249IjgiIGluPSJzaGFkb3dPZmZzZXRPdXRlcjEiIHJlc3VsdD0ic2hhZG93Qmx1ck91dGVyMSI+PC9mZUdhdXNzaWFuQmx1cj4KICAgICAgICAgICAgPGZlQ29sb3JNYXRyaXggdmFsdWVzPSIwIDAgMCAwIDAuNzQ5MDE5NjA4ICAgMCAwIDAgMCAwLjc4MDM5MjE1NyAgIDAgMCAwIDAgMC44NDMxMzcyNTUgIDAgMCAwIDAuMzEgMCIgdHlwZT0ibWF0cml4IiBpbj0ic2hhZG93Qmx1ck91dGVyMSI+PC9mZUNvbG9yTWF0cml4PgogICAgICAgIDwvZmlsdGVyPgogICAgPC9kZWZzPgogICAgPGcgaWQ9Iua0u+WKqOeJiOWdlyIgc3Ryb2tlPSJub25lIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+CiAgICAgICAgPGcgaWQ9IuaKpeWQjeihqCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTg2Mi4wMDAwMDAsIC01MjYuMDAwMDAwKSI+CiAgICAgICAgICAgIDxyZWN0IGlkPSLnn6nlvaIiIGZpbGw9IiNGNkY5RkMiIHg9IjAiIHk9IjAiIHdpZHRoPSIxOTIwIiBoZWlnaHQ9IjI2MTQiPjwvcmVjdD4KICAgICAgICAgICAgPGcgaWQ9IuiSmeeJiCI+CiAgICAgICAgICAgICAgICA8dXNlIGZpbGw9ImJsYWNrIiBmaWxsLW9wYWNpdHk9IjEiIGZpbHRlcj0idXJsKCNmaWx0ZXItMikiIHhsaW5rOmhyZWY9IiNwYXRoLTEiPjwvdXNlPgogICAgICAgICAgICAgICAgPHVzZSBmaWxsPSIjRkZGRkZGIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIHhsaW5rOmhyZWY9IiNwYXRoLTEiPjwvdXNlPgogICAgICAgICAgICA8L2c+CiAgICAgICAgICAgIDxnIGlkPSLljZXpgIkt6YCJ5Lit5aSH5Lu9LTUiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDg2Mi4wMDAwMDAsIDUyNi4wMDAwMDApIiBmaWxsLXJ1bGU9Im5vbnplcm8iPgogICAgICAgICAgICAgICAgPGcgaWQ9IuWNlemAiS3pgInkuK3lpIfku70tNCI+CiAgICAgICAgICAgICAgICAgICAgPHJlY3QgaWQ9IuefqeW9oiIgZmlsbD0iIzAwMDAwMCIgb3BhY2l0eT0iMCIgeD0iMCIgeT0iMCIgd2lkdGg9IjE2IiBoZWlnaHQ9IjE2Ij48L3JlY3Q+CiAgICAgICAgICAgICAgICAgICAgPHBhdGggZD0iTTgsMTYgQzMuNTgxNzIyLDE2IDAsMTIuNDE4Mjc4IDAsOCBDMCwzLjU4MTcyMiAzLjU4MTcyMiwwIDgsMCBDMTIuNDE4Mjc4LDAgMTYsMy41ODE3MjIgMTYsOCBDMTYsMTIuNDE4Mjc4IDEyLjQxODI3OCwxNiA4LDE2IFogTTgsMTUgQzExLjg2NTk5MzIsMTUgMTUsMTEuODY1OTkzMiAxNSw4IEMxNSw0LjEzNDAwNjc1IDExLjg2NTk5MzIsMSA4LDEgQzQuMTM0MDA2NzUsMSAxLDQuMTM0MDA2NzUgMSw4IEMxLDExLjg2NTk5MzIgNC4xMzQwMDY3NSwxNSA4LDE1IFogTTgsMTMgQzEwLjc2MTQyMzcsMTMgMTMsMTAuNzYxNDIzNyAxMyw4IEMxMyw1LjIzODU3NjI1IDEwLjc2MTQyMzcsMyA4LDMgQzUuMjM4NTc2MjUsMyAzLDUuMjM4NTc2MjUgMyw4IEMzLDEwLjc2MTQyMzcgNS4yMzg1NzYyNSwxMyA4LDEzIFoiIGlkPSLlvaLnirYiIGZpbGw9IiM0ODk1RUYiPjwvcGF0aD4KICAgICAgICAgICAgICAgIDwvZz4KICAgICAgICAgICAgPC9nPgogICAgICAgIDwvZz4KICAgIDwvZz4KPC9zdmc+" alt=""/>
                                        <img v-if="!option.checked" src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iMTZweCIgaGVpZ2h0PSIxNnB4IiB2aWV3Qm94PSIwIDAgMTYgMTYiIHZlcnNpb249IjEuMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayI+CiAgICA8IS0tIEdlbmVyYXRvcjogU2tldGNoIDU4ICg4NDY2MykgLSBodHRwczovL3NrZXRjaC5jb20gLS0+CiAgICA8dGl0bGU+5pyq6YCJ5Lit5aSH5Lu9IDc8L3RpdGxlPgogICAgPGRlc2M+Q3JlYXRlZCB3aXRoIFNrZXRjaC48L2Rlc2M+CiAgICA8ZyBpZD0i5rS75Yqo54mI5Z2XIiBzdHJva2U9Im5vbmUiIHN0cm9rZS13aWR0aD0iMSIgZmlsbD0ibm9uZSIgZmlsbC1ydWxlPSJldmVub2RkIj4KICAgICAgICA8ZyBpZD0i5oql5ZCN6KGoIiB0cmFuc2Zvcm09InRyYW5zbGF0ZSgtODYyLjAwMDAwMCwgLTQ0MC4wMDAwMDApIiBmaWxsLXJ1bGU9Im5vbnplcm8iPgogICAgICAgICAgICA8ZyBpZD0i5pyq6YCJ5Lit5aSH5Lu9LTE2IiB0cmFuc2Zvcm09InRyYW5zbGF0ZSg4NjIuMDAwMDAwLCA0NDAuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICA8ZyBpZD0i5pyq6YCJ5Lit5aSH5Lu9LTciPgogICAgICAgICAgICAgICAgICAgIDxyZWN0IGlkPSLnn6nlvaIiIGZpbGw9IiMwMDAwMDAiIG9wYWNpdHk9IjAiIHg9IjAiIHk9IjAiIHdpZHRoPSIxNiIgaGVpZ2h0PSIxNiI+PC9yZWN0PgogICAgICAgICAgICAgICAgICAgIDxwYXRoIGQ9Ik04LDEgQzExLjg2NTk5MzIsMSAxNSw0LjEzNDAwNjc1IDE1LDggQzE1LDExLjg2NTk5MzIgMTEuODY1OTkzMiwxNSA4LDE1IEM0LjEzNDAwNjc1LDE1IDEsMTEuODY1OTkzMiAxLDggQzEsNC4xMzQwMDY3NSA0LjEzNDAwNjc1LDEgOCwxIE04LDIuMjczNzM2NzVlLTEzIEMzLjU4MTcyMiwyLjI3MzczNjc1ZS0xMyAwLDMuNTgxNzIyIDAsOCBDMCwxMi40MTgyNzggMy41ODE3MjIsMTYgOCwxNiBDMTIuNDE4Mjc4LDE2IDE2LDEyLjQxODI3OCAxNiw4IEMxNiwzLjU4MTcyMiAxMi40MTgyNzgsMi4yNzM3MzY3NWUtMTMgOCwyLjI3MzczNjc1ZS0xMyBaIiBpZD0i5b2i54q2IiBmaWxsPSIjNDg5NUVGIj48L3BhdGg+CiAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgIDwvZz4KICAgICAgICA8L2c+CiAgICA8L2c+Cjwvc3ZnPg==" alt=""/>
                                        <!-- <svg-icon :icon-class="option.checked?'apply-table-selected':'apply-table-unselected'" class-name="icon"></svg-icon> -->
                                        <div class="options">{{option.options}}</div>
                                    </div>
                                </div>
                            </div>
                        </#if>

                        <#if questionnaire == "3">
                            <div class="question3-container">
                                <div class="question3-line1">
                                    <div class="title"><span class="must">*</span>${questionnaire.title!""}</div>
                                </div>
                                <div class="question3-line2">
                                    <div class="option-container">
                                        <svg-icon class-name="icon"></svg-icon>
                                        <div class="options">{{option.options}}</div>
                                    </div>
                                </div>
                            </div>
                        </#if>

                        <#if questionnaire == "4">
                            <div class="question4-container">
                                <div class="question4-line1">
                                    <div class="title"><span class="must">*</span>${questionnaire.title!""}</div>
                                </div>
                                <div class="question4-line2">
                                        {{question.annexUrl}}
                                </div>
                            </div>
                        </#if>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
