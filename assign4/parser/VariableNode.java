package assign4.parser;

import assign4.visitor.*;
import assign4.lexer.* ;

public class VariableNode extends ASTVisitor{
    public int type ; // Warning, not protected of being changed.
    public Num num ; // type 1
    public IdentifierNode id ; // type 2

    public VariableNode () {

    }
    
    public VariableNode(Num value) {
        this.type = 1;
        this.num = value ;
    }

    public VariableNode(IdentifierNode id) {
        this.type = 2 ;
        this.id = id ;
    }
    
    public void accept(ASTVisitor v) {
        v.visit(this) ;
    }

    public void print() {
        if (this.type == 1){
            System.out.print(num.toString()) ;
        }
        else {
            System.out.println() ;
            System.out.println("\ttype value has been violated") ;
            System.exit(1) ; 
        }
    }

    public void printNode() {
        if (this.type == 1){
            System.out.println("VariableNode: " + num.toString()) ;
        }
        else {
            System.out.print("VariableNode:");
            id.printNode();
        }
    }
    
}
