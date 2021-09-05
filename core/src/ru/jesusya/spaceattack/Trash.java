package ru.jesusya.spaceattack;

import com.badlogic.gdx.math.MathUtils;

class Trash extends SpaceObject {

    float aRotation;
    float vRotation;

    Trash(SpaceObject o) {
        this.x = o.x;
        this.y = o.y;
        this.width = MathUtils.random(10, 20);
        this.height = this.width;
        float a = (float)MathUtils.random(0, 360);
        float v = MathUtils.random(1.0F, 10.0F);
        this.vx = v * MathUtils.sinDeg(a);
        this.vy = v * MathUtils.cosDeg(a);
        this.vRotation = (float)MathUtils.random(-10, 10);
    }

    void move() {
        super.move();
        this.aRotation += this.vRotation;
    }
}
