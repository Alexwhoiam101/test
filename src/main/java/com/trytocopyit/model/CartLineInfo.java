package com.trytocopyit.model;

public class CartLineInfo {

    private GameInfo gameInfo;
    private int quantity;

    public CartLineInfo() {
        this.quantity = 0;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return this.gameInfo.getPrice() * this.quantity;
    }
}
