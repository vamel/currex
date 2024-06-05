package currex.interpreter.builtin;

import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.primitives.PrimitiveType;

import java.util.List;
import java.util.Map;

public class Functions {
    public static final Map<String, FunctionDefinition> FUNCTIONS = Map.ofEntries(
            Map.entry("print", new FunctionDefinition(
                    PrimitiveType.NONE,
                    "print",
                    List.of(new Parameter(PrimitiveType.NONE, "valueToPrint")),
                    new Block(List.of(new PrintFunction())))),
            Map.entry("getBalance", new FunctionDefinition(
                    PrimitiveType.FLOAT,
                    "getBalance",
                    List.of(),
                    new Block(List.of(new GetBalanceMethod())))),
            Map.entry("getCurrency", new FunctionDefinition(
                    PrimitiveType.FLOAT,
                    "getCurrency",
                    List.of(),
                    new Block(List.of(new GetCurrencyMethod()))))
    );
}
