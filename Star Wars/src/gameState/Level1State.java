package gameState;

import main.GamePanel;
import tileMap.*;
import entity.*;
import entity.Enemies.*;
//import audio.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	
	//private HUD hud;
	
	//private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		
		tileMap = new TileMap(60);
		tileMap.loadTiles("/Tilesets/BBTileSet.gif");
		tileMap.loadMap("/Maps/BBMap2.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/Desert1.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 200);
		
		//populateEnemies();
		
		//explosions = new ArrayList<Explosion>();
		
		//hud = new HUD(player);
		
		//bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		//bgMusic.play();
		
	}
	
	private void populateEnemies() {
		
		/*enemies = new ArrayList<Enemy>();
		
		//Dealer d;
		Point[] points = new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for(int i = 0; i < points.length; i++) {
			//d = new Dealer(tileMap);
			//d.setPosition(points[i].x, points[i].y);
			//enemies.add(d);
		}
		*/
	}
	
	public void update() {
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		/*// attack enemies
		player.checkAttack(enemies);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				//explosions.add(
					//new Explosion(e.getx(), e.gety()));
			}
		}
		*/
		
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		//for(int i = 0; i < enemies.size(); i++) {
		//	enemies.get(i).draw(g);
		//}
		
		
		// draw hud
		//hud.draw(g);
		
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(true);
		if(k == KeyEvent.VK_D) player.setRight(true);
		if(k == KeyEvent.VK_W) player.setUp(true);
		if(k == KeyEvent.VK_S) player.setDown(true);
		if(k == KeyEvent.VK_SPACE) player.setJumping(true);;
		if(k == KeyEvent.VK_R) player.setFiring();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_A) player.setLeft(false);
		if(k == KeyEvent.VK_D) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false);
		if(k == KeyEvent.VK_S) player.setDown(false);
		if(k == KeyEvent.VK_SPACE) player.setJumping(false);
	}
	
}