package assign5.unparser;

import assign5.parser.* ;
import assign5.visitor.* ;

public class Unparser extends ASTVisitor{
    
    public Parser parser = null ;

    public Unparser(Parser parser) {

        this.parser = parser ;
        visit(this.parser.cu) ;
    }

    public Unparser () {

        visit(this.parser.cu) ;
    }

    ////////////////////////
    // Utility Methods
    ////////////////////////

    void print(String s){
        System.out.print(s);
    }

    void println(String s){
        System.out.println(s);
    }

    void printSpace(){
        System.out.print(" ");
    }

    int indent = 0;

    void indentUp(){
        indent++;
    }

    void indentDown(){
        indent--;
    }

    void printIndent(){
        String s = "";
        for (int i=0; i<indent; i++){
            s += "  ";
        }
        print(s);
    }

    public void visit(CompilationUnit n) {

        n.block.accept(this) ;
    }

    public void visit(BlockStatementNode n) {

        println("{") ;

        indentUp() ;

        n.stmts.accept(this) ;

        indentDown() ;

        println("}") ;
 
    }

    public void visit(Statements n) {

        if (n.stmts != null) {

            n.assign.accept(this) ;
            n.stmts.accept(this) ;

        }
    }

    public void visit(AssignmentNode n) {

        printIndent();

        n.left.accept(this) ;

        print(" = ") ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof LiteralNode)
            ((LiteralNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;

        println(" ;") ;
    }

    public void visit(BinExprNode n) {

        // if (n.left instanceof IdentifierNode)
        //     ((IdentifierNode)n.left).accept(this) ;
        // else if (n.left instanceof LiteralNode)
        //     ((LiteralNode)n.left).accept(this) ;
        
        // if (n.op != null)
        //     print(" " + n.op.toString() + " ") ;

        // if (n.right != null)    
        //     n.right.accept(this) ;
        
    }

    public void visit(IdentifierNode n) {

        //printIndent() ;
        print(n.id) ;
        //println(" ;") ;
    }

    public void visit(LiteralNode n) {

        //printIndent() ;
        print("" + n.literal) ;
        //println(" ;") ;
    }
}
