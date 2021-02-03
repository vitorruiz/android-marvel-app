package br.com.vitorruiz.marvelapp.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.vitorruiz.marvelapp.model.Character

@Database(entities = [Character::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}

fun buildAppDatabase(context: Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, "appDB").build()