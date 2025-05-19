package com.marcel.eatbymark.places.placesaround.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacesAPIResponse(
    val created: CreatedAPIResponse?,
    val expires_in_seconds: Int?,
    val how_search_works_link: HowSearchWorksLinkAPIResponse?,
    val how_search_works_url: String?,
    val name: String?,
    val page_title: String?,
    val sections: List<SectionAPIResponse>,
    val show_large_title: Boolean?,
    val show_map: Boolean?,
    val sorting: SortingRootAPIResponse?,
    val track_id: String?
)

@JsonClass(generateAdapter = true)
data class CreatedAPIResponse(
    @Json(name = "\$date") val date: Long?
)

@JsonClass(generateAdapter = true)
data class HowSearchWorksLinkAPIResponse(
    val type: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class SectionAPIResponse(
    val content_id: String?,
    val content_type: String?,
    val end_of_section: EndOfSectionAPIResponse?,
    val hide_delivery_info: Boolean?,
    val items: List<GenericItemAPIResponse>?,
    val link: LinkAPIResponse?, // Section-level link
    val name: String?,
    val template: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class EndOfSectionAPIResponse(
    val link: LinkAPIResponse?,
    val type: String?
)

@JsonClass(generateAdapter = true)
data class GenericItemAPIResponse(
    val content_id: String?,
    val description: String?,
    val image: ImageAPIResponse?,
    val link: LinkAPIResponse?,
    val quantity: Int?,
    val quantity_str: String?,
    val telemetry_object_id: String?,
    val template: String?,
    val title: String?,
    val track_id: String?,
    val filtering: FilteringAPIResponse?,
    val sorting: ItemSortingAPIResponse?,
    val telemetry_venue_badges: List<Any>?,
    val venue: VenueAPIResponse?
)

@JsonClass(generateAdapter = true)
data class ImageAPIResponse(
    val blurhash: String?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class LinkAPIResponse(
    val target: String?,
    val target_sort: String?,
    val target_title: String?,
    val telemetry_object_id: String?,
    val title: String?,
    val type: String?,
    val view_name: String?,
    val selected_delivery_method: String?,
    val venue_mainimage_blurhash: String?
)

@JsonClass(generateAdapter = true)
data class FilteringAPIResponse(
    val filters: List<FilterAPIResponse>?
)

@JsonClass(generateAdapter = true)
data class FilterAPIResponse(
    val id: String?,
    val values: List<String>?
)

@JsonClass(generateAdapter = true)
data class ItemSortingAPIResponse(
    val sortables: List<SortableAPIResponse>?
)

@JsonClass(generateAdapter = true)
data class SortableAPIResponse(
    val id: String?,
    val value: Int?,
    val name: String?,
    val type: String?
)

// New data class based on the provided JSON for venue_preview_items
@JsonClass(generateAdapter = true)
data class VenuePreviewItemAPIResponse(
    val currency: String?,
    val id: String?,
    val image: ImageAPIResponse?,
    val name: String?,
    val price: Int?
)

@JsonClass(generateAdapter = true)
data class VenueAPIResponse(
    val address: String?,
    //val badges: List<Any>?,
    //val badges_v2: List<Any>?, // Type not fully defined, using Any
    val brand_image: ImageAPIResponse?,
    //val categories: List<Any>?, // Type not fully defined, using Any
    val city: String?,
    val country: String?,
    val currency: String?,
    val delivers: Boolean?,
    val delivery_highlight: Boolean?,
    val delivery_price_highlight: Boolean?,
    val estimate: Int?,
    val estimate_box: EstimateBoxAPIResponse?,
    val estimate_range: String?,
    val franchise: String?,
    val id: String?,
    val location: List<Double>?,
    val name: String?,
    val online: Boolean?,
    val price_range: Int?,
    val product_line: String?,
    //val promotions: List<Any>?, // Type not fully defined, using Any
    val rating: RatingAPIResponse?,
    val short_description: String?,
    val show_wolt_plus: Boolean?,
    val slug: String?,
    val tags: List<String>?,
    val venue_preview_items: List<VenuePreviewItemAPIResponse>?
)

@JsonClass(generateAdapter = true)
data class EstimateBoxAPIResponse(
    val subtitle: String?,
    val template: String?,
    val title: String?
)

@JsonClass(generateAdapter = true)
data class RatingAPIResponse(
    val rating: Float?,
    val score: Double?,
    val volume: Int?
)

@JsonClass(generateAdapter = true)
data class SortingRootAPIResponse( // For the root "sorting" object
    val sortables: List<SortableAPIResponse>?
)