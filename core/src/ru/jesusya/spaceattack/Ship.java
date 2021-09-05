package ru.jesusya.spaceattack;

public class Ship extends SpaceObject {

    Ship() {
        this.width = 100;
        this.height = 100;
        this.x = 270.0F - (float)(this.width / 2);
        this.y = 30.0F;
    }
}
