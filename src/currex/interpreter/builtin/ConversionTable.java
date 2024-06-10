package currex.interpreter.builtin;

import currex.interpreter.error.InterpreterErrorHandler;
import currex.parser.error.InvalidCurrencyRateError;
import currex.structure.table.ConversionRateExpression;
import currex.structure.table.ConversionRowExpression;
import currex.structure.table.CurrencyIdentifierExpression;
import currex.structure.table.TableStatement;

import java.util.ArrayList;
import java.util.List;

public class ConversionTable {
    private final List<String> columnCurrencies = new ArrayList<>();
    private final List<String> rowCurrencies = new ArrayList<>();
    private final List<List<Double>> conversionTable = new ArrayList<>();
    private final InterpreterErrorHandler errorHandler = new InterpreterErrorHandler();

    public ConversionTable(TableStatement currencyTable) throws Exception {
        for (CurrencyIdentifierExpression currencyName : currencyTable.currencyRow().currencyNames()) {
            putColumnCurrency(currencyName.name());
        }
        for (ConversionRowExpression conversionRow : currencyTable.conversionRows()) {
            String currentCurrency = conversionRow.currencyName().name();
            putRowCurrency(currentCurrency);
            int columnIndex = 0;
            List<Double> conversionList = new ArrayList<>();
            for (ConversionRateExpression rate : conversionRow.currencyRates()) {
                if (currentCurrency.equals(columnCurrencies.get(columnIndex))) {
                    if (rate.conversion() != 1.0) {
                        errorHandler.handleInterpreterError(new InvalidCurrencyRateError("CURRENCY RATE " +
                                "FOR CURRENCY " + currentCurrency + " IS EQUAL TO " +
                                rate.conversion() + " AND NOT TO 1!"));
                    }
                }
                columnIndex++;
                conversionList.add(rate.conversion());
            }
            putConversionRow(conversionList);
        }
    }

    public void putColumnCurrency(String currency) {
        columnCurrencies.add(currency);
    }

    public void putRowCurrency(String currency) {
        rowCurrencies.add(currency);
    }

    public void putConversionRow(List<Double> currencyRow) {
        conversionTable.add(currencyRow);
    }

    public List<String> getColumnCurrencies() {
        return columnCurrencies;
    }

    public List<String> getRowCurrencies() {
        return rowCurrencies;
    }

    public List<List<Double>> getConversionTable() {
        return conversionTable;
    }
}
