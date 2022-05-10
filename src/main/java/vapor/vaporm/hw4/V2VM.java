package vapor.vaporm.hw4;

import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.parser.VaporParser;


import java.io.*;
import java.util.*;

public class V2VM {

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
        VBuiltIn.Op[] ops = {VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS, VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,};
        boolean allowLocals = true;
//        String[] registers = {"v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "t8",};
        String[] registers = {};
        boolean allowStack = false;

        VaporProgram program;
        try {
            program = VaporParser.run(new InputStreamReader(in), 1, 1, java.util.Arrays.asList(ops), allowLocals, registers, allowStack);
        } catch (ProblemException ex) {
            err.println(ex.getMessage());
            return null;
        }

        return program;
    }

    public static void main(String[] args) {
        InputStream in = null;
        try {
//            in = System.in;
            in = new FileInputStream("tests/translate/vapor/BubbleSort.vapor");

//        } catch (Exception e) {
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        VaporProgram p;
        try {
            p = parseVapor(in, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e);
            throw new RuntimeException(e);

        }
        assert p != null;
        {
            for (VDataSegment segment : p.dataSegments) {
                System.out.println("const " + segment.ident);/*- 1*/
                for (VOperand.Static value : segment.values)
                    System.out.println('\t' + value.toString());
            }


            List<Graph> functions = createGraphs(p);
            for (Graph function : functions) {
                new LinearScan().linearScan(function);
//                new LinearScan().printSpillsAndRegisterMap(function);

                VMFunction functionVM = new VMFunction(function);
                System.out.println();
//                System.out.println(functionVM);
            }
//            LinearScan.LS.linearScan(functions.get(17));
//            LinearScan.LS.printSpillsAndRegisterMap(functions.get(17));
//            new LinearScan().linearScan(functions.get(1));
//            new LinearScan().printSpillsAndRegisterMap(functions.get(1));

        }

    }

    public static List<Graph> createGraphs(VaporProgram p) {
        List<Graph> graphs = new LinkedList<>();
        DUVisitor duv = new DUVisitor();
        for (VFunction F : p.functions) {

            if (F.ident.contains("Element.Equal"))
                System.out.println();
            Graph functionCFG = new Graph();
            functionCFG.original = F;
            vapor.vaporm.hw4.Node parameters = new vapor.vaporm.hw4.Node(0);
            parameters.params = new ArrayList<>();
            for (VVarRef.Local param : F.params) {
                parameters.addParameter(Variable.variable(param.toString()));
            }
            functionCFG.node(parameters);
            for (int i = 0; i < F.body.length; i++) {
                VInstr instr = F.body[i];
                vapor.vaporm.hw4.Node node = instr.accept(i + 1, duv);
                functionCFG.node(node);
            }
            for (Node node : functionCFG.nodes) {
                if (node.num == 10 )
                    System.out.println();
                if (node.num == 0) for (Variable param : node.params) param.firstDef = 0;
                if (node.def == null && node.use.isEmpty()) continue;
                if (node.def != null) if (node.def.firstDef == -1) node.def.firstDef = node.num;
                if (!node.use.isEmpty()) for (Variable use : node.use) use.lastUse = node.num;
            }
            functionCFG.collectVariables();
            functionCFG.createIntervals();
            graphs.add(functionCFG);
            Variable.reset();
        }
        return graphs;
    }

//    static void
}
class Variable {
    String name;
    Interval interval;
    int firstDef;
    int lastUse;
    int spilled; //node index variable was spilled in
    Set<Node> used; //set of nodes used in
    Set<Node> defined; //set of nodes defined in

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


//    @Override
//    public boolean equals(Object other) {
//        if (other instanceof String)
//            return other.equals(this.name);
//        else
//            return super.equals(other);
//            return false;
//        return
//    }
    //not needed because variables in the same function are literally the same object

    static class Interval {
        Variable variable;
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
        this.interval = new Interval(this, start, end);
        interval.start = start;
        interval.end = end;
    }

    void interval(int start) {
        this.interval = new Interval(this, start);
    }

    void interval() {
        this.interval = new Interval(this, firstDef, lastUse);
    }

    Interval interval(boolean differentiate) {
//        if (lastUse == -1)
//            lastUse = firstDef;
        this.interval = new Interval(this, firstDef, lastUse);
        return interval;
    }

}
