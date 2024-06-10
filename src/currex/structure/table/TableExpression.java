package currex.structure.table;

import currex.visitor.TableVisitor;

public interface TableExpression {
    void accept(TableVisitor visitor);
}
