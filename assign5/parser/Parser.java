package assign5.parser;

import assign5.visitor.* ;
import assign5.ast.* ;
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
            if (look.tag == t) {
                //System.out.print("test");
                move() ;

            }
            else
                error("Syntax error") ;
        }
        catch (Error e) {

        }
    }

    ///////////////////////////////////

    //program --> block
    public void visit(CompilationUnit n) {

        System.out.println("CompilationUnit") ;

        n.block = new BlockStatementNode() ;
        level++ ; //N
        n.block.accept(this) ;
        level-- ; //N
    }


    // block --> { decls stmts }
    public void visit (BlockStatementNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("BlockStatmentNode");

        // if (look.tag == '{')
        //     System.out.println("Matched with '{':  " + look.tag);
        match('{') ;

        n.decls = new Declarations() ;
        level++ ;
        n.decls.accept(this) ;
        level-- ;


        n.stmts = new Statements() ;
        //level++ ;
        n.stmts.accept(this) ;
        //level-- ;

        // if (look.tag == '}')
        //     System.out.println("Matched with '}':  " + look.tag);
        match('}') ;
    }

    // decls --> decls decl | e
    public void visit(Declarations n) {


        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("Declarations"); //For parser tracking 

        if (look.tag == Tag.BASIC) { //This looks to see if the next lexeme is a type (int, float...)

            n.decl = new DeclarationNode() ;
            level++ ;
            n.decl.accept(this) ;
            level-- ;

            n.decls = new Declarations() ; //right recursion to prevent left hand infinite recursion
            n.decls.accept(this) ;
        }
        //Judah: I could add a else condition to check if there has been at least one declaration
        // and if there hasnt, reprort it missing?
    }
    // decl --> type id ;
    public void visit(DeclarationNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("DeclarationNode") ; //For parser tracking

        n.type = new TypeNode() ;
        level++ ;
        n.type.accept(this) ;
        level-- ;

        n.id = new IdentifierNode() ;
        level++ ;
        n.id.accept(this) ;
        level-- ;

        match(';') ;
    }

    // type --> type[num] | basic 
    // int i ; || int[2] j ;
    public void visit (TypeNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("TypeNode: " + look) ;

        //System.out.println("****** look: " + look) ;

        if (look.toString().equals("int"))
            n.basic = Type.Int ;
        else if (look.toString().equals("float"))
            n.basic = Type.Float ;
        
        match(Tag.BASIC) ;

        // If look is "[", this type should be array type.
        if (look.toString().equals("[")) {

            n.array = new ArrayTypeNode() ;
            level++ ;
            n.array.accept(this) ;
            level-- ;
        }
        // There might be an issue here
    }

    public void visit(ArrayTypeNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("ArrayTypeNode") ;
        //System.out.print("test ");
        match('[') ;
    
        n.size = ((Num)look).value ;

        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("Array Dimentsion: " + ((Num)look).value) ;

        //
        // NUM between '[' and ']' is a NumNode .
        // Do I have to visit NumNode ?
        //
        // for int[2],
        // ArrayTypeNode(2, null) vs. ArrayTypeNode(NumNode(), null) 
        //

        //For the question above, no because of the function under this comment. NumNode() calls match(Tag.NUM)
        match(Tag.NUM) ;

        match(']') ;

        if (look.toString().equals("[")) {
            n.type = new ArrayTypeNode() ;
            level++ ;
            n.type.accept(this) ;
            level-- ;
        }

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

            // Check if look.tag is ID | if | while | do | block
            switch (look.tag) {   

                case Tag.ID:
                    n.stmt = new AssignmentNode() ;
                    level++ ;
                    ((AssignmentNode)n.stmt).accept(this) ;
                    level-- ;

                    n.stmts = new Statements() ;
                    level++ ;
                    n.stmts.accept(this) ;
                    level-- ;

                    break ;

                    //There seems to not be one for block
                    case Tag.IF:
                    case Tag.WHILE:
                    case Tag.DO:
                        System.out.println("Need to impement for parsing") ;
                        error("did not implement yet");
                        System.exit(-1) ;
                        break;

                default:
                    if (look.toString().equals("{")) {
                        error("did not implement yet");
                        System.exit(-1) ;
                    }

                    // n.assign = new AssignmentNode() ;
                    // level++ ;       // N
                    // n.assign.accept(this) ;
                    // level-- ;       //N
                    
                    // n.stmts = new Statements() ;
                    // level++ ;       //N
                    // n.stmts.accept(this) ;
                    // level-- ;       //N

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
            
            rhs_assign = new NumNode() ;
            level++ ;
            ((NumNode)rhs_assign).accept(this) ;
            level-- ;
        }

        else if (look.tag == Tag.REAL) {

            rhs_assign = new RealNode() ;
            level++ ;
            ((RealNode)rhs_assign).accept(this) ;
            level-- ;
        }

        // a = 2 ;
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
                rhs = new NumNode() ;
                level++ ;
                ((NumNode)rhs).accept(this) ;
                level-- ;
            }
            else if (look.tag == Tag.REAL) {
                rhs = new RealNode() ;
                level++ ;
                ((RealNode)rhs).accept(this) ;
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

    public void visit(NumNode n) {

        n.value = ((Num)look).value ;

        match(Tag.NUM) ;  // expext look.tag == Tag.NUM

        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
        //System.out.println("look in IdentifierNode: " +  look);
    }

    public void visit(RealNode n) {

        n.value = ((Real)look).value ;

        match(Tag.REAL) ;  // expext look.tag == Tag.NUM

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
