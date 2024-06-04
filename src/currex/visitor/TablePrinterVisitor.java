package currex.visitor;

import currex.structure.table.*;

public class TablePrinterVisitor implements TableVisitor {

    private int identation;
    private final String ident;

    public TablePrinterVisitor() {
        this.identation = 0;
        this.ident = "\t";
    }

    @Override
    public void visit(TableStatement tableStatement) {
        System.out.println(ident.repeat(identation) + "<CurrencyTable>");
        tableStatement.currencyRow().accept(this);
        for (ConversionRowExpression conversionRow: tableStatement.conversionRows()) {
            conversionRow.accept(this);
        }
    }

    @Override
    public void visit(CurrencyRowExpression currencyRow) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) + "<CurrencyRowExpression>");
        for (CurrencyIdentifierExpression identifier : currencyRow.currencyNames()) {
            identifier.accept(this);
        }
        decreaseIdendation();
    }

    @Override
    public void visit(ConversionRowExpression conversionRow) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) + "<ConversionRowExpression>");
        conversionRow.currencyName().accept(this);
        for (ConversionRateExpression conversion : conversionRow.currencyRates()) {
            conversion.accept(this);
        }
        decreaseIdendation();
    }

    @Override
    public void visit(ConversionRateExpression conversionRate) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) + "<ConversionRateExpression value=" + conversionRate.conversion() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(CurrencyIdentifierExpression currencyIdentifierExpression) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) + "<CurrencyIdentifierExpression name=" + currencyIdentifierExpression.name() + ">");
        decreaseIdendation();
    }

    private void increaseIdentation() {
        identation++;
    }

    private void decreaseIdendation() {
        identation--;
    }
}
