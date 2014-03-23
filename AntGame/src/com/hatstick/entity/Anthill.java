package com.hatstick.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/** TODO: include population size, food levels, happiness? */
public class Anthill extends Entity {
	
	private double foodStores = 0;
	
	private BitmapFont font;

	public Anthill(int id, Vector2 position) {
		super(id, position);
		setSize(60f,60f);
		font = new BitmapFont();
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

	@Override
	public void draw(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.circle(getPosition().x, getPosition().y, getSize().x);
		// Draw our food levels
		font.setScale(1.5f);
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(spriteBatch, getFoodStores()+"", getPosition().x, 
				getPosition().y);
	}
}
