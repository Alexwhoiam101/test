package com.trytocopyit.form;

import com.trytocopyit.entity.Game;
import org.springframework.web.multipart.MultipartFile;

public class GameForm {
    private String code;
    private String name;
    private double price;
    private boolean newGame = false;
    private MultipartFile fileData;

    public GameForm() {
        this.newGame = true;
    }

    public GameForm(Game game) {
        this.code = game.getCode();
        this.name = game.getName();
        this.price = game.getPrice();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public boolean isNewGame() {
        return newGame;
    }

    public void setNewGame(boolean newGame) {
        this.newGame = newGame;
    }

}