package assign6.ast;


import assign6.visitor.*;

// stmt --> while ( bool ) stmt

public class WhileStatementNode extends StatementNode {
    
    public ParenthesesNode  cond ;
    public StatementNode    stmt ; //This is for the stmt

    public WhileStatementNode () {

    }

    public WhileStatementNode(ParenthesesNode cond, StatementNode stmt) {

        this.cond = cond ;
        this.stmt = stmt ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
