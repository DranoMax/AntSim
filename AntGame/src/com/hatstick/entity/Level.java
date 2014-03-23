package com.hatstick.entity;

import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;

public class Level {
	/** Note: Integer is used to store the id of the ant for various calculations**/
	private HashMap<Ant, Integer> antsMap = new HashMap<Ant, Integer>();
	private HashMap<Anthill, Integer> antHillMap = new HashMap<Anthill, Integer>();
	private HashMap<Food, Integer> foodMap = new HashMap<Food, Integer>();

	public Level() {
		createWorld();
	}

	public void createWorld() {
		for(int i = 0; i <= 25; i++) {
			antsMap.put(new Ant(i,new Vector2(600,300)), i);
		}
		antHillMap.put(new Anthill(0, new Vector2(600,300)), 0);

		foodMap.put(new Food(0, new Vector2(100,100)), 0);
		foodMap.put(new Food(1, new Vector2(300,400)), 1);
	}

	public HashMap<Ant, Integer> getAnts() {
		return antsMap;
	}

	public HashMap<Anthill, Integer> getAnthills() {
		return antHillMap;
	}

	public HashMap<Food, Integer> getFood() {
		return foodMap;
	}
}
