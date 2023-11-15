package assign5.parser;

import assign5.visitor.* ;
import assign5.lexer.* ;

import java.io.* ;


public class Parser extends ASTVisitor {

    public CompilationUnit cu = null ;
    public Lexer lexer        = null ;

    public Token look = null ;

    int level = 0 ;
    String indent = "..." ;

    public Parser (Lexer lexer) {

        this.lexer = lexer ;
        cu = new CompilationUnit() ;

        move() ;

        visit(cu) ;
    }

    public Parser () {

        cu = new CompilationUnit() ;

        move() ;

        visit(cu) ;
    }

    ///////////
    //  Utility methods
    ///////////

    void move() {
        try {
            look = lexer.scan() ;
        }
        catch (IOException e) {

            System.out.println("IOException") ;
        }
    }

    void error (String s) {

        throw new Error("near line " + lexer.line + ": " + s) ;
    }

    void match (int t) {

        try {
            if (look.tag == t) 
                move() ;
            else
                error("Syntax error") ;
        }
        catch (Error e) {

        }
    }

    ///////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println("CompilationUnit") ;

        n.block = new BlockStatementNode() ;
        level++ ; //N
        n.block.accept(this) ;
        level-- ; //N
    }

    public void visit (BlockStatementNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("BlockStatmentNode");

        // if (look.tag == '{')
        //     System.out.println("Matched with '{':  " + look.tag);
        match('{') ;

        n.stmts = new Statements() ;
        //level++ ;
        n.stmts.accept(this) ;
        //level-- ;

        // if (look.tag == '}')
        //     System.out.println("Matched with '}':  " + look.tag);
        match('}') ;
    }

    ////////////////////////////////
    //
    // Stmts --> Stmts Stmt
    // Stmt --> id = expr
    //
    ///////////////////////////////

    public void visit(Statements n) {

        if (!look.toString().equals("}")) {
            //If it's not the end bracket, then its another assignemt

            switch (look.tag) {   

                default:
                    n.assign = new AssignmentNode() ;
                    level++ ;       // N
                    n.assign.accept(this) ;
                    level-- ;       //N
                    
                    n.stmts = new Statements() ;
                    level++ ;       //N
                    n.stmts.accept(this) ;
                    level-- ;       //N

                    break;
            }
        }
    }

    public void visit (AssignmentNode n) { // major changes

        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("AssignmentNode");

        n.left = new IdentifierNode() ;        
        level++ ;
        n.left.accept(this) ;
        level-- ;

        match('=') ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: =") ;

        Node rhs_assign = null ;

        if (look.tag == Tag.ID) {

            rhs_assign = new IdentifierNode() ;
            level++ ;
            ((IdentifierNode)rhs_assign).accept(this) ;
            level-- ;
        } 
        
        else if (look.tag == Tag.NUM) {
            
            rhs_assign = new LiteralNode() ;
            level++ ;
            ((LiteralNode)rhs_assign).accept(this) ;
            level-- ;
        }

        if (look.tag == ';') {// e.g. a = 19 ;
            
            n.right = rhs_assign ;
        }

        else { // e.g. b = a + b * c ;
            // This is the start of the precedence climbing method.
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;

            level++ ;
            // Build AST for binary expressions with operator precedence
            n.right = (BinExprNode) parseBinExprNode( rhs_assign, 0) ;
            level-- ;

            System.out.println("**** Root Node operator: " + ((BinExprNode)n.right).op) ;
        }
        
        match(';');
    }

    public void visit(BinExprNode n) {

        // System.out.println("****** " + look.toString()) ;
        // System.out.println("****** " + look) ;
    
        // if (look.tag == Tag.ID) {
        //     System.out.println("look.tag == Tag.ID: "+  look.toString());
        //     n.left = new IdentifierNode() ;
        //     ((IdentifierNode)n.left).accept(this) ;

        // } else if (look.tag == Tag.NUM) {
        //     System.out.println("look.tag == Tag.NUM: " + look.toString()) ;
        //     n.left = new LiteralNode() ;
        //     ((LiteralNode)n.left).accept(this) ;
        
        // }

        // System.out.println("look.toString() in BinExprNode: " + look.toString()) ;
        
        // if (look.toString().equals("+") || look.toString().equals("-")) {

        //     //n.op = look.toString() ;
        //     n.op = look ;
        //     move() ;

        //     n.right = new BinExprNode() ;
        //     n.right.accept(this) ;
        // }
    }

    /*
        < Operator Precedence >
        Operators are listed top to bottom in ascending precedence.

        01. assignment =, +=, -=, *=, /=, %=, &=, ^=, \=, <<=, >>=, >>>=
        02. tenary ? :
        03. logical OR ||
        04. logical AND &&
        05. bitwise inclusive OR |
        06. bitwise exclusive OR ^
        07. bitwise AND &
        08. equality ==, !=
        09. relational <, >, <=, >=
        10. shift <<, >>, >>>
        11. additive +, -
        12. multiplicative *, /, %
        13. unary ++expr, --expr, +expr, -expr
        14. postfix expr++, expr--
     */

    int getPrecedence (int op) {

        switch ( op ) {
            case '*':           
            case '/':
            case '%':
                return 12 ;     //multiplicative
            case '+':
            case '-':
                return 11 ;     //additive
            // case '<':
            // case '>':
            // case '<=':
            // case '>=':
            //      return 9 ;  //relational
            // case '==':
            // case '!=':
            //      return 8 ;  //equality
            default:
                return -1 ; // ';'
        }
    }

    // Eventually rhs should be an ExpressionNode.    <-- he said rhd but his code origianlly said lhs
    // But, for a while, it can be a Node.

    // Already, moved to the next token, which should 
    // be binary operator, in IdentifierNode || LiteralNode
    // It is stored in look now. 
    Node parseBinExprNode( Node lhs, int precedence) {

        // If the current op's precedence is higher than that of
        // the previous, then keep traversing down to create a new
        // BinExprNode for binary expressions with higher precedence
        // Otherwise, create a new BinExprNode for current lhs and rhs. 
        while ( getPrecedence(look.tag) >= precedence) {

            Token token_op = look ;
            int op = getPrecedence(look.tag) ;

            move() ;

            for (int i = 0; i < level; i++) System.out.print(indent) ;

            // if (look.tag == Tag.NUM)
            //     System.out.println("&&&& LiteralNode") ;
            // else if (look.tag == Tag.ID)
            //     System.out.println("**** IdentifierNode") ;

            Node rhs = null ;

            if (look.tag == Tag.ID) {

                rhs = new IdentifierNode() ;
                level++ ;
                ((IdentifierNode)rhs).accept(this) ;
                level-- ;
            }
            else if (look.tag == Tag.NUM) {

                rhs = new LiteralNode() ;
                level++ ;
                ((LiteralNode)rhs).accept(this) ;
                level-- ;
            }

            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator " + look) ;

            // System.out.println("op = " + op) ;
            // System.out.println("token_op = " + token_op) ;
            // System.out.println("next_op = " + getPrecedence(look.tag)) ;

            // whenever the next op's precedence is higher than that
            // of the current operator, keep recursively calling itself.
            while ( getPrecedence(look.tag) > op ) {

                rhs = parseBinExprNode( rhs, getPrecedence(look.tag) ) ;
            }
            
            lhs = new BinExprNode(token_op, lhs, rhs);

            // System.out.println("**** Created BinExprNode with op " + token_op) ;
        }

        return lhs ;
    }

    public void visit(LiteralNode n) {

        n.literal = ((Num)look).value ;

        match(Tag.NUM) ;  // expext look.tag == Tag.NUM

        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
        //System.out.println("look in IdentifierNode: " +  look);
    }

    public void visit(IdentifierNode n) {

        n.id = look.toString() ;

        match(Tag.ID) ; // expect look.tag == Tag.ID

        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
        //System.out.println("IdentifierNode: " + n.id) ;
        //System.out.println("look in IdentifierNode: " + look) ;
    }
    
}
