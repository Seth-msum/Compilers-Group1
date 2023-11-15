package assign4.parser;

import assign4.visitor.* ;
import assign4.lexer.* ;

public class LiteralNode extends Node {

    public int literal ;
    public Num v;

    public LiteralNode() {

    }

    public LiteralNode (Num v) {

        this.literal = v.value;
        this.v       = v ;
    }

    public void accept(ASTVisitor v) { //This is not technically needed.

        v.visit(this) ;
    }
    
    void printNode() {
        System.out.println("LiteralNode: " + literal);
    }
}
