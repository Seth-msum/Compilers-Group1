package assign6.ast;

import assign6.lexer.*;
import assign6.visitor.*;


// a[i] = k + j ;
// id [ExprNode]...
public class ArrayAccessNode extends ExprNode {
    
    public IdentifierNode id ;
    public ExprNode index ;

    public ArrayAccessNode () {

    }

    public ArrayAccessNode (IdentifierNode id, ExprNode index) {

        this.id = id ;
        this.index = index ; 
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}

