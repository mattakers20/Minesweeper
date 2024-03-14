package minesweeper;

import javax.swing.*;


public class Cell extends JButton{
	
	private int rowNum; 
	private int colNum; 
	
	public Cell(int r, int c) {
		super(); 
		this.rowNum = r; 
		this.colNum = c; 
	}
	
	public int getRowNum() {
		return this.rowNum; 
	}
	
	public int getColNum() {
		return this.colNum; 
	}
}
