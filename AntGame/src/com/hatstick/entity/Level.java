package com.hatstick.entity;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Level {
	/** Note: Integer is used to store the id of the ant for various calculations**/
	private HashMap<Ant, Integer> antsMap = new HashMap<Ant, Integer>();
	private HashMap<Anthill, Integer> antHillMap = new HashMap<Anthill, Integer>();
	private HashMap<Food, Integer> foodMap = new HashMap<Food, Integer>();

	private HashMap<Entity, Integer> entities = new HashMap<Entity, Integer>();

	// Load textures to be used in this level
	private static final TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("drawable/antsim.pack"));
	private static final Sprite antSprite = new Sprite(new TextureRegion(textureAtlas.findRegion("ant")));
	private static final Sprite foodSprite = new Sprite(new TextureRegion(textureAtlas.findRegion("food")));
	private static final Sprite anthillSprite = new Sprite(new TextureRegion(textureAtlas.findRegion("anthill")));

	public Level() {
		createWorld();
	}

	public void createWorld() {
		for(int i = 0; i <= 25; i++) {
			antsMap.put(new Ant(i,antSprite, new Vector2(600,300)), i);
			entities.put(new Ant(i,antSprite, new Vector2(600,300)), i);
		}
		antHillMap.put(new Anthill(0,anthillSprite, new Vector2(600,300)), 0);
		entities.put(new Anthill(0,anthillSprite, new Vector2(600,300)), 0);

		foodMap.put(new Food(0,foodSprite, new Vector2(100,100)), 0);
		foodMap.put(new Food(1,foodSprite, new Vector2(300,400)), 1);
		entities.put(new Food(0,foodSprite, new Vector2(100,100)), 0);
		entities.put(new Food(1,foodSprite, new Vector2(300,400)), 1);
	}

	public HashMap<Entity, Integer> getEntities() {
		return entities;
	}
}
