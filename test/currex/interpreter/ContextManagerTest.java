package currex.interpreter;

import currex.interpreter.error.InvalidVariableTypeError;
import currex.interpreter.error.VariableAlreadyExistsError;
import currex.interpreter.error.VariableDoesNotExistError;
import currex.structure.primitives.PrimitiveType;
import org.junit.Assert;
import org.junit.Test;

public class ContextManagerTest {

    @Test
    public void createContextManager() throws Exception {
        ContextManagerMock contextManager = new ContextManagerMock();
        Assert.assertEquals(1, contextManager.getContexts().size());
        Assert.assertEquals(5, contextManager.getCurrentContext().getVariables().size());
    }

    @Test
    public void ThrowVariableAlreadyExistsError() {
        ContextManagerMock contextManager = new ContextManagerMock();
        Value val = new Value(PrimitiveType.INTEGER, 11);
        VariableAlreadyExistsError e = Assert.assertThrows(VariableAlreadyExistsError.class, () -> {
            contextManager.addVariable("valueInt", val);
        });
    }

    @Test
    public void ThrowInvalidVariableTypeError() {
        ContextManagerMock contextManager = new ContextManagerMock();
        Value val = new Value(PrimitiveType.INTEGER, 11);
        InvalidVariableTypeError e = Assert.assertThrows(InvalidVariableTypeError.class, () -> {
            contextManager.updateVariable("valueString", val);
        });
    }

    @Test
    public void ThrowVariableDoesNotExistErrorOnUpdate() {
        ContextManagerMock contextManager = new ContextManagerMock();
        Value val = new Value(PrimitiveType.INTEGER, 11);
        VariableDoesNotExistError e = Assert.assertThrows(VariableDoesNotExistError.class, () -> {
            contextManager.updateVariable("valueInt2", val);
        });
    }

    @Test
    public void ThrowVariableDoesNotExistErrorOnFetch() {
        ContextManagerMock contextManager = new ContextManagerMock();
        VariableDoesNotExistError e = Assert.assertThrows(VariableDoesNotExistError.class, () -> {
            contextManager.fetchVariable("valueInt2");
        });
    }
}
