package assign5.unparser;

import java.net.IDN;

import javax.swing.plaf.nimbus.State;

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

        printIndent() ;
        println("{") ;

        indentUp() ;

        n.decls.accept(this) ;

        indentDown() ;

        indentUp() ;

        n.stmts.accept(this) ;

        indentDown() ;

        printIndent() ;
        println("}") ;
 
    }

    public void visit (Declarations n) {

        //At first this might seem missleading,
        //but what whill happen is that at the end of a
        //declaration, an empty declarations will be left 
        //attached to it.
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

        //((StatementNode)n.stmt).accept(this) ;
        n.stmt.accept(this) ;

        if (n.stmts != null) {

            
            n.stmts.accept(this) ;

        }
    }

    //this might not technically be needed.
    public void visit (StatementNode n) {
        //System.out.println("Got To StatemtnNode");
        //print(n.stmt.getClass().getSimpleName()) ;
        // indentUp() ;
        // if (n.stmt instanceof AssignmentNode)
        //     ((AssignmentNode)n.stmt).accept(this) ;
        // else if (n.stmt instanceof WhileNode)
        //     ((WhileNode)n.stmt).accept(this) ;
        // else if (n.stmt instanceof DoNode) 
        //     ((DoNode)n.stmt).accept(this) ;
        // else if (n.stmt instanceof IfNode)
        //     ((IfNode)n.stmt).accept(this) ;
        // else if (n.stmt instanceof BreakNode) {
        //     printIndent() ;
        //     println("break ;");
        // }
        // indentDown() ;
    }

    public void visit(AssignmentNode n) {

        printIndent();

        n.left.accept(this) ;

        print(" = ") ;
        //print("!!"+n.right.getClass().getSimpleName()+"!!") ;
        if (n.right instanceof Locations)
            ((Locations)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else if (n.right instanceof BoolNode)
            ((BoolNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;

        println(" ;") ;
    }

    public void visit(WhileNode n) {
        
        printIndent() ;

        print("while ( ");

        if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof BoolNode)
            ((BoolNode)n.left).accept(this) ;
        else 
            ((BinExprNode)n.left).accept(this) ;
        
        println(" )") ;

        // indentUp() ;
        n.right.accept(this) ;
        // indentDown() ;
    }

    public void visit(DoNode n) {

        printIndent() ;

        println("do ") ;

        // print("!!"+n.left.getClass().getSimpleName()+"!!") ;
        indentUp() ;
        n.left.accept(this) ;
        indentDown() ;
        
        printIndent() ; 
        print("while (") ;

        if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else if (n.right instanceof BoolNode)
            ((BoolNode)n.right).accept(this) ;
        else 
            ((BinExprNode)n.right).accept(this) ;
        
        println(" ) ;") ;
        println(""); 

    }

    public void visit(IfNode n) {

        printIndent(); 
        print("if (");

        if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof BoolNode)
            ((BoolNode)n.left).accept(this) ;
        else 
            ((BinExprNode)n.left).accept(this) ;
        
        println(" )") ;

        indentUp() ;
        n.right.accept(this) ;
        indentDown() ;

        if (n.theElse != null) {
            printIndent() ;
            println("else ") ;

            indentUp() ;
            n.theElse.accept(this) ;
            indentDown() ;
        }

        println("") ;
    }

    public void visit(BinExprNode n) {

        if(n.left instanceof Locations)
            ((Locations)n.left).accept(this) ;
        else if (n.left instanceof IdentifierNode)
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

    public void visit(BoolNode n) {

        print("" + n.value) ;
    }

    public void visit(Locations n) {

        indentUp() ;
        n.left.accept(this) ;
        indentDown() ;

        if (n.right != null) {

            indentUp() ;
            n.right.accept(this) ;
            indentDown() ;
        }
    }

    public void visit(LocationNode n) {

        print("[ ") ;

        if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof BoolNode)
            ((BoolNode)n.left).accept(this) ;
        else 
            ((BinExprNode)n.left).accept(this) ;
        
        print(" ]") ;

        if(n.right != null) {
            indentUp() ;
            n.right.accept(this) ;
            indentDown() ;
        }
    }

    public void visit(BreakNode n) {
        printIndent();
        println("break ;") ;
    }
}
