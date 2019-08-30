import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
public class BG3Final extends JFrame{
	
	 
	// To play sound using Clip, the process need to be alive.
	// Hence, we use a Swing application.
	
	 File soundFile = new File("[MapleStory BGM] Battle on the Deck (KMST 1.2.513).wav");
	 public Clip clip;

	   // Constructor
	   public BG3Final() {
		   
	      
	    	  try {
		    	  
		 	         // Open an audio input stream.
	    		  
		 			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
		 	         // Get a sound clip resource.
		 	         Clip clip = AudioSystem.getClip();
		 	         // Open audio clip and load samples from the audio input stream.
		 	      this.clip = clip;
		 	         clip.open(audioIn);
		 	        clip.start();
			         clip.loop(Clip.LOOP_CONTINUOUSLY);
		 	        
		 	      } 
	    	  		catch (UnsupportedAudioFileException e) {
		 	         e.printStackTrace();
		 	      } catch (IOException e) {
		 	         e.printStackTrace();
		 	      } catch (LineUnavailableException e) {
		 	         e.printStackTrace();
		 	      } 
	   
	   
		   
	    	 
			   
	   
	 
	   }
	   public void disable() {
	 	        
		         clip.stop();
	 	        
	 	      } 
  	  		
	   
	   public void enable() {
		   
	 	      
	 	         
	 	        clip.start();
		         clip.loop(Clip.LOOP_CONTINUOUSLY);
		         
	 	        
	 	     
	   }
}
