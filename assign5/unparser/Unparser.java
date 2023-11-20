package assign5.unparser;

import assign5.ast.* ;
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

        n.decls.accept(this) ;

        indentDown() ;

        indentUp() ;

        n.stmts.accept(this) ;

        indentDown() ;

        println("}") ;
 
    }

    public void visit (Declarations n) {

        if (n.decls != null) {

            n.decl.accept(this) ;

            n.decls.accept(this) ;
        }
    }

    public void visit(DeclarationNode n) {


        n.type.accept(this) ;
        print(" ") ;
        n.id.accept(this) ;
        println(" ;") ;

    }

    public void visit(TypeNode n) {

        printIndent() ;
        print(n.basic.toString()) ;
        
        
        if(n.array != null) {

            n.array.accept(this) ;

        }
    }

    public void visit (ArrayTypeNode n) {

        print("[") ;
        print("" + n.size) ;
        print("]") ;

        
        if (n.type != null) {
            
            n.type.accept(this) ;
        }
    }

    public void visit(Statements n) {

        if (n.stmts != null) {

            if (n.stmt instanceof AssignmentNode)
                ((AssignmentNode)n.stmt).accept(this) ;
            n.stmts.accept(this) ;

        }
    }

    public void visit (StatementNode n) {
    
    }

    public void visit(AssignmentNode n) {

        printIndent();

        n.left.accept(this) ;

        print(" = ") ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;

        println(" ;") ;
    }

    public void visit(BinExprNode n) {

        if(n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else
            ((BinExprNode)n.left).accept(this) ;
        
        if (n.op != null)
            print(" " + n.op.toString() + " ") ;

        if (n.right != null) {

            if (n.right instanceof IdentifierNode)
                    ((IdentifierNode)n.right).accept(this) ;
                else if (n.right instanceof NumNode)
                    ((NumNode)n.right).accept(this) ;
                else if (n.right instanceof RealNode)
                    ((RealNode)n.right).accept(this) ;
                else
                    ((BinExprNode)n.right).accept(this) ;    
        } else {

        // System.out.println("@@@ n.right == null in BinExprNode: " + n.right) ;
        } 
        
    }

    public void visit(IdentifierNode n) {

        //printIndent() ;
        print(n.id) ;
        //println(" ;") ;
    }
    public void visit(RealNode n) {
        print("" + n.value) ;
    }

    public void visit(NumNode n) {

        //printIndent() ;
        print("" + n.value) ;
        //println(" ;") ;
    }
}
