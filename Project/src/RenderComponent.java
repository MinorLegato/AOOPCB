import java.awt.*;
import java.awt.geom.*;

// abstarct class that has data and methods shared between BlockComponent and CircleComponent
public abstract class RenderComponent extends Rectangle2D.Double implements Visitable {
    // reference to the component its suppose to represent
    public final Component c;

    public RenderComponent(final Component c, final Point2D min, final Point2D max) {
        // makes sure the min and max point are correct!
        super(min.getX() < max.getX()? min.getX() : max.getX(),
              min.getY() < max.getY()? min.getY() : max.getY(),
              min.getX() < max.getX()? max.getX() - min.getX() : min.getX() - max.getX(),
              min.getY() < max.getY()? max.getY() - min.getY() : min.getY() - max.getY());

        this.c = c;
    }

    void render(Graphics2D g) {
        g.setColor(c.color);
        g.fill(this);
    }

    // abstarct class that handles the rotation of components
    abstract void rotate();

    // draws abbreviation above the component!
    void renderText(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Default", Font.BOLD, (int)Grid.tileSize));
        g.drawString(c.abbreviation, (int)getX(), (int)getY() - 2);
    }

    // gets the underlying component!
    public final Component getComponent() { return c; }
    
    public final String accept(final Visitor visitor) { return visitor.visit(this); }
}

