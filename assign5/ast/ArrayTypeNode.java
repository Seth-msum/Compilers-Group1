package assign5.ast;


import assign5.visitor.* ;

public class ArrayTypeNode extends TypeNode {

    public TypeNode type ;
    public int size = 1 ;
    public IdentifierDescriptors metaData ;

    public ArrayTypeNode () {

    }

    public ArrayTypeNode (IdentifierDescriptors metaData) {
        this.metaData = metaData ;
    }

    public ArrayTypeNode (int size, TypeNode type) {
        
        this.size = size ;
        this.type = type ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this) ;
    }
    
}
