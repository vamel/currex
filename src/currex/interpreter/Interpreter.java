package currex.interpreter;

import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.visitor.Visitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
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

    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {

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
            lastResult = new Value(PrimitiveType.INTEGER, (Integer) left.value() + (Integer) right.value());
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            lastResult = new Value(PrimitiveType.FLOAT, (Double) left.value() + (Double) right.value());
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            BigDecimal currencyValue = BigDecimal.valueOf((Double) left.value() + (Double) right.value());
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, currencyValue);
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
            lastResult = new Value(PrimitiveType.INTEGER, (Integer) left.value() - (Integer) right.value());
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            lastResult = new Value(PrimitiveType.FLOAT, (Double) left.value() - (Double) right.value());
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            BigDecimal currencyValue = BigDecimal.valueOf((Double) left.value() - (Double) right.value());
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, currencyValue);
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
            lastResult = new Value(PrimitiveType.INTEGER, (Integer) left.value() * (Integer) right.value());
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            lastResult = new Value(PrimitiveType.FLOAT, (Double) left.value() * (Double) right.value());
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            BigDecimal currencyValue = BigDecimal.valueOf((Double) left.value() * (Double) right.value());
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, currencyValue);
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
            lastResult = new Value(PrimitiveType.INTEGER, (Integer) left.value() / (Integer) right.value());
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            lastResult = new Value(PrimitiveType.FLOAT, (Double) left.value() / (Double) right.value());
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            BigDecimal currencyValue = BigDecimal.valueOf((Double) left.value() / (Double) right.value());
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, currencyValue);
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

    }

    @Override
    public void visit(MinusExpression minusExpression) {

    }

    @Override
    public void visit(AccessExpression accessExpression) {

    }

    @Override
    public void visit(FunctionCallExpression functionCallExpression) {

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
