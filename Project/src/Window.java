import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class Window {
	//private static final long serialVersionUID = 1L;
	
	static Grid workspace;
	static ArrayList<Component> componentList;
	static MenuBar menu;

    // creates the main window of the application
	public static final JFrame createMainFrame() {
		final JFrame win = new JFrame("SACD!");
		win.setSize(1200, 1000);
		workspace = new Grid(200, 200);
		componentList = Component.load();
		menu = new MenuBar(workspace, componentList);
		win.add(menu, BorderLayout.NORTH);
		win.add(workspace, BorderLayout.CENTER);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
		return win;
	}
	
    // clears the grid/workspace!
	public static final void clearGrid(){ workspace.reuse(200, 200); }

    // opens a list of avilable projects
	private static final String[] showFileList() throws FileNotFoundException {
        return Arrays.stream(new File("projects").listFiles()).map(File::getName).toArray(String[]::new);
  	}
	
    // popup for opening files
	public static final void openFilePopup() throws FileNotFoundException{
		final JFrame win = new JFrame("Open file");
		win.setSize(500,110);
		
		final JPanel controllers = new JPanel(new GridLayout(1,2));
		
		final JLabel inst = new JLabel("<html>Select file to open</html>");
		inst.setVisible(true);
	    controllers.add(inst);

		final String[] choices = showFileList();
		
	    final JComboBox<String> cb = new JComboBox<String>(choices);
	    cb.setVisible(true);
	    controllers.add(cb);
	    
	    win.add(controllers, BorderLayout.CENTER);
	
	    final JPanel buttons = new JPanel();
	
	    final JButton can = new JButton("Cancel");
	
	    can.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) { 
	            win.dispose();
	        } 
	    });
	
	    buttons.add(can);
	
	    final JButton open = new JButton("Open");
	
	    open.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            try {
	            	int index = cb.getSelectedIndex();
    				clearGrid();
    				ManageProject.load(choices[index], workspace, componentList);
    				menu.setProjName(choices[index].substring(0, choices[index].length()-5));
    			} catch (FileNotFoundException e1) {
    				e1.printStackTrace();
    			}
	        	win.dispose();
	        } 
	    });
	
	    buttons.add(open);
	    win.add(buttons, BorderLayout.SOUTH);
		
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
	}
	
	public static final void shopingListPopup(){
		final JFrame win = new JFrame("Shoping list");
		final ComponentCounter cc = workspace.getComponentList();

		win.setSize(300, cc.size()*20+40);
		win.setResizable(false);

		final JTextArea list = new JTextArea();

		for (int i = 0; i < cc.size(); i++)
		    list.append("" + cc.get(i).n + " : " + cc.get(i).name + "\n");
		
		list.setEditable(false);
		
		win.add(list, BorderLayout.CENTER);
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
	}
	
    // open "save as" popup
	public static final void saveAsPopup(){
		final JFrame win = new JFrame("Save as");
		win.setSize(300, 110);
		
		final JLabel inst = new JLabel("<html>Choose file name</html>");
		final JTextField filename = new JTextField();
		
		final JPanel controllers = new JPanel();

		controllers.setLayout(new GridLayout(1,2));
		controllers.add(inst);
		controllers.add(filename);
		win.add(controllers, BorderLayout.CENTER);
		
		final JPanel buttons = new JPanel();

	    final JButton can = new JButton("Cancel");

	    can.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
                win.dispose();
            } 
        });

	    buttons.add(can);

	    final JButton save = new JButton("Save");

	    save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filename.getText().length() == 0){
                	inst.setText("<html><font color='red'>Choose file name</font></html>");
                }
                else{
                	menu.setProjName(filename.getText());
                	try {
        				ManageProject.save(filename.getText(), workspace);
        			} catch (FileNotFoundException e1) {
        				// TODO Auto-generated catch block
        				e1.printStackTrace();
        			}
                	win.dispose();
                }
            } 
        });

	    buttons.add(save);
	    win.add(buttons, BorderLayout.SOUTH);
		
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
	}
	
    // popup for selecting component
	public static final void selectComponentPopup() {
		final JFrame win = new JFrame("Select component");
		win.setSize(500,110);
		
		final JPanel controllers = new JPanel(new GridLayout(1,2));
		
		final JLabel inst = new JLabel("<html>Select component then press select</html>");
		inst.setVisible(true);
	    controllers.add(inst);

        final String[] choices = (String[]) componentList.stream().map(e -> e.toString()).toArray(String[]::new);

	    final JComboBox<String> cb = new JComboBox<String>(choices);

	    cb.setVisible(true);
	    controllers.add(cb);
	    
	    win.add(controllers, BorderLayout.CENTER);

	    final JPanel  buttons = new JPanel();
	    final JButton can     = new JButton("Cancel");

	    can.addActionListener(e -> {  win.dispose(); });

	    buttons.add(can);

	    final JButton sel = new JButton("Select");

	    sel.addActionListener(e -> {
            final int si = cb.getSelectedIndex();
            workspace.setCurrent(componentList.get(si));
            menu.setComponent(componentList.get(si));
            win.dispose();
        });

	    buttons.add(sel);
	    win.add(buttons, BorderLayout.SOUTH);
		
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
	}

    // create popup for creating components
	public static final void createComponentPopup() {
		final JFrame win = new JFrame("Create new component");

		win.setSize(500, 200);
		
		final JLabel name = new JLabel("<html>Enter a component name</html>");
		final JTextField nameBox = new JTextField();
		final JLabel abbrev = new JLabel("<html>Enter an abbreviation, max 4 characters</html>");
		final JTextField abbrevBox = new JTextField();
		final JLabel shape = new JLabel("<html>Select a shape</html>");

		final String[] choicesShape = {
            "Rectangular Block", 
            "Square Block", 
            "Circle"
        };
		
	    final JComboBox<String> shapeBox = new JComboBox<String>(choicesShape);
	    shapeBox.setVisible(true);
		
	    final JLabel nOfInputs = new JLabel("<html>Select number of pins</html>");

	    final String[] rect = { "2","4","6","8","10","12","14","16","18","20","22","24","26","28","30" };
	    
	    DefaultComboBoxModel<String> re = new DefaultComboBoxModel<String>(rect);
	    
	    final String[] square = { "4","8","12","16","20","24","28","32","36","40", };
	    
	    DefaultComboBoxModel<String> sq = new DefaultComboBoxModel<String>(square);
	    
	    final String[] circ = { "2","3" };
	    
	    DefaultComboBoxModel<String> ci = new DefaultComboBoxModel<String>(circ);
	    
	    final JComboBox<String> nOfInputsBox = new JComboBox<String>(rect);
	    
	    shapeBox.addActionListener( e -> {
	    	if (shapeBox.getSelectedItem().equals("Rectangular Block"))
	    		nOfInputsBox.setModel(re);
			else if (shapeBox.getSelectedItem().equals("Square Block"))
				nOfInputsBox.setModel(sq);
			else
				nOfInputsBox.setModel(ci);
	    });
	    
	    JLabel color = new JLabel("<html>Select a color</html>");
		final Color[] colors = {
            Color.BLUE,     Color.CYAN,		Color.GREEN,    
            Color.MAGENTA,  Color.ORANGE,	Color.PINK,     
            Color.RED,      Color.YELLOW
        };

		final String[] choicesCol = {
            "BLUE",     "CYAN",		"GREEN",    
            "MAGENTA",  "ORANGE",	"PINK",     
            "RED",      "YELLOW"
        };
		
	    final JComboBox<String> colorBox = new JComboBox<String>(choicesCol);
	    colorBox.setVisible(true);
	    
		final JPanel controllers = new JPanel();

		controllers.setLayout(new GridLayout(5,2));
		controllers.add(name);
		controllers.add(nameBox);
		controllers.add(abbrev);
		controllers.add(abbrevBox);
		controllers.add(shape);
		controllers.add(shapeBox);
		controllers.add(nOfInputs);
		controllers.add(nOfInputsBox);
		controllers.add(color);
		controllers.add(colorBox);
		
		win.add(controllers, BorderLayout.CENTER);
			
		final JPanel buttons = new JPanel();

	    final JButton can = new JButton("Cancel");

	    can.addActionListener(e -> { win.dispose(); });

	    buttons.add(can);

	    final JButton create = new JButton("Create");

	    create.addActionListener((e) -> { 
            boolean accepted = true;
            if (nameBox.getText().length() == 0){
                name.setText("<html><font color='red'>Enter a component name</font></html>");
                accepted = false;
            } else
                name.setText("<html>Enter a component name</html>");
            
            if (abbrevBox.getText().length() > 4 || abbrevBox.getText().length() == 0){
                abbrev.setText("<html><font color='red'>Enter an abbreviation, max 4 characters</font></html>");
                accepted = false;
            } else
                abbrev.setText("<html>Enter an abbreviation, max 4 characters</html>");
            
            if (accepted){
                Component comp;
                
                if (shapeBox.getSelectedItem().equals("Rectangular Block")){
                	comp = new Component(
	                		nameBox.getText(),
	                		abbrevBox.getText(),
	                		colors[colorBox.getSelectedIndex()],
	                		Integer.parseInt(nOfInputsBox.getSelectedItem().toString()),
	                		ComponentTypes.BLOCKRECT);
                }
                else if (shapeBox.getSelectedItem().equals("Square Block")){
                	comp = new Component(
                    		nameBox.getText(),
                    		abbrevBox.getText(),
                    		colors[colorBox.getSelectedIndex()],
                    		Integer.parseInt(nOfInputsBox.getSelectedItem().toString()),
                    		ComponentTypes.BLOCKSQUARE);
                }
                else {
                	comp = new Component(
                    		nameBox.getText(),
                    		abbrevBox.getText(),
                    		colors[colorBox.getSelectedIndex()],
                    		Integer.parseInt(nOfInputsBox.getSelectedItem().toString()),
                    		ComponentTypes.CIRCLE);
                }
                componentList.add(comp);
                menu.setComponent(comp);
                componentList.sort((a, b) -> a.name.compareTo(b.name));

                try {
                    Component.save(componentList);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }

                workspace.setCurrent(comp);
                win.dispose();
            }
        });
		   
	    buttons.add(create);
	    win.add(buttons, BorderLayout.SOUTH);
		
		win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		win.setLocationRelativeTo(null);
        win.setVisible(true);
	}

	public static void main(String[] args) throws FileNotFoundException{
		openFilePopup();
	}
	
}

