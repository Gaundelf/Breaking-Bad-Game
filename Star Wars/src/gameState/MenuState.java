package gameState;

import tileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"PRESS ENTER TO START",
		//"Credits",
		//"Quit"
	};
	
	
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		
		try {
			
			bg = new Background("/Backgrounds/BBBackground.gif", 1);
			//bg.setVector(-0.1, 0);
			
			
			font = new Font("Arial", Font.PLAIN, 26);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() { /*bg.update();*/ }
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g);
		
		
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}
			else {
				g.setColor(Color.GREEN);
			}
			g.drawString(options[i], 180, 430 + i * 40);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 1) {
			// help
			//gsm.setState(GameStateManager.CREDITS);
		}
		if(currentChoice == 2) {
			//System.exit(0);
		}
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ENTER){
			select();
		}
		/*if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}*/
	}
	
	public void keyReleased(int k) {}
	
}