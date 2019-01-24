package game;
/**
 * SceneLayout.java
 * @author jackthias
 * Created 11/7/16
 * Holds all of the blocks in a scene, holds the backgrounds for a scene. Holds methods for reading
 * in terrain information from a file.
 */

import java.awt.Rectangle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import acm.graphics.GImage;
import utilities.MainApplication;

/**
 * Holds all of the blocks in a scene, holds the backgrounds for a scene. Holds methods for reading
 * in terrain information from a file.
 * @author jackthias
 */
public class SceneLayout { 
	public static int TILE_WIDTH = 64;
	public static int TILE_HEIGHT = 64;
	private static final boolean PRINT_CONTENTS = false;
	//public static final String LEVEL_PREFIX = "../media/levels/";
	//public static final String SPRITE_PREFIX = "../media/images/";
	public static final String LEVEL_PREFIX = "media/levels/";
	public static final String SPRITE_PREFIX = "media/images/";
	
	private List<List<Block>> terrain = new ArrayList<List<Block>>();
	private ArrayList<GImage> backgrounds;
	private boolean goalHit;
	
	public SceneLayout(int tileWidth, int tileHeight) {
		backgrounds = new ArrayList<GImage>();
		goalHit = false;
		readInData(LEVEL_PREFIX + "prototype1.csv");
	}
	
	public List<List<Block>> getTerrain() {
		return terrain;
	}

	public ArrayList<GImage> getBackgrounds() {
		return backgrounds;
	}

	public void setBackgrounds(ArrayList<GImage> backgrounds) {
		this.backgrounds = backgrounds;
	}

	// Reads in data for setting blocks in scene layout
	private boolean readInData(String path) {
		List<String[]> data = new ArrayList<String[]>();
		String line = "";
		String cvsSplitBy = ",";
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			while ((line = reader.readLine()) != null) {
                // use comma as separator
				data.add(line.split(cvsSplitBy));
            }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if (PRINT_CONTENTS) {
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < data.get(i).length; j++) {
					System.out.print(data.get(i)[j] + ", ");
				}
				System.out.print("\n");
			} 
		}
		
		// Reads in data from Block 2 dimensional list and adds to screen
		for (int i = 0; i < data.size(); i++) {
			terrain.add(new ArrayList<Block>());
			for (int j = 0; j < data.get(i).length; j++) {
				if (!data.get(i)[j].equals("-1")) {
					if (!(data.get(i)[j].equals("11") || data.get(i)[j].equals("12"))) {
						terrain.get(i).add(new Block(SPRITE_PREFIX + "terrain_" + data.get(i)[j] + ".png", j*TILE_HEIGHT, i*TILE_WIDTH));
					} else {
						terrain.get(i).add(new EndBlock(SPRITE_PREFIX + "terrain_" + data.get(i)[j] + ".png", j*TILE_HEIGHT, i*TILE_WIDTH));
					}
					terrain.get(i).get(j).setSize(TILE_WIDTH, TILE_HEIGHT);
				} else {
					terrain.get(i).add(null);
				}
			}
		}
		
		return true;
	}
	
	public void setBlock(int x, int y, Block b) {
		terrain.get(x).set(y, b);
	}
	
	// Detects collision of character with terrain in scene layout
	public Direction checkCollisions(Entity e) {
		Rectangle personRect = e.getBox();
		for(List<Block> row:terrain) {
			for (Block b:row) {
				if (b != null) {
					Direction d = b.getDirectionComingFrom(personRect);
					if (d != Direction.NO_DIRECTION) {
						if (b instanceof EndBlock) {
							if (e instanceof Player) {
								goalHit = true;
							}
						}
						changeCharacter(e, d, b);
						changeBlock(b, e);
						return d;
					} 
				} 
			}
		}
		return Direction.NO_DIRECTION;
	}
	
	// Checks bullet collision with terrain in scene layout
	public Direction checkBulletCollisions(Bullet m) {
		Rectangle personRect = m.getBox();
		for(List<Block> row:terrain) {
			for (Block b:row) {
				if (b != null) {
					Direction d = b.getDirectionComingFrom(personRect);
					if (d != Direction.NO_DIRECTION) {
						changeBullet(m, b);
						changeBlock(b, m);
						return d;
					} 
				} 
			}
		}
		return Direction.NO_DIRECTION;
	}
	
	private void changeBullet(Bullet m, Block b) {
		if (b.isSolid()) {
			//Exists for future expansion.
		}
	}

	private void changeBlock(Block b, Bullet m) {
		//Exists for future expansion.
	}

	public void changeBlock(Block b, Entity e) {
		
	}
	
	// Has movement of character reflected based off of collision
	// with terrain wall
	public void changeCharacter(Entity e, Direction d, Block b) {
		if (b instanceof Block) {
			if (b.isSolid()) {
				if (d == Direction.NORTH) {
					e.setJumping(false);
				}
				if (d == Direction.SOUTH) {
					e.reflectVertically();
				}
				if (d == Direction.EAST || d == Direction.WEST) {
					e.reflectHorizontally();
					if (d == Direction.EAST) {
						e.move(5, 0);
					} else {
						e.move(0, 5);
					}
				} 
			}
		}
	}

	// Looks for ground below Entity
	public Block findGround(Entity e) {
		Rectangle personRect = e.getBox();
		for(List<Block> row:terrain) {
			for (Block b:row) {
				if (b != null) {
					Direction d = b.getDirectionComingFrom(personRect);
					if (d == Direction.NORTH) {
						return b;
					} 
				} 
			}
		}
		return null;
	}
	
	public void horzScroll(double distance) {
		for (List<Block> row:terrain) {
			for (Block b: row) {
				if (b != null) {
					b.move(distance, 0);
				}
			}
		}
	}
	
	public void vertScroll(double distance) {
		for (List<Block> row:terrain) {
			for (Block b: row) {
				if (b != null) {
					b.move(0, distance);
				}
			}
		}
	}

	public boolean terrainRightOfWindow() {
		for (List<Block> row : terrain) {
			for (Block b : row) {
				if (b != null) {
					if (b.getX() > MainApplication.WINDOW_WIDTH) {
						return true;
					} 
				}
			}
		}
		return false;
	}

	public boolean terrainLeftOfWindow() {
		for (List<Block> row : terrain) {
			for (Block b : row) {
				if (b != null) {
					if (b.getX() < 0) {
						return true;
					} 
				}
			}
		}
		return false;
	}

	public boolean terrainBelowWindow() {
		for (List<Block> row : terrain) {
			for (Block b : row) {
				if (b != null) {
					if (b.getY() > MainApplication.WINDOW_HEIGHT) {
						return true;
					} 
				}
			}
		}
		return false;
	}

	public boolean terrainAboveWindow() {
		for (List<Block> row : terrain) {
			for (Block b : row) {
				if (b != null) {
					if (b.getY() < 0) {
						return true;
					} 
				}
			}
		}
		return false;
	}

	public boolean wasGoalHit() {
		return goalHit;
	}

	public void setGoalHit(boolean goalHit) {
		this.goalHit = goalHit;
	}
}
