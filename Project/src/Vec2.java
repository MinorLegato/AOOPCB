import java.awt.geom.*;

public final class Vec2 {
    public static final Vec2 add(final Point2D.Double a, final Point2D.Double b) {
        return new Point2D.Double(a.x + b.x, a.y + b.y);
    }

    public static final Vec2 sub(final Point2D.Double a, final Point2D.Double b) {
        return new Point2D.Double(a.x - b.x, a.y - b.y);
    }

    public static final Vec2 mul(final Point2D.Double a, final double s) {
        return new Point2D.Double(a.x * s, a.y * s);
    }

    public static final Vec2 div(final Point2D.Double a, final double s) {
        return new Point2D.Double(a.x / s, a.y / s);
    }
}

