package currex.visitor;

public interface Visitable {
    void accept(Visitor visitor) throws Exception;
}
