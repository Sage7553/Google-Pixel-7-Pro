package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class AppType {
    HOME, CALCULATOR, NOTES, WEATHER, SETTINGS
}

class PhoneViewModel : ViewModel() {
    private val _currentTime = MutableStateFlow(getCurrentTimeFormatted())
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _currentDate = MutableStateFlow(getCurrentDateFormatted())
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val _activeApp = MutableStateFlow(AppType.HOME)
    val activeApp: StateFlow<AppType> = _activeApp.asStateFlow()

    // Calculator State
    private val _calcDisplay = MutableStateFlow("0")
    val calcDisplay: StateFlow<String> = _calcDisplay.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                _currentTime.value = getCurrentTimeFormatted()
                _currentDate.value = getCurrentDateFormatted()
                delay(1000)
            }
        }
    }

    fun openApp(app: AppType) {
        _activeApp.value = app
    }

    fun goHome() {
        _activeApp.value = AppType.HOME
    }

    fun updateCalc(value: String) {
        if (_calcDisplay.value == "0") {
            _calcDisplay.value = value
        } else {
            _calcDisplay.value += value
        }
    }

    fun clearCalc() {
        _calcDisplay.value = "0"
    }

    private fun getCurrentTimeFormatted(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private fun getCurrentDateFormatted(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM"))
    }
}
