package minesweeper;
import javax.swing.*;

public class Game {
	public static void main(String[] args) {
		int m = getMines();
		new MineSweeper(m); 
	}
	
	public static void playAgain() {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
        	int m = getMines(); 
            new MineSweeper(m);
        } 
        
        else {
            System.exit(0);
        }
    }
	
	private static int getMines() {
        JTextField textField = new JTextField();
        Object[] message = {
                "Enter the number of mines:", textField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Set Mines", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                return Integer.parseInt(textField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return getMines();  
            }
        }

        System.exit(0);
        return 0; 
    }
}


  
    


