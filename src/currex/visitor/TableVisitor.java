package currex.visitor;

import currex.structure.table.*;

public interface TableVisitor {

    void visit(TableStatement tableStatement);
    void visit(CurrencyRowExpression currencyRow);
    void visit(ConversionRowExpression conversionRow);
    void visit(ConversionRateExpression conversionRate);
    void visit(CurrencyIdentifierExpression currencyIdentifierExpression);
}
