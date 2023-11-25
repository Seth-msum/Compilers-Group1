package assign6.ast;

import assign6.visitor.*;

//This is for the while, do , if, block...
//This is a superclass for the new nodes


public class StatementNode extends Node {
    
    
    public StatementNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
