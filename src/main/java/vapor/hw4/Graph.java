package vapor.hw4;

import cs132.vapor.ast.VFunction;
import cs132.vapor.ast.VInstr;
//import Node;
//import Register;
//import Variable;

import java.util.*;
import vapor.hw4.Register.*;

import static vapor.hw4.Register.*;

//import static Register.*;
//import static Register.$t7;

public class Graph {
    List<Node> nodes;
    Set<Variable> variables;
    List<Variable.Interval> intervals;
//    List<Graph.Edge> edges;
    VFunction function;
    List<Spill> spills;

    Graph() {
        nodes = new ArrayList<>();
        variables = new HashSet<>();
        intervals = new ArrayList<>();
//        edges = new ArrayList<>();
        spills = new ArrayList<>();
    }

    void variable(Variable v) {
        variables.add(v);
        intervals.add(v.interval);
    }

    void node(Node n) { //add node
        nodes.add(n);
    }

//    void edge(Node from, Node to) {
//        Graph.Edge e = new Graph.Edge(from, to);
//        edges.add(e);
//    }

    Node node(int time) {
        for (Node node : nodes) {
            if (node.num == time) return node;
        }
        return null;
    }

    void collectVariables() {
        for (Node n : nodes) {
//            this.variables.addAll(n.variables);
            for (Variable v : n.variables) {
                if (v.lastUse != -1) this.variables.add(v);
            }
        }
    }

    void createIntervals() {
        for (Variable v : variables) {
            Variable.Interval i = v.interval(true);
            intervals.add(i);
        }
    }

//    static class Edge {
//        Node from;
//        Node to;
//
//        Edge(Node from, Node to) {
//            this.from = from;
//            this.to = to;
//            from.successors.add(to);
//            to.predecessors.add(from);
//        }
//    }
}
class Active {
    Variable.Interval[] active;
    int length;

    Active() {
        active = new Variable.Interval[1];
        length = 0;
    }


    public void expire(Variable.Interval interval) {
        int index = 0;
        for (Variable.Interval i : active) {
            if (interval.variable.name.equals(i.variable.name)) break;
            index++;
        }
        expire(index);
    }

    public void expire(int index) {
        active[index] = null;
        ArrayList<Variable.Interval> unexpired = new ArrayList<>();
//        int size = 0;
        for (Variable.Interval j : active) {
            if (j != null) {
//                size++;
                unexpired.add(j);
            }
        }
        active = new Variable.Interval[Math.max(1, unexpired.size())];
        active = unexpired.toArray(active);
        length = active.length;
    }

    void insert(Variable.Interval interval) {
        if (length == 0 || active[0] == null) {
            active[0] = interval;
            length = 1;
            return;
        }
        active = Arrays.copyOf(active, active.length + 1);

        int end = interval.end, index;
//        int end = interval.end.num, index;

        for (index = 0; index < length; index++) {
            if (end < active[index].end)
//                if (end < active[index].end.num)

                break;
        }
        length++;
        if (length == 2) {
            if (index == 1) active[index] = interval;
            else {
                active[1] = active[0];
                active[0] = interval;
            }
            return;
        }
        System.arraycopy(active, index, active, index + 1, active.length - index - 1);
        active[index] = interval;

    }

    public Variable.Interval get(int index) {
        return active[index];
    }


}

class Variable {
    String name;
    Variable.Interval interval;
    int firstDef;
    int lastUse;
    static Dictionary<String, Variable> dict = new Hashtable<>();


    public static Variable variable(String id) {
        String u = id.intern();
        Variable variable = dict.get(u);
        if (variable == null) {
            variable = new Variable(u);
            dict.put(u, variable);
        }
        return variable;
    }

    public static void reset() {
        dict = new Hashtable<>();
    }

    private Variable(String name) {
        this.name = name;
        firstDef = -1;
        lastUse = -1;
    }

    static class Interval {
        Variable variable;

//        Interval(Variable variable, int start, int end) {
//            this.variable = variable;
//            this.start = start;
//            this.end = end;
//        }

        int start;
        int end;

        Interval(Variable variable) {
            this.variable = variable;
            start = -1;
            end = -1;
        }

        Interval(Variable variable, int start) {
            this.variable = variable;
            this.start = start;
            this.end = -1;
        }

        public Interval(Variable variable, int firstDef, int lastUse) {
            this.variable = variable;
            this.start = firstDef;
            this.end = lastUse;
        }
    }

    void interval(int start, int end) {
        this.interval = new Variable.Interval(this, start, end);
        interval.start = start;
        interval.end = end;
    }

    void interval(int start) {
        this.interval = new Variable.Interval(this, start);
    }

    void interval() {
        this.interval = new Variable.Interval(this, firstDef, lastUse);
    }

    Variable.Interval interval(boolean differentiate) {
        this.interval = new Variable.Interval(this, firstDef, lastUse);
        return interval;
    }

}

class Node {
    int num;
    List<Node> predecessors;
    List<Node> successors;
    Variable def;
    List<Variable> params; //even though 99% of the time it is only one def in a statement, you have to do this rather than 'Variable def' so that you can cover the case of parameters which are 'defined' (live) going into the funct              , or do that
    List<Variable> use;
    List<Variable> variables;
    Record record;
    VInstr instruction;

    Node(int num) {
        this.num = num;
        this.predecessors = new ArrayList<>();
        this.successors = new ArrayList<>();
        this.use = new ArrayList<>();
        this.variables = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(String.format("node %d\n", this.num + 1));
        this.record.registers.forEach((k, v) -> {
            if (v != null) output.append(String.format("Variable: \033[;34m%s\033[0m -> \033[;31m%s\033[0m\n", k, v));
        });
        output.append("Free Registers: [");
        for (int i = 0; i < this.record.free.size(); i++) {
            Register r = this.record.free.get(i);
            if (i == this.record.free.size() - 1) output.append(String.format("\033[;32m%s\033[0m", r));
            else output.append(String.format("\033[;32m%s\033[0m, ", r));
        }
        output.append("]\n\n");
        return output.toString();
    }

    void addParameter(Variable variable) {
        this.params.add(variable);
        this.variables.add(variable);
    }

    void addUse(Variable variable) {
        this.use.add(variable);
        this.variables.add(variable);
    }

    void addDef(Variable variable) {
        this.def = variable;
        this.variables.add(variable);
    }
}

class Registers {
    HashMap<Variable, Register> registerMap;
    List<Register> free;
    List<Register> used;

    Registers() {
        registerMap = new HashMap<>();
        free = new ArrayList<>(Arrays.asList($s0, $s1, $s2, $s3, $s4, $s5, $s6, $s7, $t0, $t1, $t2, $t3, $t4, $t5, $t6, $t7));
        used = new ArrayList<>();
    }

    void add(Variable.Interval i) {
        Register r = free.remove(0);
        used.add(r);
        registerMap.put(i.variable, r);
    }

    void remove(Variable.Interval i) {
        Register r = registerMap.remove(i.variable);
        free.add(r);
        used.remove(r);
    }

    Record record() {
        Record record = new Record();
        registerMap.forEach((variable, register) -> {
            record.registers.put(String.valueOf(variable.name), register);
        });
        record.free.addAll(free);
//        record.registers = (Map<Variable, Register>) registerMap.clone(); //as long as vars/nodes/registers are unchanged this is good.
        return record;
    }
}

class Record {
    Map<String, Register> registers;
    List<Register> free;
    Spill spill;

    //    List<Spill> spills;
    Record() {
        registers = new HashMap<>();
        free = new LinkedList<>();
        spill = null;
    }
}

class Spill {
    Node backupPoint;
//    Node restorePoint;  //this is found by just visiting the node and realizing you have some variables in there and they are not all mapped to a register, in that case
    // you just search trough the spills for your variable and find its stack location and restore it using the temp v1 register
//du pairs, find last use then go backwards to first def
    //and then if not in register map, just look at spill map instead (i.e. you don't really need 'restorePoint', that will just be in teh list of spills and
    // you will know to look there because it isn't in the register record for that node, so it was spilled earlier)
    Variable variable;
    int location; //stack location

    public Spill(Variable.Interval i, Node backupPoint) {
        this.backupPoint = backupPoint;
        this.variable = i.variable;
    }

    @Override
    public String toString() {
        return String.format("Spilled Variable: \033[;34m%s\033[0m at node/instruction \033[;31m%d\033[0m at local[\033[;32m%d\033[0m]\n\n", this.variable.name, this.backupPoint.num, this.location);
    }
}