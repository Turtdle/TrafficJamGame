public class Vehicle {
	private VehicleType type;
	private int row;
	private int col;
	private boolean verticle;
	private int length;
	public Vehicle(VehicleType type, int row, int col, boolean verticle, int length){
		this.type = type;
		this.row = row;
		this.col = col;
		this.verticle = verticle;
		this.length = length;
	}
	
	public String toString() {
		return("Type: " + String.valueOf(type) + ", Row and Col: " + String.valueOf(row) + " and " + String.valueOf(col) + ", is it verticle: " + String.valueOf(verticle) + ", Length: " + String.valueOf(length));
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getsRow() {
		return row;
	}

	public void setsRow(int row) {
		this.row = row;
	}

	public int getsCol() {
		return col;
	}

	public void setsCol(int col) {
		this.col = col;
	}

	public boolean isVerticle() {
		return verticle;
	}

	public void setVerticle(boolean verticle) {
		this.verticle = verticle;
	}
	
	public void move(int numSpaces) {
		//pos is up/right (working in quad 1)
		if(verticle) {
			row += numSpaces;
		}else{
			col += numSpaces;
		}
	}
	
	public Location potentialMove(int numSpaces) {
		Location out;
		if(verticle) {
			out = new Location(row  + numSpaces, col);	
		}else{
			out = new Location(row, col + numSpaces);	
		}
		return out;
	}
	
	
	
	public VehicleType getVehicleType() {
		return type;
	}

	/**
	 * Provides an array of Locations that indicate where a particular Vehicle
	 * would be on top of, calculated from the Vehicle's starting space (see narration)
	 * 
	 * @return the array of Spaces occupied by that particular Vehicles
	 */
	public Location[] locationsOn() {
		Location[] locations = new Location[length];

		for(int i = 0; i < length; i++) {

			if(verticle) {
				locations[i] = new Location(row + i, col);

			}else{
				locations[i] = new Location(row , col + i);

			}
		}
		return locations;
	}

	/**
	 * Calculates an array of the locations that would be traveled if a vehicle
	 * were to move a certain number of path, which represents the path taken
	 * 
	 * @param numSpaces
	 *            The number of spaces to move (can be negative or positive)
	 * @return The array of Locations that would need to be checked for Vehicles
	 */
	public Location[] locationsPath(int numSpaces) {
		Location[] locations = new Location[Math.abs(numSpaces)];
		for(int i = 1; i < Math.abs(numSpaces)+1; i++) {
			if(verticle) {
				if(numSpaces < 0) {
					locations[i-1] = new Location(row - i,col);
				}else {
					locations[i-1] = new Location(row + i + length - 1,col);
				}
			}else{
				if(numSpaces < 0) {
					locations[i-1] = new Location(row ,col-i);
				}else {
					locations[i-1] = new Location(row ,col+ i + length - 1);
				}
			}
		}
		return locations;
	}
	// prints out more legibly the row & columns for an array of locations
	public static void printLocations(Location[] arr) {
	 for(int i = 0; i < arr.length; i++) {
	System.out.print("r" + arr[i].getRow() + "c" + arr[i].getCol() + "; ");
	 }
	 System.out.println();
	}
	public static void main(String args[]) {
		//this snippet would go inside of a public static void main in Vehicle.java
		//Assume Vehicle constructor is type, startRow, startCol, isVertical, length
		Vehicle someTruck = new Vehicle(VehicleType.TRUCK, 1, 1, true, 3);
		Vehicle someAuto = new Vehicle(VehicleType.AUTO, 2, 2, false, 2);
		System.out.println("This next test is for locationsOn: ");
		System.out.println("vert truck at r1c1 should give you r1c1; r2c1; r3c1 as the locations its on top of...does it?");
		printLocations(someTruck.locationsOn());
		System.out.println("horiz auto at r2c2 should give you r2c2; r2c3 as the locations its on top of...does it?");
		printLocations(someAuto.locationsOn());
		System.out.println("if we were to move horiz auto -2 it should give you at least r2c0; r2c1; it may also add r2c2; r2c3 to its answer...does it?");
		printLocations(someAuto.locationsPath(-2));
		System.out.println("Moving some auto to -2, should be r2c0; r2c1");
		someAuto.move(-2);
		printLocations(someAuto.locationsOn());
		System.out.println("Potential move to plus 5, should be 5, 2 (col 5 row 2)");
		System.out.println(someAuto.potentialMove(5).getCol());
		System.out.println(someAuto.potentialMove(5).getRow());
		System.out.println("Moving some auto to 6, should be r2c6; r2c7");
		someAuto.move(6);
		printLocations(someAuto.locationsOn());
	}
}
