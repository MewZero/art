package edu.erau.se500.art;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
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

public class SelectView extends ViewPart {
	
	public static final String ID = "edu.erau.se500.art.SelectView";
	
	Composite mainPanel;
	File umlFile, javaFile, projectDirectory;
	Button btnProjectRadio, btnSingleRadio;
	Label lblFilenameProject, lblFilenameSingle;
	Button btnBrowseProject, btnBrowseSingle;
	Button btnForwardRadio, btnBackwardRadio;
	
	public void createPartControl(Composite mainPanel) {
		
		this.mainPanel = mainPanel;
	    
	    RowLayout overallLayout = new RowLayout ();
		overallLayout.type = SWT.VERTICAL;
		overallLayout.spacing = 5;
		mainPanel.setLayout (overallLayout);

		Group grpUML = new Group (mainPanel, SWT.NONE);
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

		Group grpCode = new Group (mainPanel, SWT.NONE);
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
	    
		Group grpDirection = new Group (mainPanel, SWT.NONE);
		grpDirection.setText ("Direction");
		grpDirection.setSize(200, 50);
		
	    btnForwardRadio = new Button(grpDirection, SWT.RADIO);
	    btnForwardRadio.setLocation(10, 20);
	    btnForwardRadio.setText("Forward");
	    btnForwardRadio.pack();
	    
	    btnBackwardRadio = new Button(grpDirection, SWT.RADIO);
	    btnBackwardRadio.setLocation(10, 40);
	    btnBackwardRadio.setText("Backward");
	    btnBackwardRadio.pack();
		
		grpDirection.pack();
	    
		Button btnCompute = new Button(mainPanel, SWT.PUSH);
		btnCompute.setText("Compute Traceability");
		btnCompute.pack();
		btnCompute.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Boolean doProject = null;
				if (umlFile == null) {
					MessageBox mb = new MessageBox(mainPanel.getShell(), SWT.ICON_ERROR | SWT.OK);
					mb.setText("Error");
					mb.setMessage("No UML Class Diagram was selected.");
					mb.open();
					return;
				}
				if (btnProjectRadio.getSelection()) {
					if (projectDirectory == null) {
						MessageBox mb = new MessageBox(mainPanel.getShell(), SWT.ICON_ERROR | SWT.OK);
						mb.setText("Error");
						mb.setMessage("No project directory was selected.");
						mb.open();
						return;
					} else {
						doProject = true;
					}
				} else if (btnSingleRadio.getSelection()) {
					if (javaFile == null) {
						MessageBox mb = new MessageBox(mainPanel.getShell(), SWT.ICON_ERROR | SWT.OK);
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
				computeTraceability(doProject);
			}
		});
	}
	
	private void computeTraceability(boolean doProject) {
		parseUML();
		parseJava(doProject);
		Compare.compare();
		showResultsView();
	}
	
	private void parseUML() {
		File genSrcDir = new File("src-gen");
		if (!genSrcDir.exists()) genSrcDir.mkdir();
		else {
			emptyDirectory(genSrcDir);
		}
		
		URI model = URI.createFileURI(umlFile.getAbsolutePath());
		List<String> arguments = new ArrayList<String>();
		try {
			Generate g = new Generate(model, genSrcDir, arguments);
			g.doGenerate(null);
			JavaExtractor.collectFiles(genSrcDir, true);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void parseJava(boolean doProject) {
		try {
			if (doProject) {
				JavaExtractor.collectFiles(projectDirectory, false);
			} else {
				JavaExtractor.extractFromFile(javaFile, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void showResultsView() {
		IWorkbenchPage page = getSite().getPage();
		IViewPart resultsView = page.findView("edu.erau.se500.art.ResultsView");
		if (resultsView == null) {
			try {
				resultsView = page.showView("edu.erau.se500.art.ResultsView");
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		if (resultsView != null) {
			getSite().getPage().bringToTop(resultsView);
			ResultsView rv = (ResultsView) resultsView;
			rv.showResults();
		}
	}
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
	
	public void setFocus() {
		mainPanel.setFocus();
	}

}
