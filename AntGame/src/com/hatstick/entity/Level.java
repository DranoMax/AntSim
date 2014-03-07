package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

public class Level {
	 /**  Note: Integer is used to store the id of the ant for various calculations**/
		private HashMap<Ant, Integer> antsMap = new HashMap<Ant, Integer>();
		private HashMap<Anthill, Integer> antHillMap = new HashMap<Anthill, Integer>();
		private HashMap<Food, Integer> foodMap = new HashMap<Food, Integer>();
		
		public Level() {
			createWorld();
		}
		
		public void createWorld() {
			for(int i = 0; i < 15; i++) {
				antsMap.put(new Ant(new Vector2(5,5)), i);
			}
			antHillMap.put(new Anthill(new Vector2(5,5)), 1);
			
			foodMap.put(new Food(new Vector2(1,1)), 1);
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
