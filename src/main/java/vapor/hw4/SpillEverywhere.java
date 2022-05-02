package vapor.hw4;

import cs132.util.ProblemException;
import cs132.vapor.ast.*;
import cs132.vapor.parser.VaporParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpillEverywhere {
    public static VaporProgram parseVapor(InputStream in, PrintStream err)
            throws IOException {
        VBuiltIn.Op[] ops = {
                VBuiltIn.Op.Add, VBuiltIn.Op.Sub, VBuiltIn.Op.MulS, VBuiltIn.Op.Eq, VBuiltIn.Op.Lt, VBuiltIn.Op.LtS,
                VBuiltIn.Op.PrintIntS, VBuiltIn.Op.HeapAllocZ, VBuiltIn.Op.Error,
        };
        boolean allowLocals = true;
        String[] registers = null;
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
            in = new FileInputStream("TreeVisitor.vapor");
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
        //new function
        InstructionsWrapper instructions = new InstructionsWrapper();
        for (VFunction F : p.functions) {
            //step 1
            Instruction signature = createSignature(F);
            instructions.instructions.add(signature);
            List<Instruction>
//            instructions.add(Arrays.asList(signature)); //Arrays.asList() List.of()
            int offset = F.sourcePos.line;


        }
    }

    private static Instruction createSignature(VFunction F) {
        int in = F.params.length, local = F.vars.length, out;
        InstructionTypeVisitor v = new InstructionTypeVisitor();
        int maximum = -1;
        for (VInstr instruction : F.body) {
            int type = instruction.accept(v);
            if (type == 1) {
                VCall call = (VCall) instruction;
                int argSize = call.args.length;
                maximum = argSize > maximum ? argSize : maximum;
            }
        }
        out = maximum;
        return new Instruction(0, String.format("func %s [in %d, out %d, local %d]", F.ident, in, out, local));
    }


}


class InstructionsWrapper {
    List<Instruction> instructions;

    InstructionsWrapper() {
        instructions = new ArrayList<>();
    }

    void add(Instruction instruction) {
        int index = instruction.original; //index to insert
        for (int i = 0; i < instructions.size(); i++)
            if (instructions.get(i).original < index) {
//                Collections.reverse(instruction);
                for (Instruction instr : instruction)
                    instructions.add(i, instr);
                break;
            }
    }
}


/*
*
* EX:
* t.1 = 2           line: 1
* t.2 = 3           line: 2
*    |
*    v
* $t0 = 2           original: 1
* local[1] = $t0    original: 1    |    lines: {"$t0 = 2", "local[1] = $t0"}, orig: 1
* $t0 = 3           original: 2
* local[2] = $t0    original: 2
*
* InstructionsWrapper : {
*   instructions : [
*     {
*        lines : ["$t0 = 2", "local[1] = $t0"],
*        original : 1
*     },
*     {
*       lines : ["$t0 = 3", "local[2] = $t0"],
 *      original : 2
*     }
*   ]
* }
*
*
*
*/

class Instruction {
    List<String> lines; //each line is an invididual instruction itself, they just all represent the origial instruction in vapor hence why the're gruoped together in a single instruction even though the "instruction" itself is composed of individual instructions, it is jsut  supossed to represent the grouping in the original vapr program
    // instruction number of the instruction in the original
    // function from which this instruction ijs derived
    // (spill everywhere takes each instruction in its own
    // context, doesn't care about other ones, so that means
    // if you insert instructions they all have to be grouped
    // with the original)
    int original;

    Instruction() {
        instructions = new ArrayList<>();
    }

    Instruction(String line) {
        instructions = new ArrayList<>(Arrays.asList(line));
    }

    void add(String line) {
        instructions.add(line);
    }
}

//class Line {
//    Line(int original, String instruction) {
//        this.instruction = instruction;
//    }
//
//
//    String instruction;
//}

class SEV extends VInstr.VisitorPR<VFunction, List<String>, RuntimeException> {
    public SEV() {
    }

    public List<String> visit(VFunction var1, VAssign var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VCall var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VBuiltIn var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VMemWrite var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VMemRead var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VBranch var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VGoto var2) {
        return null;
    }

    public List<String> visit(VFunction var1, VReturn var2) {
        return null;
    }
}

class InstructionTypeVisitor extends VInstr.VisitorR<Integer, RuntimeException> {
    public InstructionTypeVisitor() {
    }

    public Integer visit(VAssign var1) {
        return 0;
    }

    public Integer visit(VCall var1) {
        return 1;
    }

    public Integer visit(VBuiltIn var1) {
        return 0;
    }

    public Integer visit(VMemWrite var1) {
        return 0;
    }

    public Integer visit(VMemRead var1) {
        return 0;
    }

    public Integer visit(VBranch var1) {
        return 0;
    }

    public Integer visit(VGoto var1) {
        return 0;
    }

    public Integer visit(VReturn var1) {
        return 0;
    }
}