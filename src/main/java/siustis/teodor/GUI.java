package siustis.teodor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.sun.rowset.CachedRowSetImpl;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class GUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
    private  JTextArea textArea;
    private final JMenuBar menuBar;
    private final JMenu menuFile, menuSearch;
    private final JMenuItem newFile, openFile, saveFile, close, addDb,pacients,showText,tagger;
	List<PacientBean> pacients1;
    DatabaseDAO db = null;
    Connection connection = null;

	File file;
	JTable table;
	  List<PacientBean>pacients2 = new ArrayList<PacientBean>();
	JMenuItem ICDTable;
	boolean opened,inFile,inText=true,found;
	MyMetaMap mm;
	int i;
	JScrollPane pane;
	 StringBuilder content;
	 String text;
	 private JMenuItem ListDatabase;
	 private JMenu menuHelp;
	 private JMenuItem About;
	 private JMenuItem Usage;
    public GUI() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException{
    	
    	getContentPane();
    	setSize(700,500);
    	setTitle("Untitled ");
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	 textArea= new JTextArea("",10,10);
    	textArea.setFont(new Font("Arial",Font.BOLD,10));
    	textArea.setTabSize(2);
    	textArea.setFont(new Font("Arial",Font.BOLD,10));
    	textArea.setTabSize(2);
        opened = false;
    	db = new DatabaseDAO();
    	textArea.setLineWrap(true);
    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	getContentPane().setLayout(new BorderLayout());
    	getContentPane().add(textArea);
    	centreWindow(this);
    	menuFile = new JMenu("File");
    	menuSearch = new JMenu("Search");
        newFile = new JMenuItem("New");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        close = new JMenuItem("Quit");
        pacients = new JMenuItem("Show Table");
        addDb = new JMenuItem("Add to Database");
        showText = new JMenuItem("Show Standardized Text");
        tagger = new JMenuItem("Tagger");
        ICDTable = new JMenuItem("ICD-10 Table");
        menuSearch.add(ICDTable);
        
        
        
        menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuSearch);

       


        this.setJMenuBar(menuBar);
        

        // Set Actions:
      
        //menuBar items
        newFile.addActionListener(this);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
        menuFile.add(newFile);
        
        openFile.addActionListener(this);
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
        menuFile.add(openFile);
        
        saveFile.addActionListener(this);
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        menuFile.add(saveFile);

   
        close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        close.addActionListener(this);
        menuFile.add(close);
        showText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        showText.addActionListener(this);
        
  
        pacients.addActionListener(this);
        pacients.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        ICDTable.addActionListener(this);
        ICDTable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));

        addDb.addActionListener(this);
        addDb.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        menuSearch.add(pacients);
        menuSearch.add(addDb);
        menuSearch.add(showText);
       tagger.addActionListener(this);
        tagger.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,InputEvent.CTRL_MASK));
        menuSearch.add(tagger);
        
        ListDatabase = new JMenuItem("List Database");
        ListDatabase.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					GUIUtils.findAll();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
        	}
        });
        ListDatabase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,InputEvent.CTRL_MASK));
        menuSearch.add(ListDatabase);
        
        menuHelp = new JMenu("Help");
        menuBar.add(menuHelp);
        
        About = new JMenuItem("About");
        menuHelp.add(About);
        
        Usage = new JMenuItem("Usage");
        menuHelp.add(Usage);
        About.addActionListener(this);
        About.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        Usage.addActionListener(this);
       Usage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
   
      
        // used to create space between button groups
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 50);
        
         pane = new JScrollPane(textArea);
        getContentPane().add(pane);
     
    
        	

    }

    public void actionPerformed (ActionEvent e) {
       //close button
    	String text =textArea.getText();
        if (e.getSource() == close ) {
            this.dispose();
        }
       
        else if (e.getSource() == openFile) {
            JFileChooser open = new JFileChooser(); 
            int option = open.showOpenDialog(this); 
          
            inFile=true;
            inText = false;
            if (option == JFileChooser.APPROVE_OPTION) {
            	 textArea.setText(""); 
                try {
                 
                   
                      file = open.getSelectedFile();
                    textArea.setText(GUIUtils.parse(file));
                   
                } catch (Exception ex) { 
                	
                    System.out.println(ex.getMessage());
                }
                
                file = open.getSelectedFile();

                setTitle(file.getName());
               
            }
        }
        // save
        else if (e.getSource() == saveFile ) {
            JFileChooser fileChoose = new JFileChooser();
            int option = fileChoose.showSaveDialog(this);
            file = fileChoose.getSelectedFile();
            inFile=true;
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                     file = fileChoose.getSelectedFile();
                    setTitle(file.getName());
                    BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
                    out.write(textArea.getText());
                    out.close();
                    file = fileChoose.getSelectedFile();

                    setTitle(file.getName());
                } catch (Exception ex) { 
                    System.out.println(ex.getMessage());
                }
               
        }
           
     }
           
           
        	
        if (e.getSource() == newFile ) {
	           textArea.setText("");
        }
	        if (e.getSource() ==pacients &&(!inFile)) {
	       JOptionPane.showMessageDialog(null, new JLabel("Enter a document or save the current text into a file."));
	        }
	        else if (e.getSource() ==pacients &&(inFile==true)) {
	        	
	        		 try {
	        			 GUIUtils.fillTable(file);
	     			} catch (Exception e1) {
	     				e1.printStackTrace();
	     			}
	        	        
				
	        }
	        else if(e.getSource() == pacients  && !inFile){ 
	        	JOptionPane.showMessageDialog(null, new JLabel("Enter a document or save the current text into a file."));
	        }
	       
        if (e.getSource() == tagger &&(inFile==true)) {
        
        		 try {
        			 GUIUtils.showTagger(file);
     			} catch (Exception e1) {
     				e1.printStackTrace();
     			}
        	        
			
        }
        if(e.getSource()==About){
        	try {
				GUIUtils.showAbout();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
        }
        if(e.getSource()==Usage){
        	try {
				GUIUtils.showUsage();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
        }
        if (e.getSource() == tagger && (inFile==false)) {

        	 JOptionPane.showMessageDialog(null, new JLabel("Enter a document or save the current text into a file."));
        }
        if (e.getSource() == ICDTable ) {
            
   		 try {
   			 GUIUtils.showICD();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
   	        
		
   }
   
	        if (e.getSource() == addDb  && inFile) {
	        	
					try {
						GUIUtils.addPacientsToDb(file,file.getName());//adaugare in baza de date
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
	        if(e.getSource() == showText &&  !inFile){

	        	   JOptionPane.showMessageDialog(null, new JLabel("Enter a file or save the text."));
	        }else if(e.getSource() == showText && inFile==true){
	        	
	    					
								try {
									GUIUtils.showText(file);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
						

	    				
	                       
	         
	        		
				

	        }
	       
	        else if(e.getSource() == showText && (opened == false)&& inText == false)
	        	JOptionPane.showMessageDialog(null, new JLabel("Enter a document."));
	        if(e.getSource() == ListDatabase){
	        	try {
					GUIUtils.findAll();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
	        }
        
}
    
    public  static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

   
  
}
