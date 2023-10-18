package assign4.parser ;

import assign4.parser.*;
import assign4.pretty.PrettyPrinter;
import assign4.visitor.* ;

public class CompilationUnit extends Node {

    //Node ast ;
    //public AssignmentNode assign ;
    public BlockStatmentNode block ;
    
    public CompilationUnit () {

    }

    public CompilationUnit (BlockStatmentNode block) {

        this.block = block;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }

    // missing visit function
    public void visit(AssignmentNode an) {

    }

    public void visit(PrettyPrinter p){
        
    }

}
