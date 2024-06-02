package currex.interpreter;

import currex.structure.primitives.PrimitiveType;

public record Value(PrimitiveType valueType, Object value) {

}
