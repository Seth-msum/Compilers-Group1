package assign6.ast;

import assign6.visitor.*;

public class DoWhileStatementNode extends StatementNode {

    public StatementNode    stmt ;
    public ParenthesesNode  cond ;

    public DoWhileStatementNode () {

    }

    public DoWhileStatementNode(ParenthesesNode cond, StatementNode stmt) {

        this.cond = cond ;
        this.stmt = stmt ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
