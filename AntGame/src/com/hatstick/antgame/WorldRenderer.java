package com.hatstick.antgame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.hatstick.entity.Ant;
import com.hatstick.entity.Anthill;
import com.hatstick.entity.Entity;
import com.hatstick.interfaces.State;
import com.hatstick.entity.Food;
import com.hatstick.entity.Level;

public class WorldRenderer {

	private OrthographicCamera cam;
	/**
	 * Used to provide secondary window that zooms in on
	 * selected ants.
	 */
	private OrthographicCamera antCloseUpCam;
	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 480;

	// Mouse touch
	private Vector2 target;

	// Ant that camera is following
	Ant followingAnt = null;
	// isZooming used after selecting an ant in order to "zoom" smoothly up to it
	private boolean isZooming = false;

	private SpriteBatch spriteBatch;
	private SpriteBatch spriteBatchCloseUp;
	private ShapeRenderer shapeRenderer;

	private ArrayList<Food> foodToDelete = new ArrayList<Food>();
	private ArrayList<Entity> entityToDelete = new ArrayList<Entity>();

	private Level level;

	public WorldRenderer(Level level) {
		this.level = level;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
		this.antCloseUpCam = new OrthographicCamera(200,200);
		this.target = new Vector2(0,0);
		loadTextures();
	}

	private void loadTextures() {
		spriteBatch = new SpriteBatch();
		spriteBatchCloseUp = new SpriteBatch();
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

	private void antCloseUp() {
		smoothZoom();
		antCloseUpCam.position.set(followingAnt.getPosition().x,followingAnt.getPosition().y,0);
		antCloseUpCam.update();

		spriteBatchCloseUp.setProjectionMatrix(antCloseUpCam.combined);

		// Draw our close up
		//	spriteBatchCloseUp.begin();
		//	spriteBatchCloseUp.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}

	public void smoothZoom() {
		if (isZooming == true && antCloseUpCam.zoom-0.05f > 0.3f) {
			antCloseUpCam.zoom -= 0.05f;
		}
		else {
			isZooming = false;
		}
	}

	/**
	 * Used by GameScreen to move the camera
	 */
	public void setTranslation(Vector2 trans, OrthographicCamera camera) {
		Vector3 temp = new Vector3(trans.x,trans.y,0);
		camera.translate(temp);
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

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		for( Entity entity : level.getEntities().keySet() ) {
			if( entity instanceof Ant) {
				((Ant)entity).drawLines(shapeRenderer);
			}
		}
		shapeRenderer.end();

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		for( Entity entity : level.getEntities().keySet() ) {
			if( entity instanceof Ant) {
				((Ant)entity).drawNodes(shapeRenderer);
			}
		}
		shapeRenderer.end();

		for( Entity entity : level.getEntities().keySet() ) {
			if( entity instanceof Ant) {
				for( Entity entity2 : level.getEntities().keySet() ) {
					if( entity2 instanceof Food ) {
						((Ant)entity).checkIfInsideFood((Food)entity2);
					}
					else if( entity2 instanceof Anthill ) {
						((Ant)entity).checkIfInsideAnthill((Anthill)entity2);
					}
				}
			}
		}
	}

	public void render() {

		// tell the camera to update its matrices.
		cam.update();

		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();

		for( Entity entity : level.getEntities().keySet() ) {
			// If entity says it should be deleted, do so!
			if (!entity.draw(spriteBatch, shapeRenderer)) {
				entityToDelete.add(entity);
			}
		}

		spriteBatch.end();

		for( Entity entity : entityToDelete ) {
			entityToDelete.remove(entity);
		}

		antCalculations();

		// Check if mini window should be drawn
		if (followingAnt != null) {
			antCloseUp();
		}
	}
}
