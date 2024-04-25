package util;

import java.awt.Color;

public class Util {
	
	/**
    * generates a random number in a given range
    *
    * @param  min minumum value for the random number
    * @param  max maxiumum value for the random number
    * @return         returns the random integer
    */
	public static int randomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	/**
    * processes the users answer into a much more usable form
    *
    * @param  answer users answer as char array
    * @return         returns the x y and flag as an integer array
    */
	public static int[] processAnswer(char[] answer) {
		
		int x = 0;
		int y = 0;
		int flag = 0;
		
		// the number of spaces occured, needed to keep track of what number it's on
		int spaces = 0;
		
		// the index of last space, used to track the 10 power of current digit
		int lastSpaceIdx = answer.length;
		
		// builds numbers for all values by parsing through array
		for(int i = answer.length-1; i >= 0; i--) {
			
			// if current is space, increase space count
			if(answer[i] == ' ') {
				spaces++;
				lastSpaceIdx = i-1;
			}
			// check to make sure the current character is a number
			else if(Character.getNumericValue(answer[i]) > 9 || Character.getNumericValue(answer[i]) < 0) {
				return null;
			}
			else if(spaces == 0) {
				flag += Character.getNumericValue(answer[i]) * Math.pow(10, (lastSpaceIdx - i));
			}
			else if (spaces == 1) {
				y += Character.getNumericValue(answer[i]) * Math.pow(10, (lastSpaceIdx - i));
			}
			else if(spaces == 2) {
				x += Character.getNumericValue(answer[i]) * Math.pow(10, (lastSpaceIdx - i));
			}
		}
		int[] result = {x, y, flag};
		
		return result;
	}
	
	public static Color getColor(String text) {
		switch(text) {
			case "-":
				return Color.LIGHT_GRAY;
			case "M":
				return Color.RED;
			case "3":
				return Color.RED;
			case "4":
				return Color.MAGENTA;
			case "2":
				return Color.GREEN;
			case "1":
				return Color.BLUE;
			case "0":
				return Color.YELLOW;
			default:
				return Color.RED;
				
		}
		
	}
}
