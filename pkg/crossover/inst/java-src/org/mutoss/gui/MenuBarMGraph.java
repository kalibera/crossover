package org.mutoss.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.io.FileTransfer;
import org.af.commons.logging.LoggingSystem;
import org.af.commons.logging.widgets.DetailsDialog;
import org.af.commons.tools.OSTools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

public class MenuBarMGraph extends JMenuBar implements ActionListener {

	private static final Log logger = LogFactory.getLog(MenuBarMGraph.class);
    JMenu fmenu = new JMenu("File");
    JMenu extraMenu = new JMenu("Extras");
    JMenu exampleMenu = new JMenu("Example graphs");
    CreateGraphGUI control;

	public MenuBarMGraph(CreateGraphGUI control) {
		
		this.control = control;		

		fmenu.add(makeMenuItem("New Graph", "new graph", KeyEvent.VK_N));
		fmenu.add(makeMenuItem("Load Graph from R", "load graph from R", KeyEvent.VK_L));
		fmenu.add(makeMenuItem("Load Graph from RData file", "load graph"));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Load p-Values from R", "load p-values from R"));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Save Graph to R", "save graph to R", KeyEvent.VK_S));	
		fmenu.add(makeMenuItem("Save Graph to RData file", "save graph"));		
		fmenu.addSeparator();		
		fmenu.add(makeMenuItem("Export Graph to PNG Image", "export graph image", KeyEvent.VK_P));		
		fmenu.add(makeMenuItem("Export Graph to LaTeX File", "export graph latex", KeyEvent.VK_A));
		fmenu.add(makeMenuItem("Copy Graph to Clip Board", "copy graph to clipboard"));
		fmenu.add(makeMenuItem("Show LaTeX Code for Graph", "show graph latex", KeyEvent.VK_C));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Save LaTeX Report", "save latex report", KeyEvent.VK_R));
		JMenuItem item = makeMenuItem("Save PDF Report", "save pdf");
		item.setEnabled(false);
		fmenu.add(item);
		//fmenu.add(makeMenuItem("Save PDF Report", "save pdf"));
		fmenu.addSeparator();
		createLastUsed();
		fmenu.setMnemonic(KeyEvent.VK_F);
		add(fmenu);
		
		createExampleMenu();
		add(exampleMenu);
		exampleMenu.setMnemonic(KeyEvent.VK_X);

		JMenu menu = new JMenu("Analysis");
		menu.setMnemonic(KeyEvent.VK_A);

		menu.add(makeMenuItem("Graph analysis", "graphAnalysis"));
		//if (Configuration.getInstance().getGeneralConfig().experimentalFeatures()) {
		menu.addSeparator();
		menu.add(makeMenuItem("Power analysis (still experimental)", "powerAnalysis"));		
		//}

		add(menu);

		createExtraMenu();
		add(extraMenu);

		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menu.add(makeMenuItem("About", "showAbout", KeyEvent.VK_B));         
		menu.add(makeMenuItem("Introduction to gMCP", "showAppHelp", KeyEvent.VK_I));
		menu.add(makeMenuItem("Weighted parametric tests defined by graphs", "showParametric", KeyEvent.VK_P));
		menu.add(makeMenuItem("gMCP R Online Reference manual", "showManual", KeyEvent.VK_M));
		menu.add(makeMenuItem("Paper about gMCP in the Biometrical Journal", "showPaper1", KeyEvent.VK_P));
		menu.add(makeMenuItem("References", "showReferences", KeyEvent.VK_R));
		//menu.add(makeMenuItem("Theoretical Background", "showAppHelp"));
		/*menu.addSeparator();
         menu.add(makeMenuItem("Description of Edges with Infinitesimal Small Epsilon Weights", "showEpsDoc"));*/
		menu.addSeparator();
		menu.add(makeMenuItem("Version Info / NEWS", "showNEWS", KeyEvent.VK_N));
		add(menu);

	}
	
	public void createExampleMenu() {
		exampleMenu.removeAll();
		
		JMenu subMenu = new JMenu("Common test procedures for any number of hypotheses");		
		subMenu.add(makeMenuItem("Bonferroni-Holm procedure", "bht"));
		subMenu.add(makeMenuItem("Fixed sequence test", "fixedSequence"));
		subMenu.add(makeMenuItem("Fallback procedure", "fallback"));
		exampleMenu.add(subMenu);
		
		subMenu = new JMenu("3 unstructured hypotheses");
		subMenu.add(makeMenuItem("Improved fallback procedure I", "fallbackI"));
		subMenu.add(makeMenuItem("Improved fallback procedure II", "fallbackII"));
		exampleMenu.add(subMenu);		
		
		subMenu = new JMenu("2 primary & 2 secondary hypotheses");		
		subMenu.add(makeMenuItem("Parallel Gatekeeping with 4 Hypotheses", "pg"));
		subMenu.add(makeMenuItem("Improved Parallel Gatekeeping with 4 Hypotheses", "pgi"));
		subMenu.addSeparator();
		subMenu.add(makeMenuItem("Truncated Holm procedure", "truncHolm"));
		subMenu.addSeparator();
		subMenu.add(makeMenuItem("General successive graph", "gSuccessive"));
		subMenu.add(makeMenuItem("   Simple successive graph I from Maurer et al. (2011)", "successiveI"));
		subMenu.add(makeMenuItem("   Simple successive graph II from Maurer et al. (2011)", "successiveII"));
		subMenu.addSeparator();
		subMenu.add(makeMenuItem("Graph from Hung and Wang (2010)", "hung"));
		subMenu.add(makeMenuItem("Graph from Huque, Alosh and Bhore (2011)", "huque"));
		exampleMenu.add(subMenu);

		subMenu = new JMenu("3 primary & 3 secondary hypotheses");		
		subMenu.add(makeMenuItem("Graph from Bauer et al. (2001)", "bauer"));
		subMenu.add(makeMenuItem("Graph from Bretz et al. (2011)", "bretzEtAl"));
		exampleMenu.add(subMenu);
		
		subMenu = new JMenu("2 primary & 2 secondary & 2 tertiary hypotheses");		
		subMenu.add(makeMenuItem("Graph from Bretz et al. (2009), Figure 14a", "bretzEtAl2009a"));
		subMenu.add(makeMenuItem("Graph from Bretz et al. (2009), Figure 14b", "bretzEtAl2009b"));
		subMenu.add(makeMenuItem("Graph from Bretz et al. (2009), Figure 15", "bretzEtAl2009c"));
		exampleMenu.add(subMenu);
		
		subMenu = new JMenu("Miscellaneous");		
		subMenu.add(makeMenuItem("Graph from Hommel et al. (2007)", "hommelEtAl"));
		subMenu.add(makeMenuItem("Graph from Hommel et al. (2007) simplified", "hommelEtAlSimple"));
		//subMenu.addSeparator();
		subMenu.add(makeMenuItem("Drug clinical trial example (serial gatekeeping) from Maurer et al. (1995)", "maurer1995"));
		subMenu.add(makeMenuItem("Graph I from Ferber et al. (2011)", "ferber2011"));
		subMenu.add(makeMenuItem("Graph II from Ferber et al. (2011)", "ferber2011b"));
		exampleMenu.add(subMenu);
	}
	
	public void createExtraMenu() {
		extraMenu.removeAll();
		extraMenu.add(makeMenuItem("Options", "showOptions", KeyEvent.VK_O));
		extraMenu.add(makeMenuItem("Set all options back to default", "clearOptions", KeyEvent.VK_C));
		extraMenu.addSeparator();
		extraMenu.add(makeMenuItem("Change Layout of graph", "changeGraphLayout", KeyEvent.VK_G));
		extraMenu.add(makeMenuItem("Set variables to specific real values", "replaceVariables", KeyEvent.VK_V));
		extraMenu.addSeparator();
		extraMenu.add(makeMenuItem("Log", "showLog", KeyEvent.VK_L));
		extraMenu.add(makeMenuItem("Report error", "reportError", KeyEvent.VK_R));
		extraMenu.add(makeMenuItem("Submit your own graph to gMCP archive", "submitGraph"));
		if (System.getProperty("eclipse") != null) {		
			extraMenu.add(makeMenuItem("Debug console", "debugConsole", KeyEvent.VK_D));
		}
		extraMenu.setMnemonic(KeyEvent.VK_E);
		
	}

	private void createLastUsed() {
		List<String> graphs = Configuration.getInstance().getGeneralConfig().getLatestGraphs();
		
		for(int i=fmenu.getItemCount()-1; i>16; i--) {
			fmenu.remove(i);
		}
		
		if (graphs.size()>0) {	
			int i = 0;
			for (String graph : graphs) {
				i++;
				String s = graph;
				logger.info("Process last used graph: '"+s+"'.");			
				File f = new File(s);				
				if (f.exists()) {
					String path = f.getParent();
					if (path.length()>20) {
						path = path.substring(0, 17)+"...";
					}
					s = f.getName()+" ["+path+"]";
					fmenu.add(makeMenuItem(i+" "+s, "LOAD_GRAPH"+graph, (i+"").charAt(0)));
				} else {					
					if (s.startsWith("R Object: ")) {
						s = s.substring(10);
						if (RControl.getR().eval("exists(\""+s+"\")").asRLogical().getData()[0]) {
							fmenu.add(makeMenuItem(i+" "+s, "LOAD_GRAPH"+graph, (i+"").charAt(0)));
						}
					}
				}				
			}
			fmenu.addSeparator();
		}		
		fmenu.add(makeMenuItem("Exit", "exit", KeyEvent.VK_X));
	}

	private JMenuItem makeMenuItem(String text, String action, int key) {
		JMenuItem item = makeMenuItem(text, action);
		item.setMnemonic(key);
		return item;
	}
	
	private JMenuItem makeMenuItem(String text, String action, char key) {
		JMenuItem item = makeMenuItem(text, action);
		item.setMnemonic(key);
		return item;
	}

    public void showAbout() {
        new AboutDialog(control);
    }

    public void showLog() {
        new DetailsDialog(LoggingSystem.getInstance().makeDetailsPanel());
    }
    
    /**
     * Creates an ErrorDialog
     */
    public void reportError() {    	
        ErrorHandler.getInstance().makeErrDialog("Report Error");
    }

	public void actionPerformed(ActionEvent e) {		
        if (e.getActionCommand().equals("save graph to R")) {
        	//if (control.getNL().getNodes().size()==0) 
        	{
        		JOptionPane.showMessageDialog(control, "Will not save empty graph.", "Saving to R failed.", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	//VariableNameDialog vnd = new VariableNameDialog(control, "Dummy");
        	//String name = control.getNL().saveGraph(vnd.getName(), true);        	    	
        	//Configuration.getInstance().getGeneralConfig().addGraph("R Object: ");
        	//createLastUsed();        	
        } else if (e.getActionCommand().equals("load graph from R")) {
        	new RObjectLoadingDialog(control);
        	createLastUsed();        	
        } else if (e.getActionCommand().equals("showLog")) {    	
        	showLog();
        } else if (e.getActionCommand().equals("reportError")) {       	
        	 reportError();
        } else if (e.getActionCommand().equals("exit")) {       	
        	 control.windowClosing(null);
        } else if (e.getActionCommand().equals("showAppHelp")) {
        	showFile("doc/gMCP.pdf");       	 	
        } else if (e.getActionCommand().equals("showParametric")) {
        	showFile("doc/parametric.pdf");       	 	
        } else if (e.getActionCommand().equals("showManual")) {
        	showURL("http://cran.at.r-project.org/web/packages/gMCP/gMCP.pdf");
        } else if (e.getActionCommand().equals("showPaper1")) {
        	showURL("http://onlinelibrary.wiley.com/doi/10.1002/bimj.201000239/full");
        } else if (e.getActionCommand().equals("showReferences")) {
        	showFile("References.html");
        } else if (e.getActionCommand().equals("showEpsDoc")) {
        	showFile("doc/EpsilonEdges.pdf");       	 	
        } else if (e.getActionCommand().equals("showNEWS")) {
        	new TextFileViewer(control, new File(RControl.getR().eval("system.file(\"NEWS\", package=\"gMCP\")").asRChar().getData()[0]));      	 	
        } else if (e.getActionCommand().equals("showAbout")) {
        	new AboutDialog(control);
        } else if (e.getActionCommand().equals("showOptions")) {
        	new OptionsDialog(control);
        } else if (e.getActionCommand().equals("clearOptions")) {
        	Configuration.getInstance().clearConfiguration();
        	control.repaint();
        } else if (e.getActionCommand().equals("debugConsole")) {
        	RControl.console.setVisible(true);
        } 
	}
	
	private void showURL(String url) {
		try {	
			Method main = Class.forName("java.awt.Desktop").getDeclaredMethod("getDesktop");
			Object obj = main.invoke(new Object[0]);
			Method second = obj.getClass().getDeclaredMethod("browse", new Class[] { URI.class }); 
			second.invoke(obj, new URI(url));
		} catch (Exception exc) {			
			logger.warn("No Desktop class in Java 5 or URI error.");
			RControl.getR().eval("browseURL(\""+url+"\")");
			if (System.getProperty("eclipse") != null) exc.printStackTrace();
		}	
	}

	private void notYetSupported() {
		JOptionPane.showMessageDialog(control, "Not yet supported.", "Not yet supported", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showFile(String s) {
		File f = new File(RControl.getR().eval("system.file(\""+s+"\", package=\"gMCP\")").asRChar().getData()[0]);
		if (OSTools.isWindows() && s.indexOf('.') == -1) {
			try {
				f = FileTransfer.copyFile(f, new File(System.getProperty("java.io.tmpdir"), f.getName()+"TXT"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(control, "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
			}
		}		
		if (!f.exists()) {
			throw new RuntimeException("This is strange. The file \""+s+"\" could not be found.");
		} else {
			try {	
				Method main = Class.forName("java.awt.Desktop").getDeclaredMethod("getDesktop");
				Object obj = main.invoke(new Object[0]);
				Method second = obj.getClass().getDeclaredMethod("open", new Class[] { File.class }); 
				second.invoke(obj, f);
			} catch (Exception exc) {			
				logger.warn("No Desktop class in Java 5 or URI error.");
				if (f.getName().toLowerCase().endsWith(".html") || f.getName().toLowerCase().endsWith(".htm")) {
					RControl.getR().eval("browseURL(\"file://"+f.getAbsolutePath().replace('\\', '/')+"\")");
					return;
				}
				try {
					if (OSTools.isWindows()) {
						Process p;							
						p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + f.getAbsolutePath()+"\"");
						/*if (s.indexOf('.') == -1) {
							p = Runtime.getRuntime().exec("wordpad \"" + f.getAbsolutePath()+"\"");
						}*/						
						p.waitFor();
					} else {
						JOptionPane.showMessageDialog(control, "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage(), e1);					
					JOptionPane.showMessageDialog(control, "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
					if (System.getProperty("eclipse") != null) e1.printStackTrace();
				}

			}
		}
	}
	

	public String LATEX_BEGIN_DOCUMENT = "\\documentclass[11pt]{article}\n"+
										 "\\usepackage{tikz}\n"+
										 "\\usetikzlibrary{decorations,arrows,shapes}\n"+
										 "\\begin{document}\n";
	
	public String LATEX_END_DOCUMENT = "\\end{document}";

	

	protected JMenuItem makeMenuItem(String text, String action) {
        return makeMenuItem(text, action, true);
    }

    protected JMenuItem makeMenuItem(String text, String action, boolean enabled) {
        JMenuItem item = new JMenuItem(text);        
        item.setActionCommand(action);
        item.setEnabled(enabled);
        item.addActionListener(this);
        return (item);
    }

	public void loadGraph(String object) {
		// TODO Auto-generated method stub
		
	}

}
