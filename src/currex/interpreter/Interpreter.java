package currex.interpreter;

import currex.interpreter.builtin.*;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.utils.CurrexConfig;
import currex.visitor.Visitor;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Interpretable, Visitor {
    private final ContextManager contextManager = new ContextManager();
    private final Map<String, FunctionDefinition> functionDefinitions = new HashMap<>(Functions.FUNCTIONS);
    private final ConversionTable conversionTable;
    private final PrintStream printer;
    private Value lastResult = null;
    private boolean isReturn = false;
    private boolean isIfStatement = false;


    public Interpreter(ConversionTable conversionTable, PrintStream printer) {
        this.conversionTable = conversionTable;
        this.printer = printer;
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
            if (isReturn)  {
                isReturn = false;
                break;
            }
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
        declarationStatement.expression().accept(this);
        if (lastResult.valueType() != declarationStatement.type()) {
            System.out.println("INVALID VARIABLE TYPE");
        }
        contextManager.addVariable(declarationStatement.name(), new Value(
                declarationStatement.type(), lastResult.value()));
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) {
        assignmentStatement.left().accept(this);
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
        whileStatement.expression().accept(this);
        Value check = copyLastResult();
        BoolPrimitive checkValue = (BoolPrimitive) check.value();
        while (checkValue.value()) {
            contextManager.addContext(new Context());
            whileStatement.block().accept(this);
            contextManager.removeCurrentContext();
            whileStatement.expression().accept(this);
            checkValue = (BoolPrimitive) lastResult.value();
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        List<ElseStatement> elseStatements = ifStatement.conditionalStatements();
        for (int i = 0; i < elseStatements.size(); i++) {
            if (isIfStatement) {
                isIfStatement = false;
                break;
            }
            ElseStatement elseStatement = elseStatements.get(i);
            if (elseStatement.expression() != null) {
                elseStatement.accept(this);
            }
            else if (i == elseStatements.size() - 1) {
                contextManager.addContext(new Context());
                elseStatement.block().accept(this);
                contextManager.removeCurrentContext();
            }
        }
    }

    @Override
    public void visit(ElseStatement elseStatement) {
        elseStatement.expression().accept(this);
        Value check = copyLastResult();
        if (check.valueType() != PrimitiveType.BOOL) {
            System.out.println("Evaluated expression does not give a bool value");
        }
        BoolPrimitive leftBool = (BoolPrimitive) check.value();
        if (leftBool.value()) {
            isIfStatement = true;
            contextManager.addContext(new Context());
            elseStatement.block().accept(this);
            contextManager.removeCurrentContext();
        }
    }

    @Override
    public void visit(OrExpression orExpression) {
        orExpression.left().accept(this);
        Value left = copyLastResult();
        if (left.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive leftBool = (BoolPrimitive) left.value();
            if (leftBool.value()) {
                lastResult = new Value(PrimitiveType.BOOL, true);
                return;
            }
        }
        orExpression.right().accept(this);
        Value right = copyLastResult();
        if (right.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive rightBool = (BoolPrimitive) right.value();
            if (rightBool.value()) {
                lastResult = new Value(PrimitiveType.BOOL, true);
            }
            else {
                lastResult = new Value(PrimitiveType.BOOL, false);
            }
            return;
        }
        System.out.println("OR STATEMENT CANNOT BE EVALUATED!");
    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.left().accept(this);
        Value left = copyLastResult();
        if (left.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive leftBool = (BoolPrimitive) left.value();
            if (!leftBool.value()) {
                lastResult = new Value(PrimitiveType.BOOL, false);
                return;
            }
        }
        andExpression.right().accept(this);
        Value right = copyLastResult();
        if (right.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive rightBool = (BoolPrimitive) right.value();
            if (rightBool.value()) {
                lastResult = new Value(PrimitiveType.BOOL, true);
            }
            else {
                lastResult = new Value(PrimitiveType.BOOL, false);
            }
            return;
        }
        System.out.println("AND STATEMENT CANNOT BE EVALUATED!");
    }

    @Override
    public void visit(EqualExpression equalExpression) {
        equalExpression.left().accept(this);
        Value left = copyLastResult();
        equalExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.BOOL && right.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive leftBool = (BoolPrimitive) left.value();
            BoolPrimitive rightBool = (BoolPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftBool.value() == rightBool.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    currencyLeft.getName().equals(currencyRight.getName()) &&
                            currencyLeft.getValue().equals(currencyRight.getValue())));
        }
    }

    @Override
    public void visit(NotEqualExpression notEqualExpression) {
        notEqualExpression.left().accept(this);
        Value left = copyLastResult();
        notEqualExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (!leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.BOOL && right.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive leftBool = (BoolPrimitive) left.value();
            BoolPrimitive rightBool = (BoolPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(!leftBool.value() == rightBool.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(!leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    !currencyLeft.getName().equals(currencyRight.getName()) ||
                            !currencyLeft.getValue().equals(currencyRight.getValue())));
        }
    }

    @Override
    public void visit(GreaterExpression greaterExpression) {
        greaterExpression.left().accept(this);
        Value left = copyLastResult();
        greaterExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() > rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftValue.value() > rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison > 0)));
        }
    }

    @Override
    public void visit(LesserExpression lesserExpression) {
        lesserExpression.left().accept(this);
        Value left = copyLastResult();
        lesserExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() < rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftValue.value() < rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison < 0)));
        }
    }

    @Override
    public void visit(GreaterOrEqualExpression greaterOrEqualExpression) {
        greaterOrEqualExpression.left().accept(this);
        Value left = copyLastResult();
        greaterOrEqualExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() >= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftValue.value() >= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison >= 0)));
        }
    }

    @Override
    public void visit(LesserOrEqualExpression lesserOrEqualExpression) {
        lesserOrEqualExpression.left().accept(this);
        Value left = copyLastResult();
        lesserOrEqualExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() <= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(leftValue.value() <= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, new BoolPrimitive(
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison <= 0)));
        }
    }

    @Override
    public void visit(AdditionExpression additionExpression) {
        additionExpression.left().accept(this);
        Value left = copyLastResult();
        additionExpression.right().accept(this);
        Value right = copyLastResult();
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
        Value left = copyLastResult();
        subtractionExpression.right().accept(this);
        Value right = copyLastResult();
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
        Value left = copyLastResult();
        multiplicationExpression.right().accept(this);
        Value right = copyLastResult();
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
        Value left = copyLastResult();
        divisionExpression.right().accept(this);
        Value right = copyLastResult();
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
        currencyCastExpression.left().accept(this);
        Value left = copyLastResult();
        currencyCastExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive leftValue = (CurrencyPrimitive) left.value();
            String rightValue = "";
            if (right.valueType() == PrimitiveType.STRING) {
                rightValue = (String) right.value();
            }
            else if (right.valueType() == PrimitiveType.CURRENCY) {
                CurrencyPrimitive rightCurrency = (CurrencyPrimitive) right.value();
                rightValue = rightCurrency.getName();
            }
            else {
                System.out.println("CONVERTION VALUE CANNOT BE OF TYPE " + right.valueType().name());
            }
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(leftValue.getValue(), rightValue));
        }
        else {
            System.out.println("CURRENCY CASTING CANNOT BE APPLIED TO NON-CURRENCY VALUES!");
        }
    }

    @Override
    public void visit(CurrencyConversionExpression currencyConversionExpression) {
        currencyConversionExpression.left().accept(this);
        Value left = copyLastResult();
        currencyConversionExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive leftValue = (CurrencyPrimitive) left.value();
            String rightValue = "";
            if (right.valueType() == PrimitiveType.STRING) {
                rightValue = (String) right.value();
            }
            else if (right.valueType() == PrimitiveType.CURRENCY) {
                CurrencyPrimitive rightCurrency = (CurrencyPrimitive) right.value();
                rightValue = rightCurrency.getName();
            }
            else {
                System.out.println("CONVERTION VALUE CANNOT BE OF TYPE " + right.valueType().name());
            }
            int conversionRateColumn = conversionTable.getColumnCurrencies().indexOf(rightValue);
            int conversionRateRow = conversionTable.getRowCurrencies().indexOf(leftValue.getName());
            Double conversionRate = conversionTable.getConversionTable().get(conversionRateRow).get(conversionRateColumn);
            BigDecimal currencyValue = leftValue.getValue().multiply(BigDecimal.valueOf(conversionRate));
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(currencyValue, rightValue));
        }
        else {
            System.out.println("CURRENCY CONVERSION CANNOT BE APPLIED TO NON-CURRENCY VALUES!");
        }
    }

    @Override
    public void visit(NegationExpression negationExpression) {
        negationExpression.expression().accept(this);
        Value result = copyLastResult();
        if (result.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive boolValue = (BoolPrimitive) result.value();
            lastResult = new Value(PrimitiveType.BOOL, !boolValue.value());
        }
        else {
            System.out.println("NEGATION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(MinusExpression minusExpression) {
        minusExpression.expression().accept(this);
        Value result = copyLastResult();
        if (result.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive intValue = (IntPrimitive) result.value();
            lastResult = new Value(result.valueType(), -intValue.value());
        }
        else if (result.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive floatValue = (FloatPrimitive) result.value();
            lastResult = new Value(result.valueType(), -floatValue.value());
        }
        else if (result.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyValue = (CurrencyPrimitive) result.value();
            BigDecimal value = currencyValue.getValue().negate();
            lastResult = new Value(result.valueType(), new CurrencyPrimitive(value, currencyValue.getName()));
        }
        else {
            System.out.println("NEGATION NOT POSSIBLE");
        }
    }

    @Override
    public void visit(AccessExpression accessExpression) {
        accessExpression.left().accept(this);
        Value left = copyLastResult();
        if (left.value().getClass() != CurrencyPrimitive.class) {
            System.out.println("METHODS ARE NOT APPLICABLE TO NON-CURRENCY VALUES!");
        }
        FunctionCallExpression functionCall;
        if (accessExpression.right().getClass() != FunctionCallExpression.class) {
            System.out.println("ONLY METHOD CALLS ARE ALLOWED!");
        }
        else {
            functionCall = (FunctionCallExpression) accessExpression.right();
            FunctionDefinition function = functionDefinitions.get(functionCall.name());
            if (function == null) {
                System.out.println("THIS METHOD DOES NOT EXIST!");
                return;
            }
            FunctionDefinition tempFunc = new FunctionDefinition(function.returnType(),
                    function.name(),
                    List.of(new Parameter(PrimitiveType.CURRENCY, "currencyBalance")),
                    function.block());
            contextManager.addContext(new Context());
            contextManager.addVariable("currencyBalance", left);
            tempFunc.block().accept(this);
            contextManager.removeCurrentContext();
        }
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
        contextManager.addContext(new Context());
        for (int i = 0; i < functionArguments.size(); i++) {
            functionArguments.get(i).accept(this);
            Parameter param = functionDefinition.parameters().get(i);
            if (param.type() == PrimitiveType.NONE) {
                contextManager.addVariable(param.name(), new Value(PrimitiveType.STRING, lastResult.value().toString()));
            }
            else if (lastResult.valueType() != param.type()) {
                System.out.println("INVALID TYPE PROVIDED FOR PARAMETER " + param.name());
            }
            else {
                contextManager.addVariable(param.name(), new Value(param.type(), lastResult.value()));
            }
        }
        functionDefinition.block().accept(this);
        if (functionDefinition.returnType() == PrimitiveType.NONE) {
            lastResult = null;
        }
        else if (lastResult.valueType() != functionDefinition.returnType()) {
            System.out.println("INVALID RETURN TYPE");
        }
        else if (lastResult.value() == null) {
            System.out.println("NO RETURN VALUE SPECIFIED");
        }
        contextManager.removeCurrentContext();
    }

    @Override
    public void visit(IdentifierExpression identifierExpression) {
        if (conversionTable.getColumnCurrencies().contains(identifierExpression.name())) {
            BigDecimal temporaryValue = BigDecimal.valueOf(0.0);
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(temporaryValue, identifierExpression.name()));
            return;
        }
        Value value = contextManager.fetchVariable(identifierExpression.name());
        if (value == null) {
            System.out.println("VariableDoesNotExistError(VARIABLE " + identifierExpression.name() + " DOES NOT EXIST IN ANY CONTEXT");
        }
        else {
            lastResult = value;
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

    @Override
    public void visit(PrintFunction printFunction) {
        Value valueToPrint = contextManager.fetchVariable("valueToPrint");
        printer.println(valueToPrint.value().toString());
    }

    @Override
    public void visit(GetBalanceMethod getBalanceMethod) {
        CurrencyPrimitive balance = (CurrencyPrimitive) contextManager.fetchVariable("currencyBalance").value();
        lastResult = new Value(PrimitiveType.FLOAT, balance.getValue());
    }

    @Override
    public void visit(GetCurrencyMethod getCurrencyMethod) {
        CurrencyPrimitive balance = (CurrencyPrimitive) contextManager.fetchVariable("currencyBalance").value();
        lastResult = new Value(PrimitiveType.STRING, balance.getName());
    }

    private Value copyLastResult() {
        Value lastResultCopy = lastResult;
        lastResult = null;
        return lastResultCopy;
    }
}
