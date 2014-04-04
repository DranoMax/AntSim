package com.hatstick.antgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.hatstick.screen.GameScreen;
import com.hatstick.screen.MainMenuScreen;

public class AntGame extends Game {

	private MainMenuScreen mainMenu;
	private GameScreen gameScreen;
	
	@Override
	public void create() {
		mainMenu = new MainMenuScreen(this);
		gameScreen = new GameScreen();
		setScreen(mainMenu);
	}
	
	public MainMenuScreen getMainMenuScreen() {
		return mainMenu;
	}
	
	public GameScreen getGameScreen() {
		return gameScreen;
	}
}