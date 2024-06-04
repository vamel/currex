package currex.structure.table;

import currex.visitor.TableVisitor;

import java.util.List;

public record TableStatement(CurrencyRowExpression currencyRow, List<ConversionRowExpression> conversionRows) implements TableExpression {

    @Override
    public void accept(TableVisitor visitor) {
        visitor.visit(this);
    }
}
