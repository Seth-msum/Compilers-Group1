package assign6.ast;
 
import assign6.visitor.*;
import assign6.lexer.*;

public class FalseNode extends ExprNode {
    
    public FalseNode() {

    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
    
}