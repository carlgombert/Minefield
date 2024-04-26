package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import controller.Game;
import util.Sound;

public class MainMenu {

	private final Color BACKGROUND_COLOR = Color.LIGHT_GRAY.darker();
	private final Color BUTTON_COLOR = Color.GRAY.darker();
	
	private final Font FONT = new Font("Lucida Grande", Font.PLAIN, 24);
	
	private final Rectangle EASY_BUTTON = new Rectangle(Game.WIDTH/2-150, 200, 300, 40);
	private final Rectangle MEDIUM_BUTTON = new Rectangle(Game.WIDTH/2-150, 40+EASY_BUTTON.y+10, 300, 40);
	private final Rectangle HARD_BUTTON = new Rectangle(Game.WIDTH/2-150, 40+MEDIUM_BUTTON.y+10, 300, 40);
	
	public void render(Graphics g) {
		
		g.setColor(BACKGROUND_COLOR);
		
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		
		g.setColor(BUTTON_COLOR);
		g.setFont(FONT);
		
		g.fillRect(EASY_BUTTON.x, EASY_BUTTON.y, EASY_BUTTON.width, EASY_BUTTON.height);
		g.fillRect(MEDIUM_BUTTON.x, MEDIUM_BUTTON.y, MEDIUM_BUTTON.width, MEDIUM_BUTTON.height);
		g.fillRect(HARD_BUTTON.x, HARD_BUTTON.y, HARD_BUTTON.width, HARD_BUTTON.height);
		
		g.setColor(Color.WHITE);
		
		g.drawString("EASY", EASY_BUTTON.x+120, EASY_BUTTON.y+27);
		g.drawString("MEDIUM", MEDIUM_BUTTON.x+102, MEDIUM_BUTTON.y+27);
		g.drawString("HARD", HARD_BUTTON.x+116, HARD_BUTTON.y+27);
	}
	
	public void checkButton(int x, int y) {
		if(EASY_BUTTON.contains(x, y)) {
			Game.beginGame(Game.difficulty.Easy);
			Sound.clickSound();
		}
		if(MEDIUM_BUTTON.contains(x, y)) {
			Game.beginGame(Game.difficulty.Medium);
			Sound.clickSound();
		}
		if(HARD_BUTTON.contains(x, y)) {
			Game.beginGame(Game.difficulty.Hard);
			Sound.clickSound();
		}
	}
}
