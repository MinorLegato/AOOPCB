import java.awt.geom.*;

public class SaveVisitor implements Visitor{
	/*
	 * Returns all the different save strings needed to save a project
	 * Implements the Visitor interface
	 */

	public String visit(Component c){
	    return c.toSave();
    }
	
 	public String visit(RenderBoard rb){
 		String save = "";
 		for (Point2D v : rb){
 			save += (v.getX()+":"+v.getY()+";");
 		}
 		return save;
 	}
 	
 	public String visit(RenderComponent rc){
 	    return visit(rc.c)+":"+rc.getMinX()+":"+rc.getMinY()+":"+rc.getMaxX()+":"+rc.getMaxY();
 	}
 	
 	public String visit(RenderWire rw){
 		return rw.getX1()+":"+rw.getY1()+":"+rw.getX2()+":"+rw.getY2();
 	}
}
