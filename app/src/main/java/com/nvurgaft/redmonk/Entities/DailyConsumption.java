package com.nvurgaft.redmonk.Entities;

/**
 * Created by Koby on 26-Jun-15.
 */
public class DailyConsumption {

    private long date;
    private int calories;
    private int carbohydrates;
    private int proteins;
    private int fats;

    public DailyConsumption() {
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbs) {
        this.carbohydrates = carbs;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    @Override
    public String toString() {
        return "DailyConsumption{" +
                "calories=" + calories +
                ", date=" + date +
                ", carbohydrates=" + carbohydrates +
                ", proteins=" + proteins +
                ", fats=" + fats +
                '}';
    }
}
