/*this class extends upon the JButton 
class to represent a single cell on the 
minesweeper board */

package minesweeper;

import javax.swing.*;

public class Cell extends JButton{
	
	private int rowNum; //represents the row in which the cell is 
	private int colNum; //represents the column in which the cell is
	
	public Cell(int r, int c) { //constructor method
		super(); 
		this.rowNum = r; 
		this.colNum = c; 
	}
	
	public int getRowNum() { //returns the row number
		return this.rowNum; 
	}
	
	public int getColNum() { //returns the column number
		return this.colNum; 
	}
}
