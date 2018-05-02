import java.awt.*;
import java.awt.geom.*;

public final class Render {

    private static final Rectangle2D r2 = new Rectangle2D.Double(0, 0, 1, 1);

    public static void drawPoint(final Graphics2D g, final Point2D p, final Color color) {
        drawPoint(g, (int)p.getX(), (int)p.getY(), color);
    }

    public static void drawPoint(final Graphics2D g, int x, int y, final Color color) {
        g.setColor(color);
        g.draw(new Rectangle2D.Double((double)x - 0.5, (double)y - 0.5, 1, 1));
    }

    public static void drawLine(final Graphics2D g, final Point2D a, final Point2D b, final Color color) {
        g.setColor(color);
        g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
    }

    public static void fillOval(final Graphics2D g, final Point2D topLeft, final Point2D size, final Color color) {
        g.setColor(color);
        g.fillOval((int)topLeft.getX(), (int)topLeft.getY(), (int)size.getX(), (int)size.getY());
    }

    public static void drawOval(final Graphics2D g, final Point2D topLeft, final Point2D size, final Color color) {
        g.setColor(color);
        g.fillOval((int)topLeft.getX(), (int)topLeft.getY(), (int)size.getX(), (int)size.getY());
    }

    public static void fillRect(final Graphics2D g, final Point2D topLeft, final Point2D size, final Color color) {
        g.setColor(color);
        g.fillRect((int)topLeft.getX(), (int)topLeft.getY(), (int)size.getX(), (int)size.getY());
    }

    public static void drawRect(final Graphics2D g, final Point2D topLeft, final Point2D size, final Color color) {
        g.setColor(color);
        g.drawRect((int)topLeft.getX(), (int)topLeft.getY(), (int)size.getX(), (int)size.getY());
    }

}
