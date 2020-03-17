package com.worldelite.job.service.excel;

import com.worldelite.job.form.*;

/**
 * @author yeguozhong yedaxia.github.com
 */
public interface IExportExcelService {

    /**
     * 导出用户列表
     *
     * @param listForm
     */
    String exportUserList(Long userId, UserListForm listForm);

    /**
     * 导出企业列表
     * @param listForm
     */
    String exportCompanyList(Long userId, CompanyListForm listForm);

    /**
     * 导出简历列表
     * @param linkForm
     */
    String exportResumeList(Long userId, ResumeListForm linkForm);

    /**
     * 导出工作列表
     * @param listForm
     */
    String exportJobList(Long userId, JobListForm listForm);
}
