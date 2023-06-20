package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class BilliardTable {
    private Array<Ball> balls;

    public BilliardTable() {
        balls = new Array<>();
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    public Array<Ball> getBalls() {
        return balls;
    }

    public void draw(SpriteBatch batch) {
        for (Ball b : balls) {
            b.draw(batch);
        }
    }

    public Ball getSelectedBall() {
        return balls.first();
    }

}
