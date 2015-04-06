package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

import runner.Controller;
import treebuilder.Equation;

public class GUI {

	private JFrame frmFlashqua;
	
	private JTextPane txtpnFileinput;
	/**
	 * Column letter followed the nr of rows which are the examples to be used
	 * e.g. K 1 2 3
	 */
	private JTextPane txtpnExamplesolution;
	/**
	 * Column letters
	 * e.g. A B C D
	 */
	private JTextPane txtpnColumns;
	
	private JComboBox<String> comparatorBox;
	private static final String[] COMPARATORS = new String[]{"=", "<", "<=", ">", ">="};
	
	private JTextPane txtpnBestfoundsolution;
	private JTextPane txtpnNrOfSolutions;
	
	private static File excelFile; 
	private static final Controller controller = new Controller();

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
		btnBrowse.setBounds(430, 12, 107, 25);
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
		
		txtpnBestfoundsolution = new JTextPane();
		txtpnBestfoundsolution.setEditable(false);
		txtpnBestfoundsolution.setBounds(178, 167, 359, 25);
		frmFlashqua.getContentPane().add(txtpnBestfoundsolution);
		
		JLabel lblNorOfSolutions = new JLabel("Nr of Solutions Found:");
		lblNorOfSolutions.setBounds(12, 198, 166, 25);
		frmFlashqua.getContentPane().add(lblNorOfSolutions);
		
		txtpnNrOfSolutions = new JTextPane();
		txtpnNrOfSolutions.setEditable(false);
		txtpnNrOfSolutions.setBounds(178, 198, 359, 25);
		frmFlashqua.getContentPane().add(txtpnNrOfSolutions);
		
		final JButton btnShowAll = new JButton("Show All");
		btnShowAll.setBounds(420, 230, 117, 25);
		btnShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnShowAllClicked(btnShowAll);
			}
		});
		frmFlashqua.getContentPane().add(btnShowAll);
		
		comparatorBox = new JComboBox<String>(COMPARATORS);
		comparatorBox.setBounds(430, 49, 107, 24);
		frmFlashqua.getContentPane().add(comparatorBox);
	}
	
	/**
	 * Handles the action when the show all button is clicked
	 */
	private void btnShowAllClicked(JButton btnShowAll) {
		//custom title, no icon
		String allSol = "";
		for(Equation eq : controller.getAllEquations()) {
			allSol += eq.toString() + "\n";
		}
		JOptionPane.showMessageDialog(null, allSol, "All solutions", JOptionPane.PLAIN_MESSAGE);
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
		String[] exampleSolution = txtpnExamplesolution.getText().split(" ");
		String[] columns = txtpnColumns.getText().split(" ");
		
		executeController(exampleSolution, columns, comparatorBox.getSelectedItem());
		
		txtpnBestfoundsolution.setText(controller.getBestEquation().toString());
		txtpnNrOfSolutions.setText(controller.getNrOfSolutions() + "");
		
		double[] line;
		int i = 1;
		while((line = readLine(i, columns)) != null) {
			System.out.println("writing...");
			double result = controller.calculateAccordingToBestEquation(line);
			writeToCell(i, exampleSolution[0], result);
			i++;
		}
		
	}

	/**
	 * Executes the controller
	 * 
	 * @param exampleSolution
	 * 			The example solution
	 * @param columns
	 * 			the columns to be used
	 */
	private void executeController(String[] exampleSolution, String[] columns, Object comparator) {
		List<double[]> input = readInput(exampleSolution, columns);
		controller.excecute(input, comparator);
	}
	
	/**
	 * Reads a line
	 * @param rowNr
	 * 			The number of the line to be read
	 * @param columns
	 * 			The columns to be read
	 * @return
	 * 			The numbers of the line
	 */
	private double[] readLine(int rowNr, String[] columns) {
		boolean emptyRow = true;
		double[] result = new double[columns.length];
		for(int i = 0; i < result.length; i++) {
			result[i] = readCell(rowNr, columns[i]);
			if(result[i] != 0) {
				emptyRow = false;
			}
		}
		if(emptyRow) {
			return null;
		}
		return result;
	}

	/**
	 * Reads the input
	 * 
	 * @param exampleSolution
	 * 			The example solutions (column folowed by row numbers)
	 * @param columns
	 * 			The columns to be read
	 * @return
	 * 			The input
	 */
	private List<double[]> readInput(String[] exampleSolution, String[] columns) {
		List<double[]> input = new ArrayList<double[]>();
		for(int i = 1; i < exampleSolution.length; i++) {
			double[] example = new double[columns.length+1];
			for(int j = 0; j < columns.length; j++) {
				example[j] = readCell(Integer.parseInt(exampleSolution[i]), columns[j]);
			}
			example[example.length-1] = readCell(Integer.parseInt(exampleSolution[i]), exampleSolution[0]);
			input.add(example);
		}
		return input;
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
	
	private double readCell(int rowNr, String column) {
		try {
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelFile));
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheetAt(0);
		    HSSFRow row;
		    HSSFCell cell;
		    
		    row = sheet.getRow(rowNr-1);
		    int cellNr = (int) column.toLowerCase().toCharArray()[0]-96 - 1;
		    cell = row.getCell(cellNr);
		    double value = cell.getNumericCellValue();
		    wb.close();
		    return value;
		} catch(Exception e) {
//		    ioe.printStackTrace();
		}
		return 0.0;
	}
	
	private static void writeToCell(int rowNr, String columnString, double value) {
		try {
		    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelFile));
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheetAt(0);
		    HSSFRow row;
		    HSSFCell cell;
		    
		    int columnNr = (int) columnString.toLowerCase().toCharArray()[0]-96 - 1;

		    row = sheet.getRow(rowNr-1);
		    cell = row.getCell(columnNr);
		    if(cell == null) {
		    	cell = row.createCell(columnNr, Cell.CELL_TYPE_NUMERIC);
		    }
		    cell.setCellValue(value);
		    FileOutputStream fileOut = new FileOutputStream(excelFile);
		    wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		    wb.close();
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
	}
}
