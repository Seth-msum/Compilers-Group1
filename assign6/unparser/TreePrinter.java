package assign6.unparser;


import assign6.ast.*;
import assign6.lexer.*;
import assign6.parser.*;
import assign6.visitor.*;


public class TreePrinter extends ASTVisitor {
    
    public Parser parser = null ;
    public CompilationUnit cu = null ;


    int level = 0 ;
    String indent = "..." ;

    public TreePrinter (Parser parser) {

        this.parser = parser ;
        cu = parser.cu ;
        visit(cu) ;
    }

    public TreePrinter () {

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

    int indent_level = 0;

    void indentUp(){
        indent_level++;
    }

    void indentDown(){
        indent_level--;
    }

    void printIndent(){
        String s = "";
        for (int i=0; i<indent_level; i++){
            s += "  ";
        }
        print(s);
    }

    void printDotDotDot() { //New function of lab07

        String s = "" ;
        for (int i = 0; i < indent_level; i++ )
            s += "..." ;
        print(s) ;
    }

    public void visit(CompilationUnit n) {

        System.out.println();
        System.out.println("*****************************");
        System.out.println("*       Tree Printer        *");
        System.out.println("*****************************");
        System.out.println();
        System.out.println("CompilationUnit") ;

        indentUp() ;
        n.block.accept(this) ;
        indentDown() ;

        System.out.println("...Tree Printer ends.");
        System.out.println();
    }

    public void visit(BlockStatementNode n) {

        printDotDotDot() ;
        System.out.println("BlockStatementNode") ;
        //println("{") ;

        indentUp() ;

        for (DeclarationNode decl : n.decls)
            decl.accept(this) ;

        indentDown() ;

        indentUp() ;

        for (StatementNode stmt : n.stmts)
            stmt.accept(this) ;

        indentDown() ;

        //println("}") ;
 
    }

    // public void visit (Declarations n) {
    //     //These are of the same order so they do not need indentUp/Down
    //     if (n.decls != null) {
    //         // if(JudahsDeclCount == 0) {
    //         //     printDotDotDot();
    //         //     System.out.println("**Declarations --[--") ;
    //         // }
    //         // JudahsDeclCount++ ;

    //         n.decl.accept(this) ;

    //         n.decls.accept(this) ;
    //         // JudahsDeclCount-- ;
    //         // if(JudahsDeclCount == 0) {
    //         //     printDotDotDot();
    //         //     System.out.println("**Declarations --]-- ") ;
    //         // }
    //     }
    // }

    public void visit(DeclarationNode n) {

        printDotDotDot() ;
        System.out.println("DeclarationNode") ;

        indentUp() ;
        n.type.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.id.accept(this) ;
        indentDown() ;
    }

    public void visit(TypeNode n) {

        printDotDotDot() ;
        System.out.println("TypeNode: " + n.basic) ;
        
        if(n.array != null) {

            indentUp() ;
            n.array.accept(this) ;
            indentDown() ;
        }
    } 

    public void visit (ArrayTypeNode n) {

        printDotDotDot() ;
        System.out.println("ArrayTypeNode: " + n.size) ;

        if (n.type != null) {

            indentUp() ;
            n.type.accept(this) ;
            indentDown() ;
        }
    }

    // public void visit(Statements n) {

    //     if (n.stmts != null) {

    //         n.stmt.accept(this) ;

    //         n.stmts.accept(this) ;

    //     }
    //     // else
    //     //     System.out.println("The statements node's stmts is empty.");
    // }

    public void visit (Statements n) {
        if(n.stmts != null){
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }

    public void visit(ParenthesesNode n) {

        printDotDotDot() ;
        System.out.println("ParenthesesNode") ;

        indentUp() ;
        n.expr.accept(this) ;
        indentDown() ;
    }

    public void visit(IfStatementNode n) {

        printDotDotDot() ;
        System.out.println("IfStatementNode") ;

        indentUp() ;
        n.cond.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.stmt.accept(this) ;
        indentDown() ;

        if (n.else_stmt != null) {

            printDotDotDot() ;
            System.out.println("Else Clause") ;

            indentUp() ;
            n.else_stmt.accept(this) ;
            indentDown() ;  
        }
    }

    public void visit (WhileStatementNode n) {

        printDotDotDot() ;
        System.out.println("WhileStatementNode") ;

        indentUp() ;
        n.cond.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.stmt.accept(this) ;
        indentDown() ;
    }

    public void visit (DoWhileStatementNode n) {

        printDotDotDot() ;
        System.out.println("DoWhileStatementNode") ;

        indentUp() ;
        n.stmt.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.cond.accept(this) ;
        indentDown() ;
    }

    public void visit(ArrayAccessNode n) {
        printDotDotDot() ;
        System.out.println("ArrayAccessNode") ;

        indentUp() ;
        n.id.accept(this) ;
        indentDown() ;

        indentUp() ;
        n.index.accept(this) ;
        indentDown() ;
    }

    public void visit(ArrayDimsNode n) {

        printDotDotDot() ;
        System.out.println("ArrayDimsNode") ;
        
        indentUp() ;
        n.size.accept(this) ;
        indentDown() ;

        if (n.dim != null) {

            indentUp() ;
            n.dim.accept(this) ;
            indentDown() ;
        }
    }

    public void visit(BreakStatementNode n) {

        printDotDotDot() ;
        System.out.println("BreakStatementNode") ;
    }

    public void visit (TrueNode n) {

        printDotDotDot() ;
        System.out.println("TrueNode") ;
    }
    
    public void visit(FalseNode n) {

        printDotDotDot() ;
        System.out.println("FalseNode");
    }

    public void visit(AssignmentNode n) {
        
        printDotDotDot() ;
        System.out.println("AssignmentNode") ;
        indentUp() ;
        n.left.accept(this) ;
        indentDown() ;

        indentUp() ;
        printDotDotDot() ;
        System.out.println("Operator: = ");
        indentDown() ;

        indentUp() ;
        if (n.right instanceof ParenthesesNode)
            ((ParenthesesNode)n.right).accept(this) ;
        else if (n.right instanceof IdentifierNode)
            ((IdentifierNode)n.right).accept(this) ;
        else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this) ;
        else if (n.right instanceof RealNode)
            ((RealNode)n.right).accept(this) ;
        else if (n.right instanceof ArrayAccessNode)
            ((ArrayAccessNode)n.right).accept(this) ;
        else //BinExpr ???
            ((BinExprNode)n.right).accept(this) ;

        indentDown() ;
            //println(" ;") ;
    }

    public void visit(BinExprNode n) {

        printDotDotDot() ;
        System.out.println("BinExprNode: " + n.op) ; 

        indentUp() ;

        if (n.left instanceof ParenthesesNode)
            ((ParenthesesNode)n.left).accept(this) ;
        else if(n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof ArrayAccessNode) 
            ((ArrayAccessNode)n.left).accept(this) ;
        else
            ((BinExprNode)n.left).accept(this) ;

        if (n.op != null) {

            //System.out.println("&&& n.right in BinExprNode: " + n.right) ;
            //n.right.accept(this) ;
            //if(getPrecedence(n.op) < getPrecedence(n))
            
            if (n.right instanceof IdentifierNode)
                ((IdentifierNode)n.right).accept(this) ;
            else if (n.right instanceof NumNode)
                ((NumNode)n.right).accept(this) ;
            else if (n.right instanceof RealNode)
                ((RealNode)n.right).accept(this) ;
            else if (n.right instanceof ArrayAccessNode)
                ((ArrayAccessNode)n.right).accept(this) ;
            else
                ((BinExprNode)n.right).accept(this) ;    
        } else {

            // System.out.println("@@@ n.right == null in BinExprNode: " + n.right) ;
        }
        indentDown() ;
        
    }

    public void visit(IdentifierNode n) {

        // printIndent() ;
        printDotDotDot();
        System.out.println("IdentifierNode: " + n.id);
        //print("" + n.id) ;
        // println(" ;") ;
        n.printNode();
    }

    public void visit(NumNode n) {


        // printIndent() ;
        printDotDotDot();
        System.out.println("NumNode: " + n.value);
        //print("" + n.value) ;
        // println(" ;") ;
        n.printNode();
    }

    public void visit(RealNode n) {

        printDotDotDot();
        System.out.println("RealNode: " + n.value);
        //print("" + n.value) ;
        n.printNode();
    }
}
