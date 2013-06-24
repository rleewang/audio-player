import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileChooser extends JPanel{
	JButton openBtn;
	JFileChooser fileChooser;
	File oldFile;
	
	public FileChooser(final File wavfile) {		
		fileChooser = new JFileChooser();
		oldFile = wavfile;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Wave sound files", "wav");
		fileChooser.setFileFilter(filter);
		openBtn = new JButton("Open");
		openBtn.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.out.println("Current song is " + wavfile);
						System.out.println("Attempting to open file");
						int returnVal = fileChooser.showOpenDialog(FileChooser.this);
						System.out.println("Parent is " + FileChooser.this);
						if(returnVal == fileChooser.APPROVE_OPTION) {
							final File newFile;
							newFile = fileChooser.getSelectedFile();
							System.out.println("Loaded " + newFile);
							wavfile = newFile;
							System.out.println("Changing song to " + oldFile);
						}
					}
				});
		add(openBtn);
		
	}
	
}
