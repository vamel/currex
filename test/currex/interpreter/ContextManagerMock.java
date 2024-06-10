package currex.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContextManagerMock extends ContextManager {
    private final Deque<Context> contexts = new ArrayDeque<>();

    public Deque<Context> getContexts() {
        return contexts;
    }
}
