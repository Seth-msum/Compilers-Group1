package assign6.ast;

import assign6.visitor.* ;
import assign6.lexer.*;

public class CompilationUnit extends Node {

    //Node ast ;
    //public AssignmentNode assign ;
    public BlockStatementNode block;

    public CompilationUnit () {

    }

    public CompilationUnit (BlockStatementNode block) {

        this.block = block ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
