package com.billcorea.jikgong.network.model.chat

import com.billcorea.jikgong.network.model.common.*

/**
 * 채팅 메시지 데이터
 */
data class ChatMessageData(
  val id: String,
  val chatRoomId: String,
  val senderId: String,
  val receiverId: String,
  val message: String,
  val type: MessageType,
  val attachments: List<MessageAttachment> = emptyList(),
  val isRead: Boolean = false,
  val readAt: String? = null,
  val sentAt: String
)

/**
 * 메시지 첨부파일
 */
data class MessageAttachment(
  val type: AttachmentType,
  val url: String,
  val name: String,
  val size: Long
)

/**
 * 채팅방 데이터
 */
data class ChatRoomData(
  val id: String,
  val participants: List<String>,
  val lastMessage: ChatMessageData? = null,
  val unreadCount: Int = 0,
  val createdAt: String,
  val updatedAt: String
)

/**
 * 채팅 사용자
 */
data class ChatUser(
  val id: String,
  val name: String,
  val profileImageUrl: String? = null,
  val role: UserRole,
  val isOnline: Boolean = false,
  val lastSeenAt: String? = null
)
