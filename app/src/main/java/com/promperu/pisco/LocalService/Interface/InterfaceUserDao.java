package com.promperu.pisco.LocalService.Interface;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.promperu.pisco.LocalService.Entity.EntityStateOnboarding;
import com.promperu.pisco.LocalService.Entity.EntityUser;

@Dao
public interface InterfaceUserDao {

    @Query("SELECT * FROM "+EntityUser.TABLE_NAME+ " LIMIT 1")
    EntityUser getEntityUser();

    @Query("DELETE FROM "+EntityUser.TABLE_NAME+"")
    void deleteAll();

    @Insert
    long insert(EntityUser usuario);

    @Insert
    void insertStateOnboarding(EntityStateOnboarding entityStateOnboarding);

    @Query("SELECT * FROM "+ EntityStateOnboarding.TABLE_NAME+ " WHERE "+ EntityStateOnboarding.COLUMN_NAME+" = :pagiNombre")
    EntityStateOnboarding getEntityStateOnboarding(String pagiNombre);

    @Query("UPDATE "+EntityUser.TABLE_NAME+ " SET bebiId = :bebiId")
    void setUpdateBebiId(int bebiId);

    @Query("UPDATE "+EntityUser.TABLE_NAME+ " SET receId = :ReceId")
    void setUpdateReceId(int ReceId);

    @Query("UPDATE "+ EntityStateOnboarding.TABLE_NAME+ " SET sesiEstado= :sesiEstado  WHERE "+ EntityStateOnboarding.COLUMN_NAME+" = :pagiNombre")
    void setUpdateEntityStateOnboarding(int sesiEstado, String pagiNombre);

    @Query("DELETE FROM "+ EntityStateOnboarding.TABLE_NAME+"")
    void deleteAllEntityStateOnboarding();

}