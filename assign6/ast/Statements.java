package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

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
