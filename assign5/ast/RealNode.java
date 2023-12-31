package assign5.ast;

import assign5.visitor.* ;
import assign5.lexer.* ;


public class RealNode extends Node{
 
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