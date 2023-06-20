package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ProjectionDrawer {

    private static final float DEFAULT_ARROW_SIZE = 3;
    private Sprite ghostBall;
    private Sprite arrow;

    private Array<Ball> projectionArray;
    private Array<Vector2> collidingPoints;
    public ProjectionDrawer(){
        projectionArray = new Array<>();
        collidingPoints = new Array<>();

        ghostBall = new Sprite(Game.getAssetManager().<Texture>get("Circle.png"));
        ghostBall.setColor(.5f, .5f, 1, 1);
        ghostBall.setSize(2, 2);
        ghostBall.setOriginCenter();
        ghostBall.setOriginBasedPosition(10, 10);

        arrow = new Sprite(Game.getAssetManager().<Texture>get("Arrow.png"));
        arrow.setSize(1, 1);
        arrow.setOrigin(.5f, 0);
        arrow.setColor(.8f, .5f, .5f, 1);
    }


    public void draw(SpriteBatch batch){
        for (int i = 0; i < collidingPoints.size; i++){
            Vector2 curPoint = collidingPoints.get(i);
            Ball curBall = projectionArray.get(i);

            ghostBall.setSize(curBall.getRadius() * 2, curBall.getRadius() * 2);
            ghostBall.setOriginCenter();
            ghostBall.setOriginBasedPosition(curPoint.x, curPoint.y);

            ghostBall.draw(batch);

            arrow.setSize(arrow.getWidth(), curPoint.cpy().sub(curBall.getPosition()).len());
            arrow.setOriginBasedPosition(curBall.getPosition().x, curBall.getPosition().y);
            arrow.setRotation(curBall.getDirection().angleDeg() - 90);

            arrow.draw(batch);
        }

        Ball lastBall = projectionArray.get(projectionArray.size  - 1);


        arrow.setSize(arrow.getWidth(), DEFAULT_ARROW_SIZE);
        arrow.setOriginBasedPosition(lastBall.getPosition().x, lastBall.getPosition().y);
        arrow.setRotation(lastBall.getDirection().angleDeg() - 90);

        arrow.draw(batch);
    }

    public Array<Ball> getProjectionArray() {
        return projectionArray;
    }

    public void setProjectionArray(Array<Ball> projectionArray) {
        this.projectionArray = projectionArray;
    }


    public Array<Vector2> getCollidingPoints() {
        return collidingPoints;
    }

    public void setCollidingPoints(Array<Vector2> collidingPoints){
        this.collidingPoints = collidingPoints;
    }
}
