
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.*;

public final class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private final Grid workspace;
	private final ArrayList<Component> componentList;
	private final JTextField activeNow;
	private final JButton wireBut;
	private final JButton compBut;
	private final JButton boardBut;

	private String projName = null;

	/*
	 * Sets up and returns the menubar for the mainframe
	 * Adds all the elements and all the functionality of the menu
	 */
	public MenuBar(final Grid g, final ArrayList<Component> cl){
		workspace = g;
		componentList = cl;

		JMenu fileMenu = new JMenu("File");
		JMenuItem newFile  = new JMenuItem("new");
		
		activeNow = new JTextField();
		activeNow.setEditable(false);
		activeNow.setBorder(null);
		activeNow.setFont(new Font("Default", Font.BOLD, 20));
		activeNow.setForeground(Color.GRAY);
		setActiveNow("WIRE");
		
		wireBut = new JButton();
		wireBut.setSize(10, 10);
		wireBut.setIcon(new ImageIcon("buttonTex/wires.png"));
		wireBut.setRolloverIcon(new ImageIcon("buttonTex/wirero.png"));
		wireBut.setBorderPainted(false);
		wireBut.setContentAreaFilled(false);
		
		compBut = new JButton();
		compBut.setSize(10, 10);
		compBut.setBorderPainted(false);
		compBut.setContentAreaFilled(false);
		compBut.setIcon(new ImageIcon("buttonTex/comp.png"));
		compBut.setRolloverIcon(new ImageIcon("buttonTex/compro.png"));
		
		boardBut = new JButton();
		boardBut.setSize(10, 10);
		boardBut.setBorderPainted(false);
		boardBut.setContentAreaFilled(false);
		boardBut.setIcon(new ImageIcon("buttonTex/board.png"));
		boardBut.setRolloverIcon(new ImageIcon("buttonTex/boardro.png"));
		
		wireBut.addActionListener(e -> { 
			setWire();
		});
		
		compBut.addActionListener(e -> { 
			if (workspace.getActive() == null)
				Window.selectComponentPopup();
			else
				setComponent(workspace.getActive());
		});
		
		boardBut.addActionListener(e -> { 
			setBoard();
		});

		newFile.addActionListener(e -> {
			Window.clearGrid();
		    projName = null;
		});

		fileMenu.add(newFile);
		JMenuItem openFile = new JMenuItem("open");

        openFile.addActionListener(e -> { 
        	try{
        		Window.openFilePopup(); 
        	} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });

		fileMenu.add(openFile);
		fileMenu.addSeparator();
		JMenuItem saveFile = new JMenuItem("save");

		saveFile.addActionListener(e -> {
		    if (projName != null) {
		    	try {
					ManageProject.save(projName, workspace);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } else {
		        Window.saveAsPopup();
		    }
		});

		fileMenu.add(saveFile);
		final JMenuItem saveAsFile = new JMenuItem("save as");

		saveAsFile.addActionListener(e -> { Window.saveAsPopup(); });

		fileMenu.add(saveAsFile);
		fileMenu.addSeparator();
		final JMenuItem shopingList = new JMenuItem("shoping list");
		
		shopingList.addActionListener(e -> {
			Window.shopingListPopup();
		});
		
		fileMenu.add(shopingList);
		add(fileMenu);
		
		final JMenu compMenu = new JMenu("Draw");
		final JMenuItem addWire = new JMenuItem("wire");

        addWire.addActionListener(e -> {
			setWire();
		});

		compMenu.add(addWire);
		compMenu.addSeparator();
		final JMenuItem addComp = new JMenuItem("component");
       
        addComp.addActionListener(e -> { Window.selectComponentPopup(); });

		compMenu.add(addComp);
		final JMenuItem addCustComp = new JMenuItem("create component");
        
		addCustComp.addActionListener(e -> { Window.createComponentPopup(); });

		compMenu.add(addCustComp);
		compMenu.addSeparator();

		final JMenuItem addBoard = new JMenuItem("board");

        addBoard.addActionListener(e -> {  setBoard(); });
        
		compMenu.add(addBoard);
		add(compMenu);
		
		add(wireBut);
		add(compBut);
		add(boardBut);
		add(activeNow);
	}
	
	// Sets the name for the current project
	public final void setProjName(String s){
		projName = s;
	}
	
	// Changes the "Active now" textfield
	public final void setActiveNow(String s){
		s = " ACTIVE NOW: " + s + " ";
		Font f = activeNow.getFont();

		final AffineTransform   atf = new AffineTransform(); 
		final FontRenderContext frc = new FontRenderContext(atf, true, true);

		final int x = (int)f.getStringBounds(s, frc).getWidth();

		activeNow.setMaximumSize(new Dimension(x, 25));
		activeNow.setSize(new Dimension(x, 25));
		activeNow.setText(s);
	}
	
	// Changes the visual aspects of the menubar to show that the application is in wire mode
	// Sets the grid mode to wire
	public final void setWire(){
		wireBut.setIcon(new ImageIcon("buttonTex/wires.png"));
		compBut.setIcon(new ImageIcon("buttonTex/comp.png"));
		boardBut.setIcon(new ImageIcon("buttonTex/board.png"));		
		setActiveNow("WIRE");
        workspace.setPaint(Paint.WIRE);

	}
	
	// Changes the visual aspects of the menubar to show that the application is in component mode
	// Sets the grid mode to component
	// input active component
	public final void setComponent(Component c){
		wireBut.setIcon(new ImageIcon("buttonTex/wire.png"));
		compBut.setIcon(new ImageIcon("buttonTex/comps.png"));
		boardBut.setIcon(new ImageIcon("buttonTex/board.png")); 	
		setActiveNow(c.toString());
        workspace.setPaint(Paint.COMPONENT);
	
	}
	
	// Changes the visual aspects of the menubar to show that the application is in board mode
	// Sets the grid mode to board
	public final void setBoard(){
		wireBut.setIcon(new ImageIcon("buttonTex/wire.png"));
		compBut.setIcon(new ImageIcon("buttonTex/comp.png"));
		boardBut.setIcon(new ImageIcon("buttonTex/boards.png"));
		setActiveNow("BOARD OUTLINE");
        workspace.setPaint(Paint.BOARD);
	}
	
}
