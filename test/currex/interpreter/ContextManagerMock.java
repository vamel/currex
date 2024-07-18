package currex.interpreter;

import currex.structure.primitives.CurrencyPrimitive;
import currex.structure.primitives.PrimitiveType;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

public class ContextManagerMock extends ContextManager {
    private final Deque<Context> contexts = new ArrayDeque<>();

    public ContextManagerMock() {
        Context firstContext = new Context();
        addContext(firstContext);
        Context context = new Context();
        Value valueInt = new Value(PrimitiveType.INTEGER, 1);
        Value valueFloat = new Value(PrimitiveType.FLOAT, 10.10);
        Value valueString = new Value(PrimitiveType.STRING, "value");
        Value valueBool = new Value(PrimitiveType.BOOL, false);
        Value valueCurrency = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(BigDecimal.valueOf(4.99), "EUR"));
        context.addVariable("valueInt", valueInt);
        context.addVariable("valueFloat", valueFloat);
        context.addVariable("valueString", valueString);
        context.addVariable("valueBool", valueBool);
        context.addVariable("valueCurrency", valueCurrency);
        addContext(context);
    }

    public Deque<Context> getContexts() {
        return contexts;
    }

    public Context getCurrentContext() {
        return contexts.getLast();
    }
}
