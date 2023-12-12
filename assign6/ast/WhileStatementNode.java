package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class WhileStatementNode extends StatementNode{
    
    public ParenthesesNode cond;
    public StatementNode stmt;

    public WhileStatementNode() {

    }

    public WhileStatementNode(ParenthesesNode cond, StatementNode stmt) {

        this.cond = cond;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
