public class Location {

	private int row;
	private int col;

	public Location(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public void print() {
		System.out.println(String.valueOf(row) + " " +String.valueOf(col));
	}
}
