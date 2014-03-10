package com.hatstick.behavior;

import com.alex.interfaces.MovementBehavior;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class GoToLocation implements MovementBehavior {

	@Override
	public float move(Vector2 position, Vector2 destination) {
		position.add((destination.cpy().sub(position)).nor().scl(Gdx.graphics.getDeltaTime()));

		float angle = (float) ((Math.atan2 (destination.y - position.y, -(destination.x - position.x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		return angle;
	}
}
