import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.geom.*;
import java.util.Arrays;

public final class ManageProject {

	/*
	 * Uses the SaveVisitor to save a project.
	 * Structures the projectfile
	 * Input: filename, Grid
	 */
	public static final void save(final String filename, final Grid workspace) throws FileNotFoundException {
		try (final PrintWriter out = new PrintWriter("projects/" + filename + ".proj")) {
    		final SaveVisitor sv = new SaveVisitor();
    		out.println("BOARD");
    		String board = workspace.getRenderBoard().accept(sv);

	    	if (board.length() > 0) {
                Arrays.stream(board.split(";")).forEach(out::println);
	    	}

    		out.println("WIRES"); 
            workspace.getRenderWires().forEach(rw -> out.println(rw.accept(sv)));
	    	out.println("COMPONENTS");
            workspace.getRenderComponents().forEach(rc -> out.println(rc.accept(sv)));
    	}		
	}

	/*
	 * Reads the projectfile
	 * Sets up grid to open the project
	 * Input: Grid, ArrayList of Components
	 */
	public static final void load(
            final String filename, final Grid workspace, final ArrayList<Component> cl) throws FileNotFoundException {
		final Scanner read    = new Scanner(new File("projects/"+filename));
	    String temp     = read.nextLine();
		temp            = read.nextLine();
		final RenderBoard rb  = workspace.getRenderBoard();

		while (!temp.equals("WIRES")) {
			temp.replaceAll(" ", ":");
			final String[] split = temp.split(":");
			rb.add(new Point2D.Double(
                        Double.parseDouble(split[0]),
						Double.parseDouble(split[1])));
			temp = read.nextLine();
		}

		temp = read.nextLine();

		ArrayList<RenderWire> rw = workspace.getRenderWires();

		while (!temp.equals("COMPONENTS")){
			final String[] split = temp.split(":");
			rw.add(new RenderWire(
					new Point2D.Double(
                        Double.parseDouble(split[0]),
						Double.parseDouble(split[1])),
					new Point2D.Double(
                        Double.parseDouble(split[2]),
						Double.parseDouble(split[3]))
					));
			temp = read.nextLine();
		}

		ArrayList<RenderComponent> rc = workspace.getRenderComponents();

		while (read.hasNextLine()) {
			temp = read.nextLine();
			final String[] split = temp.split(":");
	    	String name = split[0];
	    	String abbrev = split[1];
	    	Color RGB = new Color(Integer.parseInt(split[2]));

	    	int nPins = Integer.parseInt(split[3]);
	    	ComponentTypes ct;

	    	if (split[4].equalsIgnoreCase("BLOCKRECT"))
	    		ct = ComponentTypes.BLOCKRECT;
	    	else if (split[4].equalsIgnoreCase("BLOCKSQUARE"))
	    		ct = ComponentTypes.BLOCKSQUARE;
	    	else
	    		ct = ComponentTypes.CIRCLE;
	    	
	    	Component c = new Component(name, abbrev, RGB, nPins, ct);

			int index = 0;
	    	boolean noMatch = true;

	    	for (Component o : cl){ 
	    		if (o.equals(c)){
	    			noMatch = false;
	    			break;
	    		}
	    		index++;
	    	}

	    	if (noMatch){
	    		cl.add(c);
	    		index++;
	    	}
	    		    	
	    	if (ct == ComponentTypes.CIRCLE) {
	    		rc.add(new CircleComponent(
		    			cl.get(index), 
		    			new Point2D.Double(
                            Double.parseDouble(split[5]),
		    				Double.parseDouble(split[6])), 
		    			new Point2D.Double(
                            Double.parseDouble(split[7]),
		    				Double.parseDouble(split[8]))
		    			));
	    	} else {
		    	rc.add(new BlockComponent(
		    			cl.get(index), 
		    			new Point2D.Double(
                            Double.parseDouble(split[5]),
		    				Double.parseDouble(split[6])), 
		    			new Point2D.Double(
                            Double.parseDouble(split[7]),
		    				Double.parseDouble(split[8]))
		    			));	
	    	}
		}
		cl.sort((a, b) -> a.name.compareTo(b.name));
		read.close();
	}
	
}
