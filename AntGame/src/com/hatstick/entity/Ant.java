package com.hatstick.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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

	// Ants will seek out food, once they have food,
	// they will seek out their anthill

	/** How much food the ant has if any */
	private double food = 0;

	public Ant(int id, Sprite sprite, Vector2 position) {
		super(id, sprite, position,5f);
		setSize(5f,5f);
		setSpeed(100f);

		path.insert(new PathNode(path.size(),getPosition()), State.SEARCHING);
		setState(State.SEARCHING);
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

	/**
	 * Used to determine if ant is inside a food source - if so, the ant begins gathering
	 * @param food
	 */
	public void checkIfInsideFood(Food food) {
		if( Intersector.overlaps(new Circle(getPosition().x, getPosition().y, getSize().x/2),
				new Circle(food.getPosition().x, food.getPosition().y, food.getSize().x)) && getFood() == 0) {
			food.registerObserver(this);
			takeFood(food);
			setState(State.GATHERING);
			// This is used to set a random point within the food circle (used to fix an error involving
			// ants not 'reaching' circle on subsequent visits.
			setDestination(new Vector2((float) ((food.getPosition().x-food.getSize().x/4)+((food.getSize().x/2)*Math.random())),
					(float) ((food.getPosition().y-food.getSize().y/4)+((food.getSize().y/2)*Math.random()))));
		}
	}

	/**
	 * Used to determine if ant is inside anthill - if so, drop off food if carrying
	 * @param hill
	 */
	public void checkIfInsideAnthill(Anthill hill) {
		if( Intersector.overlaps(new Circle(getPosition().x, getPosition().y, getSize().x/2),
				new Circle(hill.getPosition().x, hill.getPosition().y, hill.getSize().x))) {
			if( getFood() > 0 ) {
				putFood(hill);
			} 
			else { // If ant ends up back at the nest without food, he should restart his path instead of adding to it.
				path.reset(new PathNode(0,new Vector2(hill.getPosition().x,
						hill.getPosition().y)), State.SEARCHING);
			}
		}
	}


	// Below contains the methods called by WorldRenderer used for drawing

	@Override
	public boolean draw(SpriteBatch spriteBatch) {
		performMove();

		getSprite().setPosition(getPosition().x-getSize().x*5/2, getPosition().y-getSize().y*5/2);
		// Note: right now the sprite size is scaled by a factor of 5 - purely
		// based on trial and error for looks.  Needs to be tied somehow to screen
		// size in case I decide to change it again.
		getSprite().setSize(getSize().x*5,getSize().y*5);
		getSprite().setOrigin(getSize().x*5/2, getSize().y*5/2);
		getSprite().setRotation(getTarget());

		getSprite().draw(spriteBatch);

		return true;
	}

	public void drawLines(ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(Color.BLUE);

		PathNode head = getPath().getHead();
		if (getPath().size() >= 2) {
			while (head.getNext() != null) {
				shapeRenderer.line(head.getPos(), head.getNext().getPos());
				head = head.getNext();
			}
			shapeRenderer.line(head.getPos(), getPosition());
		}
	}

	public void drawNodes(ShapeRenderer shapeRenderer) {
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
	}
}
