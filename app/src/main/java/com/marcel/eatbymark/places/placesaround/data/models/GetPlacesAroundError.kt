package com.marcel.eatbymark.places.placesaround.data.models

import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdateError

sealed class GetPlacesAroundError(
    message: String?
) : Exception(message) {
    class ClientError(message: String?) : GetPlacesAroundError(message)
    class NetworkError(message: String?) : GetPlacesAroundError(message)
    class ServerError(message: String?) : GetPlacesAroundError(message)
    class UnknownError(message: String?) : GetPlacesAroundError(message)
}

fun GetPlacesAroundError.toPlacesUpdateError(): PlacesAroundUpdateError = when (this) {
    is GetPlacesAroundError.ClientError -> PlacesAroundUpdateError.Application
    is GetPlacesAroundError.NetworkError -> PlacesAroundUpdateError.Network
    is GetPlacesAroundError.ServerError -> PlacesAroundUpdateError.Server
    is GetPlacesAroundError.UnknownError -> PlacesAroundUpdateError.Unknown
}