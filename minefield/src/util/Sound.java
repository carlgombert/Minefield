package util;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import controller.Game;

/**
 * The Sound class serves as an audioplayer for music and different in game events
 */
public class Sound {
	
	private static Clip clip;
	private static URL[] soundFile = new URL[10];
	
	
	private static Queue<Clip>[][] repeatedClips = new LinkedList[30][2];
	
	
	public Sound() {
		
		soundFile[0] = Sound.class.getClassLoader().getResource("resources/click.wav");
		soundFile[1] = Sound.class.getClassLoader().getResource("resources/dig.wav");
		soundFile[2] = Sound.class.getClassLoader().getResource("resources/explosion.wav");
		soundFile[3] = Sound.class.getClassLoader().getResource("resources/flag.wav");
		soundFile[4] = Sound.class.getClassLoader().getResource("resources/win.wav");
		soundFile[5] = Sound.class.getClassLoader().getResource("resources/lose.wav");
		
		for(int i = 0; i <= 5; i++) {
			repeatedClips[i][0] = new LinkedList<>();
			repeatedClips[i][1] = new LinkedList<>();
			for(int j = 0; j < 10; j++) {
				setFile(i);
				repeatedClips[i][0].add(clip);
			}
		}
	}
	
	public static void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile[i]);
			clip = AudioSystem.getClip();
			if(!clip.isOpen()) {
				clip.open(ais);
			}
		} catch (LineUnavailableException | IllegalStateException | IOException | OutOfMemoryError | UnsupportedAudioFileException e) {
			System.out.println("clip not played");
		}
	}
	
	public static void play() {
		clip.start();
	}
	
	public static void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void play(int index) {
		
		// move all clips that ended from the playing queue back to the playable queue
		while(repeatedClips[index][1].peek() != null && repeatedClips[index][1].peek().isRunning() == false) {
			Clip temp = repeatedClips[index][1].remove();
			temp.setMicrosecondPosition(0);
			repeatedClips[index][0].add(temp);
		}
		
		// play the top of the playable queue
		if(repeatedClips[index][0].peek() != null) {
			Clip temp = repeatedClips[index][0].remove();
			temp.start();
			repeatedClips[index][1].add(temp);
		}
	}
	
	public static void clickSound() {
		play(0);
	}
	
	public static void digSound() {
		play(1);
	}
	
	public static void explosionSound() {
		play(2);
	}
	
	public static void flagSound() {
		play(3);
	}
	
	public static void winSound() {
		play(4);
	}
	
	public static void loseSound() {
		play(5);
	}
	
}