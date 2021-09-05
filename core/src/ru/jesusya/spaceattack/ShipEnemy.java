package ru.jesusya.spaceattack;

import com.badlogic.gdx.math.MathUtils;

class ShipEnemy extends SpaceObject {

    ShipEnemy() {
        this.width = 100;
        this.height = 100;
        this.x = (float)MathUtils.random(0, 540 - this.width);
        this.y = 960.0F;
        this.vy = (float)MathUtils.random(-5, -2);
    }
}
