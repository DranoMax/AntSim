package com.alex.antgame;

import com.badlogic.gdx.Game;
import com.hatstick.screen.GameScreen;

public class AntGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen());
	}
}