import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/* Holds all the info about a specific component!
 */
public final class Component implements Visitable {
    public final String name;
    public final String abbreviation;
    public final Color  color;
    public final int pins;
    // type of compoennt (BLOCKRECT/BLOCKSQUARE/CIRCLE)
    public final ComponentTypes ct;

    public Component(final String name, final String abbrev, final Color color, final int pins, final ComponentTypes ct) {
        this.name = name;

        if (abbrev.length() > 5)
        	this.abbreviation = abbrev.substring(0, 5);
        else
        	this.abbreviation = abbrev;
        this.color = color;
        this.pins = pins;
        this.ct = ct;
    }
    
    public final String toString(){
    	return this.name + " : " + this.abbreviation;
    }
    
    public final String accept(Visitor visitor){
    	return visitor.visit(this);
    }
    
    public final ComponentTypes getType(){
    	return ct;
    }

    public final boolean equals(Component c){
    	if (this.name.equals(c.name) && this.abbreviation.equals(c.abbreviation) &&
    			this.color.equals(c.color) && this.ct.equals(c.ct)){
    		return true;
    	}
        return false;
    }
    
    public final String toSave(){
    	return this.name + ":" + this.abbreviation + ":" + this.color.getRGB() + ":" + this.pins + ":" + this.ct;
    }
    
    public final static Component fromLoad(String s) {
    	String[] file = s.split(":");
    	String name = file[0];
    	String abbrev = file[1];
    	Color RGB = new Color(Integer.parseInt(file[2]));
    	int nPins = Integer.parseInt(file[3]);
    	ComponentTypes ct;

    	if (file[4].equalsIgnoreCase("BLOCKRECT")) {
    		ct = ComponentTypes.BLOCKRECT;
        } else if (file[4].equalsIgnoreCase("BLOCKSQUARE")) {
    		ct = ComponentTypes.BLOCKSQUARE;
        } else {
    		ct = ComponentTypes.CIRCLE;
        }
    	
    	return new Component(name, abbrev, RGB, nPins, ct);
    }
    
    public final static void save(ArrayList<Component> cl) throws FileNotFoundException{
    	try (PrintWriter out = new PrintWriter("componentlist/componentlist.txt")){
    		SaveVisitor sv = new SaveVisitor();

    		for (Component c : cl) { out.println(c.accept(sv)); }
    	}
    }
    
    public final static ArrayList<Component> load(){
    	ArrayList<Component> list = new ArrayList<Component>();

		try (Scanner read = new Scanner(new File("componentlist/componentlist.txt"))) {
			while (read.hasNextLine()) { list.add(fromLoad(read.nextLine())); }

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        list.sort((a, b) -> a.name.compareTo(b.name));
    	return list;
    }
    
}
