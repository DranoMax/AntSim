package com.hatstick.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/** The super class from which all other objects in the world are derived */
public abstract class Entity {

	private Vector2 SIZE = new Vector2(1f,1f); 

	private Vector2 position = new Vector2();
	
	private Circle boundingCircle = new Circle();
	
	private int id;
	
	private Sprite sprite;
	
	public Entity(int id, Sprite sprite, Vector2 position, float size) {
		this.position = position;
		this.id = id;
		this.sprite = sprite;
		boundingCircle.x = position.x + getSize().x/2;
		boundingCircle.y = position.y;
		boundingCircle.radius = size/2;
	}
	
	/**
	 * Called by WorldRenderer to draw the Entity described by subclass
	 * 
	 * @return boolean
	 * If the enitity is to be deleted, returns false, else true.
	 */
	public abstract boolean draw(SpriteBatch spriteBatch);

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
		boundingCircle.x = position.x+getSize().x/2;
		boundingCircle.y = position.y+getSize().y;
	}
	
	public void setSize(float width, float height) {
		SIZE.x = width;
		SIZE.y = height;
	}

	public Vector2 getSize() {
		return SIZE;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
}
