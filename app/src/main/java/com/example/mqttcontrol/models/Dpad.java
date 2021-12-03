package com.example.mqttcontrol.models;

public class Dpad {
    private int state;

    public Dpad() {
        this.resetState();
    }

    public void setUp() {
        int x = 0;
        x = 1 | x;
        x = (1 << 2) | x;
        this.state = x;
    }

    public void setDown() {
        int x = 0;
        x = 1 | x;
        x = (1 << 2) | x;
        x = (1 << 4) | x;
        x = (1 << 6) | x;
        this.state = x;
    }

    public void setLeft() {
        int x = 0;
        x = 1 | x;
        x = (1 << 2) | x;
        x = (1 << 4) | x;
        this.state = x;
    }

    public void setRight() {
        int x = 0;
        x = 1 | x;
        x = (1 << 2) | x;
        x = (1 << 6) | x;
        this.state = x;
    }

    public void resetState(){
        this.state = 0;
    }

    public int getState() {
        return state;
    }
}
