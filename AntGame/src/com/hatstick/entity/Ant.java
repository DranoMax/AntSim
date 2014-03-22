package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.Gather;
import com.hatstick.behavior.GoToLocation;
import com.hatstick.behavior.Search;
import com.hatstick.entity.PathNode;
import com.hatstick.interfaces.Observer;
import com.hatstick.interfaces.State;

public class Ant extends MovingEntity implements Observer {

	private PathList path = new PathList();

	private Search search = new Search();
	private Gather gather = new Gather();
	private GoToLocation gtLocation = new GoToLocation();

	Anthill closestHill = null;

	/** Ants only know what they know (No hive mind) */
	private HashMap<Anthill, Integer> knownHills = new HashMap<Anthill,Integer>();

	// Ants will seek out food, once they have food,
	// they will seek out their anthill

	/** How much food the ant has if any */
	private double food = 0;

	public Ant(int id, Vector2 position) {
		super(id, position);
		setSize(5f,5f);
		setSpeed(100f);
	//	knownHills.put(new Anthill(position), 1);
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
			break;	
		}
		setTarget(getMoveBehavior().move(getPosition(), getDestination(), path, getSpeed()));
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
		if (entity instanceof Food) {
			System.out.println("Food depleted");
		}
	}
}
