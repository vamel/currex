package currex.interpreter;

import org.junit.Assert;
import org.junit.Test;

public class ContextManagerTest {
    @Test
    public void createContextManager() {
        ContextManagerMock contextManager = new ContextManagerMock();
        Assert.assertEquals(contextManager.getContexts().size(), 0);
    }
}
