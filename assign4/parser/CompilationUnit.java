package assign4.parser ;

import assign4.visitor.* ;

public class CompilationUnit extends Node {

    //Node ast ;
    public AssignmentNode assign ;

    public CompilationUnit () {

    }

    public CompilationUnit (AssignmentNode assign) {

        this.assign = assign ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }

    // missing visit function
}
