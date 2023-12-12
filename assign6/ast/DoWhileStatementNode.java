package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class DoWhileStatementNode extends StatementNode{
    
    public StatementNode stmt;
    public ParenthesesNode cond;

    public DoWhileStatementNode() {

    }

    public DoWhileStatementNode(StatementNode stmt, ParenthesesNode cond) {

        this.cond = cond;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
