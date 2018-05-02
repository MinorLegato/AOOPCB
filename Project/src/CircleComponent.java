import java.awt.*;
import java.awt.geom.*;

// handles the rendering of Circular shaped components!
public class CircleComponent extends RenderComponent {
    // keeps track of the pin locations during rotations
    private boolean pinN;
    private boolean pinE;
    private boolean pinS;
    private boolean pinW;

    public CircleComponent(final Component c, final Point2D min, final Point2D max) { 
        super(c, min, max); 
        pinE = true;
        pinW = true;
        pinS = false;

        pinN = c.pins == 3;
    }

    // rotates the pins clockwise
    @Override
    public void rotate() {
        boolean temp = pinN;
        pinN = pinW;
        pinW = pinS;
        pinS = pinE;
        pinE = temp;
    }

    // renders the component according to rotation
    @Override
    public void render(Graphics2D g) {
        double x = getX();
        double y = getY();
        double w = getWidth();
        double h = getHeight();
        
        Render.fillOval(g, new Point2D.Double(x, y), new Point2D.Double(w, h), c.color);


        if (pinN){
            Render.fillRect(
                    g,
                    new Point2D.Double(x+(Grid.tileSize * 0.85), y),
                    new Point2D.Double(Grid.tileSize * 0.5, Grid.tileSize * 0.7), 
                    new Color(212, 154, 100));
        }
        if (pinE){
            Render.fillRect(
                    g,
                    new Point2D.Double(x+(Grid.tileSize * 1.35), y+(Grid.tileSize * 0.85)),
                    new Point2D.Double(Grid.tileSize * 0.7, Grid.tileSize * 0.5), 
                    new Color(212, 154, 100));
        }
        if (pinS){
            Render.fillRect(
                    g,
                    new Point2D.Double(x+(Grid.tileSize * 0.85), y+(Grid.tileSize * 1.35)), 
                    new Point2D.Double(Grid.tileSize * 0.5, Grid.tileSize * 0.7), 
                    new Color(212, 154, 100));
        }
        if (pinW){
            Render.fillRect(
                    g,
                    new Point2D.Double(x, y+(Grid.tileSize * 0.85)),
                    new Point2D.Double(Grid.tileSize * 0.7, Grid.tileSize * 0.5), 
                    new Color(212, 154, 100));
        }
    }

}

