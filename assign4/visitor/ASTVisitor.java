package assign4.visitor ;

import assign4.parser.* ;

public class ASTVisitor {

    public void visit (CompilationUnit n) {

        n.block.accept(this) ;
    }

    public void visit (AssignmentNode n) {

        n.id.accept(this) ;
        n.right.accept(this) ;
    }

    // public void visit (AdditionNode n) {

    //     n.left.accept(this) ;
    //     n.right.accept(this) ;
    // }

    public void visit (BlockStatmentNode n) {
        
        n.assign.accept(this) ;
    }

    public void visit (IdentifierNode n) {

    }
}
