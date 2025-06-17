package com.billcorea.jikgong.presentation.common

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * 텍스트 입력 필드에서 사용하는 키보드 옵션들의 상수 정의
 *
 * 사용 목적:
 * - 일관된 키보드 동작 제공
 * - 메모리 효율성 (객체 재사용)
 * - 유지보수성 향상
 */

/**
 * =============================================
 * 키보드 표현 옵션들
 * KeyboardType
 * - 키보드 표시 타입
 * - Text, Email, Number, Password, Uri 등
 * ImeAction
 * - 키보드의 액션 버튼을 "완료" 버튼으로 표시
 * - Next, Go, Search, Send 등
 * - Done: 사용자가 입력을 마쳤음을 나타냄
 * =============================================
 */

object KeyboardConstants {
  //  옵션 관련
  object Options {
    /**
     * 기본 텍스트 입력용 키보드 옵션
     * 사용처: 이름, 회사명, 주소 등 일반 텍스트
     */
    val DEFAULT = KeyboardOptions(
      keyboardType = KeyboardType.Text,
      imeAction = ImeAction.Next
    )

    /**
     * 이메일 입력용 키보드 옵션
     * 사용처: 이메일 주소 입력 필드
     */
    val EMAIL = KeyboardOptions(
      keyboardType = KeyboardType.Email,
      imeAction = ImeAction.Next
    )

    /**
     * 전화번호 입력용 키보드 옵션
     * 사용처: 전화번호, 팩스번호 입력 필드
     */
    val PHONE = KeyboardOptions(
      keyboardType = KeyboardType.Phone,
      imeAction = ImeAction.Next
    )

    /**
     * 비밀번호 입력용 키보드 옵션
     * 사용처: 비밀번호, PIN 입력 필드
     */
    val PASSWORD = KeyboardOptions(
      keyboardType = KeyboardType.Password,
      imeAction = ImeAction.Done
    )

    /**
     * 숫자 입력용 키보드 옵션
     * 사용처: 나이, 직원 수, 설립년도 등
     */
    val NUMBER = KeyboardOptions(
      keyboardType = KeyboardType.Number,
      imeAction = ImeAction.Next
    )

    /**
     * 소수점이 있는 숫자 입력용 키보드 옵션
     * 사용처: 가격, 비율, 좌표 등
     */
    val DECIMAL = KeyboardOptions(
      keyboardType = KeyboardType.Decimal,
      imeAction = ImeAction.Next
    )

    /**
     * URI 입력용 키보드 옵션
     * 사용처: 웹사이트 주소, SNS 링크 등
     */
    val URI = KeyboardOptions(
      keyboardType = KeyboardType.Uri,
      imeAction = ImeAction.Next
    )

    /**
     * 검색용 키보드 옵션
     * 사용처: 검색 입력 필드
     */
    val SEARCH = KeyboardOptions(
      keyboardType = KeyboardType.Text,
      imeAction = ImeAction.Search
    )

    /**
     * 다중 라인 텍스트용 키보드 옵션
     * 사용처: 설명, 메모, 리뷰 등
     */
    val MULTILINE = KeyboardOptions(
      keyboardType = KeyboardType.Text,
      imeAction = ImeAction.Default
    )

    /**
     * 마지막 필드용 키보드 옵션 (완료 버튼)
     * 사용처: 폼의 마지막 입력 필드
     */
    val DONE = KeyboardOptions(
      keyboardType = KeyboardType.Text,
      imeAction = ImeAction.Done
    )
  }

  //  액션 관련
  object Actions {
    /**
     * 기본 키보드 액션 (아무것도 하지 않음)
     */
    val NONE = KeyboardActions()

    /**
     * 포커스 이동용 키보드 액션
     * 다음 필드로 포커스를 이동할 때 사용
     */
    fun focusNext(onNext: () -> Unit) = KeyboardActions(
      onNext = { onNext() }
    )

    /**
     * 완료 액션용 키보드 액션
     * 입력 완료 시 특정 동작을 수행할 때 사용
     */
    fun done(onDone: () -> Unit) = KeyboardActions(
      onDone = { onDone() }
    )

    /**
     * 단순 키보드 숨김
     * 키보드를 숨길 때 사용
     */
    @Composable
    fun hide(): KeyboardActions {
      val keyboardController = LocalSoftwareKeyboardController.current
      return KeyboardActions{
        keyboardController?.hide()
      }
    }

    /**
     * 🔥 완료 액션 + 키보드 숨김
     * 입력 완료 시 특정 동작을 수행하고 키보드를 숨길 때 사용
     * 사용법: KeyboardConstants.Actions.doneAndHide { /* 완료 처리 */ }
     */
    @Composable
    fun doneAndHide(onDone: (() -> Unit)? = null): KeyboardActions {
      val keyboardController = LocalSoftwareKeyboardController.current
      return KeyboardActions(
        onDone = {
          onDone?.invoke()
          keyboardController?.hide()
        }
      )
    }

    /**
     * 검색 액션용 키보드 액션
     */
    fun search(onSearch: () -> Unit) = KeyboardActions(
      onSearch = { onSearch() }
    )

    /**
     * Go 액션용 키보드 액션
     */
    fun go(onGo: () -> Unit) = KeyboardActions(
      onGo = { onGo() }
    )
  }
}

/**
 * ==========================
 * 확장 함수들
 * ==========================
 */

/**
 * KeyboardOptions 확장 함수
 * IME 액션만 변경하고 싶을 때 사용
 */
fun KeyboardOptions.withImeAction(imeAction: ImeAction): KeyboardOptions {
  return this.copy(imeAction = imeAction)
}

/**
 * KeyboardOptions 확장 함수
 * 키보드 타입만 변경하고 싶을 때 사용
 */
fun KeyboardOptions.withKeyboardType(keyboardType: KeyboardType): KeyboardOptions {
  return this.copy(keyboardType = keyboardType)
}