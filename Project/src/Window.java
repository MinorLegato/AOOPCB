
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public Window(){
		//add(new Grid(), BorderLayout.CENTER);
		
		add(new MenuBar(), BorderLayout.NORTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	
	public void open(){
		setVisible(true);
	}

}
