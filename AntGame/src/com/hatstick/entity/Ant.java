package com.hatstick.entity;

import java.util.HashMap;

import com.alex.interfaces.MovementBehavior;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.GoToLocation;
import com.hatstick.behavior.Wander;
import com.hatstick.controller.AntController;

public class Ant extends Entity {
	
	private static float WAIT_TIME = 1f;
	private float time = 6f;
	
	private PathNodes path;
	
	private Wander wander;
	private GoToLocation gtLocation;
	
	Anthill closestHill = null;
	
	/** Ants only know what they know (No hive mind) */
	private HashMap<Anthill, Integer> knownHills = new HashMap<Anthill,Integer>();
	
	// Ants will seek out food, once they have food,
	// they will seek out their anthill
	
	/** How much food the ant has if any */
	private float food = 0;
	
	public Ant(Vector2 position) {
		super(position);
		knownHills.put(new Anthill(position), 1);
		path = new PathNodes();

		wander = new Wander();
		gtLocation = new GoToLocation();

		// TODO Auto-generated constructor stub
	}
	
	public void search() {
		
		// Find new wander target after wait
		time += Gdx.graphics.getDeltaTime();
		if(time >= WAIT_TIME) {
			setMovementBehavior(wander);
			setTarget(getMoveBehavior().move(getPosition(), getDestination()));
			time -= WAIT_TIME;
			WAIT_TIME = (float) Math.random()*2;
		}
		setMovementBehavior(gtLocation);
		getMoveBehavior().move(getPosition(), getDestination());
	}
	
	public void gather(HashMap<Anthill, Integer> antHills) {
		food = 10;
		if(path.getMap().isEmpty()) {
			double closest = 999999999;
			
			for ( Anthill hill : antHills.keySet() ) {
				if(vectorDistance(this.getPosition(),hill.getPosition()) < closest) {
					closestHill = hill;
				}
			}
			setDestination(closestHill.getPosition());
			setMovementBehavior(gtLocation);
			getMoveBehavior().move(getPosition(), getDestination());
		}
	}
	
	public double vectorDistance(Vector2 a, Vector2 b) {
		return (Math.pow(a.x-b.x, 2)+Math.pow(a.y-b.y, 2));
	}

	public float getFood() {
		return food;
	}

	public void setFood(float food) {
		this.food = food;
	}
}
