package com.trytocopyit.model;

public class OrderDetailInfo {
    private String id;
    private String GameCode;
    private String GameName;
    private int quanity;
    private double price;
    private double amount;

    public OrderDetailInfo() {

    }

    // Using for JPA/Hibernate Query.
    public OrderDetailInfo(String id, String gameCode,
                           String gameName, int quanity, double price, double amount) {
        this.id = id;
        this.GameCode = gameCode;
        this.GameName = gameName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameCode() {
        return GameCode;
    }

    public void setGameCode(String gameCode) {
        this.GameCode = gameCode;
    }

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String gameName) {
        this.GameName = gameName;
    }

    public int getQuanity() {
        return quanity;
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}