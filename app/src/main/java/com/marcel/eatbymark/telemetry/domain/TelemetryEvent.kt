package com.marcel.eatbymark.telemetry.domain

const val ATTR_PLACE_ID = "place_id"
const val ATTR_TELEMETRY_ID = "telemetry_id"

data class TelemetryEvent(
    val eventType: TelemetryEventType,
    val attributes: Map<String, String?>,
)

enum class TelemetryEventType {
    Expanded, Favourited
}