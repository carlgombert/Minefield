package controller;

import java.awt.Point;

import model.Cell;
import model.Q1Gen;

public class Solver {

	private int turnCounter = 0;
	
	private int speed = 20;
	
	public void tick() {
		turnCounter++;
		if(turnCounter > speed) {
			takeTurn();
			turnCounter = 0;
		}
	}
	
	private void takeTurn() {
		Cell[][] map = Game.minefield.getMap();
		if(Game.minefield.isFirstTurn()) {
			Game.cellManager.clickedBox(map[0].length/2*Game.cellManager.cellSize, map.length/2*Game.cellManager.cellSize, false);
			return;
		}
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				if(map[y][x].getRevealed()) {
					
					if(map[y][x].getStatus().equals("M")) {
						Game.cellManager.clickedBox(x*Game.cellManager.cellSize, y*Game.cellManager.cellSize, true);
						return;
					}
					
					int expectedMines = getNumericValue(map[y][x].getStatus());
						
					if(expectedMines != -1) {
						Q1Gen<int[]> unrevealCells = new Q1Gen<int[]>();
						int unrevealCount = 0;
						
						int mines = 0;
						
						for(int i = -1; i < 2; i++) {
	    					for(int k = -1; k < 2; k++) {
	    						if(y+i >= 0 && x+k >= 0 && y+i < map.length && x+k < map[y].length) {
		    						if(!map[y+i][x+k].getRevealed()) {
		    							unrevealCount++;
		    							int[] temp = {x+k, y+i};
		    							unrevealCells.add(temp);
		    						}
		    						else if(map[y+i][x+k].getStatus().equals("F") || map[y+i][x+k].getStatus().equals("M")) {
		    							mines++;
		    						}
	    						}
	    					}
	    				}
						
						if(expectedMines != mines) {
							if(unrevealCount == expectedMines-mines) {
								int[] temp = unrevealCells.remove();
								Game.cellManager.clickedBox(temp[0]*Game.cellManager.cellSize, temp[1]*Game.cellManager.cellSize, true);
								return;
							}
						}
						else if(unrevealCount != 0){
							int[] temp = unrevealCells.remove();
							Game.cellManager.clickedBox(temp[0]*Game.cellManager.cellSize, temp[1]*Game.cellManager.cellSize, false);
							return;
						}
						
					}
				}
			}
		}
	}
	
	private int getNumericValue(String str) {
		switch(str) {
			case "M":
			case "F":
			case "-":
				return -1;
			default:
				return Integer.parseInt(str);
		}
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
