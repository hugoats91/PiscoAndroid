package com.promperu.pisco.LocalService.Entity;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = EntityUser.TABLE_NAME)
public class EntityUser {

    public static final String TABLE_NAME = "usuario";

    public static final String COLUMN_ID = BaseColumns._ID;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    public long id;

    @ColumnInfo(name = "correo")
    public String email;

    @ColumnInfo(name = "token")
    public String token;

    @ColumnInfo(name = "tipoUsuario")
    public int userType;

    @ColumnInfo(name = "respuestaConsulta")
    public int queryAnswer;

    @ColumnInfo(name = "nombreUsuario")
    public String userName;

    @ColumnInfo(name = "estadoUsuario")
    public int userState;

    @ColumnInfo(name = "estadoBienvenido")
    public int stateWelcome;

    @ColumnInfo(name = "puntageInicio")
    public int initialScore;

    @ColumnInfo(name = "puntageBase")
    public int baseScore;

    @ColumnInfo(name = "puntageAcumulado")
    public int currentScore;

    @ColumnInfo(name = "aprendePisco")
    public String learnPisco;

    @ColumnInfo(name = "bebiId")
    public int bebiId;

    @ColumnInfo(name = "nroRuleta")
    public int numberRoulette;

    @ColumnInfo(name = "imagenRuleta")
    public String imageRoulette;

    @ColumnInfo(name = "rutaImagen")
    public String imagePath;

    @ColumnInfo(name = "receId")
    public int recipeId;

    @ColumnInfo(name = "portalId")
    public int portalId;

    @Ignore
    public EntityUser() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getQueryAnswer() {
        return queryAnswer;
    }

    public void setQueryAnswer(int queryAnswer) {
        this.queryAnswer = queryAnswer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }

    public int getStateWelcome() {
        return stateWelcome;
    }

    public void setStateWelcome(int stateWelcome) {
        this.stateWelcome = stateWelcome;
    }

    public int getInitialScore() {
        return initialScore;
    }

    public void setInitialScore(int initialScore) {
        this.initialScore = initialScore;
    }

    public int getBaseScore() {
        return baseScore;
    }

    public void setBaseScore(int baseScore) {
        this.baseScore = baseScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getLearnPisco() {
        return learnPisco;
    }

    public void setLearnPisco(String learnPisco) {
        this.learnPisco = learnPisco;
    }

    public int getBebiId() {
        return bebiId;
    }

    public void setBebiId(int bebiId) {
        this.bebiId = bebiId;
    }

    public int getNumberRoulette() {
        return numberRoulette;
    }

    public void setNumberRoulette(int numberRoulette) {
        this.numberRoulette = numberRoulette;
    }

    public String getImageRoulette() {
        return imageRoulette;
    }

    public void setImageRoulette(String imageRoulette) {
        this.imageRoulette = imageRoulette;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getPortalId() {
        return portalId;
    }

    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }

    public EntityUser(String email, String token, int userType, int queryAnswer, String userName, int userState, int stateWelcome, int initialScore, int baseScore, int currentScore, String learnPisco, int numberRoulette, String imageRoulette, String imagePath, int portalId) {
        this.email = email;
        this.token = token;
        this.userType = userType;
        this.queryAnswer = queryAnswer;
        this.userName = userName;
        this.userState = userState;
        this.stateWelcome = stateWelcome;
        this.initialScore = initialScore;
        this.baseScore = baseScore;
        this.currentScore = currentScore;
        this.learnPisco = learnPisco;
        this.numberRoulette = numberRoulette;
        this.imageRoulette = imageRoulette;
        this.imagePath = imagePath;
        this.portalId=portalId;
    }

}