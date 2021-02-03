package br.com.vitorruiz.marvelapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "characters")
data class Character(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    var isFavorite: Boolean = false
) : Parcelable {
    fun imageUrl() = "${thumbnail.path}.${thumbnail.extension}"
}

@Parcelize
data class Thumbnail(val path: String, val extension: String) : Parcelable