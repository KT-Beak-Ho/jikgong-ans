package com.billcorea.jikgong.api.repository.company.main.projectList.projectDetail

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectWorker

interface ProjectDetailRepository {
    suspend fun getProjectById(projectId: String): SimpleProject?
    suspend fun getWorkDaysForProject(projectId: String): List<WorkDay>
    suspend fun getProjectWorkers(projectId: String): List<ProjectWorker>
    suspend fun getPaymentStatus(projectId: String): Map<String, Boolean>
    suspend fun updatePaymentStatus(workDayId: String, isCompleted: Boolean): Boolean
    suspend fun deleteWorkDay(workDayId: String): Boolean
    suspend fun updateWorkDay(workDay: WorkDay): Boolean
    suspend fun createWorkDay(projectId: String, workDay: WorkDay): Boolean
    suspend fun getApplicantsForWorkDay(workDayId: String): Int
}