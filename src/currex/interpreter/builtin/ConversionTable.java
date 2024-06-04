package currex.interpreter.builtin;

import currex.structure.table.ConversionRateExpression;
import currex.structure.table.ConversionRowExpression;
import currex.structure.table.CurrencyIdentifierExpression;
import currex.structure.table.TableStatement;

import java.util.ArrayList;
import java.util.List;

public class ConversionTable {
    private final List<String> columnCurrencies = new ArrayList<>();
    private final List<String> rowCurrencies = new ArrayList<>();
    private final List<List<Double>> currencyTable = new ArrayList<>();

    public ConversionTable(TableStatement currencyTable) {
        for (CurrencyIdentifierExpression currencyName : currencyTable.currencyRow().currencyNames()) {
            putColumnCurrency(currencyName.name());
        }
        for (ConversionRowExpression conversionRow : currencyTable.conversionRows()) {
            String currentCurrency = conversionRow.currencyName().name();
            putRowCurrency(currentCurrency);
            List<Double> conversionList = new ArrayList<>();
            for (ConversionRateExpression rate : conversionRow.currencyRates()) {
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
        currencyTable.add(currencyRow);
    }

    public List<String> getColumnCurrencies() {
        return columnCurrencies;
    }

    public List<String> getRowCurrencies() {
        return rowCurrencies;
    }

    public List<List<Double>> getCurrencyTable() {
        return currencyTable;
    }
}
