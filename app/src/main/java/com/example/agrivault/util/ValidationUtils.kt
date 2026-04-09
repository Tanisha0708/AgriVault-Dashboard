package com.example.agrivault.util

import java.time.LocalDate

private val amountRegex = Regex("^\\d+(\\.\\d{1,2})?$")

fun isValidAmount(input: String): Boolean {
    return amountRegex.matches(input.trim())
}

fun isValidPastDate(selectedDate: LocalDate): Boolean {
    return !selectedDate.isAfter(LocalDate.now())
}
