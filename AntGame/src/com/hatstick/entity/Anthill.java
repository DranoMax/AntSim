package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;

/** TODO: include population size, food levels, happiness? */
public class Anthill extends Entity {
	
	private double foodStores = 0;

	public Anthill(Vector2 position) {
		super(position);
		setSize(60f,60f);
	}

	public double getFoodStores() {
		return foodStores;
	}

	public void setFoodStores(double foodStores) {
		this.foodStores = foodStores;
	}
	
	public void putFood(double food) {
		foodStores += food;
	}
}
