package com.youngsik.shared.utils

import android.location.Location
import com.naver.maps.geometry.LatLng
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)

fun changeToStringDate(date: LocalDate): String = date.format(formatter)
fun changeToLocalDate(dateString: String) = LocalDate.parse(dateString,formatter)

fun LatLng.toLocation(): Location{
    val location = Location("location")
    location.latitude = this.latitude
    location.longitude = this.longitude
    return location
}

fun String.removeHtmlTags(): String = replace(Regex("<.*?>"), "")