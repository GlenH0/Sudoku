import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

public class ScreenFinal extends JPanel implements ActionListener {
	
	int min=0,hr=0;
	
	JButton pause = new JButton("Pause");
	JButton resume = new JButton("Resume");
	JLabel timing = new JLabel();
	int time = 0;
	Timer tm = new Timer(1000,this);
	
	
	public ScreenFinal() {
		
		tm.start();
		
		//add(resume);
		//resume.addActionListener(this);
		//resume.addKeyListener(null);
		//resume.setFont(new Font("Arial", Font.BOLD, 10));
		//resume.setEnabled(false);      //set as disable first, in case people accidentally press which will increased the time by 1sec.
		
		add(timing);
		timing.setFont(new Font("Arial", Font.BOLD, 20));
		
		add(pause);
		pause.addActionListener(this);
		pause.setFont(new Font("Arial", Font.BOLD, 10));
		
		
	}
	public void clock() {
		
		if(time == 59) {
			time=-1;   //cause time is initialise to 0, so must put -1 to restart the time to 0.
			min++;
		}
		if(min==59) {			
			min=-1;
			hr++;
		}
		
		time++;
		//pause.setText("" + time);
		timing.setText("Time: "+hr+ " hr " + min + " min " +time + " sec");
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		clock();
		
		ImageIcon icon = new ImageIcon("D:\\Java\\new\\Sudoku\\pause.jpg");
		Image temp = icon.getImage().getScaledInstance(58, 58, Image.SCALE_SMOOTH);  //import java.awt.Image; then resize the image form here.
		icon = new ImageIcon(temp);
		
	
		if(e.getSource() == pause) {   //when pause button is clicked, the pause button disabled.  To prevent multiple clicks
			tm.stop();
			//pause.setEnabled(false);
			//resume.setEnabled(true);
			int result = JOptionPane.showConfirmDialog(this, "        RESUME?","Pause", JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
			
			if(result == JOptionPane.YES_OPTION) {
				tm.start();
			}
			
			while(result == JOptionPane.NO_OPTION) {
				
				result = JOptionPane.showConfirmDialog(this, "Do you want to quit?","Pause", JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
				if(result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				if(result == JOptionPane.NO_OPTION) {
					result = JOptionPane.showConfirmDialog(this, "        RESUME?","Pause", JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE,icon);
					if(result == JOptionPane.YES_OPTION) {
						tm.start();
					}
				}
			}
		
		/*if(e.getSource() == resume) { //when resume button is clicked, the resume button disabled. To prevent multiple clicks
			tm.start();
			pause.setEnabled(true);
			resume.setEnabled(false);
		}*/
	}
}
}
