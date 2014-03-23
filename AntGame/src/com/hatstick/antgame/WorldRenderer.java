package com.hatstick.antgame;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hatstick.entity.Ant;
import com.hatstick.entity.Anthill;
import com.hatstick.interfaces.State;
import com.hatstick.entity.Food;
import com.hatstick.entity.Level;
import com.hatstick.entity.PathNode;

public class WorldRenderer {

	private OrthographicCamera cam;
	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 480;

	// Mouse touch
	private Vector2 target;

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private ArrayList<Food> foodToDelete = new ArrayList<Food>();

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
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}

	public void setZoom(float i) {
		// Check that we don't zoom too close!
		if (cam.zoom + i*0.05f >= 0.2) {
			cam.zoom += i*0.05f;
		}
	}

	/**
	 * Used by GameScreen to move the camera
	 */
	public void setTranslation(Vector2 trans) {
		Vector3 temp = new Vector3(trans.x,trans.y,0);
		cam.translate(temp);
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
			//	ant.setTarget(angle);
			//	ant.setDestination(target);
		}
	}

	private void drawLines(Ant ant) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);

		PathNode head = ant.getPath().getHead();
		if (ant.getPath().size() != 0 && head.getNext() != null) {
			head = head.getNext();
			while (head.getNext() != null) {
				shapeRenderer.line(head.getPos(), head.getNext().getPos());
				head = head.getNext();
			}
			shapeRenderer.line(head.getPos(), ant.getPosition());
			shapeRenderer.end();
		}
	}

	private void drawNodes(Ant ant) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);

		for (PathNode node : ant.getPath().getMap().keySet()) {
			if (ant.getPath().getMap().get(node) == State.GATHERING && (node.getId() == 0 || node.getId() == ant.getPath().size()-1)) {

				shapeRenderer.setColor(Color.RED);
				shapeRenderer.circle(node.getPos().x, node.getPos().y,4f);
			}
			else if (ant.getPath().getMap().get(node) == State.GATHERING) {
				shapeRenderer.setColor(Color.ORANGE);
			}
			else {
				shapeRenderer.setColor(Color.BLUE);
			}
			shapeRenderer.circle(node.getPos().x, node.getPos().y,2f);
		}
		shapeRenderer.end();
	}

	public void render() {

		// tell the camera to update its matrices.
		cam.update();

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		spriteBatch.begin();
		for( Food food : level.getFood().keySet() ) {
			food.draw(spriteBatch, shapeRenderer);
		}

		for( Anthill hill : level.getAnthills().keySet() ) {
			hill.draw(spriteBatch, shapeRenderer);
		}
		spriteBatch.end();
		shapeRenderer.end();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		spriteBatch.setProjectionMatrix(cam.combined);

		for( Ant ant : level.getAnts().keySet() ) {

			for( Food food : level.getFood().keySet() ) {
				// Check if food has run out
				if( food.getStockpile() <= 0 && !foodToDelete.contains(food)) {
					foodToDelete.add(food);
				}
				else if( Intersector.overlaps(new Circle(ant.getPosition().x, ant.getPosition().y, ant.getSize().x/2),
						new Circle(food.getPosition().x, food.getPosition().y, food.getSize().x)) && ant.getFood() == 0) {
					food.registerObserver(ant);
					ant.takeFood(food);
					ant.setState(State.GATHERING);
					// This is used to set a random point within the food circle (used to fix an error involving
					// ants not 'reaching' circle on subsequent visits.
					ant.setDestination(new Vector2((float) ((food.getPosition().x-food.getSize().x/4)+((food.getSize().x/2)*Math.random())),
							(float) ((food.getPosition().y-food.getSize().y/4)+((food.getSize().y/2)*Math.random()))));
				}
			}
			// Delete the depleted food source
			if( !foodToDelete.isEmpty() ) {
				for (Food food : foodToDelete) {
					// Let ants who know about this food source know that's it's empty
					food.notifyObservers();
					level.getFood().remove(food);
				}
				foodToDelete.clear();
			}
			for( Anthill hill : level.getAnthills().keySet() ) {
				if( Intersector.overlaps(new Circle(ant.getPosition().x, ant.getPosition().y, ant.getSize().x/2),
						new Circle(hill.getPosition().x, hill.getPosition().y, hill.getSize().x)) && ant.getFood() > 0) {
					ant.putFood(hill);
				}
			}
			ant.performMove();
			drawLines(ant);
			drawNodes(ant);

			spriteBatch.begin();
			ant.draw(spriteBatch, shapeRenderer);
			spriteBatch.end();
		}
	}
}
