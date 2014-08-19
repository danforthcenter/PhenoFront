package src.ddpsc.resumeDownloadApplet;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JSeparator;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipException;

import javax.swing.JTextArea;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class JResumeDownloadApplet extends JApplet
{
	private CSV csv;
	
	private JTextArea errorPane;
	private JTextArea newDownloadLink;
	private JTextField textSelectNotification;

	/**
	 * Auto generated front end code from Java gui builder, Window Builder Pro.
	 */
	public JResumeDownloadApplet()
	{
		getContentPane().setLayout(null);
		
		JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.setFont(new Font("Arial", Font.PLAIN, 11));
		btnSelectFile.addActionListener(new LoadFileAction());
		btnSelectFile.setBounds(10, 11, 124, 23);
		getContentPane().add(btnSelectFile);
		
		JLabel lblANewDownload = new JLabel("A new download link to replace the missing files.");
		lblANewDownload.setFont(new Font("Arial", Font.PLAIN, 11));
		lblANewDownload.setBounds(10, 56, 580, 14);
		getContentPane().add(lblANewDownload);
		
		JLabel lblDownloadReplacementCsv = new JLabel("Download replacement CSV file for the selected file:");
		lblDownloadReplacementCsv.setFont(new Font("Arial", Font.PLAIN, 11));
		lblDownloadReplacementCsv.setBounds(10, 236, 446, 14);
		getContentPane().add(lblDownloadReplacementCsv);
		
		JButton btnDownloadCsv = new JButton("Download CSV");
		btnDownloadCsv.addActionListener(new DownloadCSVAction());
		btnDownloadCsv.setFont(new Font("Arial", Font.PLAIN, 11));
		btnDownloadCsv.setBounds(466, 232, 124, 23);
		getContentPane().add(btnDownloadCsv);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 45, 580, 2);
		getContentPane().add(separator);
		
		textSelectNotification = new JTextField();
		textSelectNotification.setFont(new Font("Arial", Font.PLAIN, 11));
		textSelectNotification.setText("No file selected");
		textSelectNotification.setBounds(144, 12, 446, 20);
		getContentPane().add(textSelectNotification);
		textSelectNotification.setColumns(10);
		
		JScrollPane newDownloadLinkScrollPane = new JScrollPane();
		newDownloadLinkScrollPane.setBounds(10, 100, 580, 121);
		getContentPane().add(newDownloadLinkScrollPane);
		
		newDownloadLink = new JTextArea();
		newDownloadLinkScrollPane.setViewportView(newDownloadLink);
		newDownloadLink.setFont(new Font("Arial", Font.PLAIN, 11));
		newDownloadLink.setText("example.com/PhenoFront/?snapshots=id1,id2,id3, ...");
		newDownloadLink.setLineWrap(true);
		newDownloadLink.setWrapStyleWord(true);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 266, 580, 2);
		getContentPane().add(separator_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 279, 580, 60);
		getContentPane().add(scrollPane);
		
		errorPane = new JTextArea();
		scrollPane.setViewportView(errorPane);
		
		JLabel lblBeSureTo = new JLabel("Be sure to add the website domain (e.g., example.com) to the beginning of the link!");
		lblBeSureTo.setBounds(10, 75, 580, 14);
		getContentPane().add(lblBeSureTo);
	}
	
	
	/**
	 * Action to download a replacement CSV file
	 * 
	 * Modifies the error pane if something goes wrong. Things that can go wrong:
	 * 		No incomplete download has been analyzed
	 * 		Save file was deleted or blocked during save action
	 */
	final class DownloadCSVAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (csv == null) {
				errorPane.setText("ERROR: No zip file has been processed, cannot save CSV.");
			}
			
			else {
				File saveFile = userPromptSaveFile();
				
				if (saveFile != null) {
					try {
						csv.saveTo(saveFile);
					}
					catch (FileNotFoundException e) {
						errorPane.setText("ERROR: Could not save CSV file because the file could not be found.");
					}
				}
			}
		}
	}
	
	public File userPromptSaveFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			return file;
		}
		
		return null;
	}
	
	
	/**
	 * Action to load an incomplete download file for analysis
	 * 
	 * Modifies the error pane if something goes wrong. Things that can go wrong:
	 * 		Zip file can't be read
	 * 		File is not a Zip archive
	 * 		Zip archive does not contain CSV file
	 * 		CSV file is malformed
	 * 		File is deleted or blocked by another program during read
	 */
	final class LoadFileAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			File file = userPromptForFile();
			try {
				if (file != null) {
					processIncompleteDownload(file);
					textSelectNotification.setText("File '" + file.getName() + "' loaded.");
				}
			}
			
			catch (NoCSVFileException e) {
				errorPane.setText("ERROR: " + e.getMessage());
				textSelectNotification.setText("File '" + file.getName() + "' not loaded, error found.");
			}
			catch (MalformedCSVException e) {
				errorPane.setText("ERROR: " + e.getMessage());
				textSelectNotification.setText("File '" + file.getName() + "' not loaded, error found.");
			}
			catch (ZipException e) {
				errorPane.setText("ERROR: Could not read ZIP file. ZIP file must be repaired.");
				textSelectNotification.setText("File '" + file.getName() + "' not loaded, error found.");
			}
			catch (FileNotFoundException e) {
				errorPane.setText("ERROR: Could not find the file " + file.getName() + ".");
				textSelectNotification.setText("File '" + file.getName() + "' not loaded, error found.");
			}
			catch (IOException e) {
				errorPane.setText("ERROR: Could not read file. " + e.getMessage());
				textSelectNotification.setText("File '" + file.getName() + "' not loaded, error found.");
			}
		}
	}
    
    public File userPromptForFile()
	{
		JFileChooser fileChooser = new JFileChooser("C:/");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip files", "zip");
		fileChooser.setFileFilter(filter);
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			
			errorPane.setText("Seleting file.");
			File file = fileChooser.getSelectedFile();
			errorPane.setText("Got file " + file.getName() + ".");
			return file;
		}
		
		return null;
	}
	
    /**
     * Processes an incomplete download and gives the user a download link to complete the download,
     * and the option to save a new CSV file that properly repesents the snapshots originally downloaded.
     * 
     * Expects the zip file to have been repaired by a zip repair utility.
     * 
     * Expects the CSV file to be a proper comma separated value data table with every row having columns matching the labels
     * 
     * @param incomleteDownloadZip		The zip file that wasn't completely downloaded
     * 
     * @throws FileNotFoundException	Thrown if the supplied file could not be found
     * @throws IOException				Thrown if the file is modified during read
     * @throws MalformedCSVException	Thrown if the CSV is missing columns in any given row
     * @throws NoCSVFileException		Thrown if there is no CSV file in the zip archive
     */
	public void processIncompleteDownload(File incomleteDownloadZip) throws IOException, MalformedCSVException, NoCSVFileException, FileNotFoundException
    {
    	SnapshotZipFile snapshotZipFile = new SnapshotZipFile(incomleteDownloadZip);
    	
    	String missingSnapshots = snapshotZipFile.missingSnapshots_csv();
    	newDownloadLink.setText("/phenofront/userarea/snapshots" + "?snapshotIds=" + missingSnapshots);
    	
    	csv = snapshotZipFile.replacementCSV();
    }
}
