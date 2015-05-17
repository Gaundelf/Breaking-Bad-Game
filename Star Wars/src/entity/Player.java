package entity;

import tileMap.*;
//import Audio.AudioPlayer;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Player extends MapObject {
	
	// player stuff
	private int health;
	private int maxHealth;
	private int needle;
	private int maxNeedle;
	@SuppressWarnings("unused")
	private boolean dead;
	private boolean flinching;
	private long flinchTimer;
	
	// needle
	private boolean firing;
	private int needleCost;
	private int needleDamage;
	private ArrayList<Needle> needles;;
	
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		1, 2, 1, 1, 1
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int NEEDLE = 4;
	
	//private HashMap<String, AudioPlayer> sfx;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 100;
		height = 175;
		cwidth = 60;
		cheight = 120;
		
		moveSpeed = 0.6;
		maxSpeed = 2.6;
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		facingRight = true;
		
		health = maxHealth = 5;
		needle = maxNeedle = 20;
		
		needleCost = 1;
		needleDamage = 5;
		needles = new ArrayList<Needle>();
		
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/WWSprite.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < numFrames.length; i++) {
				
				BufferedImage[] bi =
					new BufferedImage[numFrames[i]];
				
				for(int j = 0; j < numFrames[i]; j++) {
					
					/*if(i != NEEDLE) */{
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					/*else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);*/
					}
					
				//}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(400);
		
		//sfx = new HashMap<String, AudioPlayer>();
		//sfx.put("jump", new AudioPlayer("/SFX/jump.mp3"));
		
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	public int getneedle() { return needle; }
	public int getMaxNeedle() { return maxNeedle; }
	
	public void setFiring() { 
		firing = true;
	}
	
	/*public void checkAttack(ArrayList<Enemy> enemies) {
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// needles
			for(int j = 0; j < needles.size(); j++) {
				if(needles.get(j).intersects(e)) {
					e.hit(needleDamage);
					needles.get(j).setHit();
					break;
				}
			}
			
			// check enemy collision
			if(intersects(e)) {
				hit(e.getDamage());
			}
			
		}
		
	}
	*/
	public void hit(int damage) {
		if(flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// cannot move while attacking, except in air
		if((currentAction == NEEDLE) &&!(jumping || falling)) {
			dx = 0;
		}
		
		// jumping
		if(jumping && !falling) {
			//sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
			
			if(dy > 0) 
				jumping = false;
			if(dy < 0 && !jumping) 
				dy += stopJumpSpeed;
			
			if(dy > maxFallSpeed) 
				dy = maxFallSpeed;
			
		}
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check attack has stopped
		if(currentAction == NEEDLE) {
			if(animation.hasPlayedOnce()) firing = false;
		}
		
		// needle attack
		needle += 1;
		if(needle > maxNeedle) needle = maxNeedle;
		if(firing && currentAction != NEEDLE) {
			if(needle > needleCost) {
				needle -= needleCost;
				Needle n = new Needle(tileMap, facingRight);
				n.setPosition(x, y);
				needles.add(n);
			}
		}
		
		// update needles
		for(int i = 0; i < needles.size(); i++) {
			needles.get(i).update();
			if(needles.get(i).shouldRemove()) {
				needles.remove(i);
				i--;
			}
		}
		
		// check done flinching
		if(flinching) {
			long elapsed =
				(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 1000) {
				flinching = false;
			}
		}
		
		// set animation
		if(firing) {
			if(currentAction != NEEDLE) {
				currentAction = NEEDLE;
				animation.setFrames(sprites.get(NEEDLE));
				animation.setDelay(100);
				width = 100;
			}
		}
		
		else if(dy > 0) {
			if(currentAction != FALLING) {
				currentAction = FALLING;
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(100);
				width = 100;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(-1);
				width = 100;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				width = 100;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(400);
				width = 100;
			}
		}
		
		animation.update();
		
		// set direction
		if(currentAction != NEEDLE) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// draw needles
		for(int i = 0; i < needles.size(); i++) {
			needles.get(i).draw(g);
		}
		
		// draw player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
		
	}
	
}