package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.controller.AntController;

public class Ant extends Entity {
	
	private float target = 0;
	private Vector2 destination = new Vector2();
	
	private float time = 0;
	private static float WAIT_TIME = 1f;
	
	private PathNodes path;
	
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
		antController = new AntController();
		path = new PathNodes();
		// TODO Auto-generated constructor stub
	}

	private AntController antController;
	
	public void search(Vector2 target) {
		
		// Find new wander target after wait
		time += Gdx.graphics.getDeltaTime();
		if(time >- WAIT_TIME) {
			antController.wander(this);
			time -= WAIT_TIME;
			WAIT_TIME = (float) Math.random()*2;
		}
		antController.goToLocation(this);
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
			destination = closestHill.getPosition();
			antController.goToLocation(this);
			
		}
	}
	
	public double vectorDistance(Vector2 a, Vector2 b) {
		return (Math.pow(a.x-b.x, 2)+Math.pow(a.y-b.y, 2));
	}
	
	public void setTarget(float target) {
		this.target = target;
	}
	
	public float getTarget() {
		return target;
	}

	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}

	public float getFood() {
		return food;
	}

	public void setFood(float food) {
		this.food = food;
	}
}
