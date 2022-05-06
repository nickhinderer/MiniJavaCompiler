package vapor.hw4;

import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.parser.VaporParser;


import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            in = new FileInputStream("tests/translate/vapor/LinkedList.vapor");
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
//            PG pg = new PG();
            List<Graph> functions = createGraph(p);
            System.out.println();
            for (Graph function : functions) {
                new LinearScan().linearScan(function);
                new LinearScan().printSpillsAndRegisterMap(function);
            }
//            LinearScan.LS.linearScan(functions.get(17));
//            LinearScan.LS.printSpillsAndRegisterMap(functions.get(17));
            new LinearScan().linearScan(functions.get(5));
            new LinearScan().printSpillsAndRegisterMap(functions.get(5));
        }

    }
    public static List<Graph> createGraph(VaporProgram p) {
        List<Graph> graphs = new LinkedList<>();
        DUVisitor duv = new DUVisitor();
        for (VFunction F : p.functions) {
            Graph g = new Graph();
            g.function = F;
            Node parameters = new Node(0);
            parameters.params = new ArrayList<>();
            for (VVarRef.Local param : F.params) parameters.addParameter(Variable.variable(param.toString()));
            g.node(parameters);
            for (int i = 0; i < F.body.length; i++) {
                VInstr instr = F.body[i];
                Node node = instr.accept(i + 1, duv);
                g.node(node);
            }

            for (Node node : g.nodes) {
                if (node.num == 0) for (Variable param : node.params) param.firstDef = 0;
                if (node.def == null && node.use.isEmpty()) continue;
                if (node.def != null) if (node.def.firstDef == -1) node.def.firstDef = node.num;
                if (!node.use.isEmpty()) for (Variable use : node.use) use.lastUse = node.num;
            }
            g.collectVariables();
            g.createIntervals();
            graphs.add(g);
            Variable.reset();
        }
        return graphs;
    }

}
