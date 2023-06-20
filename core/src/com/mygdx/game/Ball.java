package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private float radius;

    private Sprite sprite;
    private Vector2 position;
    private Vector2 direction;

    public Ball(){
        sprite = new Sprite(Game.getAssetManager().<Texture>get("Ball.png"));
        sprite.setOriginCenter();

        float defaultRadius = 1;
        setRadius(defaultRadius);

        position = new Vector2();
        setPosition(0, 0);

        direction = new Vector2();
    }

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    public boolean collidesWith(Ball ball){
        if (direction.isZero())
            return false;

        float angle = -direction.angleDeg();

        Vector2 pos2 = ball.getPosition().cpy().sub(position).rotateDeg(angle);

        return pos2.x > ball.radius + radius && pos2.y + ball.radius > -radius && pos2.y - ball.radius < radius;
    }

    public Vector2 getPointOfStrikeWith(Ball ball){
        Vector2 pos1 = this.getPosition().cpy();
        Vector2 pos2 = ball.getPosition().cpy();

        pos2.sub(pos1);

        float angleToRotate = 90 - this.getDirection().angleDeg();

        pos2.rotateDeg(angleToRotate);

        float dy = (float) Math.sqrt(Math.pow(this.getRadius() + ball.getRadius(), 2) - Math.pow(pos2.x, 2));

        Vector2 calculated = new Vector2(0, pos2.y - dy);
        calculated.rotateDeg(-angleToRotate);

        return calculated.add(pos1);
    }

    public float getRadius(){
        return radius;
    }
    public void setRadius(float radius){
        this.radius = radius;
        sprite.setSize(radius * 2, radius * 2);
        sprite.setOriginCenter();
    }

    public Vector2 getPosition(){
        return position;
    }

    public void setPosition(float x, float y){
        position.set(x, y);
        sprite.setOriginBasedPosition(x, y);
    }

    public Vector2 getDirection(){
        return direction;
    }
    public void setDirection(float x, float y){
        direction.set(x, y);
        direction.nor();
    }
}
