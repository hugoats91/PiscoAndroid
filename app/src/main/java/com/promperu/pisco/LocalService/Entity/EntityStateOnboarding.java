package com.promperu.pisco.LocalService.Entity;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = EntityStateOnboarding.TABLE_NAME)
public class EntityStateOnboarding {

    public static final String TABLE_NAME = "onnboarding";

    public static final String COLUMN_NAME = "pagiNombre";
    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = "sesiEstado")
    public int sesiState;

    @ColumnInfo(name = "pagiId")
    public int pageId;

    @ColumnInfo(name = "pagiNombre")
    public String pageName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSesiState() {
        return sesiState;
    }

    public void setSesiState(int sesiState) {
        this.sesiState = sesiState;
    }

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public EntityStateOnboarding(int sesiState, int pageId, String pageName) {
        this.sesiState = sesiState;
        this.pageId = pageId;
        this.pageName = pageName;
    }

}