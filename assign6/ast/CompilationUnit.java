package assign6.ast;

import assign6.visitor.ASTVisitor;

public class CompilationUnit extends Node{
    
    public BlockStatementNode block ;
    
    public CompilationUnit () {
        
    }

    public CompilationUnit (BlockStatementNode block) {

        this.block = block ;
    }

    public void accept (ASTVisitor v) {

        v.visit(this) ;
    }
}
