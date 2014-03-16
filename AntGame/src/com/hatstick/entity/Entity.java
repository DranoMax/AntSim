package com.hatstick.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.behavior.GoToLocation;
import com.hatstick.interfaces.MovementBehavior;

/** The super class from which all other objects in the world are derived */
public abstract class Entity {

	private Vector2 SIZE = new Vector2(1f,1f); 

	private Vector2 position = new Vector2();
	
	private Circle boundingCircle = new Circle();
	
	public Entity(Vector2 position) {
		this.position = position;

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
}
