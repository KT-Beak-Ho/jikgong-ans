package com.billcorea.jikgong.api.repository.worker.main.projectList

import com.billcorea.jikgong.api.models.common.ApiResult
import com.billcorea.jikgong.api.models.worker.main.projectList.ApplyProjectResponse

interface ProjectListRepository {
  // suspend fun requestProjectDetail(jobPostId: Int): ApiResult<ProjectDetailResponse>
  suspend fun requestApply(jobPostId: Int, workDateList: List<String>): ApiResult<ApplyProjectResponse>
}