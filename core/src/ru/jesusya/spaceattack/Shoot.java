package ru.jesusya.spaceattack;

class Shoot extends SpaceObject {

    Shoot(SpaceObject ship) {
        this.width = 100;
        this.height = 100;
        this.x = ship.x;
        this.y = ship.y;
        this.vy = 8.0F;
    }
}
