import acm.program.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import acm.graphics.*;


//FYI I USED https://codebeautify.org/javaviewer BECAUSE MY CODE LOOKED MESSY
public class GraphicsGame extends GraphicsProgram {
    /**
     * Here are all of the constants
     */
    public static final int PROGRAM_WIDTH = 500;
    public static final int PROGRAM_HEIGHT = 500;
    public static final String lABEL_FONT = "Arial-Bold-22";
    public static final String EXIT_SIGN = "EXIT";
    public static final String IMG_FILENAME_PATH = "images/";
    public static final String IMG_EXTENSION = ".png";
    public static final String VERTICAL_IMG_FILENAME = "_vert";
    
    private double[] mouseOriginalLoc = new double[2];
    private Level level;
    private ArrayList < Vehicle > vehicles = new ArrayList<Vehicle>();

    private Vehicle selectedVehicle;

    public void init() {
        setSize(PROGRAM_WIDTH, PROGRAM_HEIGHT);
    }

    public void run() {
        level = new Level(6, 6, new Location(2, 5));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 0, 1, false, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 1, 5, true, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.MYCAR, 2, 1, false, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 4, 1, false, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 5, 1, false, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 4, 0, true, 2));
        vehicles.add(level.getBoard().addVehicle(VehicleType.TRUCK, 1, 0, true, 3));
        vehicles.add(level.getBoard().addVehicle(VehicleType.TRUCK, 3, 1, false, 3));
        vehicles.add(level.getBoard().addVehicle(VehicleType.TRUCK, 3, 4, true, 3));
        vehicles.add(level.getBoard().addVehicle(VehicleType.TRUCK, 0, 3, true, 3));
        vehicles.add(level.getBoard().addVehicle(VehicleType.AUTO, 4, 5, true, 2));
        addMouseListeners();
        drawLevel();



    }

    private void drawLevel() {

        drawGridLines();
        
        drawWinningTile();
        drawCars(vehicles);

    }

    /**
     * This should draw the label EXIT and add it to the space that represents
     * the winning tile.
     */
    private void drawWinningTile() {
    	GRect exitRect = new GRect(level.getWinLocation().getCol() * cellWidth(), level.getWinLocation().getRow() * cellHeight(), cellWidth(), cellHeight());
    	exitRect.setFillColor(Color.red);
    	exitRect.setFilled(true);
    	add(exitRect);
        GLabel exit = new GLabel("Exit", (level.getWinLocation().getCol() + 1) * cellWidth() - cellWidth() / 2 - 10, (level.getWinLocation().getRow()) * cellHeight() + cellHeight() / 2);
        add(exit);
    }

    /**
     * draw the lines of the grid. Test this out and make sure you have it
     * working first. Should draw the number of grids based on the number of
     * rows and columsn in Level
     */
    private void drawGridLines() {

        //do up down first
        for (int i = 0; i < level.getColumns(); i++) {
            GLine line = new GLine(i * cellWidth(), 0, i * cellWidth(), PROGRAM_HEIGHT);
            add(line);
        }

        //now we do left right!
        for (int i = 0; i < level.getRows(); i++) {
            GLine line = new GLine(0, i * cellHeight(), PROGRAM_WIDTH, i * cellHeight());
            add(line);
        }
    }

    /**
     * Maybe given a list of all the cars, you can go through them and call
     * drawCar on each?
     */
    private void drawCars(ArrayList < Vehicle > list) {
        for (Vehicle s: list) {
            drawCar(s);
        }
    }

    /**
     * Given a vehicle object, which we will call v, use the information from
     * that vehicle to then create a GImage and add it to the screen. Make sure
     * to use the constants for the image path ("/images"), the extension ".png"
     * and the additional suffix to the filename if the object is vertical when
     * creating your GImage. Also make sure to set the images size Saccording to
     * the size of your spaces
     * 
     * @param v
     *            the Vehicle object to be drawn
     */
    private void drawCar(Vehicle v) {
        String file;
        int imgW = 0;
        int imgH = 0;
        if (v.isVerticle()) {
            file = "images/" + v.getVehicleType().toString() + "_vert.png";
            imgW = (int) cellWidth();
            imgH = (int)(cellHeight() * v.getLength());

        } else {
            file = "images/" + v.getVehicleType().toString() + ".png";
            imgH = (int) cellHeight();
            imgW = (int) cellWidth() * v.getLength();
        }
        GImage vehicle = new GImage(file,
            convertLocationToXY(new Location(v.getsRow(), v.getsCol()))[0],
            convertLocationToXY(new Location(v.getsRow(), v.getsCol()))[1]);
        vehicle.setSize(imgW, imgH);

        add(vehicle);
    }


    /**
     * Given a xy coordinates, return the Vehicle that is currently at those x
     * and y coordinates, returning null if no Vehicle currently sits at those
     * coordinates.
     * 
     * @param x
     *            the x coordinate in pixels
     * @param y
     *            the y coordinate in pixels
     * @return the Vehicle object that currently sits at that xy location
     */
    private Vehicle getVehicleFromXY(double x, double y) {
        return level.getBoard().getVehicleAt(convertXYToLocation(x, y));
    }

    /**
     * This is a useful helper function to help you calculate the number of
     * spaces that a vehicle moved while dragging so that you can then send that
     * information over as numSpacesMoved to that particular Vehicle object.
     * 
     * @return the number of spaces that were moved
     */
    private int calculateNumSpacesMoved(double startX, double startY, double endX, double endY, boolean verticle) {
        //hope this implementation is satisfactory
        Location start = convertXYToLocation(startX, startY);
        Location end = convertXYToLocation(endX, endY);

        if (verticle) {
            return end.getRow() - start.getRow();
        } else {
            return end.getCol() - start.getCol();
        }
    }

    /**
     * Another helper function/method meant to return the location given an x and y
     * coordinate system. Use this to help you write getVehicleFromXY
     * 
     * @param x
     *            x-coordinate (in pixels)
     * @param y
     *            y-coordinate (in pixels)
     * @return the Location associated with that x and y
     */

    private Location convertXYToLocation(double x, double y) {
        //okay we are going to do x mod cell width to find which cell we in

        //update i was wrong lets use divide instead
        Location outLoc = new Location((int)(y / cellHeight()), (int)(x / cellWidth()));

        return outLoc;

    }

    private double[] convertLocationToXY(Location location) {
        double[] out = new double[2];
        out[0] = location.getCol() * cellWidth();
        out[1] = location.getRow() * cellHeight();
        return out;

    }

    public ArrayList < Vehicle > getVehiclesOnBoard() {
        return vehicles;
    }

    /**
     * 
     * @return the width (in pixels) of a single cell in the grid
     */
    private double cellWidth() {
        // TODO fix this method
        return PROGRAM_WIDTH / level.getColumns();
    }

    /**
     * 
     * @return the height in pixels of a single cell in the grid
     */
    private double cellHeight() {
        // TODO fix this method
        return PROGRAM_HEIGHT / level.getRows();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (getVehicleFromXY(e.getX(), e.getY()) != null) {
            mouseOriginalLoc[0] = e.getX();
            mouseOriginalLoc[1] = e.getY();
            selectedVehicle = getVehicleFromXY(e.getX(), e.getY());

        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedVehicle != null) {
            if (calculateNumSpacesMoved(mouseOriginalLoc[0], mouseOriginalLoc[1], e.getX(), e.getY(), selectedVehicle.isVerticle()) != 0) {
                int moves = calculateNumSpacesMoved(mouseOriginalLoc[0], mouseOriginalLoc[1], e.getX(), e.getY(), selectedVehicle.isVerticle());

                if (level.getBoard().canMoveAVehicleAt(new Location(selectedVehicle.getsRow(), selectedVehicle.getsCol()), moves)) {
                    vehicles.remove(selectedVehicle);
                    Vehicle newVehicle = level.getBoard().moveVehicleAt(new Location(selectedVehicle.getsRow(), selectedVehicle.getsCol()), moves);
                    vehicles.add(newVehicle);

                    removeAll();

                    if (level.getBoard().getVehicleAt(level.getWinLocation()) != null) {
                        if (level.getBoard().getVehicleAt(level.getWinLocation()).getVehicleType() == VehicleType.MYCAR) {
                        	GLabel winMessage = new GLabel("YOU WIN", PROGRAM_WIDTH/2-100,PROGRAM_HEIGHT/2);
                        	//Why is there no glabel dot set size or glabel dot set scale
                        	winMessage.setFont(new Font("new font", 1, 50));
                        	add(winMessage);
                        } else {
                            drawLevel();
                            selectedVehicle = null;
                        }
                    } else {
                        drawLevel();
                        selectedVehicle = null;

                    }

                }
            }else {
            	selectedVehicle = null;
            }
        }

    }
    /*
	@Override
    public void mousePressed(MouseEvent e) {
	System.out.println(convertXYToLocation(e.getX(),e.getY()).getCol());
	System.out.println(convertXYToLocation(e.getX(),e.getY()).getRow());
    }
	*/

    public static void main(String[] args) {
        new GraphicsGame().start();
    }
}