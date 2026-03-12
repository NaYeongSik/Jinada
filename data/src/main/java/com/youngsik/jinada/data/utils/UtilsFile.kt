package com.youngsik.jinada.data.utils

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId

/**
 * 데이터 계층 내부에서만 사용되는 기술적 유틸리티입니다.
 * 비즈니스 계산 로직은 domain 모듈의 UseCase로 이동되었습니다.
 */

fun Timestamp.toLocalDate(): LocalDate = this.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
fun LocalDate.toTimestamp(): Timestamp = Timestamp(this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond(), 0)
