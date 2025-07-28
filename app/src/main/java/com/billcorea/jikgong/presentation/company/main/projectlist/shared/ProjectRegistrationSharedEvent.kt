package com.billcorea.jikgong.presentation.company.main.projectlist.shared

import com.billcorea.jikgong.presentation.company.main.projectlist.data.JobType
import com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentType
import java.time.LocalDate
import java.time.LocalTime

sealed class ProjectRegistrationSharedEvent {

    // 페이지 네비게이션 이벤트
    object NextPage : ProjectRegistrationSharedEvent()
    object PreviousPage : ProjectRegistrationSharedEvent()
    data class GoToPage(val page: Int) : ProjectRegistrationSharedEvent()
    object BackPressed : ProjectRegistrationSharedEvent()

    // 저장 관련 이벤트
    object SaveDraft : ProjectRegistrationSharedEvent()
    object LoadDraft : ProjectRegistrationSharedEvent()
    object SubmitProject : ProjectRegistrationSharedEvent()
    object PublishProject : ProjectRegistrationSharedEvent()

    // 필수 정보 (Page 1) 이벤트
    data class UpdateProjectTitle(val title: String) : ProjectRegistrationSharedEvent()
    data class UpdateWorkType(val workType: JobType) : ProjectRegistrationSharedEvent()
    data class UpdateLocation(val location: String) : ProjectRegistrationSharedEvent()
    data class UpdateDetailAddress(val address: String) : ProjectRegistrationSharedEvent()
    data class UpdateStartDate(val date: LocalDate) : ProjectRegistrationSharedEvent()
    data class UpdateEndDate(val date: LocalDate) : ProjectRegistrationSharedEvent()
    data class UpdateStartTime(val time: LocalTime) : ProjectRegistrationSharedEvent()
    data class UpdateEndTime(val time: LocalTime) : ProjectRegistrationSharedEvent()

    // 팀 정보 (Page 2) 이벤트
    data class UpdateTotalWorkers(val count: Int) : ProjectRegistrationSharedEvent()
    data class UpdateRequiredWorkers(val count: Int) : ProjectRegistrationSharedEvent()
    data class UpdatePreferredAge(val age: String) : ProjectRegistrationSharedEvent()
    data class UpdateExperienceRequired(val required: Boolean) : ProjectRegistrationSharedEvent()
    data class AddRequirement(val requirement: String) : ProjectRegistrationSharedEvent()
    data class RemoveRequirement(val index: Int) : ProjectRegistrationSharedEvent()
    data class AddProvidedItem(val item: String) : ProjectRegistrationSharedEvent()
    data class RemoveProvidedItem(val index: Int) : ProjectRegistrationSharedEvent()

    // 금액 정보 (Page 3) 이벤트
    data class UpdatePaymentType(val paymentType: PaymentType) : ProjectRegistrationSharedEvent()
    data class UpdateDailyWage(val wage: Long) : ProjectRegistrationSharedEvent()
    data class UpdateHourlyWage(val wage: Long) : ProjectRegistrationSharedEvent()
    data class UpdateProjectWage(val wage: Long) : ProjectRegistrationSharedEvent()
    data class UpdateTransportationFee(val fee: Long) : ProjectRegistrationSharedEvent()
    data class UpdateMealProvided(val provided: Boolean) : ProjectRegistrationSharedEvent()
    data class UpdateAccommodationProvided(val provided: Boolean) : ProjectRegistrationSharedEvent()
    data class UpdateNotes(val notes: String) : ProjectRegistrationSharedEvent()

    // 기타 이벤트
    data class UpdateUrgent(val urgent: Boolean) : ProjectRegistrationSharedEvent()
    object ClearForm : ProjectRegistrationSharedEvent()
    object ClearError : ProjectRegistrationSharedEvent()
    data class ShowError(val message: String) : ProjectRegistrationSharedEvent()
    object ValidateCurrentPage : ProjectRegistrationSharedEvent()

    // 다이얼로그 이벤트
    object ShowExitDialog : ProjectRegistrationSharedEvent()
    object DismissExitDialog : ProjectRegistrationSharedEvent()
    object ConfirmExit : ProjectRegistrationSharedEvent()
    object ShowSaveDialog : ProjectRegistrationSharedEvent()
    object DismissSaveDialog : ProjectRegistrationSharedEvent()
}