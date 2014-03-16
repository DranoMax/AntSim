package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.GoToLocation;
import com.hatstick.behavior.Wander;
import com.hatstick.entity.PathNode;

public class Ant extends Entity {

	private static float WAIT_TIME = 1f;
	private float time = 6f;

	private PathList path = new PathList();;
	private PathNode currentNode = null;
	private boolean goingHome = false;

	/**
	 * Used for 'fudging' float measurements
	 */
	private float epsilon = 1f;

	private Wander wander;
	private GoToLocation gtLocation = new GoToLocation();;

	Anthill closestHill = null;

	/** Ants only know what they know (No hive mind) */
	private HashMap<Anthill, Integer> knownHills = new HashMap<Anthill,Integer>();

	// Ants will seek out food, once they have food,
	// they will seek out their anthill

	/** How much food the ant has if any */
	private double food = 0;

	public Ant(Vector2 position) {
		super(position);
		setSize(5f,5f);
		setSpeed(70f);
		knownHills.put(new Anthill(position), 1);
		path.insert(new PathNode(getPosition()), PathList.Type.SEARCH);
		wander = new Wander();
		setState(State.SEARCHING);
	}

	@Override
	public void performMove() {
		switch(this.getState()) {
		case SEARCHING:
			search();
			break;
		case GATHERING:
			gather();
			break;
		case IDLE: 
			break;	
		}
	}

	public void search() {
		// Find new wander target after wait
		time += Gdx.graphics.getDeltaTime();
		if(time >= WAIT_TIME) {
			// Create search node
			path.insert(new PathNode(getPosition().cpy()), PathList.Type.SEARCH);

			setMovementBehavior(wander);
			setTarget(getMoveBehavior().move(getPosition(), getDestination(), getSpeed()));
			time -= WAIT_TIME;
			WAIT_TIME = (float) Math.random()*3;
		}
		setMovementBehavior(gtLocation);
		getMoveBehavior().move(getPosition(), getDestination(), getSpeed());
	}

	public void gather() {
		
		/**
		 * Found the end of path to food
		 */
		if (currentNode == null) {
			currentNode = path.getLast();
			currentNode.setPos(getPosition());
			path.getMap().put(currentNode, PathList.Type.FOOD);
			goingHome = true;
		}

		Vector2 temp = getPosition().cpy().sub(currentNode.getPos());

		// Determine if ant is within reasonable distance of node
		if (Math.abs(temp.x) < epsilon && Math.abs(temp.y) < epsilon) {
			if (goingHome) {
				if (currentNode.getPrev() != null) {
					currentNode = currentNode.getPrev();
					path.getMap().put(currentNode, PathList.Type.FOOD);
				} else {
					goingHome = !goingHome;
				}
			} else {
				if (currentNode.getNext() != null) {
					currentNode = currentNode.getNext();
					path.getMap().put(currentNode, PathList.Type.FOOD);
				} else {
					goingHome = !goingHome;
				}
			}
		}
		setDestination(currentNode.getPos());
		setMovementBehavior(gtLocation);
		setTarget(getMoveBehavior().move(getPosition(), getDestination(), getSpeed()));
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
}
