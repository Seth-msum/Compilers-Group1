package assign5.ast;

import assign5.visitor.ASTVisitor;

public class BlockStatementNode extends StatementNode {
    
    public Declarations decls ;
    public Statements stmts ;

    public BlockStatementNode () {

    }

    public BlockStatementNode(Declarations decls, Statements stmts) {

        this.decls = decls ;
        this.stmts = stmts ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
