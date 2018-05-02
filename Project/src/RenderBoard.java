import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.util.ArrayList;

public class RenderBoard extends ArrayList<Point2D> implements Visitable {

	public void render(Graphics2D g){
		int[] x = new int[size()];
		int[] y = new int[size()];
		
		for (int i = 0; i < size(); i++){
			x[i] = (int)get(i).getX();
			y[i] = (int)get(i).getY();
		}
		
		g.setColor(Grid.boardColor);
		g.fillPolygon(x, y, x.length);

		g.setColor(Grid.boardOutlineColor);
		g.drawPolygon(x, y, x.length);
	}
	
	public String accept(Visitor visitor){
	    return visitor.visit(this);
	}
	
}

