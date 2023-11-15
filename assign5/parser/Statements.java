package assign4.parser;

import assign4.visitor.* ;
import assign4.lexer.* ;

///////
//
//  Stmts --> Stmts Stmt
//  Stmt  --> id = expr
//
///////

public class Statements extends Node {

    public Statements stmts ;
    public AssignmentNode assign ;

    public Statements () {

    }

    public Statements (Statements stmts, AssignmentNode assign) {

        this.stmts = stmts ;
        this.assign = assign ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
