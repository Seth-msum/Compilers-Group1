package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class NumNode extends ExprNode {
    
    public int value;
    public Num v;

    public NumNode() {

    }

    public NumNode(Num v) {

        this.value = v.value;
        this.v = v;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }

    public void printNode() {

        System.out.println("NumNode: " + value) ;
    }
}
