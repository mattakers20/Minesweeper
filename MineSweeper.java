package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random; 
import java.util.ArrayList;

public class MineSweeper {
	private int numMines;  //number of mines on the board
	private int tileSize = 70; //size of the tiles in pixels
	private int numRows = 8; 
	private int numCols = numRows; 
	private int boardWidth = numCols * tileSize; //total width of the screen
	private int boardHeight = numRows * tileSize; //total height of the screen
	private int tilesGuessed = 0; //keeps track of how many tiles the player has left clicked
	
	private Cell[][] board = new Cell[numRows][numCols]; 
	private Random rand = new Random(); 
	
	private ArrayList<Cell> mines = new ArrayList<Cell>(); //array list keeps track of the positions of mines

	private JPanel boardPanel = new JPanel(); 
	public JFrame f; 
	
	public boolean isOver = false; //turns to true if the player has won/lost
	
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
		
		for(int i = 0; i < numRows; i++) { //fills the board array with instances of the cell class
			for(int j = 0; j < numCols; j++) {
				board[i][j] = new Cell(i,j); 
				board[i][j].setFocusable(false);
				board[i][j].setMargin(new Insets(0,0,0,0));
				board[i][j].setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
				
				board[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) { //adds a mouse listener to each tile
                        if (isOver) {
                            return;
                        }
                        Cell c = (Cell) e.getSource();

                        //left click causes the cell to be "revealed"
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (c.getText() == "") {
                                if (mines.contains(c)) { //if the user clicks on a mine, the game is over
                                    gameOver();
                                }
                                else {
                                    reveal(c.getRowNum(), c.getColNum()); /*if the user clicks a tile that isnt a mine, the cell 
								          is revealed alongside any other cells that do not touch a mine */
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) { //right clicking a tile marks it with a flag
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

	/* this method adds the appropriate 
        number of mines to the board randomly */
	private void addMines(){
		int minesToAdd = this.numMines; 
		
		while(minesToAdd > 0){
			 int row = rand.nextInt(numRows);
	         int col = rand.nextInt(numCols);
	         
	         Cell bomb = board[row][col]; 
	         
	         if (!mines.contains(bomb)){ //ensures that a mine isnt added to a tile that already has one
	                mines.add(bomb);
	                minesToAdd--;
	         }
		}
	}

	/* reveals all the mines on the board when the 
        game is over and prompts the user to play again */
	private void gameOver(){
		 for (int i = 0; i < mines.size(); i++){
	            Cell c = mines.get(i);
	            c.setText("💣");
	        }
		 Game.playAgain();
		 f.dispose(); //closes the current board 
	}

	//this method determines if the given cell contains a mine
	private int countMines(int row, int col){
	        if (row < 0 || row >= numRows || col < 0 || col >= numCols){ //ensures that the user clicked on a valid cell
	            return 0;
	        }
	        if (mines.contains(board[row][col])){ //checks to see if the given cell is in the list of mine cells
	            return 1;
	        }
	        return 0;
    	}

	/* the reveal method reveals the selected cell, as well 
 	as other cells that do not contain or neighbor a mine */
	private void reveal(int row, int col){
	        if (row < 0 || row >= numRows || col < 0 || col >= numCols) { //ensures the cell is on the board
	            return;
	        }
	
	        Cell c = board[row][col];
	        if (!c.isEnabled()) { //ensures the user hasn't already clicked this tile
	            return;
	        }
	        c.setEnabled(false);
	        tilesGuessed++; //increments the number of guessed cells
	
	        int minesFound = 0;
	
	        //following statements check each neighboring cell for a mine
	        minesFound += countMines(row-1, col-1); 
	        minesFound += countMines(row-1, col);    
	        minesFound += countMines(row-1, col+1);  
	        minesFound += countMines(row, col-1);   
	        minesFound += countMines(row, col+1);    
	        minesFound += countMines(row+1, col-1);  
	        minesFound += countMines(row+1, col);    
	        minesFound += countMines(row+1, col+1);  
	
	        if (minesFound > 0) { 
	            c.setText(Integer.toString(minesFound)); //if the selected cell neighbors one or more mines, the cell displays the number of mines
	        }
	        else {
	            c.setText(""); //if the selected cell does NOT neighbor any mines, recursively reveals all "blank" cells
	            
	            reveal(row-1, col-1);    
	            reveal(row-1, col);    
	            reveal(row-1, col+1);    
	            reveal(row, col-1);     
	            reveal(row, col+1);      
	            reveal(row+1, col-1);    
	            reveal(row+1, col);      
	            reveal(row+1, col+1);    
	        }
	
	        if (tilesGuessed == numRows * numCols - mines.size()){ //if the user has clicked all cells that do not contain mines, they win
	            isOver = true; 
	            Game.playAgain();
	            f.dispose(); 
        }
    }
}
