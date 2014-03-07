package com.hatstick.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/** The super class from which all other objects in the world are derived */
public class Entity {

	private static Vector2 SIZE = new Vector2(0.25f,0.25f); 

	private Vector2 position = new Vector2();
	private Vector2 acceleration = new Vector2();
	
	private Vector2 velocity = new Vector2();
	private Vector2 desiredVelocity = new Vector2();
	private Vector2 steeringForce = new Vector2();
	
	private State state = State.IDLE;
	
	private Circle boundingCircle = new Circle();

	public enum State {
		IDLE, WALKING, JUMPING, DYING, ATTACKING
	}
	
	public Entity(Vector2 position) {
		this.position = position;

		velocity.x = 0;
		boundingCircle.x = position.x + getSize().x/2;
		boundingCircle.y = position.y;
		boundingCircle.radius = 50;
	}

	// Begin massive list of setters/getters ***********************************
	
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
	
	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Vector2 getAcceleration() {
		return acceleration;
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
	
	public Vector2 getSteeringForce() {
		return steeringForce;
	}
	
	public void setSteeringForce(Vector2 steeringForce) {
		this.steeringForce = steeringForce;
	}
	
	public Vector2 getDesiredVelocity() {
		return desiredVelocity;
	}
	
	public void setDesiredVelocity(Vector2 desiredVelocity) {
		this.desiredVelocity = desiredVelocity;
	}
}
