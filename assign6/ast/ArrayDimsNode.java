package assign6.ast;

import java.util.ArrayList;
import java.util.List;

import assign6.visitor.*;

public class ArrayDimsNode extends ExprNode {
    
    public ExprNode         size ;
    

    
    public ArrayDimsNode () {

    }

    public ArrayDimsNode (ExprNode size) {

        this.size = size ;
        
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
}
