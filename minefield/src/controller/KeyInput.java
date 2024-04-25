package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class KeyInput implements MouseListener {	
	
	public KeyInput() {}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseReleased(MouseEvent e) 
	{
		
	}
	
	public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(Game.state == Game.GameState.Game) {
				Game.cellManager.clickedBox(e.getX(), e.getY(), false);
			}
			if(Game.state == Game.GameState.Menu) {
				Game.mainMenu.checkButton(e.getX(), e.getY());
			}
		}
		else {
			if(Game.state == Game.GameState.Game) {
				Game.cellManager.clickedBox(e.getX(), e.getY(), true);
			}
		}
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) 
	{
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
