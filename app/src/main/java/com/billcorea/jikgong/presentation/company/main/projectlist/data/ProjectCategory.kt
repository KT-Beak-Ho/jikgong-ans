package com.billcorea.jikgong.presentation.company.main.projectlist.data

enum class ProjectCategory(val displayName: String, val icon: String) {
  BUILDING("건축", "🏗️"),
  CIVIL_ENGINEERING("토목", "🚧"),
  ELECTRICAL("전기", "⚡"),
  PLUMBING("배관", "🔧"),
  INTERIOR("인테리어", "🎨"),
  ROAD("도로", "🛣️"),
  BRIDGE("교량", "🌉"),
  DEMOLITION("철거", "🔨"),
  LANDSCAPING("조경", "🌳"),
  OTHER("기타", "📋")
}