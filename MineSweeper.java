package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random; 
import java.util.ArrayList;

public class MineSweeper {
	private int numMines; 
	private int tileSize = 70; 
	private int numRows = 8; 
	private int numCols = numRows; 
	private int boardWidth = numCols * tileSize; 
	private int boardHeight = numRows * tileSize; 
	private int tilesGuessed = 0; 
	
	private Cell[][] board = new Cell[numRows][numCols]; 
	private Random rand = new Random(); 
	
	private ArrayList<Cell> mines = new ArrayList<Cell>(); 

	private JPanel boardPanel = new JPanel(); 
	public JFrame f; 
	
	public boolean isOver = false; 
	
	public MineSweeper(int m){
		isOver = false; 
		
		this.numMines = m; 
		f = new JFrame("Minesweeper");
		f.setSize(boardWidth, boardHeight); 
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		f.setLocationRelativeTo(null);
		f.setLayout(new BorderLayout());
		
		boardPanel.setLayout(new GridLayout(numRows, numCols));
		f.add(boardPanel); 
		
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {
				board[i][j] = new Cell(i,j); 
				board[i][j].setFocusable(false);
				board[i][j].setMargin(new Insets(0,0,0,0));
				board[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
				
				board[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (isOver) {
                            return;
                        }
                        Cell c = (Cell) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (c.getText() == "") {
                                if (mines.contains(c)) {
                                    gameOver();
                                }
                                else {
                                    reveal(c.getRowNum(), c.getColNum());
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (c.getText() == "" && c.isEnabled()) {
                                c.setText("🚩");
                            }
                            else if (c.getText() == "🚩") {
                                c.setText("");
                            }
                        }
                    } 
                });
				
				boardPanel.add(board[i][j]); 
			}
		}
		
		addMines(); 
		
		f.setVisible(true);
	}
	
	private void addMines(){
		int minesToAdd = this.numMines; 
		
		while(minesToAdd > 0){
			 int row = rand.nextInt(numRows);
	         int col = rand.nextInt(numCols);
	         
	         Cell bomb = board[row][col]; 
	         
	         if (!mines.contains(bomb)){
	                mines.add(bomb);
	                minesToAdd--;
	         }
		}
	}
	
	private void gameOver(){
		 for (int i = 0; i < mines.size(); i++){
	            Cell c = mines.get(i);
	            c.setText("💣");
	        }
		 Game.playAgain();
		 f.dispose();
	}
	
	private int countMines(int row, int col){
        if (row < 0 || row >= numRows || col < 0 || col >= numCols){
            return 0;
        }
        if (mines.contains(board[row][col])){
            return 1;
        }
        return 0;
    }
	
	private void reveal(int row, int col){
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return;
        }

        Cell c = board[row][col];
        if (!c.isEnabled()) {
            return;
        }
        c.setEnabled(false);
        tilesGuessed++;

        int minesFound = 0;

        
        minesFound += countMines(row-1, col-1); 
        minesFound += countMines(row-1, col);    
        minesFound += countMines(row-1, col+1);  
        minesFound += countMines(row, col-1);   
        minesFound += countMines(row, col+1);    
        minesFound += countMines(row+1, col-1);  
        minesFound += countMines(row+1, col);    
        minesFound += countMines(row+1, col+1);  

        if (minesFound > 0) {
            c.setText(Integer.toString(minesFound));
        }
        else {
            c.setText("");
            
            reveal(row-1, col-1);    
            reveal(row-1, col);    
            reveal(row-1, col+1);    
            reveal(row, col-1);     
            reveal(row, col+1);      
            reveal(row+1, col-1);    
            reveal(row+1, col);      
            reveal(row+1, col+1);    
        }

        if (tilesGuessed == numRows * numCols - mines.size()){
            isOver = true; 
            Game.playAgain();
            f.dispose(); 
        }
    }
}
