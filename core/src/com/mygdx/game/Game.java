package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private InputProcessor inputProcessor;

	private static AssetManager assetManager;
    private Camera cam;
    private Viewport viewport;
    private Sprite background;
    private BilliardTable billiardTable;
    private ProjectionDrawer projectionDrawer;

    @Override
    public void create() {
        batch = new SpriteBatch();

        cam = new OrthographicCamera();
        viewport = new FitViewport(20, 20, cam);
        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

		initAssets();

        background = new Sprite(assetManager.<Texture>get("Background.jpg"));
        background.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        billiardTable = new BilliardTable();
        initBalls();

        projectionDrawer = new ProjectionDrawer();

        BilliardHelpInputProcessor processor =
                new BilliardHelpInputProcessor(billiardTable, viewport, projectionDrawer);
        processor.updateBalls();
        inputProcessor = processor;
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(.5f, .5f, .5f, 1);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        draw(batch);
        batch.end();
    }

    public void draw(SpriteBatch batch){
        background.draw(batch);
        billiardTable.draw(batch);
        projectionDrawer.draw(batch);
    }

    public void update(float dt){

    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private static void initAssets() {
        if (assetManager == null)
            assetManager = new AssetManager();

		assetManager.load("Ball.png", Texture.class);
		assetManager.load("Circle.png", Texture.class);
		assetManager.load("Arrow.png", Texture.class);
        assetManager.load("Background.jpg", Texture.class);
		assetManager.finishLoading();
    }

    private void initBalls(){
        Ball ball;
        ball = new Ball();
        ball.setPosition(2, 2);
        ball.setDirection(1, 2);
        billiardTable.addBall(ball);
        ball = new Ball();

        ball.setPosition(6, 11);
        billiardTable.addBall(ball);
        ball = new Ball();
        ball.setPosition(15, 15);
        billiardTable.addBall(ball);
    }

	public static AssetManager getAssetManager() {
		return assetManager;
	}

    public BilliardTable getBilliardTable() {
        return billiardTable;
    }
    public Camera getCam() {
        return cam;
    }
    public Viewport getViewport() {
        return viewport;
    }


}
