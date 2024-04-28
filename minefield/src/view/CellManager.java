package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import controller.Game;
import model.Cell;
import util.Sound;
import util.Util;

public class CellManager {
	
	private Cell[][] map;
	public int cellSize;
	private int borderWidth;
	
	private final Color BORDER_COLOR = Color.GRAY.darker();
	private final Color CELL_COLOR = Color.LIGHT_GRAY.darker();
	
	private final Rectangle QUIT_BUTTON = new Rectangle(Game.WIDTH-80, 645, 60, 15);
	
	private final Rectangle AUTO_BUTTON = new Rectangle(Game.WIDTH-160, 645, 60, 15);
	
	private Font cellFont;
	private Font hudFont = new Font("Lucida Grande", Font.PLAIN, 12);
	private Font popupFont = new Font("Lucida Grande", Font.BOLD, 22);
	
	private boolean gameOver = false;
	private boolean won = false;
	
	public CellManager(Cell[][] map) {
		this.map = map;
		
		switch(map.length) {
			case 5:
				cellSize = 128;
				borderWidth = 13;
				break;
			case 10:
				cellSize = 64;
				borderWidth = 6;
				break;
			case 32:
				cellSize = 20;
				borderWidth = 2;
				break;
			case 64:
				cellSize = 10;
				borderWidth = 1;
				break;
		}
		
		cellFont = new Font("Lucida Grande", Font.BOLD, cellSize/2);
	}
	
	public void setMap(Cell[][] map) {
		this.map = map;
	}
	
	public void render(Graphics g) {
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				
				g.setColor(BORDER_COLOR);
				
				g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
				
				g.setColor(BORDER_COLOR.darker());
				
				int[] ypoints = {y*cellSize+cellSize, y*cellSize, y*cellSize+cellSize};
				int[] xpoints = {x*cellSize+cellSize, x*cellSize+cellSize, x*cellSize};
				
				g.fillPolygon(xpoints, ypoints, 3);
				
				g.setColor(CELL_COLOR);
				
				g.fillRect(x*cellSize + borderWidth, y*cellSize + borderWidth, 
						cellSize - borderWidth*2, cellSize - borderWidth*2);
				
				if(map[y][x].getRevealed()) {
					g.setFont(cellFont);
					
					g.setColor(Util.getColor(map[y][x].getStatus()).darker());
					
					if(!gameOver || !map[y][x].getFalseFlag()) {
						g.drawString(map[y][x].getStatus(), x*cellSize+cellSize/3, y*cellSize+5*cellSize/8);
					} else {
						g.drawString("X", x*cellSize+cellSize/3, y*cellSize+5*cellSize/8);
					}
					
					
				} else if(gameOver && map[y][x].getStatus().equals("M")) {
					g.setFont(cellFont);
					
					g.setColor(Util.getColor(map[y][x].getStatus()).darker());
					
					g.drawString(map[y][x].getStatus(), x*cellSize+cellSize/3, y*cellSize+5*cellSize/8);
				}
			}
		}
		
		g.setFont(hudFont);
		
		g.setColor(BORDER_COLOR.darker());
		
		g.fillRect(0, 640, 640, 30);
		
		g.setColor(Color.white);
		
		g.drawString("Flags left: " + Game.minefield.getFlags(), 20, 655);
		
		g.setColor(Color.RED.darker().darker());
		
		g.fillRect(QUIT_BUTTON.x, QUIT_BUTTON.y, QUIT_BUTTON.width, QUIT_BUTTON.height);
		
		if(!Game.autoSolve) {
			g.setColor(Color.GREEN.darker().darker());
		}
		
		g.fillRect(AUTO_BUTTON.x, AUTO_BUTTON.y, AUTO_BUTTON.width, AUTO_BUTTON.height);
		
		g.setColor(Color.white);
		
		g.drawString("quit", QUIT_BUTTON.x+18, QUIT_BUTTON.y+11);
		
		g.drawString("auto", AUTO_BUTTON.x+18, AUTO_BUTTON.y+11);
		
		if(gameOver) {
			g.setColor(CELL_COLOR.brighter());
			g.setFont(popupFont);
			
			if(won) {
				g.fillRect(Game.WIDTH/2-100, Game.HEIGHT/2-50, 200, 40);
				
				g.setColor(Color.GREEN.darker().darker());
				
				g.drawString("YOU WIN!", Game.WIDTH/2-55, Game.HEIGHT/2-23);
			}else {
				g.fillRect(Game.WIDTH/2-100, Game.HEIGHT/2-50, 200, 40);
				
				g.setColor(Color.RED.darker().darker());
				
				g.drawString("you lost.", Game.WIDTH/2-50, Game.HEIGHT/2-23);
			}
		}
	}
	
	public void clickedBox(int x, int y, boolean flag) {
		if(QUIT_BUTTON.contains(x, y)) {
			Game.autoSolve = false;
			Game.endGame();
			Sound.clickSound();
		}
		else if(AUTO_BUTTON.contains(x, y) && !gameOver) {
			if(Game.autoSolve) {
				Game.autoSolve = false;
			} else {
				Game.autoSolve = true;
				Game.solver.setSpeed(borderWidth*2);
			}
		}
		else if(!gameOver){
			
			x = x - (x % cellSize);
			x = x / cellSize;
			
			y = y - (y % cellSize);
			y = y / cellSize;
			
			Game.minefield.guess(y, x, flag);
			
			if(Game.minefield.gameOver()) {
				Game.autoSolve = false;
				gameOver = true;
				if(Game.minefield.getMines() == 0) {
					won = true;
					Sound.winSound();
				}
				else {
					won = false;
					Sound.loseSound();
				}
			}
		}
	}
}
