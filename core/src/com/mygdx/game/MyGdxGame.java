package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	Random random;

	FitViewport fitViewport;
	SpriteBatch batch;
	BitmapFont font;

	Texture virus, top, bot, start, over;

	float virusX;
	float virusY;
	float virusVelocidadY;

	float gravedad = 0.4f; //980f;

	float[] tubosX = new float[2];
	float[] tubosY = new float[2];

	float pantalla;

	float tiempo;
	
	@Override
	public void create () {
		random = new Random();

		fitViewport = new FitViewport(640,480);
		batch = new SpriteBatch();

		font = new BitmapFont(Gdx.files.internal("press_start_2p.fnt"), Gdx.files.internal("press_start_2p_0.png"), false);
		font.setColor(0.5f, 0.3f, 0.8f, 1);

		virus = new Texture("virus.png");
		top = new Texture("top.png");
		bot = new Texture("bot.png");
		start = new Texture("start.png");
		over = new Texture("over.png");

		virusX = 64;
		virusY = 300;
		virusVelocidadY = 0;

		tubosX[0] = 700;
		tubosX[1] = 700+360;
		tubosY[0] = -random.nextInt(290);
		tubosY[1] = -random.nextInt(290);

		tiempo = 0;

		pantalla = 0;
	}

	@Override
	public void render () {
		if(pantalla == 1) {

			float delta = Gdx.graphics.getDeltaTime();
			tiempo += delta;

			Gdx.gl.glClearColor(0.46f, 0.7f, 0.99f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
				virusVelocidadY = 6f;
			}

			virusY += virusVelocidadY;
			virusVelocidadY -= gravedad;

			for (int i = 0; i < tubosX.length; i++) {
				tubosX[i] -= 5;

				if (tubosX[i] < -80) {
					tubosX[i] = 640;
					tubosY[i] = -random.nextInt(290);
				}

				if (virusX < tubosX[i] + 80 && virusX + 64 > tubosX[i] && (virusY < tubosY[i] + 310 || virusY + 64 > tubosY[i] + 150 + 310)) {
					pantalla = 2;
				}
			}

			batch.begin();
			batch.draw(virus, virusX, virusY);
			for (int i = 0; i < tubosX.length; i++) {
				batch.draw(top, tubosX[i], tubosY[i] + 310 + 150);
				batch.draw(bot, tubosX[i], tubosY[i]);
			}
			batch.end();

		} else if(pantalla == 0){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
				pantalla = 1;
			}
			batch.begin();
			batch.draw(start, 0, 0);
			batch.end();

		} else if(pantalla == 2){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
				pantalla = 1;
				virusX = 64;
				virusY = 300;
				virusVelocidadY = 0;
				tubosX[0] = 700;
				tubosX[1] = 700+360;
				tubosY[0] = -random.nextInt(290);
				tubosY[1] = -random.nextInt(290);
				tiempo = 0;
			}

			batch.begin();
			batch.draw(over, 0, 0);
			font.draw(batch, "Time: " + String.format("%5.2f",tiempo), 195, 220);
			batch.end();

		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		fitViewport.update(width, height, true);
		batch.setProjectionMatrix(fitViewport.getCamera().combined);
	}
}
