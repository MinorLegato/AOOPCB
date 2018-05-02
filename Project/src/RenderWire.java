import java.awt.*;
import java.awt.geom.*;

// class that handles the rendering of wires!
public class RenderWire extends Line2D implements Visitable {
    // edges of the line segment
    private final Point2D a;
    private final Point2D b;

    public RenderWire(final Point2D a, final Point2D b) {
        this.a = a;
        this.b = b;
    }

    public double getX1() { return a.getX(); }
    public double getY1() { return a.getY(); }
    public double getX2() { return b.getX(); }
    public double getY2() { return b.getY(); }

    public void setLine(Point2D a, Point2D b) {
        a.setLocation(a.getX(), a.getY());
        b.setLocation(b.getX(), b.getY());
    }

    public void setLine(double x1, double y1, double x2, double y2) {
        a.setLocation(x1, y1);
        b.setLocation(x2, y2);
    }

    public Rectangle2D getBounds2D() {
         return new Rectangle2D.Double(
                a.getX() < b.getX()? a.getX() : b.getX(),
                a.getY() < b.getY()? a.getY() : b.getY(),
                a.getX() < b.getX()? b.getX() - a.getX() : a.getX() - b.getX(),
                a.getY() < b.getY()? b.getY() - a.getY() : a.getY() - b.getY());
    }

    public final Point2D getP1() { return a; }
    public final Point2D getP2() { return b; }

    public void render(Graphics2D g) {
        g.draw(this);

        g.fillOval((int)getX1() - 3, (int)getY1() - 3, 6, 6);
        g.fillOval((int)getX2() - 3, (int)getY2() - 3, 6, 6);
    }

    public boolean intersectsLine(RenderWire rw) {
        return super.intersectsLine(rw) &&
            !getP1().equals(rw.getP1()) && !getP1().equals(rw.getP2()) &&
            !getP2().equals(rw.getP1()) && !getP2().equals(rw.getP2());
    }

    public String accept(Visitor visitor){
    	return visitor.visit(this);
    }
}

