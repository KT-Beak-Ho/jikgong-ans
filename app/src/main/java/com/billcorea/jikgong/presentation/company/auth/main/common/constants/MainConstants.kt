package com.billcorea.jikgong.presentation.company.auth.main.common.constants


object MainConstants {
    // 네비게이션 라우트
    object Routes {
        const val COMPANY_MAIN = "company_main"
        const val PROJECT_LIST = "company_project_list"
        const val SCOUT = "company_scout"
        const val MONEY = "company_money"
        const val INFO = "company_info"
    }

    // 프로젝트 관련 상수
    object Project {
        const val MAX_PROJECT_TITLE_LENGTH = 50
        const val MAX_PROJECT_DESCRIPTION_LENGTH = 500
        const val DEFAULT_PROJECT_DURATION_DAYS = 30
    }

    // 스카웃 관련 상수
    object Scout {
        const val MAX_SCOUT_RADIUS_KM = 50
        const val DEFAULT_SCOUT_RADIUS_KM = 10
        const val MAX_PROPOSAL_MESSAGE_LENGTH = 200
    }

    // 임금 관리 관련 상수
    object Money {
        const val PAYMENT_DEADLINE_HOURS = 48
        const val MIN_WAGE_PER_HOUR = 9620 // 2024년 최저시급
    }
}