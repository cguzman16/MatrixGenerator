/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixgenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author carolynguzman
 */
public class App extends JFrame {

    //Instance variables    
    //Private instance vars accessible only by instances of this class
    private MatrixGenerator mtxGen;
    private final JLabel[] label;
    private final JPanel panel;
    private final JPanel[] innerPanel;
    private App app;
    private final Font font;
    private final JTextField[] txtField;
    private final JButton[] button;
    private final JFileChooser[] fileChooser;
    private File file;
    private ActionListener openFile, generateFiles, openDirectory;

    //Constructor
    public App() {
        super("Matrix Generator");
        txtField = new JTextField[2];
        button = new JButton[3];
        label = new JLabel[4];
        panel = new JPanel();

        //**************************
        innerPanel = new JPanel[4];

        fileChooser = new JFileChooser[2];
        fileChooser[0] = new JFileChooser();
        fileChooser[1] = new JFileChooser();
        fileChooser[1].setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        font = new Font("Monospaced", 0, 12);
        setFrame(750, 150);
        display();
    } //end constructor

    //Static main method
    public static void main(String[] args) {
        App app = new App();
    } //end main method

    //Displays the GUI when called
    private void display() {
        setVisible(true);
    } //end display method

    //Calles methods to set the bigger components of the frame
    private void setFrame(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPanel();
        setEventListeners();
        pack();
    } //end setFrame method

    //Sets the panel and its components and adds them to the frame
    private void setPanel() {
        add(panel, BorderLayout.NORTH);
        setButtons();
        setLabels();
        setTextFields();
        setInnerPanels();
        panel.add(innerPanel[0]);
        panel.add(innerPanel[1]);
        panel.add(innerPanel[2]);
        panel.add(innerPanel[3]);
        add(label[2]);
    } //end setInputPanel method

    //Sets the GUI buttons
    private void setButtons() {
        button[0] = new JButton("Browse");
        button[0].setFont(font);
        button[1] = new JButton("Generate Matrix");
        button[1].setFont(font);
        button[2] = new JButton("Browse");
        button[2].setFont(font);
    } //end setButtons method

    //Sets the GUI labels
    private void setLabels() {
        label[0] = new JLabel("Select matrix file:", JLabel.CENTER);
        label[0].setFont(font);
        label[1] = new JLabel("Output directory:", JLabel.CENTER);
        label[1].setFont(font);
        label[2] = new JLabel("Report bugs to: carolyn.guzman@utdallas.edu\t", JLabel.RIGHT);
        label[2].setFont(font);
        label[3] = new JLabel();
        label[3].setFont(font);
    } //end setLabels method

    //Sets the text fields for the GUI
    private void setTextFields() {
        txtField[0] = new JTextField(50);
        txtField[0].setEditable(false);
        txtField[0].setFont(font);
        txtField[0].setBackground(Color.lightGray);
        txtField[1] = new JTextField(50);
        txtField[1].setEditable(false);
        txtField[1].setFont(font);
    } //end setTextField method

    //Sets the inner panel that are part of the input panel
    private void setInnerPanels() {
        innerPanel[0] = new JPanel();
        innerPanel[1] = new JPanel();
        innerPanel[2] = new JPanel();
        innerPanel[3] = new JPanel();
        innerPanel[0].setLayout(new GridLayout(2, 1, 0, 10));
        innerPanel[0].add(label[0]);
        innerPanel[0].add(label[1]);
        innerPanel[1].setLayout(new GridLayout(2, 1));
        innerPanel[1].add(txtField[0]);
        innerPanel[1].add(txtField[1]);
        innerPanel[2].setLayout(new GridLayout(2, 1));
        innerPanel[2].add(button[0]);
        innerPanel[2].add(button[2]);
        innerPanel[3].setLayout(new GridLayout(2, 1));
        innerPanel[3].add(label[3]);
        innerPanel[3].add(button[1]);
    } //end setInnerPanels method

    //Sets the event listeners for the buttons and text fields in the GUI
    protected void setEventListeners() {
        setActionListenerObjects();
        //Assigns an action to each button and the search text field
        button[0].addActionListener(openFile);
        button[1].addActionListener(generateFiles);
        button[2].addActionListener(openDirectory);
    } //end setEventListeners method

    //Sets ActionListener objects with anonymous classes and functional operations
    private void setActionListenerObjects() {
        //Uses functional expressions to define each object that will be added to 
        //the buttons and text fields in order to perform given functions
        openFile = (ActionEvent e) -> {
            int returnVal = fileChooser[0].showOpenDialog(App.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.file = fileChooser[0].getSelectedFile();
                txtField[0].setText(fileChooser[0].getName(file));
            }
        };

        openDirectory = (ActionEvent e) -> {
            int returnVal = fileChooser[1].showOpenDialog(App.this);
            txtField[1].setText(fileChooser[1].getSelectedFile().toString());
        };

        generateFiles = (ActionEvent e) -> {
            try {
                readFile();
                JOptionPane.showMessageDialog(App.this, "Sparse Matrix Generated", "", JOptionPane.WARNING_MESSAGE);
            } catch (mtxGenException ex) {
                JOptionPane.showMessageDialog(App.this, ex.getError(), "", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(App.this, "Unknown Error", "", JOptionPane.WARNING_MESSAGE);
            }

        };

    } //end setActionListenerObjects method

    //readFile initializes world and calls worls.readFile()
    public void readFile() throws FileNotFoundException, IOException, Exception {
        mtxGen = new MatrixGenerator(file, txtField[1].getText());
        mtxGen.readFile();
    } //end readFile method

}
