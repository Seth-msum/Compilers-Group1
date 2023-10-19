package assign4.visitor ;

import assign4.parser.* ;

public class ASTVisitor {

    public void visit (CompilationUnit n) {

        n.block.accept(this) ;
    }

    public void visit (BlockStatmentNode n) {
        int count = 0;
        //Old: n.assign.accept(this) ;
        n.acceptAssignmentNode(count, this) ;
    }

    public void visit (AssignmentNode n) {

        n.id.accept(this) ;
        n.right.accept(this) ;
    }

    public void visit (AdditionNode n) {

        n.left.accept(this) ;
        n.right.accept(this) ;
    }

    public void visit(VariableNode n) {
        
    }

    public void visit (IdentifierNode n) {

    }

    public void visit(WhileNode n) {

    }
}
