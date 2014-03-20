package com.hatstick.entity;

import com.badlogic.gdx.math.Vector2;

public class Food extends Entity{

	private double stockpile = 100;
	
	public Food(int id, Vector2 position) {
		super(id, position);
		setSize(60f,60f);
		// TODO Auto-generated constructor stub
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
}
