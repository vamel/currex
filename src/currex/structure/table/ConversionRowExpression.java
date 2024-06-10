package currex.structure.table;

import currex.visitor.TableVisitor;

import java.util.List;

public record ConversionRowExpression(CurrencyIdentifierExpression currencyName, List<ConversionRateExpression> currencyRates) implements TableExpression {

    @Override
    public void accept(TableVisitor visitor) {
        visitor.visit(this);
    }
}
