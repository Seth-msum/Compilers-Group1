package assign4.parser;

import assign4.visitor.* ;

public class BlockStatementNode {

    public Statements stmts ;

    public BlockStatementNode() {

    }

    public BlockStatementNode (Statements stmts) {

        this.stmts = stmts ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
