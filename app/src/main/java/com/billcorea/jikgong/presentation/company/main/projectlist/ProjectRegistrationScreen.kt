package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectRegistrationBottomBar
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectRegistrationForm
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectRegistrationTopBar
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedViewModel
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = "company_project_list")
@Composable
fun ProjectRegistrationScreen(
    navigator: DestinationsNavigator,
    projectRegistrationViewModel: ProjectRegistrationSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by projectRegistrationViewModel.uiState.collectAsStateWithLifecycle()

    // 네비게이션 이벤트 처리 - 뒤로가기
    LaunchedEffect(uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) {
            navigator.navigateUp()
            projectRegistrationViewModel.clearNavigationEvents()
        }
    }

    // 네비게이션 이벤트 처리 - 목록으로 이동
    LaunchedEffect(uiState.shouldNavigateToList) {
        if (uiState.shouldNavigateToList) {
            navigator.navigateUp()
            projectRegistrationViewModel.clearNavigationEvents()
        }
    }

    // 에러 다이얼로그
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.ClearError)
            },
            title = { Text("오류") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    // 종료 확인 다이얼로그
    if (uiState.showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.DismissExitDialog)
            },
            title = { Text("등록 취소") },
            text = { Text("작성 중인 내용이 사라집니다. 정말 나가시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.ConfirmExit)
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.DismissExitDialog)
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }

    // 임시저장 확인 다이얼로그
    if (uiState.showSaveDialog) {
        AlertDialog(
            onDismissRequest = {
                projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.DismissSaveDialog)
            },
            title = { Text("임시저장 완료") },
            text = { Text("프로젝트가 임시저장되었습니다.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.DismissSaveDialog)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProjectRegistrationTopBar(
                currentPage = uiState.currentPage,
                totalPages = uiState.totalPages,
                hasDraft = uiState.hasDraft,
                lastSavedTime = uiState.lastSavedTime,
                isSaving = uiState.isSaving,
                onBackClick = {
                    projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.BackPressed)
                },
                onSaveClick = {
                    projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.SaveDraft)
                }
            )
        },
        bottomBar = {
            ProjectRegistrationBottomBar(
                currentPage = uiState.currentPage,
                totalPages = uiState.totalPages,
                isNextEnabled = uiState.isCurrentPageComplete,
                isLoading = uiState.isSubmitting,
                nextButtonText = uiState.nextButtonText,
                onPreviousClick = {
                    projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.PreviousPage)
                },
                onNextClick = {
                    projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.NextPage)
                },
                onPageClick = { page ->
                    projectRegistrationViewModel.onEvent(ProjectRegistrationSharedEvent.GoToPage(page))
                }
            )
        }
    ) { innerPadding ->
        ProjectRegistrationForm(
            projectData = uiState.projectData,
            currentPage = uiState.currentPage,
            validationErrors = uiState.validationErrors,
            onEvent = projectRegistrationViewModel::onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProjectRegistrationScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        ProjectRegistrationScreen(
            navigator = navigator,
            projectRegistrationViewModel = ProjectRegistrationSharedViewModel()
        )
    }
}