package assign5.parser;

import assign5.visitor.* ;

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
