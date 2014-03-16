package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;
import com.hatstick.interfaces.MovementBehavior;
import com.hatstick.interfaces.State;

public abstract class MovingEntity extends Entity {
	
	public MovingEntity(Vector2 position) {
		super(position);
	}
	
	private State state = State.IDLE;
	private Vector2 destination = new Vector2();
	private float speed = 0;
	
	// Degree angle towards target (for properly displaying image)
	private float target = 0;
	
	private MovementBehavior moveBehavior;
	
	public abstract void performMove();
	
	// Begin massive list of setters and getters
	public MovementBehavior getMoveBehavior() {
		return moveBehavior;
	}

	public void setMoveBehavior(MovementBehavior moveBehavior) {
		this.moveBehavior = moveBehavior;
	}
	
	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
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
}
