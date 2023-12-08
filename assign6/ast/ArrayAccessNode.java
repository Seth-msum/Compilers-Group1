package assign6.ast;

import java.util.ArrayList;
import java.util.List;

import assign6.lexer.*;
import assign6.visitor.*;


// a[i] = k + j ;
// id [ExprNode]...
public class ArrayAccessNode extends IdentifierNode {
    
    public IdentifierNode id ;
    public List<ArrayDimsNode> dims = new ArrayList<ArrayDimsNode>() ;

    public ArrayAccessNode () {

    }

    public ArrayAccessNode (IdentifierNode id, List<ArrayDimsNode> dims) {

        this.id = id ;
        this.dims = dims ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}

