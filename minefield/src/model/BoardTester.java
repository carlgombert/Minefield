package model;

import controller.Game;
import util.Util;

/*
 * BoardTester verifies that the current board is winnable without having to guess
 * 
 * */
public class BoardTester {

	private static int revealedCells = 0;
	private static int totalCells;
	private static Cell[][] map;
	private static Minefield minefield;
	
	public static boolean testBoard(Cell[][] mineMap, int x, int y, int totalCells, int flags, int mines) {
		BoardTester.totalCells = totalCells;
		
		System.out.println("start");
		
		map = Util.copy2DCell(mineMap);
		
		minefield = new Minefield(map, flags, mines);
		
		minefield.guess(y, x, false);
		
		boolean running = true;
		
		while(running) {
			running = takeTurn();
			revealedCells = minefield.getRevealedCells();
			if(revealedCells == totalCells && minefield.getFlags() == 0) {
				System.out.println("good");
				return true;
			}else if(minefield.gameOver()) {
				System.out.println("dead?");
				return false;
			}
		}
		System.out.println("bad");
		return false;
	}
	
	private static boolean takeTurn() {
		for(int y = 0; y < map.length; y++) {
			for(int x = 0; x < map[y].length; x++) {
				if(map[y][x].getRevealed()) {
					
					if(map[y][x].getStatus().equals("M")) {
						minefield.guess(y, x, true);
						return true;
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
								minefield.guess(temp[1], temp[0], true);
								
								return true;
							}
						}
						else if(unrevealCount != 0){
							int[] temp = unrevealCells.remove();
							minefield.guess(temp[1], temp[0], false);
							
							return true;
						}
						
					}
				}
			}
		}
		return false;
	}
	
	private static int getNumericValue(String str) {
		switch(str) {
			case "M":
			case "F":
			case "-":
				return -1;
			default:
				return Integer.parseInt(str);
		}
	}
}
