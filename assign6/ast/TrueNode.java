package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;

public class TrueNode extends ExprNode{
 
    public TrueNode () {

    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
