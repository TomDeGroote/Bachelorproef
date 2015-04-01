package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class GUI {

	private JFrame frmFlashqua;
	
	private JTextPane txtpnFileinput;
	private JTextPane txtpnExamplesolution;
	private JTextPane txtpnColumns;
	
	private File excelFile; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmFlashqua.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFlashqua = new JFrame();
		frmFlashqua.setResizable(false);
		frmFlashqua.setTitle("FlashQua");
		frmFlashqua.setBounds(100, 100, 551, 388);
		frmFlashqua.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFlashqua.getContentPane().setLayout(null);
		
		final JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(430, 12, 117, 25);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnBrowseClicked(btnBrowse);
			}
		});
		frmFlashqua.getContentPane().add(btnBrowse);
		
		txtpnFileinput = new JTextPane();
		txtpnFileinput.setBounds(86, 12, 332, 25);
		frmFlashqua.getContentPane().add(txtpnFileinput);
		
		final JButton btnCalculate = new JButton("Calculate");
		btnCalculate.setBackground(new Color(0, 102, 0));
		btnCalculate.setBounds(12, 311, 525, 36);
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCalculateClicked(btnCalculate);
			}
		});
		frmFlashqua.getContentPane().add(btnCalculate);
		
		JLabel lblExcelFile = new JLabel("Excel file:");
		lblExcelFile.setBounds(12, 12, 71, 25);
		frmFlashqua.getContentPane().add(lblExcelFile);
		
		JLabel lblBest = new JLabel("Example Solution:");
		lblBest.setBounds(12, 49, 129, 25);
		frmFlashqua.getContentPane().add(lblBest);
		
		JLabel lblColumns = new JLabel("Columns:");
		lblColumns.setBounds(12, 86, 71, 25);
		frmFlashqua.getContentPane().add(lblColumns);
		
		txtpnColumns = new JTextPane();
		txtpnColumns.setBounds(86, 86, 332, 25);
		frmFlashqua.getContentPane().add(txtpnColumns);
		
		txtpnExamplesolution = new JTextPane();
		txtpnExamplesolution.setBounds(144, 49, 274, 25);
		frmFlashqua.getContentPane().add(txtpnExamplesolution);
		
		JLabel lblBestFoundSolution = new JLabel("Best Found Solution:");
		lblBestFoundSolution.setBounds(10, 167, 150, 25);
		frmFlashqua.getContentPane().add(lblBestFoundSolution);
		
		JTextPane txtpnBestfoundsolution = new JTextPane();
		txtpnBestfoundsolution.setEditable(false);
		txtpnBestfoundsolution.setBounds(178, 167, 359, 25);
		frmFlashqua.getContentPane().add(txtpnBestfoundsolution);
		
		JLabel lblNorOfSolutions = new JLabel("Nr of Solutions Found:");
		lblNorOfSolutions.setBounds(12, 198, 166, 25);
		frmFlashqua.getContentPane().add(lblNorOfSolutions);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBounds(178, 198, 359, 25);
		frmFlashqua.getContentPane().add(textPane);
		
		JButton btnShowAll = new JButton("Show All");
		btnShowAll.setBounds(420, 230, 117, 25);
		frmFlashqua.getContentPane().add(btnShowAll);
	}
	
	/**
	 * Handles the action when the browse button for bullseye is clicked
	 */
	private void btnBrowseClicked(JButton button) {
		excelFile = selectFile(button);
		txtpnFileinput.setText(excelFile.getAbsolutePath());
	}
	
	/**
	 * Handles the action when the browse button for bullseye is clicked
	 */
	private void btnCalculateClicked(JButton button) {
		String exampleSolution = txtpnExamplesolution.getText();
		String columns = txtpnColumns.getText();
	}
	
	/**
	 * Opens the file manager and let's the user select a file
	 * 
	 * @param button
	 * 			The button that was clicked
	 * @return
	 * 			The file the user selected
	 */
	private File selectFile(JButton button) {
		final JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(button);

		try {
			// Open an input stream
			return fc.getSelectedFile();
		} catch (Exception ex) {

		}
		return null;
	}
}
