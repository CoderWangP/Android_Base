package com.wp.android_base.api.mining;

/**
 * Created by wangpeng on 2018/7/26.
 */

public class DifficultyData {

    private String difficulty;
    private String prediction;
    private String update_time;

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
