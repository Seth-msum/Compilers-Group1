package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class IfStatementNode extends StatementNode {
    
    public ParenthesesNode cond;
    public StatementNode stmt;
    public StatementNode else_stmt;

    public IfStatementNode() {

    }

    public IfStatementNode(ParenthesesNode cond, StatementNode stmt, StatementNode else_stmt) {

        this.cond = cond;
        this.stmt = stmt;
        this.else_stmt = else_stmt;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
