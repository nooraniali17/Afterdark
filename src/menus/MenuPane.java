package menus;

/*
 * This is the main menu. This is the screen the user is first introduced with and interacts with.
 * From this screen, the user can play/resume game, look at controls or go to settings. The user can also
 * click on the title and be sent to the credits page.
 */
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import acm.graphics.GImage;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import utilities.AudioPlayer;
import utilities.GButton;
import utilities.GraphicsPane;
import utilities.MainApplication;

public class MenuPane extends GraphicsPane {
	private MainApplication program; //you will use program to get access to all of the GraphicsProgram calls
	private GImage background;
	private GButton playButton;
	private GButton settingsButton;
	private GButton controlsButton;
	private GButton titleButton;
	
	
	
//	These are all the factors for the buttons we put into the menu. 
//	The buttons are all invisible due to the background.
	private static final double WIDTH_FACTOR = 25.6;
	private static final double PLAY_HEIGHT_FACTOR = 24;
	private static final double SETTINGS_HEIGHT_FACTOR = 7.45;
	private static final double CONTROLS_HEIGHT_FACTOR = 5.29;
	private static final double PLAY_SIZE_WIDTH = 4.68;
	private static final double PLAY_SIZE_HEIGHT = 10.97;
	private static final double OTHER_SIZE_HEIGHT = 19.2;
	private static final double OTHER_SIZE_WIDTH = 4.57;
	private static final double TITLE_OFFSET = 500;
	
	private final double WIDTH_OFFSET= program.WINDOW_WIDTH/WIDTH_FACTOR;
	private final double PLAY_HEIGHT_OFFSET = program.WINDOW_HEIGHT/PLAY_HEIGHT_FACTOR;
	private final double SETTINGS_HEIGHT_OFFSET = program.WINDOW_HEIGHT/SETTINGS_HEIGHT_FACTOR;
	private final double CONTROLS_HEIGHT_OFFSET = program.WINDOW_HEIGHT/CONTROLS_HEIGHT_FACTOR;

	private AudioPlayer music;
	private GraphicsPane settings;
	
	/*
	 * Creating various buttons below and adding them to the menu pane.
	 * Each button is made in such a way that as the background image 
	 * expands the buttons will too.
	 */
	public MenuPane(MainApplication app) {
		program = app;
		background = new GImage("images/Main Menu.png", 0, 0);
		playButton = new GButton(WIDTH_OFFSET, PLAY_HEIGHT_OFFSET, 
						program.WINDOW_WIDTH/PLAY_SIZE_WIDTH,
						program.WINDOW_HEIGHT/PLAY_SIZE_HEIGHT, false);
		settingsButton = new GButton(WIDTH_OFFSET, SETTINGS_HEIGHT_OFFSET,
						program.WINDOW_WIDTH/OTHER_SIZE_WIDTH,
						program.WINDOW_HEIGHT/OTHER_SIZE_HEIGHT, false);
		controlsButton = new GButton(WIDTH_OFFSET, CONTROLS_HEIGHT_OFFSET,
						program.WINDOW_WIDTH/OTHER_SIZE_WIDTH,
						program.WINDOW_HEIGHT/OTHER_SIZE_HEIGHT, false);
		titleButton = new GButton(TITLE_OFFSET, 0, 520, 75, false);
		
		music = AudioPlayer.getInstance();
	}
	
	@Override
	/*
	 * Adding the various items to the screen
	 */
	public void showContents() {
		program.add(background);
		program.add(playButton);
		program.add(settingsButton);
		program.add(controlsButton);
		program.add(titleButton);
		
		//make sure to put ../ in front of sounds so that we get out of the
		//menus folder and then go to the sounds folder
	    if(program.isMusicOn()){
	    	music.stopSound("../sounds", "game_music.mp3");
	    	music.playSound("../sounds", "menu_music.mp3");
	    }
	    else{
	    	music.stopSound("../sounds", "menu_music.mp3");
	    }
	}

	@Override
	public void hideContents() {
		program.remove(background);
		program.remove(playButton);
		program.remove(settingsButton);
		program.remove(controlsButton);
		program.remove(titleButton);
	}
	
	/*
	 * When various keys are pressed throughout the game different
	 * actions will be performed, such as turning the music on or off.
	 */
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_P){
			program.switchToGame();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_S){
			program.switchToSettingsMenu();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_C){
			program.switchToControlsMenu();
		}
		
		if(e.getKeyCode() == KeyEvent.VK_M){
			if(program.isMusicOn() == true){
				program.setMusicIsOn(false);
		    	music.stopSound("../sounds", "menu_music.mp3");
			}
			else{
				program.setMusicIsOn(true);
		    	music.playSound("../sounds", "menu_music.mp3");
			}
			
			if(program.isSoundOn() == true){
				program.setSoundIsOn(false);
			}
			else{
				program.setSoundIsOn(true);
			}
		}
		
	}

	@Override
	/*
	 * This method does the appropriate task depending on what
	 * button the user clicks on.
	 */
	public void mousePressed(MouseEvent e) {
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if(obj == playButton) {
			program.switchToGame();
		}
		if(obj == settingsButton){
			program.switchToSettingsMenu();
		}
		if(obj == controlsButton){
			program.switchToControlsMenu();
		}
		if(obj == titleButton){
			program.switchToCredits();
		}
	}
}