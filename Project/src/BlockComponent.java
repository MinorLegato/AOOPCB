import java.awt.*;
import java.awt.geom.*;

// Handles square and rectangular shaped compoennts!
public class BlockComponent extends RenderComponent {
    boolean square;
    boolean horizontal = true;
    boolean vertical   = true;

    public BlockComponent(final Component c, final Point2D min, final Point2D max) { 
        super(c, min, max); 
        square = c.ct == ComponentTypes.BLOCKSQUARE;

        if (!square){
            horizontal = c.pins <= 4? !(getWidth() > getHeight()) : getWidth() > getHeight();
            vertical   = !horizontal;
        }
    }

    @Override
    public final void render(Graphics2D g) {
        final double x = getX();
        final double y = getY();
        final double w = getWidth();
        final double h = getHeight();
        
        Render.fillRect(g, new Point2D.Double(x, y), new Point2D.Double(w, h), c.color);

        final int sx = (int)(x / Grid.tileSize);
        final int sy = (int)(y / Grid.tileSize);
        final int ex = (int)((x + w - 1) / Grid.tileSize);
        final int ey = (int)((y + h - 1) / Grid.tileSize);

        if (horizontal) { horizontalPins(g, sx, ex, ey, y); }
        if (vertical)   { verticalPins(g, sy, ex, ey, x); }
    }

    @Override
    public void rotate() {
        if (!square){
            final double temp = width;
            width       = height;
            height      = temp;
            horizontal  = !horizontal;
            vertical    = !vertical;
        }
    }

    // renders the horizontal pins
    private final void horizontalPins(Graphics2D g, int sx, int ex, int ey, double y){
        for (int i = sx + 1; i <= ex; i++) {
            Render.fillRect(
                    g,
                    new Point2D.Double((i * Grid.tileSize)-(Grid.tileSize * 0.15), y),
                    new Point2D.Double(Grid.tileSize * 0.5, Grid.tileSize * 0.7), 
                    new Color(212, 154, 100));
            Render.fillRect(
                    g,
                    new Point2D.Double((i * Grid.tileSize)-(Grid.tileSize * 0.15), (ey * Grid.tileSize) + (Grid.tileSize * 0.35)), 
                    new Point2D.Double(Grid.tileSize * 0.5, Grid.tileSize * 0.7), 
                    new Color(212, 154, 100));
        }
    }

    // renders the vertical pins
    private final void verticalPins(Graphics2D g, int sy, int ex, int ey, double x){
        for (int i = sy + 1; i <= ey; i++) {
            Render.fillRect(
                    g,
                    new Point2D.Double(x, (i * Grid.tileSize)-(Grid.tileSize * 0.15)),
                    new Point2D.Double(Grid.tileSize * 0.7, Grid.tileSize * 0.5), 
                    new Color(212, 154, 100));
            Render.fillRect(
                    g,
                    new Point2D.Double((ex * Grid.tileSize) + (Grid.tileSize * 0.35), (i * Grid.tileSize)-(Grid.tileSize * 0.15)),
                    new Point2D.Double(Grid.tileSize * 0.7, Grid.tileSize * 0.5), 
                    new Color(212, 154, 100));
        }
    }

}

