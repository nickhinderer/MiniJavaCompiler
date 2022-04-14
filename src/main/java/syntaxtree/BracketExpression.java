//
// Generated by JTB 1.3.2
//

package syntaxtree;
import visitor.Visitor;

/**
 * Grammar production:
 * f0 -> "("
 * f1 -> Expression()
 * f2 -> ")"
 */
public class BracketExpression implements Node {
   public NodeToken f0;
   public Expression f1;
   public NodeToken f2;

   public BracketExpression(NodeToken n0, Expression n1, NodeToken n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
   }

   public BracketExpression(Expression n0) {
      f0 = new NodeToken("(");
      f1 = n0;
      f2 = new NodeToken(")");
   }

   public void accept(Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}

