import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class AudioPlay implements LineListener {
	private AudioInputStream wavstream, mp3stream;
	private File wavfile, mp3wavfile;
	private Clip wavaudio, mp3audio;
	private int min, sec;
	private final JButton playBtn = new JButton();
	private final JButton pauseBtn = new JButton();
	private final JButton stopBtn = new JButton();
	
	public AudioPlay() {
		wavInit();
		initComponents();
	}
	
	public void initComponents() {
		//Create the frame for the player which everything is added to
		JFrame player = new JFrame();
		//Create menu bar		
		JMenuBar greenMenu = new JMenuBar();
		greenMenu.setOpaque(true);
		greenMenu.setBackground(new Color(154, 165, 127));
		greenMenu.setPreferredSize(new Dimension(400, 20));
		//Add menu bar
		player.setJMenuBar(greenMenu);
		//Describe the layout style of the frame
		player.getContentPane().setLayout(new FlowLayout());
		//Set the title of the player to the name of the song
		player.setTitle(wavfile.getName());
		player.setDefaultCloseOperation(player.EXIT_ON_CLOSE);
		//Set images for the buttons
		try {
			Image img = ImageIO.read(getClass().getResource("resources/play.bmp"));
			playBtn.setIcon(new ImageIcon(img));
			playBtn.setBorder(BorderFactory.createBevelBorder(0));
			img = ImageIO.read(getClass().getResource("resources/pause.bmp"));
			pauseBtn.setIcon(new ImageIcon(img));
			pauseBtn.setBorder(BorderFactory.createBevelBorder(0));
			img = ImageIO.read(getClass().getResource("resources/stop.bmp"));
			stopBtn.setIcon(new ImageIcon(img));
			stopBtn.setBorder(BorderFactory.createBevelBorder(0));
		} catch(IOException e) {
			
		}
		
		//Toggle button defaults
		playBtn.setEnabled(true);
		pauseBtn.setEnabled(false);
		stopBtn.setEnabled(false);
		//Describe what actions to perform when the play button is clicked
		playBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						playBtn.setEnabled(false);
						pauseBtn.setEnabled(true);
						stopBtn.setEnabled(true);
						if(!wavaudio.isOpen()) {
							wavInit();
						}
						wavaudio.start();
					}
				}
				);
		//Add the play button to the frame to be displayed
		player.getContentPane().add(playBtn);
		//Describe what actions to perform when the pause button is clicked
		pauseBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						playBtn.setEnabled(true);
						pauseBtn.setEnabled(false);
						stopBtn.setEnabled(true);
						wavaudio.stop();
					}
				}
				);
		//Add the pause button to the frame to be displayed
		player.getContentPane().add(pauseBtn);
		//Describe what actions to perform when the stop button is clicked
		stopBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						wavaudio.stop();
						playBtn.setEnabled(true);
						pauseBtn.setEnabled(false);
						stopBtn.setEnabled(false);
						wavaudio.setFramePosition(0);						
					}
				}
				);
		//Add the stop button to the frame to be displayed
		player.getContentPane().add(stopBtn);
		//Create a JLabel object to display text/images on the frame, timerLabel will display the current playtime
		JLabel timerLabel = new JLabel();
		player.add(timerLabel);
		
		//Determine the total runtime
		String temp;
		int min_t = (int) (wavaudio.getMicrosecondLength()/1000000.0)/60;
		int sec_t = (int) (wavaudio.getMicrosecondLength() - min_t*60*1000000)/1000000;
		String t = " / " + min_t + ":" + sec_t;
		
		//Create the seek bar
		SeekBar progressBar = new SeekBar();
		player.add(progressBar);
		
		player.pack();
		player.setVisible(true);
		//Keep updating the current play time of the song
		while(true) {
			temp = progressTime();
			timerLabel.setText(temp + t);
		}
	}
	
	public boolean wavInit() {
		try {
			//Set up data input
			wavfile = new File("MissingYou.wav");
			wavstream = AudioSystem.getAudioInputStream(wavfile);
			wavaudio = AudioSystem.getClip();
			wavaudio.open(wavstream);
			//Set loop points (Only if you want to loop a song)
			int startFrame = 0;
			int endFrame = wavaudio.getFrameLength() - 2;
			wavaudio.setLoopPoints(startFrame, endFrame);
			//Add a line listener to perform certain actions on events
			wavaudio.addLineListener(this);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	//Need to implement this method to qualify as an implementation of LineListener
	//This tells the listener what to do when an event occurs
	public void update(LineEvent event) {
		Line eventLine = event.getLine();
		LineEvent.Type eventType = event.getType();
		if(eventLine == wavaudio && eventType == LineEvent.Type.STOP) {
			//Differentiate between pressing the pause/stop button and letting the song run to the end
			if(pauseBtn.isEnabled() && !playBtn.isEnabled()) {
				playBtn.setEnabled(true);
				pauseBtn.setEnabled(false);
				stopBtn.setEnabled(false);
				wavaudio.setFramePosition(0);
			} else if(!pauseBtn.isEnabled() && playBtn.isEnabled()) {
				playBtn.setEnabled(true);
				pauseBtn.setEnabled(false);
				stopBtn.setEnabled(true);
			}
		} 
	}

	public String progressTime() {
		min = (int) (wavaudio.getMicrosecondPosition()/1000000.0)/60;
		sec = (int) (wavaudio.getMicrosecondPosition() - min*60*1000000)/1000000;
		if(sec < 10) {
			return min + ":0" + sec;
		} else {
			return min + ":" + sec;
		}
	}

	public static void main(String[] args) throws Exception{
		//Initialize the demo
		AudioPlay demo = new AudioPlay();
	}
}