package com.game.floppy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Floppy extends ApplicationAdapter {
	BitmapFont font;
	SpriteBatch batch;
	Texture background;
	Texture gameOver;
	ShapeRenderer shapeRenderer;
	Texture[] birds;
	Texture topTube,bottomTube;
	Circle birdCircle=new Circle();
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	int state=0;
	float birdY=0;
	float velocity=0;

	int gameState=0;
	float gravity=2;

	float gap=400;
	float maxtubeOffset;

	Random rangen;

	float tubeVelocity=4;

	int noOfTubes=4;
	float[] tubeX=new float[noOfTubes];
	float[] tubeOffset=new float[noOfTubes];

	int dist;

	int score=0;
	int scoringTube=0;

	@Override
	public void create () {
		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(10);

		batch = new SpriteBatch();
		background=new Texture("bg.png");
		gameOver=new Texture("gameOver.png");
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");



		topTube=new Texture("top.png");
		bottomTube=new Texture("bottom.png");
		maxtubeOffset=(Gdx.graphics.getHeight()/2)-(gap/2)-(100);
		rangen=new Random();

		dist=Gdx.graphics.getWidth()/2;
		shapeRenderer=new ShapeRenderer();

		birdCircle=new Circle();
		topTubeRectangles = new Rectangle[noOfTubes];
		bottomTubeRectangles = new Rectangle[noOfTubes];
		gameStart();
	}
	//to make the bird at the centre of the screen and rearrange the pipes !
	public void gameStart(){
		birdY=(Gdx.graphics.getHeight()/2)-(birds[0].getHeight()/2);
		for(int i=0;i<noOfTubes;i++){
			tubeOffset[i]=(rangen.nextFloat() - 0.5f)*((Gdx.graphics.getHeight())-(gap)-(200));
			tubeX[i]=(Gdx.graphics.getWidth()/2) - (topTube.getWidth()/2) + Gdx.graphics.getWidth() +(i*dist);
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	//ongoing loop
	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {
			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("score",Integer.toString(score));
				if(scoringTube <  noOfTubes-1){
					scoringTube++;
				}else {
					scoringTube=0;
				}
			}
			if (Gdx.input.justTouched()) {

				velocity = -30;

			}

			for (int i = 0; i < noOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()) {

					tubeX[i] += noOfTubes * dist;
					tubeOffset[i] = (rangen.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}



			if (birdY > 0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
				gameState=2;
			}

		} else if (gameState==0){

			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		else { //if game state = 2
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);

			font.draw(batch,"Score:"+String.valueOf(score),Gdx.graphics.getWidth()/2-230,Gdx.graphics.getHeight()/2-100);

			if(Gdx.input.justTouched()){
				gameState=1;
				gameStart();
				score=0;
				scoringTube=0;
				velocity=0;
			}
		}
		if (state == 0) {
			state = 1;
		} else {
			state = 0;
		}
		batch.draw(birds[state], Gdx.graphics.getWidth() / 2 - birds[state].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score),100,200);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[state].getHeight() / 2, birds[state].getWidth() / 2);


		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < noOfTubes; i++) {

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){

				Gdx.app.log("Collision", "Yes!");
				gameState=2;

			}

		}


		//shapeRenderer.end();



	}


	/*@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
