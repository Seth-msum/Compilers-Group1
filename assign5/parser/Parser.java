package assign5.parser;

import assign5.visitor.* ;
import assign5.ast.* ;
import assign5.lexer.* ;

import java.io.* ;
//import java.util.ArrayList;
import java.util.Hashtable;


public class Parser extends ASTVisitor {
    //region
    public CompilationUnit cu   = null ;
    public Lexer    lexer       = null ;
    public Token    look        = null ;
    int             level       = 0 ;
    String          indent      = "..." ;
    Boolean         printMetaData = false ;
    //public  int     debugSkp     = 0 ;
    //endregion

    //public Hashtable<Lexer, ArrayList<Object>> declardvariables = new Hashtable<Lexer, ArrayList<Object>>() ;
    public Hashtable<String, IdentifierDescriptors> declaredArrays = new Hashtable<String, IdentifierDescriptors>() ;

    /*
    Word w = (Word)words.get(s) ;
    if (w != null )
        return w ;
    w = new Word(s, Tag.ID) ;
    words.put(s, w) ;
    return w ;
    declaredVariables.put(n.id.w, 1) ;
     */

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
    //region
    void move() {
        try { look = lexer.scan() ; }
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
                move() ;
            }
            else
                error("Syntax error") ;
        }
        catch (Error e) {
            System.out.println("Unnown error in match");
            System.exit(-1) ;
        }
    }
    //endregion

    ///////////////////////////////////

    //program --> block
    public void visit(CompilationUnit n) {

        System.out.println("CompilationUnit") ;
        n.block = new BlockStatementNode() ;
        level++ ;
        n.block.accept(this) ;
        level-- ;
        }

    // block --> { decls stmts }
    public void visit (BlockStatementNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("BlockStatmentNode");
        //System.out.println("Going into {} block.");
        match('{') ;

        n.decls = new Declarations() ;
        level++ ;
        n.decls.accept(this) ;
        level-- ;

        //Statements is an abstract for nesting.
        n.stmts = new Statements() ;
        n.stmts.accept(this) ;

        match('}') ;
        //System.out.println("Leaving {} block");

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
        
        IdentifierDescriptors metaData = new IdentifierDescriptors() ;

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("DeclarationNode") ; //For parser tracking

        n.type = new TypeNode(metaData) ;
        level++ ;
        n.type.accept(this) ;
        level-- ;

        // I belive  that there should be a carry value from 
        //      the type node to the identifier node.

        n.id = new IdentifierNode() ;
        level++ ;
        n.id.accept(this) ;
        level-- ;

        match(';') ;
        declaredArrays.put(n.id.id, metaData) ;

        //System.out.println("Leaving DeclarationNode");
    }

    // type--> basic | ArrayTypeNode
    public void visit (TypeNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("TypeNode: " + look) ;

        if (look.toString().equals("int"))
            n.basic = Type.Int ;
        else if (look.toString().equals("float"))
            n.basic = Type.Float ;
        else if (look.toString().equals("bool"))
            n.basic = Type.Bool ;
        else if (look.toString().equals("char"))
            n.basic = Type.Char ;
        else {
            System.out.println("Missing or unnown type: " + look.toString()) ;
            System.exit(-1) ;
        }
            
        n.metaData.type = n.basic ;
        match(Tag.BASIC) ;

        if (look.toString().equals("[")) {
            n.metaData.isArray = true ;
            n.metaData.arrayDimentions++ ;
            n.array = new ArrayTypeNode(n.metaData) ;
            level++ ;
            n.array.accept(this) ;
            level-- ;
        }
        // There might be an issue here
    }

    // ArrayTypeNOde --> type[num][[num]]^*
    public void visit(ArrayTypeNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("ArrayTypeNode") ;

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

        //For the question above, 
        //      no because of the function under this comment. NumNode() calls match(Tag.NUM)
        match(Tag.NUM) ;

        match(']') ;

        if (look.toString().equals("[")) {
            n.metaData.arrayDimentions++ ;
            n.type = new ArrayTypeNode(n.metaData) ;
            level++ ;
            n.type.accept(this) ;
            level-- ;
        }

    }


    ////////////////////////////////
    //
    // Stmts --> StatementNode Stmts | e
    //
    ///////////////////////////////

    public void visit(Statements n) {
        // for (int i = 0; i < level; i++) System.out.print(indent) ;
        // System.out.println("Statements[---");
        //If it's not the end bracket, then its another assignemt

        n.stmt = stmt() ;
        
        if (!look.toString().equals("}")) {
                n.stmts = new Statements() ;
                level++ ;
                n.stmts.accept(this) ;
                level-- ;
                // for (int i = 0; i < level; i++) System.out.print(indent) ;
                // System.out.println("Statements---]");
            }
        else {
                // for (int i = 0; i < level; i++) System.out.print(indent) ;
                // System.out.println("Statements---]");
            }
    }

    public StatementNode stmt() {
        //This does not accept() ;
       StatementNode tmp  = null; 

        if (!look.toString().equals("}")) {
            // Check if look.tag is ID | if | while | do | block
            switch (look.tag) {  
                case Tag.ID:
                    tmp = new AssignmentNode() ;
                    level++ ;
                    ((AssignmentNode)tmp).accept(this) ;
                    level-- ;
                    break ;

                case Tag.WHILE:
                    tmp = new WhileNode() ;
                    level++ ;
                    ((WhileNode)tmp).accept(this) ;
                    level-- ;
                    break ;

                case Tag.DO:
                    tmp = new DoNode() ;
                    level++ ;
                    ((DoNode)tmp).accept(this) ;
                    level-- ;
                    break ;

                case Tag.IF:
                    tmp = new IfNode() ;
                    level++ ;
                    ((IfNode)tmp).accept(this) ;
                    level-- ;
                    break;

                case '{': //block
                    //System.out.println("Going into {} block.");
                    tmp = new BlockStatementNode() ;
                    level++ ;
                    ((BlockStatementNode)tmp).accept(this) ;
                    level-- ;
                    //System.out.println("Leaving {} block");
                    break ;
                
                case Tag.BREAK:
                    
                    match(Tag.BREAK);
                    tmp = new BreakNode() ;
                    for (int i = 0; i < level; i++) System.out.print(indent) ;
                    System.out.println("Break");
                    match(';') ;
                    
                    break ;

                default:
                        error("{did not implement yet}");
                        System.exit(-1) ;
                    break;
            
            }
        }
        return tmp ;
        
    }

    // stmt --> loc = bool ;
    public void visit (AssignmentNode n) { // major changes

        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("AssignmentNode");

        n.left = new Locations() ;        
        level++ ;
        n.left.accept(this) ;
        level-- ;

        match('=') ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("operator: =") ;

        Node rhs_assign = null ;
        if (look.tag == Tag.ID) {

            rhs_assign = new Locations() ;
            level++ ;
            ((Locations)rhs_assign).accept(this) ;
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
        //System.out.println("Closing Assignment");
    }
    
    // stmt --> while (bool) stmt 
    public void visit(WhileNode n) {

        //Declare in WhileNode
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("WhileNode");

        //move past while keyword.
        match(Tag.WHILE) ;
        //move past (
        match('(') ;

        //Start of bool
        Node lhs_assign = null ;
        if (look.tag == Tag.ID) {
            lhs_assign = new Locations() ;
            level++ ;
            ((Locations)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.NUM) {
            lhs_assign = new NumNode() ;
            level++ ;
            ((NumNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.REAL) {
            lhs_assign = new RealNode() ;
            level++ ;
            ((RealNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE) {
            lhs_assign = new BoolNode(look.tag) ;
            level++ ;
            ((BoolNode)lhs_assign).accept(this) ;
            level-- ;
        }


        if (look.tag == ')') {// e.g. while (b|5|4.19);
            //System.out.println("found edge");
            n.left = lhs_assign ;
        } else { // e.g. while (b=5) ;
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;

            level++ ;
            // Build AST for binary expressions with operator precedence
            n.left = (BinExprNode) parseBinExprNode( lhs_assign, 0) ;
            level-- ;

            System.out.println("**** Root Node operator: " + ((BinExprNode)n.left).op) ;
        }
        //move past and finnish )
        match(')') ;

        // move past stmt
        // match('{') ;
        level++ ;
        n.right = stmt() ;
        level-- ;
        // match('}') ;
    }

    // stmt --> do stmt while (bool) ;
    public void visit(DoNode n) {

        //Declare in DoNode
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("DoNode");

        // move past do key
        match(Tag.DO);

        level++ ;
        n.left  = stmt() ;
        level-- ;

        match(Tag.WHILE) ;
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("WhileForDo");
        //System.out.println("Moving past DO While");
        match('(') ;

        //Start of bool
        Node lhs_assign = null ;
        if (look.tag == Tag.ID) {
            lhs_assign = new Locations() ;
            level++ ;
            ((Locations)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.NUM) {
            lhs_assign = new NumNode() ;
            level++ ;
            ((NumNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.REAL) {
            lhs_assign = new RealNode() ;
            level++ ;
            ((RealNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE) {
            lhs_assign = new BoolNode(look.tag) ;
            level++ ;
            ((BoolNode)lhs_assign).accept(this) ;
            level-- ;
        }


        if (look.tag == ')') {// e.g. while (b|5|4.19);
            //System.out.println("found edge");
            n.right = lhs_assign ;
        } else { // e.g. while (b=5) ;
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;

            level++ ;
            // Build AST for binary expressions with operator precedence
            n.right = (BinExprNode) parseBinExprNode( lhs_assign, 0) ;
            level-- ;

            System.out.println("**** Root Node operator: " + ((BinExprNode)n.right).op) ;
        }
        //move past and finnish )
        match(')') ;

        match(';') ;
    }
    // stmt --> if ( bool ) stmt [else stmt]
    public void visit (IfNode n) {
        
        for (int i = 0; i < level; i++) System.out.print(indent) ;
        System.out.println("IfNode");

        match(Tag.IF) ;

        match('(') ;

        //Start of bool
        Node lhs_assign = null ;
        if (look.tag == Tag.ID) {
            lhs_assign = new Locations() ;
            level++ ;
            ((Locations)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.NUM) {
            lhs_assign = new NumNode() ;
            level++ ;
            ((NumNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.REAL) {
            lhs_assign = new RealNode() ;
            level++ ;
            ((RealNode)lhs_assign).accept(this) ;
            level-- ;
        }
        else if (look.tag == Tag.TRUE || look.tag == Tag.FALSE) {
            lhs_assign = new BoolNode(look.tag) ;
            level++ ;
            ((BoolNode)lhs_assign).accept(this) ;
            level-- ;
        }

        if (look.tag == ')') {// e.g. while (b|5|4.19);
            //System.out.println("found edge");
            n.left = lhs_assign ;
        } else { // e.g. while (b=5) ;
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;

            level++ ;
            // Build AST for binary expressions with operator precedence
            n.left = (BinExprNode) parseBinExprNode( lhs_assign, 0) ;
            level-- ;

            System.out.println("**** Root Node operator: " + ((BinExprNode)n.left).op) ;
        }
        //move past and finnish )
        match(')') ;

        level++ ;
        n.right  = stmt() ;
        level-- ;

        if (look.tag == Tag.ELSE) {
            match(Tag.ELSE);
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("else") ;
            level++ ;
            n.theElse  = stmt() ;
            level-- ;
        }

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
            case '<':
            case '>':
            case Tag.LE:
            case Tag.GE:
                  return 9 ;  //relational
            case Tag.EQ:
            case Tag.NE:
                  return 8 ;  //equality
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
            //System.out.println("Seeing" + look.toString());
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

    public void visit(BoolNode n) {

        if (n.v == null) {
            if (look.tag == Tag.TRUE) {
                n.value = true ;
                n.v = Word.True ;
            }
            else if (look.tag == Tag.FALSE) {
                n.value = false ;
                n.v = Word.True ;
            }
            
        }
        match(n.v.tag) ; //Judahs sus solution
        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
    }
    
    //
    public void visit(Locations n) {
        
        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("Locations");

        level++ ;
        n.left = new IdentifierNode() ;
        n.left.accept(this) ;
        level-- ;

        if (look.tag == '[') {
            level++ ;
            n.right = new LocationNode() ;
            n.right.accept(this) ;
            level-- ;
        }

    }

    // loc --> loc[bool] | id
    public void visit(LocationNode n) {

        for (int i = 0; i < level; i++) System.out.print(indent) ;        //N
        System.out.println("LocationNode");

        match('[') ;

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

        if (look.tag == ']') {// e.g. a = 19 ;
            
            n.left = rhs_assign ;
        }
        else { // e.g. b = a + b * c ;
            // This is the start of the precedence climbing method.
            for (int i = 0; i < level; i++) System.out.print(indent) ;
            System.out.println("operator: " + look) ;
            level++ ;
            // Build AST for binary expressions with operator precedence
            n.left = (BinExprNode) parseBinExprNode( rhs_assign, 0) ;
            level-- ;
            System.out.println("**** Root Node operator: " + ((BinExprNode)n.left).op) ;
        }
        match(']') ;

        if (look.tag == '[') {
            level++ ;
            n.right = new LocationNode() ;
            n.right.accept(this) ;
            level-- ;
        }

    }

    public void visit(IdentifierNode n) {

        n.id = look.toString() ;

        match(Tag.ID) ; // expect look.tag == Tag.ID

        for (int i = 0; i < level; i++ ) System.out.print(indent) ;
        n.printNode() ;
        IdentifierDescriptors metaCheck = (IdentifierDescriptors)declaredArrays.get(n.id) ;
        if (metaCheck != null && printMetaData) {
            metaCheck.printMeta();
        }
        //System.out.println("IdentifierNode: " + n.id) ;
        //System.out.println("look in IdentifierNode: " + look) ;

    }
    
}
