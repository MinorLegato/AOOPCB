import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import java.util.function.*;

/* grid keeps track of all the graphical components!
 */
public final class Grid extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

    // the different avalible mouse actions
    private enum MouseAction {
        NONE,
        PLACE_WIRE,
        PLACE_COMPONENT,
        PLACE_BOARD,
        MOVE_WIRE,
        MOVE_COMPONENT,
        MOVE_BOARD,
        MOVE_POINTS
    };

    // ========================================= VARS =================================== //

    // enables anti-alias for making the diagonal wires look nice 
    private static final RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // the different colors used for rendering
    public static final Color dotColor            = new Color(0, 0, 0, 150);
    public static final Color wireColor           = new Color(212, 154, 100, 255);
    public static final Color wirePlaceColor      = new Color(255, 0, 0, 100);
    public static final Color componentPlaceColor = new Color(100, 0, 255, 100);
    public static final Color boardColor          = new Color(50, 180, 50, 125);
    public static final Color boardOutlineColor   = new Color(50, 50, 50, 200);

    // pixel width/height between grid dots
	public static final double tileSize = 16;

    // lists that keep track of the location and some behavior of the render components
    private final RenderBoard                   renderBoard         = new RenderBoard();
    private final ArrayList<RenderComponent>    renderComponents    = new ArrayList<>();
    private final ArrayList<RenderWire>         renderWires         = new ArrayList<>();

    // line endings marked when moving the wires or board
    private final ArrayList<Point2D> activePoints = new ArrayList<>();
    // component marked for moving
    private RenderComponent  activeComponent = null;

    // mouse positions!  ( mp = mouse moved,  md = mouse dragged )
    private final Point2D mp = new Point2D.Double(0, 0);
    private final Point2D md = new Point2D.Double(0, 0);

    // which editor mode the user is in
    private Paint paintType = Paint.WIRE;
    // keeps track of what the mouse is doing
    private MouseAction mouseAction = MouseAction.NONE;

    // the currently selected component!
    private Component current = new Component("NIL", "NIL", new Color(255, 0, 255), 6, ComponentTypes.BLOCKRECT);

    private int width;
    private int height;

    // ====================================== CONSTRUCTOR ==================================== //
    
	public Grid(int width, int height) {
		this.width    = width;
        this.height   = height;

        setPreferredSize(new Dimension(width, height));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // ========================================= METHODS ======================================= //
    
    // sets what mode the user want  (WIRE/COMPONENT/BOARD)
    public final void setPaint(Paint paint) { paintType = paint; }
	
    // resets parts of the grid!  used when opening new porjects
    public final void reuse(int width, int height) {
        this.width    = width;
        this.height   = height;

        renderBoard.clear();
        renderComponents.clear();
        renderWires.clear();

        current = null;
        activeComponent = null;
    }

    // sets the current component!
    public final void setCurrent(final Component c) {
        current = c;
    }

    // handles all the rendering of the board
    public final void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        // enable AA
        g2.setRenderingHints(hints);
        // render board
        renderBoard.render(g2);
        // render components
        for (final RenderComponent rc : renderComponents) { rc.render(g2); }
        // render text:
        for (final RenderComponent rc : renderComponents) { rc.renderText(g2); }
        // render wires
        g2.setColor(wireColor);
        g2.setStroke(new BasicStroke(2));
        for (final RenderWire rw : renderWires) { rw.render(g2); }
        g2.setStroke(new BasicStroke(1));

        // renders the line when placing wires
        if (mouseAction == MouseAction.PLACE_WIRE) {
            Render.drawLine(g2, (Point2D)mp.clone(), (Point2D)md.clone(), wirePlaceColor);
        }

        // renders the rect/cirele when placing a new component
        if (mouseAction == MouseAction.PLACE_COMPONENT) {
            final double minx = mp.getX();
            final double miny = mp.getY();
            final double maxx = md.getX();
            final double maxy = md.getY();
            
            double x;
            double y;
            double w;
            double h;

            if (current.getType() == ComponentTypes.CIRCLE){
                w = 2 * tileSize;
                h = 2 * tileSize;
                x = maxx < minx? minx - w : minx;
                y = maxy < miny? miny - h : miny;

                Render.fillOval(g2, new Point2D.Double(x, y), new Point2D.Double(w, h), componentPlaceColor);
            } else {
                if (current.getType() == ComponentTypes.BLOCKRECT){
                    if (Math.abs(maxx - minx) > Math.abs(maxy - miny)) {
                        w = (double)(current.pins / 2 + 1) * tileSize;
                        h = 3 * tileSize;
                    } else {
                        h = (double)(current.pins / 2 + 1) * tileSize;
                        w = 3 * tileSize;
                    }
                } else {
                    w = (double)(current.pins / 4 + 1) * tileSize;
                    h = (double)(current.pins / 4 + 1) * tileSize;
                }

                x = maxx < minx? minx - w : minx;
                y = maxy < miny? miny - h : miny;

                Render.fillRect(g2, new Point2D.Double(x, y), new Point2D.Double(w, h), componentPlaceColor);
            }
        }
        
        // draw dots!
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Render.drawPoint(g2, x * (int)Grid.tileSize, y * (int)Grid.tileSize, dotColor);
            }
        }
    }

    public final void mouseMoved(final MouseEvent e) {
        setSnappedMousePos(e, mp);
    }

    // updates the dragged position and moves the active wire ends or component
    public final void mouseDragged(final MouseEvent e) {
        setSnappedMousePos(e, md);

        if (mouseAction == MouseAction.MOVE_POINTS) {
            activePoints.forEach(p -> p.setLocation((Point2D)md.clone()));
        }

        if (mouseAction == MouseAction.MOVE_COMPONENT) {
            double oldx = activeComponent.x;
            double oldy = activeComponent.y;

            activeComponent.x = md.getX() - Math.round((0.5 * activeComponent.getWidth())  / tileSize) * tileSize;
            activeComponent.y = md.getY() - Math.round((0.5 * activeComponent.getHeight()) / tileSize) * tileSize;

            if (renderComponents.stream().anyMatch(c -> c != activeComponent && c.intersects(activeComponent))) {
                activeComponent.x = oldx;
                activeComponent.y = oldy;
            }
        }
    }

    // sets up the corrent mouseAction depending paintMode and whats being pressed!
    // will also delete a component from current mode if right clicked on
    public final void mousePressed(final MouseEvent e) {
        setSnappedMousePos(e, md);

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (paintType == Paint.WIRE) {
                mouseAction = MouseAction.PLACE_WIRE;

                if (e.getClickCount() == 2) {
                    for (RenderWire rw : renderWires) {
                        if (mp.equals(rw.getP1())) { mouseAction = MouseAction.MOVE_POINTS; activePoints.add(rw.getP1()); }
                        if (mp.equals(rw.getP2())) { mouseAction = MouseAction.MOVE_POINTS; activePoints.add(rw.getP2()); }
                    }
                }
            }

            if (paintType == Paint.COMPONENT) {
                mouseAction = MouseAction.PLACE_COMPONENT;

                if (e.getClickCount() == 2) {
                    for (RenderComponent rc : renderComponents) {
                        if (rc.contains(new Point2D.Double(e.getX(), e.getY()))) {
                            rc.rotate();
                            break;
                        }
                    }
                } else {
                    for (RenderComponent rc : renderComponents) {
                        if (rc.contains(mp)) {
                            mouseAction = MouseAction.MOVE_COMPONENT;
                            activeComponent = rc;
                            break;
                        }
                    }
                }
            }

            if (paintType == Paint.BOARD) {
                mouseAction = MouseAction.PLACE_BOARD;

                for (final Point2D p : renderBoard) {
                    if (mp.equals(p)) { mouseAction = MouseAction.MOVE_POINTS; activePoints.add(p); }
                }
            }
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            if (paintType == Paint.WIRE)        { renderWires.removeIf(rw -> rw.getP1().equals(mp) || rw.getP2().equals(mp)); }
            if (paintType == Paint.COMPONENT)   { renderComponents.removeIf(rc -> rc.contains(mp)); }
            if (paintType == Paint.BOARD)       { renderBoard.removeIf(p -> p.equals(mp)); }
        }
    }

    // checks what mouseAction is active from the key press and handles it
    // tex. adding the component/wire to the grid
    public final void mouseReleased(MouseEvent e) {

        // add wire to board
        if (mouseAction == MouseAction.PLACE_WIRE) {
            if (mp.distance(md) >= Grid.tileSize) {
                final RenderWire rw = new RenderWire((Point2D)mp.clone(), (Point2D)md.clone());
                if (renderWires.stream().noneMatch(w -> w.intersectsLine(rw))) {
                    renderWires.add(rw);
                }
            }
        }

        // add component to board
        if (mouseAction == MouseAction.PLACE_COMPONENT) {
            if (mp.distance(md) > 5) {
                final double minx = mp.getX();
                final double miny = mp.getY();
                final double maxx = md.getX();
                final double maxy = md.getY();
                
                double x;
                double y;
                double w;
                double h;

                if (current.getType() == ComponentTypes.CIRCLE) {
                    w = 2 * tileSize;
                    h = 2 * tileSize;
                    x = maxx < minx? minx - w : minx;
                    y = maxy < miny? miny - h : miny;
                    w = x + w;
                    h = y + h;

                    final CircleComponent bc = new CircleComponent(current, new Point2D.Double(x, y), new Point2D.Double(w, h));

                    if (renderComponents.stream().noneMatch(b -> bc.intersects(b))) {
                        renderComponents.add(bc);
                    }
                } else {
                    if (current.getType() == ComponentTypes.BLOCKRECT) {
                        if (Math.abs(maxx - minx) > Math.abs(maxy - miny)) {
                            w = (double)(current.pins / 2 + 1) * tileSize;
                            h = 3 * tileSize;
                        } else {
                            h = (double)(current.pins / 2 + 1) * tileSize;
                            w = 3 * tileSize;
                        }

                    } else {
                        w = (double)(current.pins / 4 + 1) * tileSize;
                        h = (double)(current.pins / 4 + 1) * tileSize;
                    }

                    x = maxx < minx? minx - w : minx;
                    y = maxy < miny? miny - h : miny;
                    w = x + w;
                    h = y + h;

                    final BlockComponent bc = new BlockComponent(current, new Point2D.Double(x, y), new Point2D.Double(w, h));

                    if (renderComponents.stream().noneMatch(b -> bc.intersects(b))) {
                        renderComponents.add(bc);
                    }
                }
            }
        }

        // add new point to board polygon
        if (mouseAction == MouseAction.PLACE_BOARD) {
            final Optional<Point2D> o = renderBoard.stream().min((a, b) -> (int)(a.distance(md) - b.distance(md)));
            renderBoard.add(!o.isPresent()? 0 : renderBoard.indexOf(o.get()), (Point2D)md.clone());
        }

        // update new point positions if no connections intersect
        if (mouseAction == MouseAction.MOVE_POINTS) {
            for (int i = 0; i < renderWires.size(); i++) {
                for (int j = 0; j < renderWires.size(); j++) {
                    if (i == j) { continue; }
                    final RenderWire a = renderWires.get(i);
                    final RenderWire b = renderWires.get(j);

                    if (a.intersectsLine(b)) {
                        for (Point2D p : activePoints) {
                            p.setLocation(mp);
                        }
                    }
                }
            }
        }

        // makes sure everything is ready for the next mousePress
        setSnappedMousePos(e, mp);
        setSnappedMousePos(e, md);
        activeComponent = null;
        mouseAction = MouseAction.NONE;
        activePoints.clear();
    }

    public final void mouseEntered(MouseEvent e)  { }
    public final void mouseExited(MouseEvent e)   { }
    public final void mouseClicked(MouseEvent e)  { }

    // sets the mouse location in p! and snapps it to the grid!
    private void setSnappedMousePos(final MouseEvent e, final Point2D p) {
        p.setLocation(
                Math.round(e.getX() / tileSize) * tileSize,
                Math.round(e.getY() / tileSize) * tileSize);
    }

    // some getters!
    public final Component getActive(){ return current; }

    public final RenderBoard                getRenderBoard()        { return renderBoard; }
    public final ArrayList<RenderWire>      getRenderWires()        { return renderWires; }
    public final ArrayList<RenderComponent> getRenderComponents()   { return renderComponents; }

    // gets the list of used components!
    public final ComponentCounter getComponentList() {
        final ComponentCounter cc = new ComponentCounter();

        for (RenderComponent rc : renderComponents) {
            cc.addComponent(rc);
        }

        return cc;
    }

}

