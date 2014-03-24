package com.hatstick.behavior;

import com.badlogic.gdx.math.Vector2;
import com.hatstick.entity.PathList;
import com.hatstick.interfaces.MovementBehavior;

public class Idle implements MovementBehavior {

	@Override
	public float move(Vector2 position, Vector2 destination, PathList path,
			float speed) {
		// Do nothing
		
		float angle = (float) ((Math.atan2 (destination.y - position.y, -(destination.x - position.x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		return angle;
	}
}
