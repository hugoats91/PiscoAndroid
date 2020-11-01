package com.promperu.pisco.ViewModel.LiveData;

import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("RespId")
    int answerId;

    @SerializedName("RespRespuesta")
    String answer;

    @SerializedName("RespPuntaje")
    int score;

    public Question(int answerId, String answer, int score) {
        this.answerId = answerId;
        this.answer = answer;
        this.score = score;
    }

}