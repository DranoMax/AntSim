package com.hatstick.antgame;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
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
import com.hatstick.entity.Entity;
import com.hatstick.entity.Food;
import com.hatstick.entity.Level;

public class WorldRenderer {

	private OrthographicCamera cam;

	private static final float CAMERA_WIDTH = 800;
	private static final float CAMERA_HEIGHT = 480;

	// Ant that camera is following
	Ant followingAnt = null;
	// isZooming used after selecting an ant in order to "zoom" smoothly up to it
	private boolean isZooming = false;

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private Level level;
	
	private ArrayList<Ant> newAnts = new ArrayList<Ant>();
	
	// Our temporary variables (to avoid multiple unnecessary instantiations
	private Vector3 temp = new Vector3();
	private Vector3 destination = new Vector3();

	public WorldRenderer(Level level) {
		this.level = level;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
		this.cam.update();
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
		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
			if (entity instanceof Ant) {
				if (Intersector.overlaps(((Ant)entity).getBoundingCircle(),touch)) {
					isZooming = true;
					setFollowingAnt((Ant)entity);
					break;
				}
			}
		}
	}

	public void setFollowingAnt(Ant ant) {
		followingAnt = ant;
	}

	private void antCloseUp() {
		smoothZoom();
		smoothPan(followingAnt.getPosition().x, followingAnt.getPosition().y);
	}
	
	public void smoothPan(float x, float y) {
		destination = new Vector3(x,y,0);
		temp = cam.position.cpy().sub(destination);
		float speed = 4*destination.dst(cam.position);
		float epsilon =  Gdx.graphics.getDeltaTime()*speed;
		// Determine if cam is within reasonable distance of ant (to prevent graphical issues)
		if (Math.abs(temp.x) > epsilon || Math.abs(temp.y) > epsilon) {
			cam.position.add((destination.cpy().sub(cam.position)).nor().scl(Gdx.graphics.getDeltaTime()*speed));
		} else {
			cam.position.set(x,y,0);
		}
	}

	public void smoothZoom() {
		if (isZooming == true && cam.zoom-0.05f > 0.3f) {
			cam.zoom -= 0.05f*(cam.zoom-0.3f);
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
	
	private void drawPaths() {
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Line);
		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
			if( entity instanceof Ant) {
				((Ant)entity).drawLines(shapeRenderer);
			}
		}
		shapeRenderer.end();
	}
	
	private void drawNodes() {
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
			if( entity instanceof Ant) {
				((Ant)entity).drawNodes(shapeRenderer);
			}
		}
		shapeRenderer.end();
	}

	private void antCalculations() {
		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
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

	private void createNewAnts() {
		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
			if( entity instanceof Anthill) {
				Ant ant = ((Anthill) entity).createAnt();
				if( ant != null ) {
					newAnts.add(ant);
				}
			}
		}
		for(Iterator<Ant> iter = newAnts.iterator(); iter.hasNext(); ) {
			Ant ant = iter.next();
			ant.setId(level.getEntities().size());
			level.getEntities().put(ant, level.getEntities().size());
		}
	}

	public void render() {

		// tell the camera to update its matrices.
		cam.update();

	//	drawPaths();
		drawNodes();
		antCalculations();
		createNewAnts();

		spriteBatch.setProjectionMatrix(cam.combined);
		spriteBatch.begin();

		for(Iterator<Entity> iter = level.getEntities().keySet().iterator(); iter.hasNext(); ) {
			Entity entity = iter.next();
			if (!entity.draw(spriteBatch)) {
				iter.remove();
			}
		}

		spriteBatch.end();

		// Check if mini window should be drawn
		if (followingAnt != null) {
			antCloseUp();
		}
	}
}