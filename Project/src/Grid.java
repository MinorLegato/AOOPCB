import javax.swing.*;
import java.awt.*;

public class Grid extends JPanel {
	private static final long serialVersionUID = 1L;

	public Grid() {
        setPreferredSize(new Dimensions(800, 600));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
    }

    public static void main(final String[] args) {
    }

}