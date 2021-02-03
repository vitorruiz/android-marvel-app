package br.com.vitorruiz.marvelapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.vitorruiz.marvelapp.model.Character

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(character: Character)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getById(id: Int): Character?

    @Query("SELECT * FROM characters")
    suspend fun getAll(): List<Character>

    @Query("SELECT * FROM characters")
    fun getAllLive(): LiveData<List<Character>>

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun delete(id: Int)
}