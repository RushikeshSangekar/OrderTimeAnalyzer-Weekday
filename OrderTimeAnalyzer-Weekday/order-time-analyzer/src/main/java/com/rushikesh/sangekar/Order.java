package com.rushikesh.sangekar;

public class Order{
    public static final int SPEED_INV = 8;
    int id;
    int cookingTime;
    double deliverTime;
    double startTime;
    double distance;
    int requiredSlots;
    int mealCnt;
    int appetCnt;
    double totTime;
    public Order(int id, int mealCnt, int appetCnt, double distance){
        this.id = id;
        this.appetCnt = appetCnt;
        this.mealCnt = mealCnt;
        this.distance = distance;
        this.requiredSlots = this.mealCnt*2 + this.appetCnt;
        this.cookingTime = (mealCnt>0)?29:17;
        this.deliverTime = distance*SPEED_INV;
        this.totTime = this.cookingTime + this.deliverTime;
    }
    public void setStartTime(double startTime){
        this.startTime = startTime;
        this.totTime += startTime;
    }
    public double getTotTime(){
        return this.totTime;
    }
    public int getRequiredSlots(){
        return this.requiredSlots;
    }
}
