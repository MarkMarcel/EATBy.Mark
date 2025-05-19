package com.marcel.eatbymark.telemetry.domain

import kotlinx.coroutines.CoroutineScope

class RecordTelemetryUseCase(
    private val applicationScope: CoroutineScope,
) {
    suspend fun recordEvent(event: TelemetryEvent) {
        /* Ideally will pass this to a telemetry service. Since none exists
        * for this app, does nothing */
    }
}