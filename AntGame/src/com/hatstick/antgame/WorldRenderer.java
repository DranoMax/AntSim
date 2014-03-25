package com.hatstick.antgame;

import java.util.ArrayList;
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

public class WorldRenderer {

	private OrthographicCamera cam;
	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 480;

	// Mouse touch
	private Vector2 target;

	// Ant that camera is following
	Ant followingAnt = null;
	// isZooming used after selecting an ant in order to "zoom" smoothly up to it
	private boolean isZooming = false;

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

	public OrthographicCamera getCam() {
		return cam;
	}

	public void setZoom(float i) {
		// Check that we don't zoom too close!
		if (cam.zoom + i*0.05f >= 0.2) {
			cam.zoom += i*0.05f;
		}
	}

	public void selectAnt(Circle touch) {
		for (Ant ant : level.getAnts().keySet()) {
			if (Intersector.overlaps(ant.getBoundingCircle(),touch)) {
				isZooming = true;
				setFollowingAnt(ant);
				break;
			}
		}
	}

	public void setFollowingAnt(Ant ant) {
		followingAnt = ant;
	}

	public void smoothZoom() {
		if (isZooming == true && cam.zoom-0.05f > 0.3f) {
			cam.zoom -= 0.05f;
		}
		else {
			isZooming = false;
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

	private void antCalculations() {
		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		spriteBatch.setProjectionMatrix(cam.combined);

		for( Ant ant : level.getAnts().keySet() ) {

			for( Food food : level.getFood().keySet() ) {
				ant.checkIfInsideFood(food);
			}
			for( Anthill hill : level.getAnthills().keySet() ) {
				ant.checkIfInsideAnthill(hill);
			}

			ant.performMove();
			ant.drawNodes(shapeRenderer);
			ant.drawLines(shapeRenderer);

			spriteBatch.begin();
			ant.draw(spriteBatch, shapeRenderer);
			spriteBatch.end();
		}
	}

	public void render() {

		// tell the camera to update its matrices.
		cam.update();

		if (followingAnt != null) {
			smoothZoom();
			cam.position.set(followingAnt.getPosition().x,followingAnt.getPosition().y,0);
		}

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		spriteBatch.begin();
		
		for( Food food : level.getFood().keySet() ) {
			// Check if food has run out
			if( food.getStockpile() <= 0 && !foodToDelete.contains(food)) {
				foodToDelete.add(food);
			}
			food.draw(spriteBatch, shapeRenderer);
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
			hill.draw(spriteBatch, shapeRenderer);
		}
		spriteBatch.end();
		shapeRenderer.end();

		antCalculations();
	}
}
