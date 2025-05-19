package com.marcel.eatbymark.telemetry.domain

import com.marcel.eatbymark.core.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TelemetryUseCaseProvider @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    val recordTelemetryUseCase: RecordTelemetryUseCase
        get() = RecordTelemetryUseCase(applicationScope)
}