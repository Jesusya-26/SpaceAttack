package ru.jesusya.spaceattack;

class Stars extends SpaceObject {

    Stars(float y) {
        this.width = 540;
        this.height = 960;
        this.vy = -1.0F;
        this.y = y;
    }

    void move() {
        super.move();
        if (this.y <= (float)(-this.height)) {
            this.y = (float)this.height;
        }

    }
}
