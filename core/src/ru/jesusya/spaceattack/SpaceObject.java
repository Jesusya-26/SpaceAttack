package ru.jesusya.spaceattack;

public class SpaceObject {

    float x;
    float y;
    int width;
    int height;
    boolean isAlive = true;
    float vx;
    float vy;

    public SpaceObject() {
    }

    void move() {
        this.x += this.vx;
        this.y += this.vy;
        if (this.x < (float)(0 - this.width) || this.x > 540.0F || this.y < (float)(0 - this.height) || this.y > 960.0F) {
            this.isAlive = false;
        }

    }

    boolean overlaps(SpaceObject o) {
        return (this.x > o.x && this.x < o.x + (float)o.width || o.x > this.x && o.x < this.x + (float)this.width) && (this.y > o.y && this.y < o.y + (float)o.height || o.y > this.y && o.y < this.y + (float)this.height);
    }
}
