
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Window extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public Window(){
		add(new Grid(200, 200, 32), BorderLayout.CENTER);
	}
	
	public void open(){
		setVisible(true);
	}

}
