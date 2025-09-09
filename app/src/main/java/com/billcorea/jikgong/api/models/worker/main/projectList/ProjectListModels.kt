package com.billcorea.jikgong.api.models.worker.main.projectList


// 모집공고 상세보기

data class ProjectDetailRequest(
  var jobPostId: Int
)

// data class ProjectDetailResponse

// 지원하기

data class ApplyProjectRequest(
  var jobPostId: Int,
  var workerDateList: List<String>
)

data class ApplyProjectResponse(
  var data: List<String>?, // 임시로 지정
  var message: String
)