package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;


public class RealNode extends ExprNode{
 
    public float value ;
    public Real v;

    public RealNode() {

    }

    public RealNode (Real v) {

        this.value = v.value;
        this.v       = v ;
    }

    public void accept(ASTVisitor v) { 

        v.visit(this) ;
    }
    
    public void printNode() {
        System.out.println("RealNode: " + value);
    }
}