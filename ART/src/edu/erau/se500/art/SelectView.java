package edu.erau.se500.art;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.github.javaparser.ParseProblemException;

import edu.erau.se500.art.main.Generate;

/**
 * Eclipse view for the user to select their files and options for the traceability calculation
 */
public class SelectView extends ViewPart {
	
	/**
	 * Unique identifier for this view
	 */
	public static final String ID = "edu.erau.se500.art.SelectView";
	
	/**
	 * Reference to the parent Composite object that contains the UI elements of this view
	 */
	Composite mainComposite;
	
	/**
	 * References to the files/directories that the user selected for their traceability
	 */
	File umlFile, javaFile, projectDirectory;
	
	/**
	 * Radio buttons to toggle between using a project or single file for the source code 
	 */
	Button btnProjectRadio, btnSingleRadio;
	
	/**
	 * Labels to display the file paths of the uml and source code 
	 */
	Label lblFilenameProject, lblFilenameSingle;
	
	/**
	 * Buttons that open file chooser windows to select files
	 */
	Button btnBrowseProject, btnBrowseSingle;
	
	/**
	 * Radio muttons to toggle between forward and backward traceability 
	 */
	Button btnForwardRadio, btnBackwardRadio;
	
	/**
	 * Create GUI for the view
	 */
	public void createPartControl(Composite parent) {
		
		mainComposite = parent;
	    
	    RowLayout overallLayout = new RowLayout ();
		overallLayout.type = SWT.VERTICAL;
		overallLayout.spacing = 5;
		parent.setLayout (overallLayout);

		Group grpUML = new Group (parent, SWT.NONE);
		grpUML.setText ("UML Class Diagram");
		
		Label lblFilenameUML = new Label (grpUML, SWT.NONE);
		lblFilenameUML.setBounds(30, 25, 160, 20);
		lblFilenameUML.setText("No file selected");
		lblFilenameUML.setEnabled(false);
		lblFilenameUML.pack();
		
		Button btnBrowseUML = new Button(grpUML, SWT.PUSH);
		btnBrowseUML.setText("Browse");
		btnBrowseUML.setLocation(200, 20);
		btnBrowseUML.pack();
		btnBrowseUML.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(btnBrowseUML.getShell(),  SWT.OPEN  );
				dlg.setText("Select Class Diagram");
				final String[] allowedExtensions = {"*.uml"};
				dlg.setFilterExtensions(allowedExtensions);
				String path = dlg.open();
				if (path == null) return;
				lblFilenameUML.setText(dlg.getFileName());
				lblFilenameUML.setEnabled(true);
				umlFile = new File(path);
			}
		});
		
		grpUML.pack();

		Group grpCode = new Group (parent, SWT.NONE);
		grpCode.setText ("Source Code");

	    btnProjectRadio = new Button(grpCode, SWT.RADIO);
	    btnProjectRadio.setLocation(10, 20);
	    btnProjectRadio.setText("Project");
	    btnProjectRadio.setSelection(true);
	    btnProjectRadio.pack();
	    btnProjectRadio.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		radioSourceSelectionChanged(btnProjectRadio);
	    	}
	    });
	    
		lblFilenameProject = new Label (grpCode, SWT.NONE);
		lblFilenameProject.setBounds(30, 45, 170, 20);
		lblFilenameProject.setText("No Project Selected");
		lblFilenameProject.setEnabled(false);
		lblFilenameProject.pack();
		
		btnBrowseProject = new Button(grpCode, SWT.PUSH);
		btnBrowseProject.setText("Browse");
		btnBrowseProject.setLocation(200, 40);
		btnBrowseProject.pack();
		btnBrowseProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(btnBrowseProject.getShell());
				dlg.setText("Select Project Directory");
				String path = dlg.open();
				if (path == null) return;
				lblFilenameProject.setText(path);
				lblFilenameProject.setEnabled(true);
				projectDirectory = new File(path);
			}
		});
	    
	    btnSingleRadio = new Button(grpCode, SWT.RADIO);
	    btnSingleRadio.setLocation(10, 70);
	    btnSingleRadio.setText("Single File");
	    btnSingleRadio.pack();
	    btnSingleRadio.addSelectionListener(new SelectionAdapter() {
	    	@Override
	    	public void widgetSelected(SelectionEvent e) {
	    		radioSourceSelectionChanged(btnSingleRadio);
	    	}
	    });
	    
		lblFilenameSingle = new Label (grpCode, SWT.NONE);
		lblFilenameSingle.setBounds(30, 95, 170, 20);
		lblFilenameSingle.setText("No file selected");
		lblFilenameSingle.setEnabled(false);
		lblFilenameSingle.pack();
		
		btnBrowseSingle = new Button(grpCode, SWT.PUSH);
		btnBrowseSingle.setText("Browse");
		btnBrowseSingle.setEnabled(false);
		btnBrowseSingle.setLocation(200, 90);
		btnBrowseSingle.pack();
		btnBrowseSingle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(btnBrowseSingle.getShell(),  SWT.OPEN  );
				dlg.setText("Select Java File");
				final String[] allowedExtensions = {"*.java"};
				dlg.setFilterExtensions(allowedExtensions);
				String path = dlg.open();
				if (path == null) return;
				lblFilenameSingle.setText(dlg.getFileName());
				lblFilenameSingle.setEnabled(true);
				javaFile = new File(path);
			}
		});
	    
	    grpCode.pack();
	    
		Group grpDirection = new Group (parent, SWT.NONE);
		grpDirection.setText ("Direction");
		grpDirection.setSize(200, 50);
		
	    btnForwardRadio = new Button(grpDirection, SWT.RADIO);
	    btnForwardRadio.setSelection(true);
	    btnForwardRadio.setLocation(10, 20);
	    btnForwardRadio.setText("Forward");
	    btnForwardRadio.pack();
	    
	    btnBackwardRadio = new Button(grpDirection, SWT.RADIO);
	    btnBackwardRadio.setLocation(10, 40);
	    btnBackwardRadio.setText("Backward");
	    btnBackwardRadio.pack();
		
		grpDirection.pack();
	    
		Button btnCompute = new Button(parent, SWT.PUSH);
		btnCompute.setText("Compute Traceability");
		btnCompute.pack();
		btnCompute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Boolean doProject = null;
				Boolean doForward = null;
				
				//check uml source
				if (umlFile == null) {
					MessageBox mb = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
					mb.setText("Error");
					mb.setMessage("No UML Class Diagram was selected.");
					mb.open();
					return;
				}
				//check java source
				if (btnProjectRadio.getSelection()) {
					if (projectDirectory == null) {
						MessageBox mb = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						mb.setText("Error");
						mb.setMessage("No project directory was selected.");
						mb.open();
						return;
					} else {
						doProject = true;
					}
				} else if (btnSingleRadio.getSelection()) {
					if (javaFile == null) {
						MessageBox mb = new MessageBox(parent.getShell(), SWT.ICON_ERROR | SWT.OK);
						mb.setText("Error");
						mb.setMessage("No java file was selected.");
						mb.open();
						return;
					} else {
						doProject = false;
					}
				} else {
					//nothing selected
					return;
				}
				//set direction
				if (btnForwardRadio.getSelection()) {
					doForward = true;
				} else if (btnBackwardRadio.getSelection()) {
					doForward = false;
				} else {
					//nothing selected
					return;
				}
				computeTraceability(doProject, doForward);
			}
		});
	}
	
	/** Overall method that performs the traceability through other methods
	 * @param doProject - Whether or not the user selected a project (false = single file)
	 * @param doForward - Whether or not forward traceability was selected (false = backward) 
	 */
	private void computeTraceability(boolean doProject, boolean doForward) {
		//erase previous results
		Compare.UMLClasses.clear();
		Compare.javaClasses.clear();
		Compare.results.clear();
		
		//Compute new results
		parseUML();
		parseJava(doProject);
		Compare.compare(doForward);
		showResultsView();
	}
	
	/**
	 * Takes the UML diagram and generates java code from it using acceleo plug-in.
	 */
	private void parseUML() {
		File genSrcDir = new File(System.getProperty("user.dir")+File.separator+"src-gen");
		System.out.println(genSrcDir.getAbsolutePath());
		if (!genSrcDir.exists()) genSrcDir.mkdir();
		else {
			emptyDirectory(genSrcDir);
		}
		
		URI model = URI.createFileURI(umlFile.getAbsolutePath());
		List<String> arguments = new ArrayList<String>();
		try {
			Generate g = new Generate(model, genSrcDir, arguments);
			g.doGenerate(null);
			JavaExtractor.fromUML = true;
			JavaExtractor.collectFiles(genSrcDir);
		} catch (ParseProblemException e) {
			MessageBox mb = new MessageBox(mainComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error parsing generated java");
			mb.setMessage("A java file generated based on your UML could not be parsed.\n"+JavaExtractor.currentFile+"\n\n"+e.getMessage());
			mb.open();
			return;
		} catch (Exception e) {
			MessageBox mb = new MessageBox(mainComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText("Exception during parseUML");
			mb.setMessage(e.getMessage());
			mb.open();
		}
	}
	
	/** Extracts the data out of the java files through the JavaExtractor class
	 * @param doProject - whether ot not a project was selected as source code (false = single file)
	 */
	private void parseJava(boolean doProject) {
		try {
			JavaExtractor.fromUML = false;
			if (doProject) {
				JavaExtractor.collectFiles(projectDirectory);
			} else {
				JavaExtractor.extractFromFile(javaFile);
			}
		} catch (ParseProblemException e) {
			MessageBox mb = new MessageBox(mainComposite.getShell(), SWT.ICON_ERROR | SWT.OK);
			mb.setText("Error parsing java source code");
			mb.setMessage("A java source code file could not be parsed.\n"+JavaExtractor.currentFile+"\n\n"+e.getMessage());
			mb.open();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens the results view
	 */
	private void showResultsView() {
		IViewPart resultsView = getResultsView();
		if (resultsView != null) {
			getSite().getPage().bringToTop(resultsView);
			resultsView.setFocus(); //triggers table results to refresh
		}
	}
	
	/** Updates the UI by disabling certain elements depending on whether user chose project or single file for source code
	 * @param source - The button that they clicked to trigger the action
	 */
	private void radioSourceSelectionChanged(Button source) {
		if (source == btnProjectRadio) {
			if (projectDirectory != null) lblFilenameProject.setEnabled(true); //don't enable unless directory selected
			btnBrowseProject.setEnabled(true);
			lblFilenameSingle.setEnabled(false);
			btnBrowseSingle.setEnabled(false);
		} else {
			if (javaFile != null) lblFilenameSingle.setEnabled(true); //dont enable unless file selected
			btnBrowseSingle.setEnabled(true);
			lblFilenameProject.setEnabled(false);
			btnBrowseProject.setEnabled(false);
		}
	}
	
	/** Deletes all files in the selected folder
	 * @param thisDir - the folder chosen to empty
	 */
	private void emptyDirectory(File thisDir) {
		File[] listOfFiles = thisDir.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        listOfFiles[i].delete();
	      } else if (listOfFiles[i].isDirectory()) {
	       emptyDirectory(listOfFiles[i]);
	       listOfFiles[i].delete();
	      }
	    }
	}
	
	/** Creates reference to the ResultsView view
	 * @return
	 */
	private IViewPart getResultsView() {
		IWorkbenchPage page = getSite().getPage();
		IViewPart resultsView = page.findView("edu.erau.se500.art.ResultsView");
		if (resultsView == null) {
			try {
				resultsView = page.showView("edu.erau.se500.art.ResultsView");
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		return resultsView;
	}
	
	/**
	 * Sets focus to the view
	 */
	public void setFocus() {
		mainComposite.setFocus();
	}

}
