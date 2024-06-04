package currex.interpreter.builtin;

import java.util.ArrayList;
import java.util.List;

public class ConversionTable {
    private List<String> columnCurrencies;
    private List<String> rowCurrencies;
    private List<List<Double>> currencyTable = new ArrayList<>();

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
