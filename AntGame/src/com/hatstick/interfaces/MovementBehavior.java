package com.hatstick.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.hatstick.entity.PathList;

public interface MovementBehavior {
	public float move(Vector2 position, Vector2 destination, PathList path, float speed);
}
