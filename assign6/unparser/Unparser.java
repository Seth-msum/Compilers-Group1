package assign6.unparser;


import assign6.ast.*;
import assign6.parser.*;
import assign6.typechecker.TypeChecker;
import assign6.visitor.*;

public class Unparser extends ASTVisitor{
    
    public Parser parser = null ;

    public Unparser(Parser parser) {

        this.parser = parser ;
        visit(((Parser)this.parser).cu) ;
    }
    public Unparser(TypeChecker typechecker) {
        this.parser = typechecker.parser ;
        visit((this.parser).cu) ;
    }

    public Unparser () {

        visit(((Parser)this.parser).cu) ;
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

        for (DeclarationNode decl : n.decls)
            decl.accept(this) ;

        indentDown() ;

        indentUp() ;

        for (StatementNode stmt : n.stmts)
            stmt.accept(this) ;

        indentDown() ;

        printIndent() ;
        println("}") ;
 
    }

    // public void visit (Declarations n) {

    //     //At first this might seem missleading,
    //     //but what whill happen is that at the end of a
    //     //declaration, an empty declarations will be left 
    //     //attached to it.
    //     if (n.decls != null) {

    //         n.decl.accept(this) ;

    //         n.decls.accept(this) ;
    //     }
    // }

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

    // public void visit(Statements n) {

    //     if (n.stmts != null) {

    //         n.stmt.accept(this) ;
    //         n.stmts.accept(this) ;

    //     }
    // }

    //this might not technically be needed.
    public void visit (StatementNode n) {
    
    }

    public void visit (ParenthesesNode n) {
        print("(");
        n.expr.accept(this) ;
        print(")") ;
    }

    public void visit(IfStatementNode n) {

        printIndent() ;
        print("if ") ;
        n.cond.accept(this) ;
        println("") ;

        if (!(n.stmt instanceof BlockStatementNode))
            indentUp() ;
        n.stmt.accept(this) ;
        if (!(n.stmt instanceof BlockStatementNode))
            indentDown() ;
        
        if (n.else_stmt != null) {
            printIndent() ;
            print("else ") ;
            println("") ;

            if(!(n.else_stmt instanceof BlockStatementNode))
                indentUp() ;
            n.else_stmt.accept(this) ;
            if (!(n.else_stmt instanceof BlockStatementNode))
                indentDown() ;
        }
    }

    public void visit(WhileStatementNode n) {
        
        printIndent() ;
        print("while ") ;
        n.cond.accept(this) ;
        println("") ;
        
        if(!(n.stmt instanceof BlockStatementNode))
            indentUp() ;
        n.stmt.accept(this) ;
        if(!(n.stmt instanceof BlockStatementNode))
            indentDown() ;
    }

    public void visit(DoWhileStatementNode n) {
        printIndent() ;
        print("do ") ;
        println("") ;

        if(!(n.stmt instanceof BlockStatementNode))
            indentUp() ;
        n.stmt.accept(this) ;
        if(!(n.stmt instanceof BlockStatementNode))
            indentDown() ;
        
        printIndent() ;
        print("while") ;
        n.cond.accept(this) ;

        println(";") ;
        
    }

    public void visit(ArrayAccessNode n) {
        
        n.id.accept(this) ;
        n.index.accept(this) ;
    }

    public void visit(ArrayDimsNode n) {

        print("[") ;
        n.size.accept(this) ;
        print("]") ;

        if (n.dim != null)
            n.dim.accept(this) ;
    }

    public void visit(AssignmentNode n) {

        printIndent();

        n.left.accept(this) ;

        print(" = ") ;
        //print("!!"+n.right.getClass().getSimpleName()+"!!") ;
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
        else if (n.right instanceof BinExprNode)
            ((BinExprNode)n.right).accept(this) ;
        else {

        }

        println(" ;") ;
    }

    public void visit(BreakStatementNode n) {

        printIndent() ;
        println("Break ;");
    }

    public void visit (TrueNode n) {
        print("true") ;
    }

    public void visit (FalseNode n) {
        print("false") ;
    }

    public void visit(BinExprNode n) {

        if(n.left instanceof ParenthesesNode)
            ((ParenthesesNode)n.left).accept(this) ;
        else if (n.left instanceof IdentifierNode)
            ((IdentifierNode)n.left).accept(this) ;
        else if (n.left instanceof NumNode)
            ((NumNode)n.left).accept(this) ;
        else if (n.left instanceof RealNode)
            ((RealNode)n.left).accept(this) ;
        else if (n.left instanceof ArrayAccessNode)
            ((ArrayAccessNode)n.left).accept(this) ;
        else if (n.left instanceof BinExprNode)
            ((BinExprNode)n.left).accept(this) ;
        else {

        }
        
        if (n.op != null)
            print(" " + n.op.toString() + " ") ;

        if (n.right != null) {

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
            else if (n.right instanceof BinExprNode)
                ((BinExprNode)n.right).accept(this) ;
            else {

            } 
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
