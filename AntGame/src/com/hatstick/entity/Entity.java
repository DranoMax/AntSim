package com.hatstick.entity;

import com.alex.interfaces.MovementBehavior;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.GoToLocation;

/** The super class from which all other objects in the world are derived */
public abstract class Entity {

	private static Vector2 SIZE = new Vector2(0.25f,0.25f); 

	private Vector2 position = new Vector2();
	
	private Vector2 destination = new Vector2();
	
	private State state = State.IDLE;
	
	private Circle boundingCircle = new Circle();
	
	private MovementBehavior moveBehavior;
	
	// Degree angle towards target (for properly displaying image)
	private float target = 0;

	public enum State {
		IDLE, WALKING, JUMPING, DYING, ATTACKING
	}
	
	public Entity(Vector2 position) {
		this.position = position;

		boundingCircle.x = position.x + getSize().x/2;
		boundingCircle.y = position.y;
		boundingCircle.radius = 50;
	}

	// Begin massive list of setters/getters ***********************************
	
	public void performMove() {
		getMoveBehavior().move(position,destination);
	}
	
	public void setMovementBehavior(MovementBehavior mv) {
		setMoveBehavior(mv);
	}
	
	public void setBoundingCircle(float x, float y) {
		boundingCircle.x = x;
		boundingCircle.y = y;
	}
	
	public Circle getBoundingCircle() {
		return boundingCircle;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position2) {
		position = position2;
	}
	
	public void setSize(float width, float height) {
		SIZE.x = width;
		SIZE.y = height;
	}

	public Vector2 getSize() {
		return SIZE;
	}
	
	public void setState(State newState) {
		this.state = newState;
	}

	public State getState() {
		return state;
	}
	
	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}
	
	public void setTarget(float target) {
		this.target = target;
	}
	
	public float getTarget() {
		return target;
	}

	public MovementBehavior getMoveBehavior() {
		return moveBehavior;
	}

	public void setMoveBehavior(MovementBehavior moveBehavior) {
		this.moveBehavior = moveBehavior;
	}
}
