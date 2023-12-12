package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class BreakStatementNode extends StatementNode{
    
    public BreakStatementNode() {

    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}