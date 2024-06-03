package currex.interpreter;

import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.utils.CurrexConfig;
import currex.visitor.Visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Interpretable, Visitor {
    private final ContextManager contextManager = new ContextManager();
    private final Map<String, FunctionDefinition> functionDefinitions = new HashMap<>();
    private Value lastResult = null;
    private boolean isReturn = false;


    private Value copyLastResult() {
        Value lastResultCopy = lastResult;
        lastResult = null;
        return lastResultCopy;
    }

    @Override
    public void run(Program program) {
        visit(program);
    }

    @Override
    public void visit(Program program) {
        functionDefinitions.putAll(program.functionDefinitions());
        FunctionCallExpression main = new FunctionCallExpression(CurrexConfig.MAIN_FUNCTION_NAME, List.of());
        main.accept(this);
    }

    @Override
    public void visit(Block block) {
        for (Statement statement : block.statementList()) {
            statement.accept(this);
            if (isReturn) break;
        }
    }

    @Override
    public void visit(FunctionDefinition functionDefinition) {

    }

    @Override
    public void visit(Parameter parameter) {

    }

    @Override
    public void visit(DeclarationStatement declarationStatement) {
        Context currentContext = contextManager.getLastContext();
        declarationStatement.expression().accept(this);
        if (lastResult.valueType() != declarationStatement.type()) {
            System.out.println("INVALID VARIABLE TYPE");
        }
        Variable variable = new Variable(declarationStatement.name(), new Value(
                declarationStatement.type(), lastResult));
        currentContext.addVariable(variable);
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        assignmentStatement.left().accept(this);
        Value leftValue = copyLastResult();
        assignmentStatement.right().accept(this);
        if (lastResult != null) {
            Value rightValue = copyLastResult();
            if (assignmentStatement.left().getClass() == IdentifierExpression.class) {
                String variableName = ((IdentifierExpression) assignmentStatement.left()).name();
                contextManager.updateVariable(variableName, rightValue);
            }
        }
    }

    @Override
    public void visit(ReturnStatement returnStatement) {
        if (returnStatement.expression() != null) {
            returnStatement.expression().accept(this);
            isReturn = true;
        }
        else {
            copyLastResult();
        }
        System.out.println("RETURN:" + lastResult);
    }

    @Override
    public void visit(WhileStatement whileStatement) {

    }

    @Override
    public void visit(IfStatement ifStatement) {

    }

    @Override
    public void visit(ElseStatement elseStatement) {

    }

    @Override
    public void visit(OrExpression orExpression) {

    }

    @Override
    public void visit(AndExpression andExpression) {

    }

    @Override
    public void visit(GreaterExpression greaterExpression) {

    }

    @Override
    public void visit(LesserExpression lesserExpression) {

    }

    @Override
    public void visit(EqualExpression equalExpression) {

    }

    @Override
    public void visit(NotEqualExpression notEqualExpression) {

    }

    @Override
    public void visit(GreaterOrEqualExpression greaterOrEqualExpression) {

    }

    @Override
    public void visit(LesserOrEqualExpression lesserOrEqualExpression) {

    }

    @Override
    public void visit(AdditionExpression additionExpression) {
        additionExpression.left().accept(this);
        Value left = lastResult;
        additionExpression.right().accept(this);
        Value right = lastResult;
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() + rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() + rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (currencyLeft.getName().equals(currencyRight.getName())) {
                BigDecimal currencyValue = currencyLeft.getValue().add(currencyRight.getValue());
                currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
                CurrencyPrimitive primitive = new CurrencyPrimitive(currencyValue, currencyLeft.getName());
                lastResult = new Value(PrimitiveType.CURRENCY, primitive);
            }
            else {
                System.out.println("INCOMPATIBLE CURRENCY TYPES");
            }
        }
        else {
            System.out.println("ADDITION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(SubtractionExpression subtractionExpression) {
        subtractionExpression.left().accept(this);
        Value left = lastResult;
        subtractionExpression.right().accept(this);
        Value right = lastResult;
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() - rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() - rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            System.out.println(left);
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (currencyLeft.getName().equals(currencyRight.getName())) {
                BigDecimal currencyValue = currencyLeft.getValue().subtract(currencyRight.getValue());
                currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
                CurrencyPrimitive primitive = new CurrencyPrimitive(currencyValue, currencyLeft.getName());
                lastResult = new Value(PrimitiveType.CURRENCY, primitive);
            }
            else {
                System.out.println("INCOMPATIBLE CURRENCY TYPES");
            }
        }
        else {
            System.out.println("SUBTRACTION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(MultiplicationExpression multiplicationExpression) {
        multiplicationExpression.left().accept(this);
        Value left = lastResult;
        multiplicationExpression.right().accept(this);
        Value right = lastResult;
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() * rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() * rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (currencyLeft.getName().equals(currencyRight.getName())) {
                BigDecimal currencyValue = currencyLeft.getValue().multiply(currencyRight.getValue());
                currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
                CurrencyPrimitive primitive = new CurrencyPrimitive(currencyValue, currencyLeft.getName());
                lastResult = new Value(PrimitiveType.CURRENCY, primitive);
            }
            else {
                System.out.println("INCOMPATIBLE CURRENCY TYPES");
            }
        }
        else {
            System.out.println("MULTIPLICATION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(DivisionExpression divisionExpression) {
        divisionExpression.left().accept(this);
        Value left = lastResult;
        divisionExpression.right().accept(this);
        Value right = lastResult;
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            if (rightValue.value().equals(0)) {
                System.out.println("DIVISION BY ZERO NOT POSSIBLE");
            }
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() / rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            if (rightValue.value().equals(0.0)) {
                System.out.println("DIVISION BY ZERO NOT POSSIBLE");
            }
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() / rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (currencyRight.getValue().equals(BigDecimal.valueOf(0.0))) {
                System.out.println("DIVISION BY ZERO NOT POSSIBLE");
            }
            if (currencyLeft.getName().equals(currencyRight.getName())) {
                BigDecimal currencyValue = currencyLeft.getValue().divide(currencyRight.getValue(), RoundingMode.HALF_DOWN);
                currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
                CurrencyPrimitive primitive = new CurrencyPrimitive(currencyValue, currencyLeft.getName());
                lastResult = new Value(PrimitiveType.CURRENCY, primitive);
            }
            else {
                System.out.println("INCOMPATIBLE CURRENCY TYPES");
            }
        }
        else {
            System.out.println("DIVISION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(CurrencyCastExpression currencyCastExpression) {

    }

    @Override
    public void visit(CurrencyConversionExpression currencyConversionExpression) {

    }

    @Override
    public void visit(NegationExpression negationExpression) {
        negationExpression.accept(this);
        Value result = lastResult;
        if (result.valueType() == PrimitiveType.BOOL) {
            lastResult = new Value(PrimitiveType.BOOL, !(Boolean) result.value());
        }
        else {
            System.out.println("NEGATION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(MinusExpression minusExpression) {
        minusExpression.accept(this);
        Value result = lastResult;
        if (result.valueType() == PrimitiveType.INTEGER) {
            lastResult = new Value(result.valueType(), -(Integer) result.value());
        }
        else if (result.valueType() == PrimitiveType.FLOAT) {
            lastResult = new Value(result.valueType(), -(Double) result.value());
        }
        else if (result.valueType() == PrimitiveType.CURRENCY) {
            BigDecimal value = BigDecimal.valueOf(-(Double) result.value());
            lastResult = new Value(result.valueType(), value);
        }
        else {
            System.out.println("NEGATION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(AccessExpression accessExpression) {
    }

    @Override
    public void visit(FunctionCallExpression functionCallExpression) {
        if (!functionDefinitions.containsKey(functionCallExpression.name())) {
            System.out.println("NO FUNCTION WITH THIS NAME ERROR");
        }
        FunctionDefinition functionDefinition = functionDefinitions.get(functionCallExpression.name());
        if (functionDefinition.parameters().size() != functionCallExpression.arguments().size()) {
            System.out.println("INVALID NUMBER OF ARGUMENTS FOR FUNCTION XYZ, EXPECTED: Y BUT RECEIVED: X");
        }
        List<Expression> functionArguments = functionCallExpression.arguments();
        Context functionCallContext = new Context(new HashMap<>());
        for (int i = 0; i < functionArguments.size(); i++) {
            functionArguments.get(i).accept(this);
            Parameter param = functionDefinition.parameters().get(i);
            if (lastResult.valueType() != param.type()) {
                System.out.println("INVALID ARGUMENT TYPE");
            }
            else {
                Variable variable = new Variable(param.name(), new Value(param.type(), lastResult));
                functionCallContext.addVariable(variable);
            }
        }
        contextManager.addContext(functionCallContext);
        functionDefinition.block().accept(this);
        if (functionDefinition.returnType() == PrimitiveType.NONE) {
            lastResult = null;
        }
        else if (lastResult.valueType() != functionDefinition.returnType()) {
            System.out.println("INVALID RETURN TYPE");
        }
        else {
            System.out.println("NO RETURN VALUE SPECIFIED");
        }
        contextManager.removeCurrentContext();
    }

    @Override
    public void visit(IdentifierExpression identifierExpression) {
        Variable variable = contextManager.fetchVariable(identifierExpression.name());
        if (variable == null) {
            System.out.println("VariableDoesNotExistError(VARIABLE " + identifierExpression.name() + " DOES NOT EXIST IN ANY CONTEXT");
        }
        else {
            lastResult = variable.value();
        }
    }

    @Override
    public void visit(IntPrimitive intPrimitive) {
        lastResult = new Value(PrimitiveType.INTEGER, intPrimitive);
    }

    @Override
    public void visit(FloatPrimitive floatPrimitive) {
        lastResult = new Value(PrimitiveType.FLOAT, floatPrimitive);
    }

    @Override
    public void visit(StringPrimitive stringPrimitive) {
        lastResult = new Value(PrimitiveType.STRING, stringPrimitive);
    }

    @Override
    public void visit(BoolPrimitive boolPrimitive) {
        lastResult = new Value(PrimitiveType.BOOL, boolPrimitive);
    }

    @Override
    public void visit(CurrencyPrimitive currencyPrimitive) {
        lastResult = new Value(PrimitiveType.CURRENCY, currencyPrimitive);
    }
}
