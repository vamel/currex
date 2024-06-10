package currex.structure.table;

import currex.visitor.TableVisitor;

import java.util.List;

public record CurrencyRowExpression(List<CurrencyIdentifierExpression> currencyNames) implements TableExpression {

    @Override
    public void accept(TableVisitor visitor) {
        visitor.visit(this);
    }
}
