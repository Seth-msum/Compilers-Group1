package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

public class FalseNode extends ExprNode {
    
    public FalseNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
