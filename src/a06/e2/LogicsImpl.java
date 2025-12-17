package a06.e2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class LogicsImpl {

    private final Map<Position, Integer> grid = new LinkedHashMap<>();
    private final List<Position> active = new ArrayList<>();
    private final Set<Position> shown = new HashSet<>();
    
    public LogicsImpl(int size) {
        Random r = new Random();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid.put(new Position(i, j), r.nextInt(6)+1);
            }
        }
    }

    public void hit(Position p) {
        if (this.active.size() == 2) {
            this.active.clear();
        }
        this.active.add(p);
        if (this.active.size() == 2 && 
            this.grid.get(this.active.get(0)).equals(this.grid.get(this.active.get(1)))) {
                this.shown.add(this.active.get(0));
                this.shown.add(this.active.get(1));
        }
    }

    public boolean toQuit() {
        List<Integer> notShown = this.grid.keySet().stream().filter(pos -> !this.shown.contains(pos)).map(this.grid::get).toList();
        return notShown.stream().distinct().count() == notShown.size();
    }

    public Optional<Integer> found(Position p) {
        return Optional.of(p).filter(pp -> this.shown.contains(pp)).map(this.grid::get);
    }

    public Optional<Integer> temporary(Position p) {
        return Optional.of(p).filter(pp -> this.active.contains(pp)).map(this.grid::get);
    }
    
}