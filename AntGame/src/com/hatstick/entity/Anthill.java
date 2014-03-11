package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;

/** TODO: include population size, food levels, happiness? */
public class Anthill extends Entity {
	
	private float foodStores = 0;

	public Anthill(Vector2 position) {
		super(position);
		setSize(20f,20f);
	}

	public float getFoodStores() {
		return foodStores;
	}

	public void setFoodStores(float foodStores) {
		this.foodStores = foodStores;
	}
}
