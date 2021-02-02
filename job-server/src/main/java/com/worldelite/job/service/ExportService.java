package com.worldelite.job.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.worldelite.job.context.config.DomainConfig;
import com.worldelite.job.entity.Download;
import com.worldelite.job.entity.Resume;
import com.worldelite.job.exception.ServiceException;
import com.worldelite.job.form.IExportable;
import com.worldelite.job.form.RegistrationExportForm;
import com.worldelite.job.form.RegistrationListForm;
import com.worldelite.job.mapper.ResumeMapper;
import com.worldelite.job.mapper.UserApplicantMapper;
import com.worldelite.job.mq.ExportMessage;
import com.worldelite.job.mq.ExportTaskHandler;
import com.worldelite.job.service.excel.RegistrationExcel;
import com.worldelite.job.vo.ApiCode;
import com.worldelite.job.vo.PageResult;
import com.worldelite.job.vo.RegistrationExportVo;
import com.worldelite.job.vo.RegistrationVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Slf4j
@Service
public class ExportService extends BaseService {

    //最大 excel 记录
    @Value("${export.excel.max.count}")
    private Long excelMaxCount;

    @Value("${export.pdf.host}")
    private String exportHost;

    @Autowired
    private FileService fileService;

    @Autowired
    private DomainConfig domainConfig;

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    private UserApplicantMapper userMapper;

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ExportTaskHandler exportTaskHandler;

    @Autowired
    private RegistrationService registrationService;

    /**
     * 导出PDF简历
     *
     * @param resumeId
     * @return
     */
    public String exportResumeToPdf(Long resumeId) {
        Resume resume = resumeMapper.selectByPrimaryKey(resumeId);
        if(resume == null){
            throw new ServiceException(ApiCode.OBJECT_NOT_FOUND);
        }
        //UserApplicant loginUser = userMapper.selectByPrimaryKey(curUser().getId());
        final String resumeTplUrl = String.format("%s/resume/%s",
                exportHost, resumeId);
        File resumePdfFile = null;
        try {
            resumePdfFile = fileService.getFile(UUID.randomUUID().toString() + ".pdf");
            final String exportCommand = String.format("wkhtmltopdf --cookie web_user_token %s %s %s",
                    curUser().getToken(),resumeTplUrl, resumePdfFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(exportCommand);
            log.info("export resume: " + exportCommand);
            if(process.waitFor(100, TimeUnit.SECONDS)){
                return resumePdfFile.getName();
            }else{
                throw new ServiceException(message("export.resume.fail: exceed timeout"));
            }
        } catch (Exception ex) {
            FileUtil.del(resumePdfFile);
            log.error("导出简历失败:" + resumeId, ex);
            throw new ServiceException(message("export.resume.fail"));
        }
    }

    /**
     * 导出PDF活动报名表
     *
     * @param registrationId
     * @return
     */
    public String exportRegistrationToPdf(Integer registrationId) {
        final String registrationTplUrl = String.format("%s/activity/apply/%s",
                exportHost, registrationId);
        File registrationPdfFile = null;
        try {
            registrationPdfFile = fileService.getFile(UUID.randomUUID().toString() + ".pdf");
            final String exportCommand = String.format("wkhtmltopdf" +
                    " --cookie web_user_token %s %s %s", curUser().getToken(),registrationTplUrl, registrationPdfFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec(exportCommand);
            log.info("export registration: " + exportCommand);
            if(process.waitFor(100, TimeUnit.SECONDS)){
                return registrationPdfFile.getName();
            }else{
                throw new ServiceException("export.registration.fail: exceed timeout");
            }
        } catch (Exception ex) {
            FileUtil.del(registrationPdfFile);
            log.error("导出活动报名信息失败:" + registrationId, ex);
            throw new ServiceException(message("export.registration.fail"));
        }
    }

    /**
     * 导出活动信息和简历信息
     * @return
     */
    public RegistrationExportVo exportRegistrationWithResume(RegistrationExportForm registrationExportForm){
        try {
            return exportRegistrationToExcel(registrationExportForm);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("导出报名信息失败：{}",e.getMessage());
            throw new ServiceException("导出报名信息失败");
        }
    }

    public String exportRegistrationToExcel(RegistrationListForm listForm){
        final String excelFileName = listForm.genExportExcelName();
        final File excelFile = fileService.getFile(excelFileName);
        if (excelFile.exists()) {
            return excelFileName;
        }

        PageResult<RegistrationVo> pageResult;
        List<RegistrationExcel> excelData = new ArrayList<>();

        long exportRecordCount = 0;

        do {
            pageResult = registrationService.getRegistrationList(listForm);
            for (RegistrationVo registrationVo : pageResult.getList()) {
                excelData.add(toRegistrationExcel(registrationVo));
                exportRecordCount++;
            }
            listForm.setPage(listForm.getPage() + 1);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), RegistrationExcel.class).sheet().doWrite(excelData);

        return excelFileName;
    }

    public RegistrationExportVo exportRegistrationToExcel(RegistrationExportForm registrationExportForm) throws IOException, InterruptedException {
        final String excelFileName = registrationExportForm.genExportExcelName();
        final File excelFile = fileService.getFile(excelFileName);
        List<String> registrationPdfList = new ArrayList<>();
        List<String> resumePdfList = new ArrayList<>();

        PageResult<RegistrationVo> pageResult;
        List<RegistrationExcel> excelData = new ArrayList<>();

        long exportRecordCount = 0;

        do {
            pageResult = registrationService.getRegistrationList(registrationExportForm);
            for (RegistrationVo registrationVo : pageResult.getList()) {
                if("0".equals(registrationExportForm.getNameFlag())){
                    registrationVo.setName("");
                }
                if("0".equals(registrationExportForm.getGenderFlag())){
                    registrationVo.setGender("");
                }
                if("0".equals(registrationExportForm.getPhoneFlag())){
                    registrationVo.setPhone("");
                }
                if("0".equals(registrationExportForm.getEmailFlag())){
                    registrationVo.setEmail("");
                }
                if("0".equals(registrationExportForm.getSchoolFlag())){
                    registrationVo.setSchool("");
                }
                if("0".equals(registrationExportForm.getProfessionFlag())){
                    registrationVo.setProfession("");
                }
                if("0".equals(registrationExportForm.getGradeFlag())){
                    registrationVo.setGrade("");
                }
                if("0".equals(registrationExportForm.getEducationFlag())){
                    registrationVo.setEducation(null);
                }
                if(!"0".equals(registrationExportForm.getResumePdfFlag())){
                    if(registrationVo.getResumeId() != null){
                        resumePdfList.add(exportResumeToPdf(registrationVo.getResumeId()));
                    }
                }
                if(!"0".equals(registrationExportForm.getRegistrationPdfFlag())){
                    registrationPdfList.add(exportRegistrationToPdf(registrationVo.getId()));
                }
                excelData.add(toRegistrationExcel(registrationVo));
                exportRecordCount++;
            }
            registrationExportForm.setPage(registrationExportForm.getPage() + 1);
        } while (pageResult.getHasMore() && exportRecordCount < excelMaxCount);

        EasyExcel.write(fileService.getFile(excelFileName), RegistrationExcel.class).sheet().doWrite(excelData);

        RegistrationExportVo registrationExportVo = new RegistrationExportVo();
        registrationExportVo.setRegistrationExcel(excelFileName);
        if(!"0".equals(registrationExportForm.getRegistrationPdfFlag())){
            registrationExportVo.setRegistrationPdf(toZipFile(registrationPdfList));
        }
        if(!"0".equals(registrationExportForm.getResumePdfFlag())){
            registrationExportVo.setResumePdf(toZipFile(resumePdfList));
        }
        return registrationExportVo;
    }

    private RegistrationExcel toRegistrationExcel(RegistrationVo registrationVo) {
        RegistrationExcel registrationExcel = new RegistrationExcel();
        BeanUtil.copyProperties(registrationVo,registrationExcel);
        registrationExcel.setNumber("");
        registrationExcel.setEducation("");
        registrationExcel.setStatus("");
        return registrationExcel;
    }

    /**
     * 把导出excel任务放进队列
     *
     * @param messageType
     * @param exportForm
     */
    public void queueExportExcel(Byte messageType, IExportable exportForm){
        final String excelKey = exportForm.genExportExcelName();
        final String lockFlag = curUser().getId() + excelKey;
        if(stringRedisTemplate.opsForValue().get(lockFlag) != null){
            throw new ServiceException(message("export.excel.task.running"));
        }
        stringRedisTemplate.opsForValue().set(lockFlag, "true", 1, TimeUnit.MINUTES);
        Download download = downloadService.getDownloadByUrl(excelKey);
        if(download != null){
            throw new ServiceException(message("export.excel.task.exists"));
        }
        ExportMessage exportMessage = new ExportMessage(messageType, curUser().getId(), JSON.toJSONString(exportForm));
        exportTaskHandler.sendExportMessage(exportMessage);
    }

    /**
     * 将临时文件夹下一些文件打包成zip包
     * @param keyList
     * @return
     */
    private String toZipFile(List<String> keyList) throws InterruptedException, IOException {
        //创建一个名字随机的文件夹，用来存放要打包的文件
        File zipDir = fileService.getFile(UUID.randomUUID().toString());
        zipDir.mkdir();
        //将要打包的文件移动到创建好的文件夹
        StringBuffer mvCommand = new StringBuffer();
        mvCommand.append("mv ");
        for(String fileName:keyList){
            mvCommand.append(fileName);
            mvCommand.append(" ");
        }
        mvCommand.append("-t ");
        mvCommand.append(zipDir.getName());
        log.info("移动文件: " + mvCommand.toString());
        Process process = Runtime.getRuntime().exec(mvCommand.toString(),null,zipDir.getParentFile());
        if(!process.waitFor(20, TimeUnit.SECONDS)){
            throw new ServiceException("移动文件命令超时");
        }
        //打包成zip文件
        String zipCommand = "zip -q -r "+zipDir.getName()+".zip "+zipDir.getName();
        log.info("打包文件: " + zipCommand);
        process = Runtime.getRuntime().exec(zipCommand,null,zipDir.getParentFile());
        if(!process.waitFor(20, TimeUnit.SECONDS)){
            throw new ServiceException("打包文件命令超时");
        }
        //删除原文件夹
        fileService.deleteFile(zipDir.getName());
        return zipDir.getName()+".zip";
    }
}
