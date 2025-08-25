package com.billcorea.jikgong.network.model.notification

import com.billcorea.jikgong.network.model.common.*

/**
 * 알림 데이터
 */
data class NotificationData(
  val id: String,
  val userId: String,
  val type: NotificationType,
  val title: String,
  val message: String,
  val priority: NotificationPriority,
  val isRead: Boolean = false,
  val readAt: String? = null,
  val actionUrl: String? = null,
  val metadata: Map<String, Any> = emptyMap(),
  val createdAt: String
)

/**
 * 알림 설정
 */
data class NotificationSetting(
  val userId: String,
  val jobMatch: Boolean = true,
  val payment: Boolean = true,
  val urgent: Boolean = true,
  val chat: Boolean = true,
  val review: Boolean = true,
  val project: Boolean = true,
  val attendance: Boolean = true,
  val marketing: Boolean = false,
  val nightTime: Boolean = false,
  val weekend: Boolean = true
)
