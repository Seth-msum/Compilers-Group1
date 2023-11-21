package assign5.ast;

import assign5.visitor.ASTVisitor;

//This is for the while, do , if, block...
//This is a superclass for the new nodes


public class StatementNode extends Node {
    
    public StatementNode stmt ;
    public StatementNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
