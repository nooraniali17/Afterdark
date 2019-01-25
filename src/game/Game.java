package game;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import menus.*;
import java.util.List;
import javax.swing.Timer;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import utilities.AudioPlayer;
import utilities.GButton;
import utilities.GraphicsPane;
import utilities.MainApplication;

/*
 * This class creates the various thresholds for the scrolling, it senses the
 * different keyboard presses and releases, updates the health bar and makes
 * the character, bullet, enemy, and camera view move along the scene.
 */
public class Game extends GraphicsPane implements ActionListener {
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	public static final int GROUND_HEIGHT = 0;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int GROUND_Y = HEIGHT - GROUND_HEIGHT;
	public static final int BLOCK_HEIGHT = GROUND_HEIGHT/7;
	public static final int BLOCK_WIDTH = GROUND_HEIGHT;
	private static final double VERTICAL_SCROLL_RATIO = 2;
	private static final double HORIZONTAL_SCROLL_RATIO = 2;
	
	private MainApplication program;
	private Timer gameLoop;
	private List<Scene> scenes;
	private int sceneNum;
	private Direction walk;
	private GButton healthShell;
	private GButton healthButton;
	private GLabel healthLabel;
	private AudioPlayer music;
	
	//TODO creating private variables here for stopwatch button test
	private GButton stopwatchButton;
	private GLabel stopwatchLabel;
	
	//TODO create public variable for time elapsed while playing game
	public static int secondsPassed;
	
	//TODO create private variable for the number of ticks before changing time
	private int numTicks;
	
	public Game(MainApplication app) {
		this.program = app;
		sceneNum = 0;
		scenes = new ArrayList<Scene>();
		scenes.add(new Scene(TILE_WIDTH, TILE_HEIGHT, program));
		gameLoop = new Timer(20, this);
		walk = Direction.NO_DIRECTION;
		music = AudioPlayer.getInstance();
		healthShell = new GButton("", 100, 50, 200, 20, Color.white);
		healthButton = new GButton("100", 100, 50, 200, 20, Color.red);
		healthButton.setColor(Color.black);
		healthLabel = new GLabel("Health", 100, 40);
		healthLabel.setColor(Color.red);
		healthLabel.setFont("Comic Sans MS-30");
		//TODO create stopwatch button and label below
		stopwatchButton = new GButton("0", 400, 50, 150, 50, Color.green);
		stopwatchLabel = new GLabel("Seconds Elapsed", 400, 40);
		stopwatchLabel.setColor(Color.green);
		stopwatchLabel.setFont("Comic Sans MS-30");
	}
	/*
	 * This maintains the various thresholds, which determines when the 
	 * screen should actually start to scroll.
	 */
	static int leftThreshold() {
		return MainApplication.WINDOW_WIDTH / (int) HORIZONTAL_SCROLL_RATIO;
	}
	static int rightThreshold() {
		return MainApplication.WINDOW_WIDTH - (MainApplication.WINDOW_WIDTH / (int) HORIZONTAL_SCROLL_RATIO);
	}
	static int topThreshold() {
		return (MainApplication.WINDOW_HEIGHT / (int) VERTICAL_SCROLL_RATIO);
	}
	static int bottomThreshold() {
		return MainApplication.WINDOW_HEIGHT - (MainApplication.WINDOW_HEIGHT / (int) VERTICAL_SCROLL_RATIO);
	}
	static int horzCenter() {
		return MainApplication.WINDOW_WIDTH / 2;
	}
	static int vertCenter() {
		return MainApplication.WINDOW_HEIGHT / 2;
	}
	
	@Override
	/*
	 * When various keys are pressed throughout the game different
	 * actions will be performed, such as the movement of the character
	 * or the firing of the gun.
	 */
	public void keyPressed(KeyEvent e) {
		Scene curScene = scenes.get(sceneNum);
//		Entity player = curScene.getPlayer();
		// User presses M: turns music on or off
		if(e.getKeyCode() == KeyEvent.VK_M){
			if(program.isMusicOn() == true){
				program.setMusicIsOn(false);
				music.stopSound("../sounds", "game_music.mp3");
			}
			else{
				program.setMusicIsOn(true);
				music.playSound("../sounds", "game_music.mp3");
			}
			
			if(program.isSoundOn() == true){
				program.setSoundIsOn(false);
			}
			else{
				program.setSoundIsOn(true);
			}
		}
		// User presses P or Esc: pauses game
		if(e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			program.switchToPauseMenu();
		}
		// User presses Right or D: moves character right
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			walk = Direction.EAST;
			curScene.playerWalk(Direction.EAST);
		}
		// User presses Left or A: moves character left
		else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			walk = Direction.WEST;
			curScene.playerWalk(Direction.WEST);
		}
		else if(e.getKeyCode() == KeyEvent.VK_W){
			program.switchToGameWon();
		}
		// User presses Space, Up, or W: character jumps up
		if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			if(!curScene.isPlayerJumping()) {
				curScene.playerJump();
				if(program.isSoundOn()){
					music.playSound("../sounds/jumping_sound.wav");
				}
				else{
					music.stopSound("../sounds", "jumping_sound.wav");
				}
			}
		}
	}
	
	@Override
	/* When user lets go of key, character begins to stop moving
	 * and friction begins
	 */
	public void keyReleased(KeyEvent e) {
		Scene curScene = scenes.get(sceneNum);
		Entity player = curScene.getPlayer();

		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
			walk = Direction.NO_DIRECTION;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_V) {
			if (!((Player) player).isOnCooldown()) {
				((Player) player).goOnCooldown();
				if (program.isSoundOn()) {
					music.playSound("../sounds/shoot_sound.wav");
				} else {
					music.stopSound("../sounds", "shoot_sound.wav");
				}
				program.add(curScene.addBullet(curScene.getPlayer(), player.getX() + 15, player.getY() + 25,
						curScene.getPlayer().isDirectionFacing()).getSprite());
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		updateHealthBar();
		Scene curScene = scenes.get(sceneNum);
		curScene.tick(walk);
		//TODO increment the number of ticks to keep track of how many occur
		numTicks++;
		
		//TODO Need to update stopwatch after every second of gameplay
		//check the number of ticks and see if it equals 1 second
		//ticks happen once every 20ms so 50 ticks is 1 second
		//if it does then increment the number of seconds passed in the game
		if(numTicks % 50 == 0) {
			updateStopwatch();
		}
	}
	
	/*
	 * This changes the size of the health bar depending on the amount of
	 * damage the player is taking and how much health it currently has left.
	 */
	public void updateHealthBar(){
		healthButton.setSize(scenes.get(sceneNum).getPlayer().getHealth()*2, 20);
		healthButton.setLabel(Integer.toString(scenes.get(sceneNum).getPlayer().getHealth()));
	}

	/*
	 * This method will need to display an actual stopwatch in the label that times
	 * how long it takes the player to reach the end of that particular level.
	 */
	public void updateStopwatch() {
		//TODO set the label of the stopwatch to show the actual timer
		secondsPassed++;
		stopwatchButton.setLabel(Integer.toString(secondsPassed));
	}
	
	//TODO need this getter so that way MainApplication.java can access seconds passed
	public int getSecondsPassed() {
		return secondsPassed;
	}
	
	@Override
	/*
	 * Adding the various items to the screen
	 */
	public void showContents() {
		program.setBackground(Color.DARK_GRAY);
		Scene curScene = scenes.get(sceneNum);
		Entity player = curScene.getPlayer();
		program.add(curScene.getPlayer().getSprite());
		for (Entity e: curScene.getNPCs()) {
			program.add(e.getSprite());
		}
		for (List<Block> row: curScene.getTerrain()) {
			for (Block b: row) {
				if (b != null) {
					program.add(b);
				}
			}
		}
		scenes.get(sceneNum).drawScene();
		gameLoop.start();
		
		//adding in the music
		if(program.isMusicOn()){
	    	music.stopSound("../sounds", "menu_music.mp3");
			music.playSound("../sounds", "game_music.mp3");
	    }
	    else{
	    	music.stopSound("../sounds", "game_music.mp3");
	    }
		program.add(healthShell);
		program.add(healthButton);
		program.add(healthLabel);
		
		//TODO add the stopwatch button to the screen
		program.add(stopwatchButton);
		//TODO add the stopwatch label to the screen
		program.add(stopwatchLabel);
		
		
	}

	@Override
	public void hideContents() {
		program.removeAll();
		gameLoop.stop(); 
	}
	
	
	
}