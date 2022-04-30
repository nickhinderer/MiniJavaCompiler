package vapor.hw4;

import cs132.vapor.ast.*;

import cs132.util.*;
//import cs132.vapor.*;
import cs132.vapor.parser.*;
//import cs132.vapor.ast.*;
import cs132.vapor.ast.VBuiltIn.Op;
//import cs132.util.ProblemException;
//import cs132.vapor.parser.VaporParser;
//import vapor.parser.ast.VaporParser;
//import cs132.vapor.ast.VaporProgram;
//import cs132.vapor.ast.VBuiltIn.Op;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum REGISTER {
    s0, s1, s2
}

public class vapor {
    public static VaporProgram parseVapor(InputStream in, PrintStream err)
            throws IOException {
        Op[] ops = {
                Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
                Op.PrintIntS, Op.HeapAllocZ, Op.Error,
        };
        boolean allowLocals = true;
        String[] registers = null;
        boolean allowStack = false;

        VaporProgram program;
        try {
            program = VaporParser.run(new InputStreamReader(in), 1, 1,
                    java.util.Arrays.asList(ops),
                    allowLocals, registers, allowStack);
        } catch (ProblemException ex) {
            err.println(ex.getMessage());
            return null;
        }

        return program;
    }

    public static void main(String[] args) {
        InputStream in = null;
        try {
            in = new FileInputStream("LinkedList.vapor");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        VaporProgram p;
        try {
            p = parseVapor(in, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert p != null;
//        System.out.println(p.functions[0].vars[0]);
        Map<String, REGISTER> registerMap;
        Map<String, Integer> spillMap;
        for (VFunction F : p.functions) {
            Graph cf = new Graph(F); //1 make cfg for each function
            Map<String, Interval> intervals = cf.computeIntervals();  //2 compute live intervals
            registerMap = new HashMap<>();
            spillMap = new HashMap<>();
            computeRegistersSpills(intervals, registerMap, spillMap); //3
//            regalloc(F, registerMap, spillMap); //4
//            backupCalleeSavedRegisters(F); //5
            transmuteCalls(p); //6
            transmuteArguments(p); //7
            transmuteSignatures(p); //8
        }

        //spill everywhere
        for (VFunction F : p.functions) {
            spillMap = new HashMap<>();
            String[] vars = F.vars;
            int localSize = vars.length;
            computeMap(F, spillMap);
            regallocSpillEverywhere(F, spillMap);

        }


        //spill everywhere
// 1. compute size of local
/* 2. create map from temp variable to memory (local) array
   3. insert backup and restore (lhs/rhs)
   4. change register names (s0, s1, s2)
   5. update method signatures, argument passing, and method calls (a0, v0, in/out/local, etc.)
* */
    }

    //spill everywhere: dest: t0  src: t1, t2
    private static void regallocSpillEverywhere(VFunction F, Map<String, Integer> spillMap) {
        InstructionVisitor v = new InstructionVisitor();
        ArrayList<VInstr> instructions = new ArrayList<>();
        ArrayList<String> vars = new ArrayList<>(List.of(F.vars));
        int startPos = F.body[0].sourcePos.line;
        String dest, source;
        VMemWrite backup;
        VMemRead restore;
        for (VInstr instr : F.body) {
            dest = null;
            source = null;
            backup = null;
            restore = null;
            switch (instr.accept(v)) {
                case 0: //VAssign
                    VAssign assign = (VAssign) instr;
                    dest = assign.dest.toString();
                    source = assign.source.toString();//, /*backup = null,*/;// restore = null;
                    if (vars.contains(source)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
                        restore = new VMemRead(pos, (VVarRef) assign.source, spill);
                        instructions.add(restore);
                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
                    }
                    instructions.add(assign);
                    System.out.println(dest + " = " + source);
                    if (vars.contains(dest)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
                        backup = new VMemWrite(pos, spill, assign.dest);
                        instructions.add(backup);
//                        backup = new VMemWrite()
                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
                    }
                    System.out.println();
                    break;
                case 1:
                    VCall call = (VCall) instr;
                    dest = call.dest.toString();
                    source = call.addr.toString();
                    if (vars.contains(source)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
                        VAddr.Var<VFunction> addr = (VAddr.Var<VFunction>) call.addr;
//                        VAddr.Var<VFunction> var = addr;
                        restore = new VMemRead(pos, addr.var, spill);
                        instructions.add(restore);
                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
                    }
                    String funcargs = "";
                    boolean first = true;
                    for (VOperand o : call.args) {
                        if (first) {
                            funcargs += o.toString();
                            first = false;
                            continue;
                        }
                        funcargs += ' ' + o.toString();
                    }
                    instructions.add(call);
                    System.out.println(dest + " = call " + source + "(" + funcargs + ")");
                    if (vars.contains(dest)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
                        backup = new VMemWrite(pos, spill, call.dest);
                        instructions.add(backup);
//                        backup = new VMemWrite()
                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
                    }
                    System.out.println();
//                    String dest= assign.dest.toString(), source = assign.source.toString();//, /*backup = null,*/;// restore = null;
//                    VMemWrite backup = null;
//                    VMemRead restore = null;
//                    if (vars.contains(source)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
//                        restore = new VMemRead(pos, (VVarRef) assign.source, spill);
//                        instructions.add(restore);
//                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
//                    }
//                    instructions.add(assign);
//                    System.out.println(dest + " = " + source);
//                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        backup = new VMemWrite(pos, spill, assign.dest);
//                        instructions.add(backup);
//                        backup = new VMemWrite()
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
//                    }
                    break;
                case 2:
                    VBuiltIn builtin = (VBuiltIn) instr;
                    if (builtin.op == Op.Error || builtin.op == Op.PrintIntS)
                        break;
                    dest = builtin.dest.toString();
                    String[] args = new String[builtin.args.length];
                    int i = 0;
                    for (VOperand operand : builtin.args) {
                        args[i] = operand.toString();
                        i++;
                    }
//                    source = op.addr.toString();
                    if (builtin.op != Op.HeapAllocZ) {
                        i = 0;
                        for (String _source : args) {
                            if (vars.contains(_source)) {
                                SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                                VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(_source));
                                restore = new VMemRead(pos, (VVarRef) builtin.args[i], spill);
                                instructions.add(restore);
                                System.out.println(_source + " = local[" + spillMap.get(_source) + "]");
                            }
                            i++;
                        }
                    }
//                    String args = "";
//                    boolean first = true;
//                    for (VOperand o : call.args) {
//                        if (first) {
//                            args += o.toString();
//                            first = false;
//                            continue;
//                        }
//                        args += ' ' + o.toString();
//                    }
                    instructions.add(builtin);
                    String builtinArgs = ""; first = true;
                    for (VOperand operand : builtin.args) {
                        if (first) {
                            builtinArgs += operand.toString();
                            first = false;
                            continue;
                        }
                        builtinArgs += ' ' + operand.toString();
                    }
                    System.out.println(dest + " = " + builtin.op.name + "(" +  builtinArgs + ")");
                    if (vars.contains(dest)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
                        backup = new VMemWrite(pos, spill, builtin.dest);
                        instructions.add(backup);
//                        backup = new VMemWrite()
                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
                    }
                    System.out.println();
                    instructions.add(instr);
                    break;
                case 3:
                    VMemWrite write = (VMemWrite) instr;
                    VMemRef.Global ref = (VMemRef.Global) write.dest;
                    dest = ref.base.toString();
                    source = write.source.toString();//, /*backup = null,*/;// restore = null;
                    if (vars.contains(source)) {
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(source));
                        restore = new VMemRead(pos, (VVarRef) write.source, spill);
                        instructions.add(restore);
                        System.out.println(source + " = local[" + spillMap.get(source) + "]");
                    }
                    if (vars.contains(dest)) {
//                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
//                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
////                        backup = new VMemWrite(pos, spill, write.dest);
//                        instructions.add(backup);
////                        backup = new VMemWrite()
//                        System.out.println("local[" + spillMap.get(dest) + "] = " + dest);
                        SourcePos pos = new SourcePos(startPos + instructions.size(), instr.sourcePos.column);
                        VMemRef spill = new VMemRef.Stack(pos, VMemRef.Stack.Region.Local, spillMap.get(dest));
//                        VVarRef var = F.vars[vars.indexOf(dest)];
                        VAddr.Var<VDataSegment> base = (VAddr.Var<VDataSegment>) ref.base;
                        restore = new VMemRead(pos, base.var, spill);
                        instructions.add(restore);
                        System.out.println(dest + " = local[" + spillMap.get(dest) + "]");
                    }
                    instructions.add(write);
                    System.out.println("[" + dest + "] = " + source);

                    System.out.println();
                    break;
                case 4:
                    instructions.add(instr);
                    break;
                case 5:
                    instructions.add(instr);
                    break;
                case 6:
                    instructions.add(instr);
                    break;
                case 7:
                    instructions.add(instr);
                    break;
            }
//            Integer k = i;
        }
    }

    private static void computeMap(VFunction F, Map<String, Integer> spillMap) {
        int i = 0;
        for (String var : F.vars) {
            spillMap.put(var, i);
            i++;
        }
    }

    ///////////////

    private static void computeRegistersSpills(Map<String, Interval> intervals, Map<String, REGISTER> registerMap, Map<String, Integer> spillMap) {

    }

    private static void regalloc(VaporProgram program, Map<String, REGISTER> registerMap, Map<String, Integer> spillMap) {

    }

    private static void backupCalleeSavedRegisters(VaporProgram program) {

    }

    private static void transmuteCalls(VaporProgram program) {

    }

    private static void transmuteArguments(VaporProgram program) {

    }

    private static void transmuteSignatures(VaporProgram program) {

    }
}