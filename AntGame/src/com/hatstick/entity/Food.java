package com.hatstick.entity;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.interfaces.Observable;
import com.hatstick.interfaces.Observer;

public class Food extends Entity implements Observable {

	/**
	 * Observers is used to contain all ants who know about this
	 * food source.  Used to inform ants when food source is gone.
	 */
	private ArrayList observers = new ArrayList();
	private double stockpile = 100;

	private BitmapFont font;

	public Food(int id, Sprite sprite, Vector2 position) {
		super(id, sprite, position, 60f);
		setSize(60f,60f);
		font = new BitmapFont();
	}

	public double takeFood(double amount) {
		if (stockpile - amount <= 0) {
			amount = amount+(stockpile - amount);
			stockpile = 0;
			return amount;
		}
		else {
			stockpile -= amount;
			return amount;
		}
	}

	public double getStockpile() {
		return stockpile;
	}

	public void setStockpile(double stockpile) {
		this.stockpile = stockpile;
	}

	@Override
	public void registerObserver(Observer o) {
		if (!observers.contains(o)) {
			observers.add(o);
		}
	}

	@Override
	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >=0) {
			observers.remove(o);
		}
	}	

	@Override
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); i++) {
			Observer observer = (Observer)observers.get(i);
			observer.update(this);
		}
	}

	@Override
	public boolean draw(SpriteBatch spriteBatch) {

		// Check if we contain food. If not, don't draw and order deletion!
		if (stockpile > 0) {

			getSprite().setPosition(getPosition().x-getSize().x*2.5f/2, getPosition().y-getSize().y*2.5f/2);
			// Note: right now the sprite size is scaled by a factor of 2.5 - purely
			// based on trial and error for looks.  Needs to be tied somehow to screen
			// size in case I decide to change it again.
			getSprite().setSize(getSize().x*2.5f,getSize().y*2.5f);
			getSprite().draw(spriteBatch);

			// Draw our food levels
			font.setScale(1.5f);
			font.setColor(0.0f, 0.0f, 0.0f, 1.0f);
			font.draw(spriteBatch, getStockpile()+"", getPosition().x, 
					getPosition().y);
			return true;
		}
		else {
			// Let our observing ants know that this food source is gone!
			// And then return false to delete this food source.
			notifyObservers();
			return false;
		}
	}
}
