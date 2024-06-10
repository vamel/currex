package currex.structure.table;

import currex.visitor.TableVisitor;

public record CurrencyIdentifierExpression(String name) implements TableExpression {

    @Override
    public void accept(TableVisitor visitor) {
        visitor.visit(this);
    }
}
