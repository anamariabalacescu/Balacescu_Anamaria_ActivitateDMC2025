// BancaDao.java - adăugăm metoda de update
package com.example.activitatedmc;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface BancaDao {

    @Insert
    long insert(Banca banca);

    @Update
    int update(Banca banca);

    @Query("SELECT * FROM banca_table")
    List<Banca> getAllBanci();

    @Query("SELECT * FROM banca_table WHERE numeBanca = :name LIMIT 1")
    Banca getBancaByName(String name);

    @Query("SELECT * FROM banca_table WHERE numarFiliale BETWEEN :min AND :max")
    List<Banca> getBanciBetweenFiliale(int min, int max);

    @Query("DELETE FROM banca_table WHERE numarFiliale > :value")
    int deleteBanciWithFilialeGreaterThan(int value);

    @Query("DELETE FROM banca_table WHERE numarFiliale < :value")
    int deleteBanciWithFilialeLessThan(int value);

    @Query("UPDATE banca_table SET numarFiliale = numarFiliale + 1 WHERE numeBanca LIKE :letter || '%'")
    int incrementFilialeForBanciStartingWith(String letter);
}
