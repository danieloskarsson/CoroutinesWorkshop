package com.kotlinconf.workshop.kettle.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kotlinconf.workshop.kettle.*
import com.kotlinconf.workshop.kettle.network.KettleService
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class KettleViewModel(
    private val kettleService: KettleService,
    parentScope: CoroutineScope,
) {
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage
    private fun showErrorMessage(throwable: Throwable) {
        log("Error occurred: $throwable")
        _errorMessage.value = "Can't perform an operation: network error"
        scope.launch {
            delay(5000)
            _errorMessage.value = ""
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable -> showErrorMessage(throwable) }

    private val scope = CoroutineScope(
        parentScope.coroutineContext
    )

    fun switchOn() {
        scope.launch {
            kettleService.switchOn(100.0.celsius)
        }
    }

    fun switchOff() {
        scope.launch {
            kettleService.switchOff()
        }
    }

    val kettlePowerState: Flow<KettlePowerState> =
        kettleService.observeKettlePowerState()
//            .stateIn(scope, SharingStarted.Lazily, KettleState.OFF)
            .shareIn(scope, SharingStarted.Lazily)

    val celsiusTemperature: Flow<CelsiusTemperature?> =
        kettleService.observeTemperature()

    val fahrenheitTemperature: Flow<FahrenheitTemperature?> =
    // initial code:
//        flowOf(null)
        celsiusTemperature.map { it?.toFahrenheit() }

    val smoothCelsiusTemperature: Flow<CelsiusTemperature> =
        celsiusTemperature
            .filterNotNull()
            .map { it.value }
            .averageOfLast(5)
            .map { it.celsius }


    // Alternative implementation using StateFlow
    private val _celsiusStateFlow = MutableStateFlow<CelsiusTemperature?>(null)
    val celsiusStateFlow: StateFlow<CelsiusTemperature?> get() = _celsiusStateFlow

    private val _fahrenheitStateFlow = MutableStateFlow<FahrenheitTemperature?>(null)
    val fahrenheitStateFlow: StateFlow<FahrenheitTemperature?> get() = _fahrenheitStateFlow

    init {
        scope.launch {
            kettleService.observeTemperature().collect {
                _celsiusStateFlow.value = it
                _fahrenheitStateFlow.value = it?.toFahrenheit()
            }
        }
    }
}

private fun Flow<Double>.averageOfLast(n: Int): Flow<Double> = flow {
    val deque = ArrayDeque<Double>(n)
    this@averageOfLast.collect {
        if (deque.size > n) {
            deque.removeFirst()
        }
        deque.addLast(it)
        emit(deque.average())
    }
}