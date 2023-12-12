package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class StatementNode extends Node {
    
    public void accept(ASTVisitor v) {
        
        v.visit(this);
    }
}
