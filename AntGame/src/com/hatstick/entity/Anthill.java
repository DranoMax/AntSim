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
	
	private BitmapFont font;
	private Sprite anthillImage;

	public Anthill(int id, Vector2 position) {
		super(id, position,60f);
		setSize(60f,60f);
		font = new BitmapFont();
		anthillImage = new Sprite(new Texture(Gdx.files.internal("data/anthill.png")));
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
	public boolean draw(SpriteBatch spriteBatch) {
		
		anthillImage.setPosition(getPosition().x-getSize().x*2.5f/2, getPosition().y-getSize().y*2.5f/2);
		// Note: right now the anthillImage size is scaled by a factor of 2.5 - purely
		// based on trial and error for looks.  Needs to be tied somehow to screen
		// size in case I decide to change it again.
		anthillImage.setSize(getSize().x*2.5f,getSize().y*2.5f);
		anthillImage.draw(spriteBatch);

		
		// Draw our food levels
		font.setScale(1.5f);
		font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		font.draw(spriteBatch, getFoodStores()+"", getPosition().x, 
				getPosition().y);
		return true;
	}
}
