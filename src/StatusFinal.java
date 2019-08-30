import javax.swing.*;

public class StatusFinal extends JPanel {
	
	JProgressBar pb = new JProgressBar();
	int count = 0;
	
	public StatusFinal() {
		pb.setBounds(50,50,250,30);
		pb.setValue(0);
		pb.setStringPainted(true);
		this.add(pb);
		this.setSize(450,450);
		this.setLayout(null);
	}
	public void update() {
		if (count <81) {
			count++;
		}
	}
	public static void main(String[] args) {
		StatusFinal pd = new StatusFinal();
		pd.setVisible(true);
		pd.update();

	}
	
}