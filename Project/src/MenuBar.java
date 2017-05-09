import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MenuBar extends JMenuBar{

	private static final long serialVersionUID = 1L;

	public MenuBar(){
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem("new"));
		fileMenu.add(new JMenuItem("open"));
		fileMenu.add(new JMenuItem("save"));
		fileMenu.add(new JMenuItem("save as"));
		this.add(fileMenu);
		JMenu compMenu = new JMenu("Components");
		JMenuItem addComp = new JMenuItem("add");
		compMenu.add(addComp);
		addComp.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		        JFrame hello = new JFrame("POPUP");
		        hello.setSize(100,75);
		        hello.setDefaultCloseOperation(hello.EXIT_ON_CLOSE);
		        hello.setVisible(true);
		    }
		});
		this.add(compMenu);
	}
	
	
}
