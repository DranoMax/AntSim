package com.hatstick.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hatstick.antgame.AntGame;

public class MainMenuScreen implements Screen {

	// Following used for overlying menu
	private Stage menu;
	private Skin skin;
	private AntGame game;
	
	/**
	 * The biggest caveat I have with Libgdx is the absence of xml layouts -
	 * it makes things a bit harder than they should be.  Fortunately, they
	 * provide the next best thing with the use of Tables, which is what we
	 * have here.
	 */
	private Table table;

	public MainMenuScreen(AntGame game) {
		this.game = game;
		createMenu();
	}

	public void createMenu() {
		
		menu = new Stage(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
		
		table = new Table();
		table.setFillParent(true);
	    menu.addActor(table);
		
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"));
		skin.getFont("default-font").scale(.1f);

		final TextButton startButton = new TextButton("Start normal",skin,"default");
		startButton.setWidth(Gdx.graphics.getWidth()/2.5f);
		startButton.setHeight(Gdx.graphics.getHeight()/6);

		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getGameScreen());
			}
		});
		table.add(startButton).height(startButton.getHeight()).width(startButton.getWidth()).space(10);
		table.row();
		
		final TextButton murderButton = new TextButton("Start ant-smasher",skin,"default");
		murderButton.setWidth(Gdx.graphics.getWidth()/2.5f);
		murderButton.setHeight(Gdx.graphics.getHeight()/6);
		murderButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(game.getGameScreen());
			}
		});
		table.add(murderButton).height(murderButton.getHeight()).width(murderButton.getWidth()).space(10);
		table.row();
	}

	@Override
	public void render(float delta) {

		// clear the screen with white.
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		menu.act(Gdx.graphics.getDeltaTime());
		menu.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(menu);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}	
}
