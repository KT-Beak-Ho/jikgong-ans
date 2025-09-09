package com.billcorea.jikgong.api.repository.company.main.projectList.projectDetail

import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectWorker
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import kotlinx.coroutines.delay

class ProjectDetailRepositoryImpl : ProjectDetailRepository {
    
    // 임시 데이터 저장소 (실제로는 Room DB나 Remote API 사용)
    private val paymentStatusCache = mutableMapOf<String, Boolean>()
    private val workDaysCache = mutableListOf<WorkDay>()
    
    init {
        // 초기 샘플 데이터 설정
        paymentStatusCache["9"] = true
        paymentStatusCache["10"] = false
        paymentStatusCache["11"] = true
        paymentStatusCache["12"] = false
    }
    
    override suspend fun getProjectById(projectId: String): SimpleProject? {
        delay(300) // 네트워크 시뮬레이션
        
        val baseProject = CompanyMockDataFactory.getProjectById(projectId)
        return if (baseProject != null) {
            SimpleProject(
                id = baseProject.id,
                title = baseProject.title,
                company = baseProject.company,
                location = baseProject.location,
                category = baseProject.category,
                status = baseProject.status,
                startDate = baseProject.startDate,
                endDate = baseProject.endDate,
                wage = baseProject.wage,
                currentApplicants = baseProject.currentApplicants,
                maxApplicants = baseProject.maxApplicants,
                isUrgent = baseProject.isUrgent,
                isBookmarked = baseProject.isBookmarked
            )
        } else {
            // 기본 프로젝트 (fallback)
            CompanyMockDataFactory.getSimpleProjects().firstOrNull() ?: SimpleProject(
                id = projectId,
                title = "프로젝트를 찾을 수 없습니다",
                company = "알 수 없음",
                location = "알 수 없음",
                category = "일반",
                status = "IN_PROGRESS",
                startDate = "2025-08-01",
                endDate = "2025-08-31",
                wage = 150000,
                currentApplicants = 0,
                maxApplicants = 10,
                isUrgent = false,
                isBookmarked = false
            )
        }
    }
    
    override suspend fun getWorkDaysForProject(projectId: String): List<WorkDay> {
        delay(300) // 네트워크 시뮬레이션
        
        // 캐시에서 먼저 확인
        if (workDaysCache.isNotEmpty()) {
            return workDaysCache.toList()
        }
        
        // Mock 데이터에서 가져오기
        val workDays = CompanyMockDataFactory.getWorkDaysForProject(projectId)
        workDaysCache.clear()
        workDaysCache.addAll(workDays)
        
        return workDays
    }
    
    override suspend fun getProjectWorkers(projectId: String): List<ProjectWorker> {
        delay(300) // 네트워크 시뮬레이션
        return CompanyMockDataFactory.getProjectWorkers(projectId)
    }
    
    override suspend fun getPaymentStatus(projectId: String): Map<String, Boolean> {
        delay(200) // 네트워크 시뮬레이션
        return paymentStatusCache.toMap()
    }
    
    override suspend fun updatePaymentStatus(workDayId: String, isCompleted: Boolean): Boolean {
        return try {
            delay(500) // 네트워크 시뮬레이션
            paymentStatusCache[workDayId] = isCompleted
            // 실제 구현에서는 백엔드 API 호출
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun deleteWorkDay(workDayId: String): Boolean {
        return try {
            delay(500) // 네트워크 시뮬레이션
            
            // 캐시에서 삭제
            workDaysCache.removeIf { it.id == workDayId }
            
            // 실제 구현에서는 백엔드 API 호출
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun updateWorkDay(workDay: WorkDay): Boolean {
        return try {
            delay(500) // 네트워크 시뮬레이션
            
            // 캐시 업데이트
            val index = workDaysCache.indexOfFirst { it.id == workDay.id }
            if (index != -1) {
                workDaysCache[index] = workDay
            }
            
            // 실제 구현에서는 백엔드 API 호출
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun createWorkDay(projectId: String, workDay: WorkDay): Boolean {
        return try {
            delay(500) // 네트워크 시뮬레이션
            
            // 새 ID 생성 및 캐시에 추가
            val newWorkDay = workDay.copy(
                id = System.currentTimeMillis().toString()
            )
            workDaysCache.add(newWorkDay)
            
            // 실제 구현에서는 백엔드 API 호출
            true
        } catch (e: Exception) {
            false
        }
    }
    
    override suspend fun getApplicantsForWorkDay(workDayId: String): Int {
        delay(200) // 네트워크 시뮬레이션
        
        // 캐시에서 찾기
        val workDay = workDaysCache.find { it.id == workDayId }
        return workDay?.applicants ?: 0
        
        // 실제 구현에서는 백엔드 API 호출
    }
}