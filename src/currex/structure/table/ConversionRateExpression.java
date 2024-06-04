package currex.structure.table;

import currex.visitor.TableVisitor;

public record ConversionRateExpression(Double conversion) implements TableExpression {

    @Override
    public void accept(TableVisitor visitor) {
        visitor.visit(this);
    }
}
