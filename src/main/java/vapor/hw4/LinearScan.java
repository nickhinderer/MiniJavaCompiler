package vapor.hw4;

import vapor.hw4.Active;
import vapor.hw4.Graph;
import vapor.hw4.Node;
import vapor.hw4.Registers;
import vapor.hw4.Spill;
import vapor.hw4.Variable;

import java.util.ArrayList;
import java.util.List;

public class LinearScan {
    Graph CFG;
    List<Variable.Interval> liveIntervals;// = new ArrayList<>();
    Registers registers; // = new Registers();
    List<Spill> spills;// = new ArrayList<>();
    int time, R = 2;

    Active active;

//    LinearScan() {
//        spills = new ArrayList<>();
//    }
    void linearScan(Graph CFG) {
        spills = new ArrayList<>();
        registers = new Registers();
        liveIntervals = new ArrayList<>();
        active = new Active();
        this.CFG = CFG;
        sortLiveIntervals(CFG);
        for (time = 0; time < CFG.nodes.size(); time++) {
            for (Variable.Interval i : liveIntervals)
                if (i.start == time) {
                    expireOldIntervals(i.start);
                    if (active.length == R) {
                        spillAtInterval(i);
                    } else {
                        registers.add(i);
                        active.insert(i);
                    }
                } else {
                    expireOldIntervals(time);
                }
            CFG.nodes.get(time).record = registers.record();
//            CFG.nodes.get(time).record.
        }
        CFG.spills = spills;
    }


    void expireOldIntervals(int time) {
        for (int index = 0; index < active.length; index++) {
            Variable.Interval j = active.get(index);
            if (j != null) {
                if (j.end >= time) return;
                active.expire(index);
                registers.remove(j);
            }
        }


//            for (Variable.Interval j : active) {
//                if (j.end.num >= i.start.num)
//                    continue;
//                expired.add(j);
//            }
//            for (Variable.Interval j : expired) {
//                active.remove(j);
//                registers.remove(j);
//            }
    }

    void spillAtInterval(Variable.Interval i) {
        Variable.Interval spill = active.get(active.length - 1);
        if (spill.end > i.end) {
//            if (spill.end.num > i.end.num) {
            registers.remove(spill);
            registers.add(i);
            Spill location = new Spill(spill, CFG.node(time));
            spills.add(location);
            active.expire(spill);
            active.insert(i);
        } else {
            Spill location = new Spill(i, CFG.node(time));
            spills.add(location);
        }
    }

    void sortLiveIntervals(Graph CFG) {
        for (Variable.Interval interval : CFG.intervals) {
            int i;
            for (i = 0; i < liveIntervals.size(); i++)
//                    if (interval.start.num < liveIntervals.get(i).start.num) break;
                if (interval.start < liveIntervals.get(i).start) break;

            liveIntervals.add(i, interval);
        }
    }

//        static void activeInsert(Variable.Interval interval) {
//            int t;
//            for (t = 0; t < active.size(); t++)
//                if (interval.end.num < active.get(t).end.num) break;
//            active.add(t, interval);
//        }

    static void printSpillsAndRegisterMap(Graph CFG) {
        for (Spill spill : CFG.spills) {
            System.out.print(spill);
//            System.out.printf("Spilled Variable: \033[;34m%s\033[0m at node/instruction \033[;31m%d\033[0m at local[\033[;32m%d\033[0m]\n\n", spill.variable.name, spill.backupPoint.num, spill.location);
//                System.out.printf("Spilled Variable: \033[;34m%s\033[0m at node/instruction \033[;31m%d\033[0m at local[%d]\nlocal[%d] becomes free after node %d\n\n", spill.variable.name, spill.backupPoint.num, spill.location, spill.location, spill.variable.interval.end.num);
        }
        for (Node node : CFG.nodes) {
            System.out.print(node);
        }
    }
}


