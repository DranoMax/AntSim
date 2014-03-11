package com.hatstick.interfaces;

import com.badlogic.gdx.math.Vector2;

public interface MovementBehavior {
	public float move(Vector2 position, Vector2 destination, float speed);
}
