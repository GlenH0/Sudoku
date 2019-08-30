import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;

// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SFXFinal extends JFrame {
	private boolean answer;

   // Constructor
   public SFXFinal(boolean answer) {

	  if(answer == true) { 
		  try {
			  // Open an audio input stream.
			  File soundFile = new File("Correct.wav");
			  AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			  // Get a sound clip resource.
			  Clip clip = AudioSystem.getClip();
			  // Open audio clip and load samples from the audio input stream.
			  clip.open(audioIn);
			  clip.start();
		  }
	  
	  
	  catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   

   	}else
   	{ 
		  try {
			  // Open an audio input stream.
			  File soundFile = new File("Wrong.wav");
			  AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			  // Get a sound clip resource.
			  Clip clip = AudioSystem.getClip();
			  // Open audio clip and load samples from the audio input stream.
			  clip.open(audioIn);
			  clip.start();
		  }
	  
	  
	  catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   

   	}     
}
}