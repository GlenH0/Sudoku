import javax.swing.*;
import java.awt.*;

public class TimerFrameFinal extends JFrame{
	
	public TimerFrameFinal () {
		
		add(new ScreenFinal());
		setSize(new Dimension(200,200));
		setLocation(500,500);
		setTitle("Time");
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		TimerFrameFinal time = new TimerFrameFinal();
	}
}