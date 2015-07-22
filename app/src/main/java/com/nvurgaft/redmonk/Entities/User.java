package com.nvurgaft.redmonk.Entities;

/**
 * Created by Koby on 26-Jun-15.
 */
public class User {

    private String date;
    private String gender;
    private int height;
    private int weight;
    private String diabetesType;

    public User() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String name) {
        this.date = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDiabetesType() {
        return diabetesType;
    }

    public void setDiabetesType(String diabetesType) {
        this.diabetesType = diabetesType;
    }

    @Override
    public String toString() {
        return "User{" +
                "date='" + date + '\'' +
                ", gender='" + gender + '\'' +
                ", height=" + height +
                ", weight=" + weight +
                ", diabetesType=" + diabetesType +
                '}';
    }
}
