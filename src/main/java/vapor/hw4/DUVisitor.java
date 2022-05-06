package vapor.hw4;

import cs132.vapor.ast.*;


public class DUVisitor extends VInstr.VisitorPR<Integer, Node, RuntimeException> {
    public DUVisitor() {
    }

    public Node visit(Integer num, VAssign assign) {
        Node node = new Node(num);
        node.instruction = assign;

        if (!isIntLiteral(assign.source)) {
            Variable use = Variable.variable(assign.source.toString());
            Variable.Interval useInterval = new Variable.Interval(use);
            node.addUse(use);
        }

        Variable def = Variable.variable(assign.dest.toString());
        def.interval(num);
        node.addDef(def);

        return node;
    }

    public Node visit(Integer num, VCall call) {
        Node node = new Node(num);
        node.instruction = call;
        VVarRef.Local dest = call.dest;
        if (dest != null) node.addDef(Variable.variable(dest.toString()));
        VAddr<VFunction> addr = call.addr;
        if (!(addr instanceof VAddr.Label)) node.addUse(Variable.variable(addr.toString()));
        for (VOperand arg : call.args) if (!isIntLiteral(arg)) node.addUse(Variable.variable(arg.toString())); //can just check litint, because if it is static, then only other choice is label ref, otherwise it is not static, otherwise it iis vvarref (can't be memref, and within varref it can't be register bc its vapor not vaporm)
        //ask if we have to translate all of vapor-m
        return node;
    }

    public Node visit(Integer num, VBuiltIn builtIn) {
        Node node = new Node(num);
        node.instruction = builtIn;

        //shortcut: yy
        VBuiltIn.Op op = builtIn.op;
        switch (op.name) {
            case "Error":
                return node;
            case "HeapAllocZ": {
                VVarRef dest = builtIn.dest;
                if (dest != null) node.addDef(Variable.variable(dest.toString()));
                VOperand arg = builtIn.args[0];
                if (isIntLiteral(arg)) return node;
                else node.addUse(Variable.variable(arg.toString()));
                return node;
            }
            case "PrintIntS": {
                VOperand arg = builtIn.args[0];
                if (isIntLiteral(arg)) return node;
                else node.addUse(Variable.variable(arg.toString()));
                return node;
            }
            case "Add":
            case "Sub":
            case "MulS":
            case "Eq":
            case "Lt":
            case "LtS": {
                VVarRef dest = builtIn.dest;
                if (dest != null) node.addDef(Variable.variable(dest.toString()));
                VOperand arg1 = builtIn.args[0];
                if (isIntLiteral(arg1)) return node;
                else node.addUse(Variable.variable(arg1.toString()));
                VOperand arg2 = builtIn.args[1];
                if (isIntLiteral(arg2)) return node;
                else node.addUse(Variable.variable(arg2.toString()));
                return node;
            }
        }
        return new Node(num);
    }

    public Node visit(Integer num, VMemWrite write) {
        Node node = new Node(num);
        node.instruction = write;
        //can't be 'stack' because that is only in vaporm
        VMemRef.Global dest = (VMemRef.Global) write.dest;
        node.addUse(Variable.variable(dest.base.toString()));
        VOperand source = write.source;
        if (isIntLiteral(source))
            return node; //not going to be static (lit int, lit string, label) wait it could be a lebel
        if (source instanceof VLabelRef) return node;
        node.addUse(Variable.variable(source.toString()));
        return node;
    }

    public Node visit(Integer num, VMemRead read) {
        Node node = new Node(num);
        node.instruction = read;
        VMemRef.Global source = (VMemRef.Global) read.source;
        node.addUse(Variable.variable(source.base.toString()));
        VOperand dest = read.dest;
//        if (!(dest instanceof ))
        //its not going to be a label ref
        //ask teacher, so you say the hw is not using the previous homework input
        //does that mean we have to cover everything in vapor even if it is not in MJ? If so, why are we not doing computed gotos and where specifically
        // does it say what and what not we have to cover.    because we are not doing
        //computed
        node.addDef(Variable.variable(dest.toString()));
        return node;
    }

    public Node visit(Integer num, VBranch branch) {
        Node node = new Node(num);
        VOperand value = branch.value;
        if (isIntLiteral(value)) return node;
        node.addUse(Variable.variable(value.toString()));
        return node;
    }

    public Node visit(Integer num, VGoto _goto) {
        //no computed goto, couldn't be Var
//        if (_goto.target instanceof VAddr.Var)
//            node.addUse(new Variable(_goto.target.toString()));
        Node node = new Node(num);
        node.instruction = _goto;
        return node;
    }

    public Node visit(Integer num, VReturn ret) {
        Node node = new Node(num);
        node.instruction = ret;
        VOperand value = ret.value;
        if (value == null) return node;
        if (isIntLiteral(value)) return node;
        //not a label (can't return functions), not a register (vapor, not vaporm), not any other type of static (lit int, label ref, and kind of strings but they are different)
        //so its going to be a VVarRef and a local one (only other option is register)
        node.addUse(Variable.variable(value.toString()));
        return node;
    }

    static boolean isIntLiteral(VOperand op) {
        return op instanceof VLitInt;
    }

}
