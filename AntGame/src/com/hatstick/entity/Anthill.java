package com.hatstick.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/** TODO: include population size, food levels, happiness? */
public class Anthill extends Entity {

	private double foodStores = 0;

	/**
	 * Used for timing the addition of new ants
	 */
	private float newAntTimer = 6f;
	private final int NEW_ANT_WAIT = 10;

	private BitmapFont font;

	public Anthill(int id, Vector2 position) {
		super(id, position,60f);
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

	public Ant createAnt() {
		
		newAntTimer += Gdx.graphics.getDeltaTime();
		//System.err.println(newAntTimer);
		if (newAntTimer >= NEW_ANT_WAIT && foodStores >= 5) {
			foodStores -= 5;
			newAntTimer -= NEW_ANT_WAIT;
			return new Ant(0, new Vector2(getPosition().x+getSize().x/2,getPosition().y+getSize().y/2));
		}
		else {
			return null;
		}
	}

	@Override
	public boolean draw(SpriteBatch spriteBatch, Sprite sprite) {

		sprite.setPosition(getPosition().x-getSize().x*2.5f/2, getPosition().y-getSize().y*2.5f/2);
		// Note: right now the sprite size is scaled by a factor of 2.5 - purely
		// based on trial and error for looks.  Needs to be tied somehow to screen
		// size in case I decide to change it again.
		sprite.setSize(getSize().x*2.5f,getSize().y*2.5f);
		sprite.draw(spriteBatch);


		// Draw our food levels
		font.setScale(1.5f);
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(spriteBatch, getFoodStores()+"", getPosition().x, 
				getPosition().y);
		return true;
	}
}
