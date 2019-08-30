import java.awt.*;        // Uses AWT's Layout Managers
import java.awt.event.*;  // Uses AWT's Event Handlers
import javax.swing.*;     // Uses Swing's Container/Components
import java.util.concurrent.ThreadLocalRandom;
/**
 * The Sudoku game.
 * To solve the number puzzle, each row, each column, and each of the
 * nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
@SuppressWarnings("serial") //@SuppressWarnings("serial") makes the compiler shut up about a missing serialVersionUID .

public class Sudoku extends JFrame implements ActionListener{
   // Name-constants for the game properties
   public static final int GRID_SIZE = 9;    // Size of the board
   public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
 
   // Name-constants for UI control (sizes, colors and fonts)
   public static final int CELL_SIZE = 50;   // Cell width/height in pixels
   public static final int CANVAS_WIDTH  = CELL_SIZE * GRID_SIZE;
   public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
                                             
   public static final Color OPEN_CELL_BGCOLOR =  new Color(222,184,135);  //light brown (open cell)
   public static final Color OPEN_CELL_BGCOLOR2 =  new Color(250,50,50);  //light brown (open cell)
   public static final Color OPEN_CELL_TEXT_YES = Color.BLACK;  // RGB 
   public static final Color OPEN_CELL_TEXT_NO = Color.RED; 
   public static final Color CLOSED_CELL_BGCOLOR = new Color(210,105,30); // dark brown (closed cell)
   public static final Color CLOSED_CELL_BGCOLOR2 = new Color(210,55,55); // dark brown (closed cell)
   public static final Color CLOSED_CELL_TEXT =  Color.BLACK;
   public static final Font FONT_NUMBERS = new Font("DialogInput", Font.BOLD, 40);
   public static final Color Correct_Ans = Color.black;   				  //correct answer text color.

   // The game board composes of 9x9 JTextFields,
   // each containing String "1" to "9", or empty String
   private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];     
   public static int [] row1 = new int [GRID_SIZE];    // declare temp array to store for Randomiser.
   public static int [] row2 = new int [GRID_SIZE];			
   public static int [] row3 = new int [GRID_SIZE];		
   public static int level = 1;
 
   // Puzzle to be solved and the mask (which can be used to control the
   //  difficulty level).
   // Hardcoded here. Extra credit for automatic puzzle generation
   //  with various difficulty levels.
   
   private static int[][] puzzle =
      {{5, 3, 4, 6, 7, 8, 9, 1, 2},
       {6, 7, 2, 1, 9, 5, 3, 4, 8},
       {1, 9, 8, 3, 4, 2, 5, 6, 7},
       {8, 5, 9, 7, 6, 1, 4, 2, 3},
       {4, 2, 6, 8, 5, 3, 7, 9, 1},
       {7, 1, 3, 9, 2, 4, 8, 5, 6},
       {9, 6, 1, 5, 3, 7, 2, 8, 4},
       {2, 8, 7, 4, 1, 9, 6, 3, 5},
       {3, 4, 5, 2, 8, 6, 1, 7, 9}};
   
   private static boolean[][] masks = new boolean [GRID_SIZE][GRID_SIZE];
      /*{{false, false, false, false, false, true, false, false, false},
       {false, false, false, false, false, false, false, false, true},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false}};*/
   //------------------------------------------------------------------------------------------------------------------------------------------------------------------//
   
 
   private int count = 81;  //total number of cells 9 x 9.
   private int maskCount;   // total number of open cells.
   private BG1 background = new BG1();  
   private BG2 background2 = new BG2();
   private BG3 background3 = new BG3();
   private NoSolutionBG nosolutionbg = new NoSolutionBG(); 
  
   //JMenuBar
   //New Game
   private JMenuItem easy = new JMenuItem("Easy Level");
   private JMenuItem intermediate = new JMenuItem("Intermediate Level");
   private JMenuItem hard = new JMenuItem("Hard Level");
   //File
   private JMenuItem exit = new JMenuItem("Exit");
   //Help
   private JMenuItem help = new JMenuItem("Solution");
   //Options
   private JMenuItem disableSound = new JMenuItem("Disable Sound");
   private JMenuItem enableSound = new JMenuItem("Enable Sound");
   //About
   private JMenuItem about = new JMenuItem("The Team");
   private JLabel counter;
   
   private JMenuItem themeBlack = new JMenuItem("Black");
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------//
   //Default Constructor
   public Sudoku() {
	      
	   Container cp = getContentPane();
	      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  

	      cp.setLayout(new BorderLayout());  
	      JPanel tfPanel = new JPanel (new GridLayout(GRID_SIZE, GRID_SIZE));
	      
	      cp.add(tfPanel, BorderLayout.CENTER);       //Sudoku.
	      cp.add(new Screen2(), BorderLayout.NORTH);  //create a new panel to store the timer.
	      //Title icon.
	      JLabel image = new JLabel(new ImageIcon(((new ImageIcon("D:\\Java\\new\\Sudoku\\sudoku-title.jpg")).getImage()).getScaledInstance(335, 85, Image.SCALE_SMOOTH)));
	      JPanel imagePanel = new JPanel();
	      imagePanel.setLayout(new BorderLayout());
	      
	      counter = new JLabel();  //for the number of cells left after the user input. creating a new label to place it on top of the image(above).
	     // counter.setText(" "+ maskCount + " Cell(s) Left.");
   
	      imagePanel.add(counter, BorderLayout.WEST);
	      imagePanel.setBackground(Color.WHITE);
	      imagePanel.add(image, BorderLayout.CENTER); 
	      cp.add(imagePanel, BorderLayout.SOUTH);    //Our icon.
	 
	      // Allocate a common listener as the ActionEvent listener for all the
	      //  JTextFields
	      // ... [TODO 3] 
	      InputListener listener = new InputListener();
	      
	      initGame();  //initialise game 
	      
	      // Construct 9x9 JTextFields and add to the content-pane
	      for (int row = 0; row < GRID_SIZE; ++row) {
	         for (int col = 0; col < GRID_SIZE; ++col) {
	            tfCells[row][col] = new JTextField(); // Allocate element of array
	            tfPanel.add(tfCells[row][col]);            // ContentPane adds JTextField
	            if (masks[row][col]) {
	               tfCells[row][col].setText("");     // set to empty string
	               tfCells[row][col].setEditable(true);
	               tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
	 
	               // Add ActionEvent listener to process the input
	               // ... [TODO 4] 
	               tfCells[row][col].addActionListener(listener);
	            } else {
	               tfCells[row][col].setText(puzzle[row][col] + "");
	               tfCells[row][col].setEditable(false);
	               tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
	               tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
	            }
	            // Beautify all the cells
	            tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
	            tfCells[row][col].setFont(FONT_NUMBERS);
	         }
	      }
 
	      // Set the size of the content-pane and pack all the components
	      //  under this container.
	      cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
	      pack();
	      
	      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
	      setTitle("Sudoku");
	      setVisible(true);
	      
	  //Count number of open cells left.
	      for (int row = 0; row < GRID_SIZE; ++row) {
	          for (int col = 0; col < GRID_SIZE; ++col) {
	        	  if(masks[row][col] == true)
	        		  maskCount++;
	          }
	      }
	      count = count - maskCount;
      
   //--------------------------------------------------------------------------------------------------------------------------------------------------------------//   
      JMenuBar menubar = new JMenuBar();
      JMenu file = new JMenu("File");
      JMenu option = new JMenu("Options");
      JMenu helpBar = new JMenu("Help");
      JMenu New = new JMenu("New Game");  
      JMenu aboutUs = new JMenu("About");
      JMenu theme = new JMenu ("Theme");
      			
      easy.addActionListener(this);					//Easy Level
      intermediate.addActionListener(this);			//Intermediate Level
      hard.addActionListener(this);					//Hard Level
      exit.addActionListener(this);					//Exit
      New.addActionListener(this);					//New Game
      help.addActionListener(this);      			//Help
      disableSound.addActionListener(this);			//Disable Sound
      enableSound.addActionListener(this);			//Enable Sound 
      about.addActionListener(this);				//About
      themeBlack.addActionListener(this);
      
      //add components onto menubar
      menubar.add(file);
      menubar.add(option);
      menubar.add(helpBar);
      menubar.add(aboutUs);
      menubar.add(theme);
   
      file.add(New);
      file.addSeparator();
      file.add(exit);
      
      New.add(easy);
      New.addSeparator();
      New.add(intermediate);
      New.addSeparator();
      New.add(hard);
      
      option.add(disableSound);
      option.addSeparator();
      option.add(enableSound);    
      
      helpBar.add(help);
      
      aboutUs.add(about);
      theme.add(themeBlack);
 
      this.setJMenuBar(menubar);
      this.setSize(800,800);
      this.setVisible(true);
   }
   
   @Override
   public void actionPerformed(ActionEvent e) {
	   
	if(e.getSource() == easy) {
		level = 1;
		getPuzzle();
		getMasks(level);
		dispose();
		new Sudoku();
		background.disable();
		background2.disable();
		background3.disable();
		final JOptionPane pane = new JOptionPane("           "+"  		  "+"Welcome to Easy Mode");
	    final JDialog d = pane.createDialog((JFrame)null, "Sudoku");
	    d.setLocation(300,300);
	    d.setVisible(true);
		
		
	}else if(e.getSource() == intermediate) {
		
		level = 2;
		getPuzzle();
		getMasks(level);
		Sudoku.this.dispose();
		new Sudoku();
		background.disable();
		background2.disable();
		background3.disable();
		final JOptionPane pane = new JOptionPane("           "+"Welcome to Intermediate Mode");
	    final JDialog d = pane.createDialog((JFrame)null, "Sudoku");
	    d.setLocation(300,300);
	    d.setVisible(true);
		
	}else if(e.getSource() == hard) {
		level = 3;
		getPuzzle();
		getMasks(level);
		Sudoku.this.dispose();
		new Sudoku();
		background.disable();
		background2.disable();
		background3.disable();
		final JOptionPane pane = new JOptionPane("           "+"    	   "+"Welcome to Hard Mode");
	    final JDialog d = pane.createDialog((JFrame)null, "Sudoku");
	    d.setLocation(300,300);
	    d.setVisible(true);
		
		
	}else if(e.getSource() == exit) {
		System.exit(0);	
		
	}else if(e.getSource() == help && level == 1) {
		
		ImageIcon icon = new ImageIcon("D:\\Java\\new\\Sudoku\\noob.jpg");
		Image temp = icon.getImage().getScaledInstance(58, 58, Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		
		background.disable();
		nosolutionbg.enable();
		JOptionPane.showMessageDialog(this, "NO SOLUTION HAHAHAHA!","NOOB",JOptionPane.WARNING_MESSAGE,icon);
		nosolutionbg.disable();
		background.enable();

    //Sound Control - Enable/Disable
	}
	else if(e.getSource() == help && level == 2) {
		
		ImageIcon icon = new ImageIcon("D:\\Java\\new\\Sudoku\\noob.jpg");
		Image temp = icon.getImage().getScaledInstance(58, 58, Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
		
		background2.disable();
		nosolutionbg.enable();
		JOptionPane.showMessageDialog(this, "NO SOLUTION HAHAHAHA!","NOOB",JOptionPane.WARNING_MESSAGE,icon);
		nosolutionbg.disable();
		background2.enable();

    //Sound Control - Enable/Disable
	}
	else if(e.getSource() == help && level == 3) {
	
		ImageIcon icon = new ImageIcon("D:\\Java\\new\\Sudoku\\noob.jpg");
		Image temp = icon.getImage().getScaledInstance(58, 58, Image.SCALE_SMOOTH);
		icon = new ImageIcon(temp);
	
		background3.disable();
		nosolutionbg.enable();
		JOptionPane.showMessageDialog(this, "NO SOULUTION!" ,"SOLUTION",JOptionPane.WARNING_MESSAGE,icon);
		nosolutionbg.disable();
		background3.enable();

//Sound Control - Enable/Disable
	}else if(e.getSource() == enableSound && level == 1) {
		background.enable();
	}else if(e.getSource() == enableSound && level == 2) {
		background2.enable();
	}else if(e.getSource() == enableSound && level == 3) {
		background3.enable();
	}else if(e.getSource() == disableSound) {
		background.disable();
		background2.disable();
		background3.disable();
	}else if(e.getSource()== about) {
		JOptionPane.showMessageDialog(this, "Done by:\r\n"+"Khuz (U1722592A)\r\n"+"Glen (U1721881L)");
	}
	else if(e.getSource() == themeBlack) {
		repaint();
		for(int row = 0; row <GRID_SIZE;row++) {
			for(int col = 0; col <GRID_SIZE; col++) {
				tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR2);
			}
		}
		
	}
  }

   /** The entry main() entry method */
   public static void main(String[] args) {
      // [TODO 1] (Now)
      // Check Swing program template on how to run the constructor
      SwingUtilities.invokeLater(new Runnable(){
    	  @Override 
    	  public void run() {
    		JOptionPane.showMessageDialog(null, "About Sudoku\r\n\n" 
    				
    				+"-Sudoku are easy to learn yet highly addictive language-independent logic puzzles which have recently taken the whole world by storm.\r\n\n"
    				
    				+"-Using pure logic and requiring no math to solve, these fascinating puzzles offer endless fun and intellectual entertainment to puzzle\r\n"
    				+" fans of all skills and ages.\r\n\n"  
    				
    				+"-The Classic Sudoku is a number placing puzzle based on a 9x9 grid with several given numbers.\r\n\n"
    				
    				+"-The object is to place the numbers 1 to 9 in the empty squares so that each row, each column and each 3x3 box contains the same number only once.\r\n\n" + 
    				
    				"-Sudoku puzzles come in endless number combinations and range from very easy to extremely difficult taking anything from five minutes to several hours to solve.\r\n\n"
    				
    				+"-Sudoku puzzles also come in many variants, each variant looking differently and each variant offering a unique twist of brain challenging logic.\r\n\n" + 
    				
    				"-However, make one mistake and you’ll find yourself stuck later on as you get closer to the solution… Try these puzzles, and see if you can solve them too!\r\n\n"
    				
    				+"Are you ready...?");
    		
    		 new Sudoku();
    		 
    		 final JOptionPane pane = new JOptionPane("           "+ "          "+"Welcome to Sudoku");
    		    final JDialog d = pane.createDialog((JFrame)null, "Sudoku");
    		    d.setLocation(300,300);
    		    d.setVisible(true);
    	  }
      });
   }
  //initialise new game.
   public void initGame() {
	   getPuzzle();
	   getMasks(level);
	   
	   //what music will play at what level.
	   if(level == 1) {
		   background.enable();
		   this.background2.disable();
		   background3.disable();
	   }
	   
	   else if(level == 2) {
		   background.disable();
		   background2.enable();
		   background3.disable();
	   }
	   
	   else if(level == 3) {
		   background.disable();
		   background2.disable();
		   background3.enable();
	   }
   }
//Randomiser
   public static void getPuzzle() {
	   
	  int randomiser = ThreadLocalRandom.current().nextInt(1,3); //check from min = 1 to max = (2), but syntax requires to max + 1
	   
	   if(randomiser == 1) {
		   for(int col = 0; col<GRID_SIZE;col++) {
			   row1[col] = puzzle[0][col];
			   row2[col] = puzzle[1][col];
			   row3[col] = puzzle[2][col];
			   puzzle[1][col] = row1[col];
			   puzzle[2][col] = row2[col];
			   puzzle[0][col] = row3[col];
			   
			   row1[col] = puzzle[3][col];
			   row2[col] = puzzle[4][col];
			   row3[col] = puzzle[5][col];
			   puzzle[4][col] = row1[col];
			   puzzle[5][col] = row2[col];
			   puzzle[3][col] = row3[col];
			   
			   row1[col] = puzzle[6][col];
			   row2[col] = puzzle[7][col];
			   row3[col] = puzzle[8][col];
			   puzzle[7][col] = row1[col];
			   puzzle[8][col] = row2[col];
			   puzzle[6][col] = row3[col];
		   }   
		   }
	   else if(randomiser == 2) {
		   for(int row = 0; row <GRID_SIZE; row++) {
			   row1[row] = puzzle[row][0];
			   row2[row] = puzzle[row][1];
			   row3[row] = puzzle[row][2];
			   puzzle[row][1] = row1[row];
			   puzzle[row][2] = row2[row];
			   puzzle[row][0] = row3[row];
			   
			   row1[row] = puzzle[row][3];
			   row2[row] = puzzle[row][4];
			   row3[row] = puzzle[row][5];
			   puzzle[row][4] = row1[row];
			   puzzle[row][5] = row2[row];
			   puzzle[row][3] = row3[row];
			   
			   row1[row] = puzzle[row][6];
			   row2[row] = puzzle[row][7];
			   row3[row] = puzzle[row][8];
			   puzzle[row][7] = row1[row];
			   puzzle[row][8] = row2[row];
			   puzzle[row][6] = row3[row];
		   }
		   
	   }
	   for(int row =0; row<9 ; row++) {
		   for(int col = 0 ; col < 9; col++) {
			   System.out.print(puzzle[row][col] + " ");
		   }
		   System.out.println();
	   }
	   
   }
   public static void getMasks(int level) {
	   int maskRandom;
	   
	   if(level == 1) { //level = 1 means easy.
		   for(int row = 0; row < GRID_SIZE; row++) {
			   for(int col = 0; col < GRID_SIZE; col++) {
				  maskRandom = ThreadLocalRandom.current().nextInt(1,101);  // 1 - 100%, 101 because threadlocalrandom is the max + 1. 
				   if(maskRandom <=25) {  //25% of the cells are OPEN. (means need user to input)
					   {
						   masks[row][col] = true;
					   }
				   }
				   else if (maskRandom >25)
					   {
					   masks[row][col]= false; //75% of the cells are CLOSE.
					   }
			   }
		   }
		   
	   }
	   else if(level == 2) { // level = 2 means intermediate.
		   for(int row = 0; row < GRID_SIZE; row++) {
			   for(int col = 0; col < GRID_SIZE; col++) {
				  maskRandom = ThreadLocalRandom.current().nextInt(1,101);
				   if(maskRandom <=45) { 
					   {
						   masks[row][col] = true;
					   }
				   }
				   else if (maskRandom >45)
					   {
					   masks[row][col]= false;
					   }
			   }
		   }
		   
	   }
	   else if(level == 3) {  //level = 3 means hard.
		   for(int row = 0; row < GRID_SIZE; row++) {
			   for(int col = 0; col < GRID_SIZE; col++) {
				  maskRandom = ThreadLocalRandom.current().nextInt(1,101);
				   if(maskRandom <=60) {
					   {
						   masks[row][col] = true;
					   }
				   }
				   else if (maskRandom >60)
					   {
					   masks[row][col]= false;
					   }
			   }
		   }
		   
	   }
	   
   }
  

   // [TODO 2]
   // Inner class to be used as ActionEvent listener for ALL JTextFields
   private class InputListener implements ActionListener {  
 
	   int rowSelected = -1;
       int colSelected = -1;
       
      @Override
      public void actionPerformed(ActionEvent e) {
         // All the 9*9 JTextFileds invoke this handler. We need to determine
         // which JTextField (which row and column) is the source for this invocation.
        
 
         // Get the source object that fired the event
         JTextField source = (JTextField)e.getSource();
         // Scan JTextFileds for all rows and columns, and match with the source object
         boolean found = false;
         for (int row = 0; row < GRID_SIZE && !found; ++row) {
            for (int col = 0; col < GRID_SIZE && !found; ++col) {
               if (tfCells[row][col] == source) {
                  rowSelected = row;
                  colSelected = col;
                  found = true;  // break the inner/outer loops
               }
            }
         }
 
         /*
          * [TODO 5]
          * 1. Get the input String via tfCells[rowSelected][colSelected].getText()
          * 2. Convert the String to int via Integer.parseInt().
          * 3. Assume that the solution is unique. Compare the input number with
          *    the number in the puzzle[rowSelected][colSelected].  If they are the same,
          *    set the background to green (Color.GREEN); otherwise, set to red (Color.RED).
          */
       //-----------------------------------------------------------------------------------------------------------------------------------------------------------//
         int inStr = Integer.parseInt(tfCells[rowSelected][colSelected].getText());
        
         //what happens when user input is correct
         if(inStr ==  puzzle[rowSelected][colSelected]){
        	 tfCells[rowSelected][colSelected].setBackground(Color.GREEN);
        	 masks[rowSelected][colSelected] = true; 
    		 count++;
    		tfCells[rowSelected][colSelected].setEditable(false);
    		tfCells[rowSelected][colSelected].setEnabled(false);
    		tfCells[rowSelected][colSelected].setDisabledTextColor(Correct_Ans);
    		new SFX1(true); 
   	 	}
    
     else{
        	
        	tfCells[rowSelected][colSelected].setBackground(Color.RED);
   	 		new SFX1(false);
   	 		
   	 		int row = rowSelected;
   	 		for(int col = 0; col < GRID_SIZE; ++ col) {
   	 			if(inStr == puzzle[row][col] && (masks[row][col] != true || tfCells[row][col].getBackground() == Color.GREEN)) {			
   	 				tfCells[row][col].setBackground(Color.BLUE);
   	 			}
   	 		}		
   	 		int col = colSelected;
	 			for(int row1 = 0; row1 < GRID_SIZE; ++ row1) {
	 				if(inStr == puzzle[row1][col] && (masks[row1][col] != true || tfCells[row1][col].getBackground() == Color.GREEN)) {
	 					tfCells[row1][col].setBackground(Color.BLUE);
	 				}
	 			}
   	 	}
	 	//Conflicting numbers.	
         new Timer(1500, new ActionListener() { //1500 is the delay
	            @Override
			public void actionPerformed(ActionEvent e) {
	         	
	            	int  row = rowSelected;
	            	int  col = colSelected;
	            	
	            	
	         for(int col1 = 0; col1 < GRID_SIZE; ++ col1) {
	            		
	      		if(inStr == puzzle[row][col1] && ((masks[row][col1] != true || tfCells[row][col1].getBackground() == Color.BLUE))) {
  					tfCells[row][col1].setBackground(CLOSED_CELL_BGCOLOR);
  					
  					if(tfCells[row][col1].getDisabledTextColor() == Correct_Ans)
  						tfCells[row][col1].setBackground(Color.GREEN);
	            		}
	         }
	     	
	     		for(int row1 = 0; row1 < GRID_SIZE; ++ row1) {
	     			if(inStr == puzzle[row1][col] && ((masks[row1][col] != true || tfCells[row1][col].getBackground() == Color.BLUE))) {
	     	
	    						tfCells[row1][col].setBackground(CLOSED_CELL_BGCOLOR);
	    						
	     				if(tfCells[row1][col].getDisabledTextColor() == Correct_Ans)
	     					tfCells[row1][col].setBackground(Color.GREEN);
	     			}
	     		}
	            }
	          }).start();
	 		//count the number of open cells left. (after each correct input, the counter will count down.
	 		if(inStr == puzzle[rowSelected][colSelected]) {
	 			masks[rowSelected][colSelected] = true;
	 			maskCount--;
	 			counter.setText("    " + maskCount + " Cell(s) Left.");
	 		}
	 		
         /* 
          * [TODO 6] Check if the player has solved the puzzle after this move.
          * You could update the masks[][] on correct guess, and check the masks[][] if
          * any input cell pending.
          */
	 		
        ImageIcon icon = new ImageIcon("D:\\Java\\new\\Sudoku\\win (2).gif");
         if(count == 81) {
        	EndBG end = new EndBG();
        	 new Congrats1();
        	 end.enable();
        	 background.disable();
        	 background2.disable();
        	 background3.disable();
        	 
        	 
        	 int result = JOptionPane.showConfirmDialog(null,
       		        "Congratulation! Do you want to try again?",
       		        "Sudoku", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, icon);
        	 
       		if (result == JOptionPane.NO_OPTION) {
       			System.exit(0);
       		}
       		else if(result == JOptionPane.YES_OPTION) {
       			Sudoku.this.dispose();
       			new Sudoku();
       			background.disable();
       			background2.disable();
       			background3.disable();
       			end.disable();
       			
       			final JOptionPane pane = new JOptionPane("           "+ "          "+"Welcome to Sudoku");
    		    final JDialog d = pane.createDialog((JFrame)null, "Sudoku");
    		    d.setLocation(300,300);
    		    d.setVisible(true);
       			
       		}
         }
      }
   }    
}