package com.hatstick.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.Gather;
import com.hatstick.behavior.GoToLocation;
import com.hatstick.behavior.Idle;
import com.hatstick.behavior.Search;
import com.hatstick.entity.PathNode;
import com.hatstick.interfaces.Observer;
import com.hatstick.interfaces.State;

public class Ant extends MovingEntity implements Observer {

	private PathList path = new PathList();

	private Search search = new Search();
	private Gather gather = new Gather();
	private GoToLocation gtLocation = new GoToLocation();
	private Idle idle = new Idle();

	Anthill closestHill = null;

	private Sprite antImage;

	// Ants will seek out food, once they have food,
	// they will seek out their anthill

	/** How much food the ant has if any */
	private double food = 0;

	public Ant(int id, Vector2 position) {
		super(id, position,5f);
		setSize(5f,5f);
		setSpeed(100f);
		//	knownHills.put(new Anthill(position), 1);
		path.insert(new PathNode(path.size(),getPosition()), State.SEARCHING);
		setState(State.SEARCHING);

		// binding texture to sprite and setting some attributes
		// loading a texture from image file
		Texture.setEnforcePotImages(false);
		antImage = new Sprite(new Texture(Gdx.files.internal("data/ant.png")));
	}

	@Override
	public void performMove() {
		switch(this.getState()) {
		case SEARCHING:
			setMoveBehavior(search);
			break;
		case GATHERING:
			setMoveBehavior(gather);
			break;
		case IDLE: 
			setMoveBehavior(idle);
			break;	
		}
		setTarget(getMoveBehavior().move(getPosition(), getDestination(), path, getSpeed()));
		// Note that we call setPosition to update boundingCircle's we have to do this manually
		// even though nothing is changing because of how we are updating position via addition
		// in our moveBehaviors
		setPosition(getPosition());
	}

	public double vectorDistance(Vector2 a, Vector2 b) {
		return (Math.pow(a.x-b.x, 2)+Math.pow(a.y-b.y, 2));
	}

	public PathList getPath() {
		return path;
	}

	/**
	 * Called when ant enters a food pile.  Takes food if ant
	 * has available space.
	 */
	public void takeFood(Food food) {
		setFood(food.takeFood(10));
	}

	/**
	 * Called when ant enters anthill with food.  Places all
	 * carried food into said anthill.
	 */
	public void putFood(Anthill hill) {
		hill.putFood(food);
		food = 0;
	}

	public double getFood() {
		return food;
	}

	public void setFood(double food) {
		this.food = food;
	}

	@Override
	public void update(Entity entity) {
		setState(State.SEARCHING);
		// Food source has been depleted
		if (entity instanceof Food) {
			setState(State.SEARCHING);
		}
	}

	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		antImage.setPosition(getPosition().x-getSize().x*5/2, getPosition().y-getSize().y*5/2);
		// Note: right now the antImage size is scaled by a factor of 5 - purely
		// based on trial and error for looks.  Needs to be tied somehow to screen
		// size in case I decide to change it again.
		antImage.setSize(getSize().x*5,getSize().y*5);
		antImage.setOrigin(getSize().x*5/2, getSize().y*5/2);
		antImage.setRotation(getTarget());

		antImage.draw(spriteBatch);
	}
	
	public void drawLines(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);

		PathNode head = getPath().getHead();
		if (getPath().size() != 0 && head.getNext() != null) {
			head = head.getNext();
			while (head.getNext() != null) {
				shapeRenderer.line(head.getPos(), head.getNext().getPos());
				head = head.getNext();
			}
			shapeRenderer.line(head.getPos(), getPosition());
			shapeRenderer.end();
		}
	}
	
	public void drawNodes(ShapeRenderer shapeRenderer) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.ORANGE);

		for (PathNode node : getPath().getMap().keySet()) {
			if (getPath().getMap().get(node) == State.GATHERING && (node.getId() == 0 || node.getId() == getPath().size()-1)) {

				shapeRenderer.setColor(Color.RED);
				shapeRenderer.circle(node.getPos().x, node.getPos().y,4f);
			}
			else if (getPath().getMap().get(node) == State.GATHERING) {
				shapeRenderer.setColor(Color.ORANGE);
			}
			else {
				shapeRenderer.setColor(Color.BLUE);
			}
			shapeRenderer.circle(node.getPos().x, node.getPos().y,2f);
		}
		shapeRenderer.end();
	}
}
