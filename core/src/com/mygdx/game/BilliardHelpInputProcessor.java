package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BilliardHelpInputProcessor implements InputProcessor {

    private BilliardTable billiardTable;
    private Viewport viewport;
    private Ball ballToDrag;

    private ProjectionDrawer projectionDrawer;

    public BilliardHelpInputProcessor() {
    }

    public BilliardHelpInputProcessor(BilliardTable billiardTable, Viewport viewport, ProjectionDrawer projectionDrawer) {
        this.billiardTable = billiardTable;
        this.viewport = viewport;
        this.projectionDrawer = projectionDrawer;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SPACE){
            Ball ball = new Ball();
            ball.setPosition(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
            billiardTable.addBall(ball);
            updateBalls();
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (billiardTable.getBalls().size == 0)
            return false;

        Vector2 worldCoordinates = toWorldCoordinates(screenX, screenY);

        for (Ball b : billiardTable.getBalls()) {
            if (b.getPosition().cpy().sub(worldCoordinates).len() <= b.getRadius()) {
                ballToDrag = b;
                break;
            }
        }

        if (ballToDrag == null)
            redirectSelectedBall(worldCoordinates.cpy().sub(billiardTable.getSelectedBall().getPosition()));

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        ballToDrag = null;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 worldCoordinates = toWorldCoordinates(screenX, screenY);

        if (ballToDrag != null) {
            moveBallTo(ballToDrag, worldCoordinates);
        } else {
            redirectSelectedBall(worldCoordinates.cpy().sub(billiardTable.getSelectedBall().getPosition()));
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private Vector2 toWorldCoordinates(int screenX, int screenY) {
        Vector3 screenCoordinates = new Vector3(screenX, screenY, 0);
        Vector3 worldCoordinates = viewport.unproject(screenCoordinates);
        return new Vector2(worldCoordinates.x, worldCoordinates.y);
    }

    private void moveBallTo(Ball ball, Vector2 worldCoordinates) {
        Vector2 newPosition = new Vector2(worldCoordinates);

        if (newPosition.x - ball.getRadius() < 0)
            newPosition.x = ball.getRadius();
        else if (newPosition.x + ball.getRadius() > viewport.getWorldWidth())
            newPosition.x = viewport.getWorldWidth() - ball.getRadius();

        if (newPosition.y - ball.getRadius() < 0)
            newPosition.y = ball.getRadius();
        else if (newPosition.y + ball.getRadius() > viewport.getWorldHeight())
            newPosition.y = viewport.getWorldHeight() - ball.getRadius();

        ball.setPosition(newPosition.x, newPosition.y);
        updateBalls();
    }

    private void redirectSelectedBall(Vector2 newDir) {

        Ball ball = billiardTable.getSelectedBall();

        ball.setDirection(newDir.x, newDir.y);
        updateBalls();

    }

    public void updateBalls() {
        Array<Ball> remainingBalls = new Array<>(billiardTable.getBalls());
        Array<Ball> projectionArray = projectionDrawer.getProjectionArray();
        Array<Vector2> collidingPoints = projectionDrawer.getCollidingPoints();
        projectionArray.clear();
        collidingPoints.clear();

        Ball currentBall = billiardTable.getSelectedBall();

        projectionArray.add(currentBall);
        remainingBalls.removeValue(currentBall, true);


        while (!remainingBalls.isEmpty()) {

            Ball nextBall = getClosest(currentBall, remainingBalls);

            if (nextBall != null) {
                projectionArray.add(nextBall);
                Vector2 newCollidingPoint = currentBall.getPointOfStrikeWith(nextBall);
                collidingPoints.add(newCollidingPoint);
                remainingBalls.removeValue(nextBall, true);

                Vector2 newDirection = newCollidingPoint.cpy().sub(nextBall.getPosition()).scl(-1);
                nextBall.setDirection(newDirection.x, newDirection.y);

                currentBall = nextBall;

            } else {
                break;
            }
        }

        projectionDrawer.setProjectionArray(projectionArray);
        projectionDrawer.setCollidingPoints(collidingPoints);
    }

    private Ball getClosest(Ball current, Array<Ball> remaining) {
        float minDistance = Float.MAX_VALUE;
        Ball nextBall = null;
        for (Ball b : remaining) {
            float curDistance = current.getPosition().cpy().sub(b.getPosition()).len();
            if (curDistance < minDistance && current.collidesWith(b)) {
                minDistance = curDistance;
                nextBall = b;
            }
        }
        return nextBall;
    }


    public BilliardTable getBilliardTable() {
        return billiardTable;
    }

    public void setBilliardTable(BilliardTable billiardTable) {
        this.billiardTable = billiardTable;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public ProjectionDrawer getProjectionDrawer() {
        return projectionDrawer;
    }

    public void setProjectionDrawer(ProjectionDrawer projectionDrawer) {
        this.projectionDrawer = projectionDrawer;
    }
}
