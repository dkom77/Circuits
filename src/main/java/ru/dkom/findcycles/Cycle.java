package ru.dkom.findcycles;


import java.util.*;

public class Cycle{

    private List<Integer> cycle;

    public Cycle(ArrayList<Integer> cycle){
        this.cycle = new ArrayList<>(cycle);
        this.cycle.remove(cycle.size() - 1);

        this.cycle.add(cycle.get(0));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        cycle.forEach(c -> sb.append(c).append(" "));
        return sb.toString().trim();
    }

}
