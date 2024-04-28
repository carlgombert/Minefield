package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import controller.Game;
import util.Sound;
import util.Util;
import view.CellManager;

import java.awt.Point;

public class Minefield {
    /**
    Global Section
    */
    public static final String ANSI_YELLOW_BRIGHT = "\u001B[33;1m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_PURPLE = "\u001b[35m";
    public static final String ANSI_CYAN = "\u001b[36m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001b[47m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_GREY_BACKGROUND = "\u001b[0m";

    /* 
     * Class Variable Section
     * 
    */
    private boolean gameOver = false;
    private Cell[][] map;
    private int flags;
    private int mines;
    private int rows;
    private int cols;
    
    private boolean firstMove = true;
    
    private boolean debugMode = false;
    
    private int revealedCells = 0;
    private int totalCells;
    
    private boolean testMode = false;
    
    /*Things to Note:
     * Please review ALL files given before attempting to write these functions.
     * Understand the Cell.java class to know what object our array contains and what methods you can utilize
     * Understand the StackGen.java class to know what type of stack you will be working with and methods you can utilize
     * Understand the QGen.java class to know what type of queue you will be working with and methods you can utilize
     */
    
    /**
     * Minefield
     * 
     * Build a 2-d Cell array representing your minefield.
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags, boolean debugMode) {
    	this.rows = rows;
    	this.cols = columns;
    	map = new Cell[rows][columns];
    	for(int y = 0; y < rows; y++) {
    		for(int x = 0; x < cols; x++) {
    			map[y][x] = new Cell(false, "-");
			}
		}
    	this.flags = flags;
    	this.mines = flags;
    	this.debugMode = debugMode;
    	
    	this.totalCells = rows * columns;
    	
    	Game.cellManager = new CellManager(map);

    }
    
    public Minefield(Cell[][] map, int flags, int mines) {
    	this.map = map;
    	this.flags = flags;
    	this.mines = flags;
    	
    	this.rows = map.length;
    	this.cols = map[0].length;
    	
    	this.totalCells = rows * cols;
    	
    	testMode = true;
    }

    /**
     * evaluateField
     * 
     *
     * @function:
     * Evaluate entire array.
     * When a mine is found check the surrounding adjacent tiles. If another mine is found during this check, increment adjacent cells status by 1.
     * 
     */
    public void evaluateField() {
    	for(int y = 0; y < rows; y++) {
    		for(int x = 0; x < cols; x++) {
    			if(map[y][x].getStatus().equals("-")) {
    				int mineCount = 0;
    				for(int i = -1; i < 2; i++) {
    					for(int k = -1; k < 2; k++) {
    						if(y+i >= 0 && x+k >= 0 && y+i < rows && x+k < cols) {
    							 if(map[y+i][x+k].getStatus().equals("M")) {
    								mineCount++; 
    							 }
    						}
    					}
    				}
    				map[y][x].setStatus(""+mineCount);
					 
				}
  
    			
    		}
    	}
    }

    /**
     * createMines
     * 
     * Randomly generate coordinates for possible mine locations.
     * If the coordinate has not already been generated and is not equal to the starting cell set the cell to be a mine.
     * utilize rand.nextInt()
     * 
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int y, int x, int mines) {
    	boolean running = true;
    	
    	Cell[][] tempMap = Util.copy2DCell(map);
    	while(running) {
	    	tempMap = Util.copy2DCell(map);
	    	
	    	for(int i = 0; i < mines; i++) {
	    		int randX = Util.randomNumber(0, cols);
	    		int randY = Util.randomNumber(0, rows);
	    		if(x != randX && y != randY && !tempMap[randY][randX].getStatus().equals("M")) {
	    			tempMap[randY][randX].setStatus("M");
	    		}
	    		else {
	    			i--;
	    		}
	    	}
	    	
	    	running = !BoardTester.testBoard(Util.copy2DCell(tempMap), y, x, totalCells, flags, mines);
    	}
    	map = tempMap;
    	Game.cellManager.setMap(map);
    }

    /**
     * guess
     * 
     * Check if the guessed cell is inbounds (if not done in the Main class). 
     * Either place a flag on the designated cell if the flag boolean is true or clear it.
     * If the cell has a 0 call the revealZeroes() method or if the cell has a mine end the game.
     * At the end reveal the cell to the user.
     * 
     * 
     * @param row       The x value the user entered.
     * @param col       The y value the user entered.
     * @param flag    A boolean value that allows the user to place a flag on the corresponding square.
     * @return boolean Return false if guess did not hit mine or if flag was placed, true if mine found.
     */
    public boolean guess(int row, int col, boolean flag) {
    	if(row > rows-1 || row < 0 || col > cols-1 || col < 0){
			return false;
		}
    	else {
    		if(firstMove) {
    			if(testMode) {
    				evaluateField();
    				if(map[row][col].getStatus().equals("0")) {
		    			revealZeroes(row,col);
		    		}
	    			revealStartingArea(row, col);
	    			firstMove = false;
    			}
    			else {
	    			Sound.digSound();
	    			createMines(row, col, mines);
	    			evaluateField();
	    			if(map[row][col].getStatus().equals("0")) {
		    			revealZeroes(row,col);
		    		}
	    			revealStartingArea(row, col);
	    			firstMove = false;
    			}
    		}
    		else if(map[row][col].getStatus().equals("M")) {
				if(flag) {
					if(flags > 0 && !map[row][col].getStatus().equals("F")) {
						map[row][col].setStatus("F");
						flags--;
						mines--;
						revealCell(map[row][col]);
						if(!testMode) {
							Sound.flagSound();
						}
					}
				}
				else {
					if(!testMode) {
						Sound.explosionSound();
					}
					revealCell(map[row][col]);
					gameOver = true;
				}
	    	}
			else if(!map[row][col].getStatus().equals("M")){
	    		if(flag) {
	    			if(flags > 0 && !map[row][col].getRevealed()) {
		    			flags--;
		    			map[row][col].setStatus("F");
		    			map[row][col].setFalseFlag(true);
		    			revealCell(map[row][col]);
		    			if(!testMode) {
		    				Sound.flagSound();
		    			}
	    			}
	    		}
	    		else {
	    			if(!testMode) {
	    				Sound.digSound();
	    			}
	    			revealCell(map[row][col]);
	    			if(map[row][col].getStatus().equals("0")) {
		    			revealZeroes(row,col);
		    		}
	    		}
			}
    		if(revealedCells == totalCells) {
    			gameOver = true;
    		}
	    	return true;
    	}
    }

    /**
     * gameOver
     * 
     * Ways a game of Minesweeper ends:
     * 1. player guesses a cell with a mine: game over -> player loses
     * 2. player has revealed the last cell without revealing any mines -> player wins
     * 
     * @return boolean Return false if game is not over and squares have yet to be revealed, otheriwse return true.
     */
    public boolean gameOver() {
    	return gameOver;
    }

    /**
     * Reveal the cells that contain zeroes that surround the inputted cell.
     * Continue revealing 0-cells in every direction until no more 0-cells are found in any direction.
     * Utilize a STACK to accomplish this.
     *
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a stack be useful here rather than a queue?
     *
     * @param row      The x value the user entered.
     * @param col      The y value the user entered.
     */
    public void revealZeroes(int row, int col) {
    	Stack1Gen stack = new Stack1Gen();
        int[] points = {row,col};
        stack.push(points);
        while(!stack.isEmpty()){
            int[] curr = (int[]) stack.pop();
            revealCell(map[curr[0]][curr[1]]);
            if (curr[0] + 1 <= rows-1 && map[curr[0]+1][curr[1]].getStatus().equals("0") && map[curr[0]+1][curr[1]].getRevealed() == false) {
                int[] addDown = {curr[0] + 1,curr[1]};
                stack.push(addDown);
            }
            if (curr[0] - 1 >= 0 && map[curr[0]-1][curr[1]].getStatus().equals("0") && map[curr[0]-1][curr[1]].getRevealed() == false) {
                int[] addUp = {curr[0] - 1,curr[1]};
                stack.push(addUp);
            }
            if (curr[1] + 1 <= cols-1 && map[curr[0]][curr[1]+1].getStatus().equals("0") && map[curr[0]][curr[1]+1].getRevealed() == false) {
                int[] addRight = {curr[0],curr[1] + 1};
                stack.push(addRight);
            }
            if (curr[1] - 1 >= 0 && map[curr[0]][curr[1]-1].getStatus().equals("0") && map[curr[0]][curr[1]-1].getRevealed() == false) {
                int[] addLeft = {curr[0],curr[1] - 1};
                stack.push(addLeft);
            }
        }
    }

    /**
     * revealStartingArea
     *
     * On the starting move only reveal the neighboring cells of the inital cell and continue revealing the surrounding concealed cells until a mine is found.
     * Utilize a QUEUE to accomplish this.
     * 
     * This method should follow the psuedocode given in the lab writeup.
     * Why might a queue be useful for this function?
     *
     * @param x     The x value the user entered.
     * @param y     The y value the user entered.
     */
    public void revealStartingArea(int y, int x) {
    	Q1Gen<Point> queue = new Q1Gen<Point>();
  
    	revealCell(map[y][x]);
    	Point loc = new Point(x,y);
    	
    	queue.add(loc);
    	
    	while(queue.length() != 0) {
    		
    		Point curr = queue.remove();
    		revealCell(map[curr.y][curr.x]);
    		
    		if(map[curr.y][curr.x].getStatus().equals("M")) {
    			return;
    		}
    		
    		if(curr.y-1 >= 0) {
    			Point p = new Point(curr.x,curr.y-1);
    			if(!map[p.y][p.x].getRevealed()) {
					queue.add(p);
				}
    		}
    		if(curr.y+1 < rows) {
    			Point p = new Point(curr.x,curr.y+1);
    			if(!map[p.y][p.x].getRevealed()) {
					queue.add(p);
				}
    		}
    		if(curr.x-1 >= 0) {
    			Point p = new Point(curr.x-1,curr.y);
    			if(!map[p.y][p.x].getRevealed()) {
					queue.add(p);
				}
    		}
    		if(curr.x+1 < cols) {
    			Point p = new Point(curr.x+1,curr.y);
    			if(!map[p.y][p.x].getRevealed()) {
					queue.add(p);
				}
    		}
    		
    	}
    	
    	
    }

    /**
     * For both printing methods utilize the ANSI colour codes provided! 
     * 
     * 
     * 
     * 
     * 
     * debug
     *
     * @function This method should print the entire minefield, regardless if the user has guessed a square.
     * *This method should print out when debug mode has been selected. 
     */
    public void debug() {
    	
    	System.out.println("DEBUGGING VIEW:");
    	System.out.print("-");
    	for(int i = 0; i < cols; i++) {
    		System.out.print("---");
    	}
    	System.out.print("\n");
    	
    	System.out.print("   ");
    	for(int i = 0; i < cols; i++) {
    		if(i < 10) {
    			System.out.print(i + "  ");
    		}else {
    			System.out.print(i + " ");
    		}
    	}
    	System.out.print("\n");
    	
    	for(int y = 0; y < rows; y++) {
    		if(y < 10) {
    			System.out.print(y + "  ");
    		}else {
    			System.out.print(y + " ");
    		}
    		for(int x = 0; x < cols; x++) {
    			System.out.print(color(map[y][x].getStatus()) + "  ");
			}
    		System.out.println();
		}
    	
    	System.out.print("-");
    	for(int i = 0; i < cols; i++) {
    		System.out.print("---");
    	}
    	System.out.print("\n");
    	
    	System.out.println();
    }

    /**
     * toString
     *
     * @return String The string that is returned only has the squares that has been revealed to the user or that the user has guessed.
     */
    public String toString() {
    	if(debugMode) {
    		debug();
    	}
    	String str = "";
    	
    	str += "MINEFIELD:\n";
    	
    	str += "-";
    	for(int i = 0; i < cols; i++) {
    		str += "---";
    	}
    	str += "\n";
    	
    	str += "   ";
    	for(int i = 0; i < cols; i++) {
    		if(i < 10) {
    			str += i + "  ";
    		} else {
    			str += i + " ";
    		}
    	}
    	str += "\n";
    	
    	for(int y = 0; y < rows; y++) {
    		if(y < 10) {
    			str += y + "  ";
    		}else {
    			str += y + " ";
    		}
    		for(int x = 0; x < cols; x++) {
    			if(map[y][x].getRevealed()) {
    				str += color(map[y][x].getStatus()) + "  ";
    			} else {
    				str += "-  ";
    			}
			}
    		str += "\n";
		}
    	
    	str += "-";
    	for(int i = 0; i < cols; i++) {
    		str += "---";
    	}
    	str += "\n";
    	
    	return str;

    }
    
    public String color(String input) {
    	switch(input) {
    		case "-":
    			return ANSI_GREY_BACKGROUND + input;
    		case "M":
    			return ANSI_RED + input + ANSI_GREY_BACKGROUND;
    		case "4":
    			return ANSI_RED + input + ANSI_GREY_BACKGROUND;
    		case "3":
    			return ANSI_PURPLE + input + ANSI_GREY_BACKGROUND;
    		case "2":
    			return ANSI_GREEN + input + ANSI_GREY_BACKGROUND;
    		case "1":
    			return ANSI_CYAN + input + ANSI_GREY_BACKGROUND;
    		case "0":
				return ANSI_YELLOW + input + ANSI_GREY_BACKGROUND;
			default:
				return ANSI_RED + input + ANSI_GREY_BACKGROUND;
    			
    	}
    }
    
    private void revealCell(Cell cell) {
    	if(!cell.getRevealed()) {
	    	revealedCells++;
	    	cell.setRevealed(true);
    	}
    }

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getMines(){ return mines;}

	public Cell[][] getMap() {
		return map;
	}

	public void setMap(Cell[][] map) {
		this.map = map;
	}
	
	public int getRevealedCells() {
		return revealedCells;
	}
	
	public boolean isFirstTurn() {
		return firstMove;
	}
}
