package com.hatstick.behavior;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.hatstick.interfaces.MovementBehavior;

public class GoToLocation implements MovementBehavior {

	/**
	 * Used for 'fudging' float measurements
	 */
	private float epsilon = .2f;
	private Vector2 temp;

	@Override
	public float move(Vector2 position, Vector2 destination, float speed) {

		temp = position.cpy().sub(destination);

		// Determine if ant is within reasonable distance of destination (to prevent graphical issues)
		if (Math.abs(temp.x) > epsilon && Math.abs(temp.y) > epsilon) {
			position.add((destination.cpy().sub(position)).nor().scl(Gdx.graphics.getDeltaTime()*speed));
		}
		float angle = (float) ((Math.atan2 (destination.y - position.y, -(destination.x - position.x))*180.0d/Math.PI));
		angle -= 90;
		if(angle < 0) angle = 360-(-angle);
		angle = 360-angle;
		return angle;
	}
}
