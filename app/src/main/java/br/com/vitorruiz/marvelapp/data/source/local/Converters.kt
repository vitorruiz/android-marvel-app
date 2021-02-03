package br.com.vitorruiz.marvelapp.data.source.local

import androidx.room.TypeConverter
import br.com.vitorruiz.marvelapp.model.Thumbnail
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun toThumbnail(value: String): Thumbnail {
        return Gson().fromJson(value, Thumbnail::class.java)
    }

    @TypeConverter
    fun fromThumbnail(thumbnail: Thumbnail): String {
        return Gson().toJson(thumbnail)
    }
}