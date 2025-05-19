package com.marcel.eatbymark.places.favouriteplaces.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_places")
data class FavouritePlaceDatabaseModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "image_blur_hash")
    val imageBlurHash: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "short_description")
    val shortDescription: String,

    @ColumnInfo(name = "telemetry_id")
    val telemetryId: String?
)


