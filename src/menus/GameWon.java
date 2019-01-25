package menus;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;

import utilities.GButton;
import utilities.GraphicsPane;
import utilities.MainApplication;

//TODO import necessary libraries
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.Desktop;
import java.io.File;

/*
 * This is a pane that shows up when the player has won the game! The only way the user can win the game is 
 * if the user passes the level.
 */
public class GameWon extends GraphicsPane {
	private GButton returnToMain;
	private GLabel congratMessage;
	private MainApplication program;
	//TODO create GLabel for the stopwatchMessage
	private GLabel timeMessage;
	
	
	//TODO create GButton to access the high scores text file
	private GButton highScoreButton;
	
	//TODO create old score variable here
	public static String oldScoreString;

	/*
	 * This is the constructor that adds the various buttons and labels to
	 * our screen in the correct positions.
	 */
//	Adds buttons and labels on the screen that is shows

	public GameWon(MainApplication main){
		program = main;
		returnToMain = new GButton("Return to Main", (double) MainApplication.WINDOW_WIDTH / 5 + 100, (double) MainApplication.WINDOW_HEIGHT / 10, 400, 100, Color.DARK_GRAY);
		returnToMain.setColor(Color.WHITE);
		congratMessage = new GLabel("Congratulations you beat the Level!", MainApplication.WINDOW_WIDTH / 5, MainApplication.WINDOW_HEIGHT / 3 + 20);
		congratMessage.setFont("Comic Sans MS-36");
		congratMessage.setColor(Color.WHITE);
		
		//TODO stop the timer and display time in new label
		timeMessage = new GLabel("", MainApplication.WINDOW_WIDTH / 3 - 80, 2 * MainApplication.WINDOW_HEIGHT / 3);
		
		//TODO set font for the stopwatchMessage
		timeMessage.setFont("Comic Sans MS-36");
		
		//TODO set color for the stopwatchMessage
		timeMessage.setColor(Color.WHITE);
		
		
		
		//TODO save the stopwatchMessage in a text file that gets appended to
		highScoreButton = new GButton("View High Scores", (double) MainApplication.WINDOW_WIDTH / 5 + 100, 2 * (double) MainApplication.WINDOW_HEIGHT / 3 + 100, 400, 100, Color.DARK_GRAY);
		
	}
	
	/*
	 * This method will take in the click of the mouse, find its location
	 * and then see if it is on a particular button. If the mouse clicks
	 * on the button then the screen will change.
	 */

//	If the user clicks on the button, it takes it to the location it needs to go
//	In this case, it goes back to menu
	public void mousePressed(MouseEvent e){
		int bufferSize = 8 * 1024;
		GObject obj = program.getElementAt(e.getX(), e.getY());
		if(obj == returnToMain){
			program.resetGame();
			program.switchToMenu();
		}
		//TODO if mouse pressed on view high scores open text file
		if(obj == highScoreButton) {
			//TODO open text file here
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader("High Scores.txt"), bufferSize);
				try {
					oldScoreString = bufferedReader.readLine();
					bufferedReader.close();
					System.out.println("OLD SCORE: "+Integer.parseInt(oldScoreString));
					if(program.getSecondsPassed() < Integer.parseInt(oldScoreString)) {
						//TODO write the seconds passed into a text file
						FileWriter writer = new FileWriter("High Scores.txt");
						writer.write(Integer.toString(program.getSecondsPassed()));
						writer.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			File file = new File("High Scores.txt");
			Desktop desktop = Desktop.getDesktop();
			if(file.exists()) {
				try {
					desktop.open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	
//	Shows all buttons and labels made for this class
	@Override
	public void showContents() {
		program.add(returnToMain);
		program.add(congratMessage);
		program.setBackground(Color.DARK_GRAY);
		//TODO add buttons and labels for the stopwatch here
		timeMessage.setLabel("You finished the level in "+program.getSecondsPassed()+" seconds!");
		System.out.println(program.getSecondsPassed());
		program.add(timeMessage);
		//TODO add high score button
		program.add(highScoreButton);
		
	}

//	Hides all buttons and labels made for this class
	@Override
	public void hideContents() {
		program.setBackground(null);
		program.remove(returnToMain);
		program.remove(congratMessage);
		
		//TODO remove buttons and labels for the stopwatch here
		program.remove(timeMessage);
		
		//TODO remove high score button
		program.remove(highScoreButton);
	}

}
