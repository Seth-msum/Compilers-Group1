package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class ParenthesesNode extends ExprNode {
    
    public ExprNode expr;

    public ParenthesesNode() {

    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
    
}

