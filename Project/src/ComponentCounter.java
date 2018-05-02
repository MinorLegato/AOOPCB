import java.util.*;

// holds the name of the compoennt and the number of time
// its encountred!
class Counter {
    public final String name;
    public int n = 1;

    public Counter(final String name) { this.name = name; }
}

// a ArrayList of counter objects generated with from the grid class 
public class ComponentCounter extends ArrayList<Counter> {
    public void addComponent(final RenderComponent rc) {
        int i = 0;
        while (i < this.size() && !this.get(i).name.equals(rc.c.toString())) i++;
        if (i == this.size()) {
            this.add(new Counter(rc.c.toString()));
        } else {
            this.get(i).n++;
        }
    }
}

