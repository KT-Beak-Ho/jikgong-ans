package com.billcorea.jikgong.presentation.worker.projectList.shared

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkerProjectListSharedViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(WorkerProjectListSharedUiState())
  val uiState: StateFlow<WorkerProjectListSharedUiState> = _uiState.asStateFlow()

}