package assign5.ast;

import assign5.visitor.* ;


import assign5.lexer.* ;

///////
//
//  Stmts --> Stmts Stmt
//  Stmt  --> id = expr
//
///////

public class Statements extends StatementNode {

    public Statements stmts ;
    public StatementNode stmt ;

    public Statements () {

    }

    public Statements (Statements stmts, StatementNode stmt) {

        this.stmts = stmts ;
        this.stmt = stmt ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
