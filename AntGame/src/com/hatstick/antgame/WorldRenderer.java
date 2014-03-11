package com.hatstick.antgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hatstick.entity.Ant;
import com.hatstick.entity.Anthill;
import com.hatstick.entity.Food;
import com.hatstick.entity.Level;
import com.hatstick.entity.PathList;
import com.hatstick.entity.PathNode;

public class WorldRenderer {

	private OrthographicCamera cam;
	private static final float CAMERA_DISTANCE = 15;
	private static final float CAMERA_WIDTH = 10f*CAMERA_DISTANCE;
	private static final float CAMERA_HEIGHT = 7f*CAMERA_DISTANCE;

	// Mouse touch
	private Vector2 target;

	private Sprite antImage;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private BitmapFont font;


	private static Intersector intersector = new Intersector();


	private Level level;

	public WorldRenderer(Level level) {
		this.level = level;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		target = new Vector2(0,0);
		loadTextures();
	}

	private void loadTextures() {
		// loading a texture from image file
		Texture.setEnforcePotImages(false);
		// binding texture to sprite and setting some attributes
		antImage = new Sprite(new Texture(Gdx.files.internal("data/ant.png")));
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		font = new BitmapFont();
	}

	public void setZoom(float i) {
		cam.zoom += i*0.05f;

		cam.position.x += i/(Gdx.graphics.getWidth()/CAMERA_WIDTH*0.05f);
		cam.position.y += i/(Gdx.graphics.getHeight()/CAMERA_HEIGHT*0.05f);
	}

	public void setTouch(float x, float y) {
		Vector3 temp = new Vector3(x, y, 0);
		cam.unproject(temp);
		target.x = temp.x;
		target.y = temp.y;
		float angle = 0.0f;
		for( Ant ant : level.getAnts().keySet() ) {
			angle = (float) ((Math.atan2 (target.y - ant.getPosition().y, -(target.x - ant.getPosition().x))*180.0d/Math.PI));
			angle -= 90;
			if(angle < 0) angle = 360-(-angle);
			angle = 360-angle;
			ant.setTarget(angle);
			ant.setDestination(target);
		}
	}

	private void drawNodes(Ant ant) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);

		PathNode head = ant.getPath().getHead();
		/*
		while (head.getNext() != null) {
			shapeRenderer.line(head.getPos(), head.getNext().getPos());
			head = head.getNext();
		}*/

		for (PathNode node : ant.getPath().getMap().keySet()) {
			if (ant.getPath().getMap().get(node) == PathList.Type.FOOD) {
				shapeRenderer.circle(node.getPos().x, node.getPos().y,.25f);
			}
		}
		shapeRenderer.end();
	}

	public void render() {
		// clear the screen with white.
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		cam.update();

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);

		for( Food food : level.getFood().keySet() ) {
			shapeRenderer.circle(food.getPosition().x, food.getPosition().y, food.getSize().x);

			// Draw our food levels
			spriteBatch.begin();
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.setScale(0.1f);
			font.draw(spriteBatch, food.getStockpile()+"", food.getPosition().x+food.getSize().x/2, 
					food.getPosition().y+food.getSize().x/2);
			spriteBatch.end();
		}
		shapeRenderer.setColor(Color.BLACK);
		for( Anthill hill : level.getAnthills().keySet() ) {
			shapeRenderer.circle(hill.getPosition().x, hill.getPosition().y, hill.getSize().x);

			// Draw our food levels
			spriteBatch.begin();
			font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			font.draw(spriteBatch, hill.getFoodStores()+"", hill.getPosition().x+hill.getSize().x/2, 
					hill.getPosition().y+hill.getSize().x/2);
			spriteBatch.end();
		}
		shapeRenderer.end();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		spriteBatch.setProjectionMatrix(cam.combined);


		for( Ant ant : level.getAnts().keySet() ) {

			for( Food food : level.getFood().keySet() ) {
				if( intersector.overlaps(new Circle(ant.getPosition().x, ant.getPosition().y, ant.getSize().x/2),
						new Circle(food.getPosition().x, food.getPosition().y, food.getSize().x/2)) || ant.getFood() > 0) {
					ant.gather();
				}
				else {
					ant.search();
				}
			}
			drawNodes(ant);

			antImage.setPosition(ant.getPosition().x, ant.getPosition().y);
			antImage.setSize(ant.getSize().x,ant.getSize().y);
			antImage.setOrigin(ant.getSize().x/2, ant.getSize().y/2);
			antImage.setRotation(ant.getTarget());
			spriteBatch.begin();

			antImage.draw(spriteBatch);
			spriteBatch.end();
		}
	}
}
