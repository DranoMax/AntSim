package com.hatstick.entity;

import java.util.ArrayList;

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
}
