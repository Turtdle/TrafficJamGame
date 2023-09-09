import java.util.*;

/**
 * This represents the board.  Really what it is going to do is just have a 2d array of the vehicles
 * (which we'll refer to as grid), and it will be in charge of doing the bounds type checking for doing any of the moves.
 * It will also have a method display board which will give back a string representation of the board
 * 
 * @author Osvaldo
 *
 */

public class Board {
	Vehicle[][] grid;

	
	//TODO Add the other methods that are in the handout, and fill out the rest of this file
	
	/**
	 * Constructor for the board which sets up an empty grid of size rows by columns
	 * Use the first array index as the rows and the second index as the columns
	 * 
	 * @param rows number of rows on the board
	 * @param cols number of columns on the board
	 */
	public Board(int rows, int cols) {
		grid = new Vehicle[rows][cols];
	}
	
	/**
	 * @return number of columns the board has
	 */
	public int getNumCols() {
		//TODO change this method, which should return the number of columns the grid has
		return grid[0].length;
	}

	/**
	 * @return number of rows the board has
	 */
	public int getNumRows() {
		//TODO change this method, which should return the number of rows the grid has
		return grid.length;
	}
	
	/**
	 * Grabs the vehicle present on a particular space if any is there
	 * If a Vehicle occupies three spaces, the same Vehicle pointer should be returned for all three spaces
	 * 
	 * @param s the desired space where you want to look to see if a vehicle is there
	 * @return a pointer to the Vehicle object present on that space, if no Vehicle is present, null is returned
	 */
	public Vehicle getVehicleAt(Location s) {
		return grid[s.getRow()][s.getCol()];
	}

	/**
	 * adds a vehicle to the board. It would be good to do some checks for a legal placement here.
	 * 
	 * @param type type of the vehicle
	 * @param startRow row for location of vehicle's top
	 * @param startCol column for for location of vehicle leftmost space
	 * @param vert true if the vehicle should be vertical
	 * @param length number of spaces the vehicle occupies on the board
	 */
	public void addVehicle(VehicleType type, int startRow, int startCol, boolean vert, int length) {
		Vehicle newVehicle = new Vehicle(type, startRow, startCol, vert, length);
		for(Location location : newVehicle.locationsOn()) {
			grid[location.getRow()][location.getCol()] = newVehicle;
		}
	}

	/**
	 * This method moves the vehicle at a certain location a specific number of spaces and updates the board's grid to reflect it
	 * 
	 * @param start the starting location of the vehicle in question
	 * @param numSpaces the number of spaces to be moved by the vehicle (can be positive or negative)
	 * @return whether or not the move actually happened
	 */
	public boolean moveVehicleAt(Location start, int numSpaces) {
		if(grid[start.getRow()][start.getCol()] == null) {
			return false;
		}
		if(canMoveAVehicleAt(start, numSpaces)) {
			Vehicle startVehicle = grid[start.getRow()][start.getCol()];
			addVehicle(startVehicle.getVehicleType(), startVehicle.potentialMove(numSpaces).getRow(), startVehicle.potentialMove(numSpaces).getCol(), startVehicle.isVerticle(), startVehicle.getLength());
			for(Location location : startVehicle.locationsOn()) {
				if(grid[location.getRow()][location.getCol()] == startVehicle) {
					grid[location.getRow()][location.getCol()] = null;
				}
				
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * This method just checks to see if the vehicle on a certain location can move a specific number of spaces, though
	 * it will not move the vehicle.  You should use this when you wish to move or want to see if you can
	 * move a vehicle numSpaces without going out of bounds or hitting another vehicle
	 * 
	 * @param start the starting row/column of the vehicle in question
	 * @param numSpaces number of spaces to be moved by the vehicle (positive or negative)
	 * @return whether or not the move is possible
	 */
	public boolean canMoveAVehicleAt(Location start, int numSpaces) {
		if(grid[start.getRow()][start.getCol()] == null) {
			return false;
		}
		Vehicle startVehicle = grid[start.getRow()][start.getCol()];
		for(Location location : startVehicle.locationsPath(numSpaces)) {
			try {
				if(grid[location.getRow()][location.getCol()] != null) {
					return false;
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				//do i get points off if i do this? i can also check if location.getRow/Col is negative but this works too
				return false;
			}
		}
		return true;
	}
	
	// This method helps create a string version of the board
	// You do not need to call this at all, just let it be
	public String toString() {
		return BoardConverter.createString(this);
	}
	
	/* Testing methods down here for testing the board 
	 * make sure you run the board and it works before you write the rest of the program! */
	
	public static void main(String[] args) {
		Board b = new Board(5, 5);
		b.addVehicle(VehicleType.MYCAR, 1, 0, false, 2);
		b.addVehicle(VehicleType.TRUCK, 0, 2, true, 3);
		b.addVehicle(VehicleType.AUTO, 3, 3, true, 2);
		b.addVehicle(VehicleType.AUTO, 0, 3, true, 2);
		System.out.println(b);
		testCanMove(b);
		testMoving(b);
		System.out.println(b);
	}
	
	public static void testMoving(Board b) {
		System.out.println("just moving some stuff around");
		b.moveVehicleAt(new Location(1, 2), 1);
		b.moveVehicleAt(new Location(1, 2), 1);
		b.moveVehicleAt(new Location(1, 1), 1);
	}
	
	public static void testCanMove(Board b) {
		System.out.println("Ok, now testing some moves...");
		System.out.println("These should all be true");
		System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(0, 2), 2));
		System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(1, 2), 2));
		System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(2, 2), 2));
		System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(3, 3), -1));
		System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(4, 3), -1));
		
		System.out.println("\nAnd these should all be false");
		System.out.println("Moving truck down " + b.canMoveAVehicleAt(new Location(3, 2), 2));
		System.out.println("Moving the car into truck " + b.canMoveAVehicleAt(new Location(1, 0), 1));
		System.out.println("Moving the car into truck " + b.canMoveAVehicleAt(new Location(1, 0), 2));
		System.out.println("Moving nothing at all " + b.canMoveAVehicleAt(new Location(4, 4), -1));
		System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(3, 3), -2));
		System.out.println("Moving lower auto up " + b.canMoveAVehicleAt(new Location(4, 3), -2));
		System.out.println("Moving upper auto up " + b.canMoveAVehicleAt(new Location(0, 3), -1));
		System.out.println("Moving upper auto up " + b.canMoveAVehicleAt(new Location(1, 3), -1));
	}
}
