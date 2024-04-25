package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import controller.Game;
import model.Cell;
import util.Util;

public class CellManager {
	
	private Cell[][] map;
	private int cellSize;
	private int borderWidth;
	
	private final Color BORDER_COLOR = Color.GRAY.darker();
	private final Color CELL_COLOR = Color.LIGHT_GRAY.darker();
	
	private static Font cellFont;
	private static Font hudFont = new Font("Lucida Grande", Font.PLAIN, 12);
	
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
		
		cellFont = new Font("Lucida Grande", Font.PLAIN, cellSize/2);
	}
	
	public void render(Graphics g) {
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				
				g.setColor(BORDER_COLOR);
				
				g.fillRect(x*cellSize, y*cellSize, cellSize, cellSize);
				
				g.setColor(CELL_COLOR);
				
				g.fillRect(x*cellSize + borderWidth, y*cellSize + borderWidth, 
						cellSize - borderWidth*2, cellSize - borderWidth*2);
				
				if(map[y][x].getRevealed()) {
					
					g.setFont(cellFont);
					
					g.setColor(Util.getColor(map[y][x].getStatus()));
					
					g.drawString(map[y][x].getStatus(), x*cellSize+cellSize/3, y*cellSize+5*cellSize/8);
				}
			}
		}
		
		g.setFont(hudFont);
		
		g.setColor(BORDER_COLOR.darker());
		
		g.fillRect(0, 640, 640, 30);
		
		g.setColor(Color.white);
		
		g.drawString("Flags left: " + Game.minefield.getFlags(), 20, 655);
	}
	
	public void clickedBox(int x, int y, boolean flag) {
		x = x - (x % cellSize);
		x = x / cellSize;
		
		y = y - (y % cellSize);
		y = y / cellSize;
		
		System.out.println(Game.minefield.guess(y, x, flag));
	}
}
