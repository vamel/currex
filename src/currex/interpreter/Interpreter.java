package currex.interpreter;

import currex.interpreter.builtin.*;
import currex.interpreter.error.*;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.utils.CurrexConfig;
import currex.utils.CurrexLimits;
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
    private final InterpreterErrorHandler errorHandler;
    private Value lastResult = null;
    private boolean isReturn = false;
    private boolean isIfStatement = false;


    public Interpreter(ConversionTable conversionTable, PrintStream printer) {
        this.conversionTable = conversionTable;
        this.printer = printer;
        this.errorHandler = new InterpreterErrorHandler();
    }

    @Override
    public void run(Program program) throws Exception {
        visit(program);
    }

    @Override
    public void visit(Program program) throws Exception {
        functionDefinitions.putAll(program.functionDefinitions());
        FunctionCallExpression main = new FunctionCallExpression(CurrexConfig.MAIN_FUNCTION_NAME, List.of());

        main.accept(this);
    }

    @Override
    public void visit(Block block) throws Exception {
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
    public void visit(DeclarationStatement declarationStatement) throws Exception {
        declarationStatement.expression().accept(this);
        if (lastResult.valueType() != declarationStatement.type()) {
            errorHandler.handleInterpreterError(new InvalidVariableTypeError("INVALID VARIABLE OF TYPE " +
                    lastResult.valueType().name() + "!"));
        }
        contextManager.addVariable(declarationStatement.name(), new Value(
                declarationStatement.type(), lastResult.value()));
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) throws Exception {
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
    public void visit(ReturnStatement returnStatement) throws Exception {
        if (returnStatement.expression() != null) {
            returnStatement.expression().accept(this);
            isReturn = true;
        }
        else {
            copyLastResult();
        }
    }

    @Override
    public void visit(WhileStatement whileStatement) throws Exception {
        whileStatement.expression().accept(this);
        Value check = copyLastResult();
        if (check.valueType() != PrimitiveType.BOOL) {
            errorHandler.handleInterpreterError(new InvalidBoolValueError(
                    "EVALUATED EXPRESSION DOES NOT GIVE A BOOL VALUE!"
            ));
        }
        Boolean checkValue = (Boolean) check.value();
        while (checkValue) {
            contextManager.addContext(new Context());
            whileStatement.block().accept(this);
            contextManager.removeCurrentContext();
            whileStatement.expression().accept(this);
            checkValue = (Boolean) lastResult.value();
        }
    }

    @Override
    public void visit(IfStatement ifStatement) throws Exception {
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
    public void visit(ElseStatement elseStatement) throws Exception {
        elseStatement.expression().accept(this);
        Value check = copyLastResult();
        if (check.valueType() != PrimitiveType.BOOL) {
            errorHandler.handleInterpreterError(new InvalidBoolValueError(
                    "EVALUATED EXPRESSION DOES NOT GIVE A BOOL VALUE!"
            ));
        }
        Boolean leftBool = (Boolean) check.value();
        if (leftBool) {
            isIfStatement = true;
            contextManager.addContext(new Context());
            elseStatement.block().accept(this);
            contextManager.removeCurrentContext();
        }
    }

    @Override
    public void visit(OrExpression orExpression) throws Exception {
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
            Boolean rightBool = (Boolean) right.value();
            if (rightBool) {
                lastResult = new Value(PrimitiveType.BOOL, true);
            }
            else {
                lastResult = new Value(PrimitiveType.BOOL, false);
            }
            return;
        }
        errorHandler.handleInterpreterError(new InvalidBoolValueError("OR STATEMENT CANNOT BE EVALUATED!"));
    }

    @Override
    public void visit(AndExpression andExpression) throws Exception {
        andExpression.left().accept(this);
        Value left = copyLastResult();
        if (left.valueType() == PrimitiveType.BOOL) {
            Boolean leftBool = (Boolean) left.value();
            if (!leftBool) {
                lastResult = new Value(PrimitiveType.BOOL, false);
                return;
            }
        }
        andExpression.right().accept(this);
        Value right = copyLastResult();
        if (right.valueType() == PrimitiveType.BOOL) {
            Boolean rightBool = (Boolean) right.value();
            if (rightBool) {
                lastResult = new Value(PrimitiveType.BOOL, true);
            }
            else {
                lastResult = new Value(PrimitiveType.BOOL, false);
            }
            return;
        }
        errorHandler.handleInterpreterError(new InvalidBoolValueError("AND STATEMENT CANNOT BE EVALUATED!"));
    }

    @Override
    public void visit(EqualExpression equalExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (leftBool.value() == rightBool.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (
                    currencyLeft.getName().equals(currencyRight.getName()) &&
                            currencyLeft.getValue().equals(currencyRight.getValue())));
        }
        else if (left.valueType() == PrimitiveType.STRING && right.valueType() == PrimitiveType.STRING) {
            StringPrimitive leftValue = (StringPrimitive) left.value();
            StringPrimitive rightValue = (StringPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (
                    leftValue.value().equals(rightValue.value())));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(NotEqualExpression notEqualExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (!leftBool.value() == rightBool.value()));
        }
        else if (left.valueType() == PrimitiveType.FLOAT && right.valueType() == PrimitiveType.FLOAT) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (!leftValue.value().equals(rightValue.value())));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (
                    !currencyLeft.getName().equals(currencyRight.getName()) ||
                            !currencyLeft.getValue().equals(currencyRight.getValue())));
        }
        else if (left.valueType() == PrimitiveType.STRING && right.valueType() == PrimitiveType.STRING) {
            StringPrimitive leftValue = (StringPrimitive) left.value();
            StringPrimitive rightValue = (StringPrimitive) right.value();
            lastResult = new Value(PrimitiveType.BOOL, (
                    !leftValue.value().equals(rightValue.value())));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(GreaterExpression greaterExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() > rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (!currencyLeft.getName().equals(currencyRight.getName())) {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CURRENCY " + currencyLeft.getName() + " CANNOT BE COMPARED TO " + currencyRight.getName()
                ));
            }
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, (
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison > 0)));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(LesserExpression lesserExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() < rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            if (!currencyLeft.getName().equals(currencyRight.getName())) {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CURRENCY " + currencyLeft.getName() + " CANNOT BE COMPARED TO " + currencyRight.getName()
                ));
            }
            lastResult = new Value(PrimitiveType.BOOL, (
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison < 0)));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(GreaterOrEqualExpression greaterOrEqualExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() >= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            if (!currencyLeft.getName().equals(currencyRight.getName())) {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CURRENCY " + currencyLeft.getName() + " CANNOT BE COMPARED TO " + currencyRight.getName()
                ));
            }
            lastResult = new Value(PrimitiveType.BOOL, (
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison >= 0)));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(LesserOrEqualExpression lesserOrEqualExpression) throws Exception {
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
            lastResult = new Value(PrimitiveType.BOOL, (leftValue.value() <= rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (!currencyLeft.getName().equals(currencyRight.getName())) {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CURRENCY " + currencyLeft.getName() + " CANNOT BE COMPARED TO " + currencyRight.getName()
                ));
            }
            int comparison = currencyLeft.getValue().compareTo(currencyRight.getValue());
            lastResult = new Value(PrimitiveType.BOOL, (
                    currencyLeft.getName().equals(currencyRight.getName()) && (comparison <= 0)));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("TYPE " + left.valueType() +
                    " CANNOT BE COMPARED WITH TYPE " + right.valueType() + "!"));
        }
    }

    @Override
    public void visit(AdditionExpression additionExpression) throws Exception {
        additionExpression.left().accept(this);
        Value left = copyLastResult();
        additionExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            if (CurrexLimits.INTEGER_MAX_VALUE - leftValue.value() < rightValue.value()) {
                errorHandler.handleInterpreterError(new OverflowError("INTEGER VALUE IS TOO BIG!"));
            }
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() + rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() + rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.STRING && right.valueType() == PrimitiveType.STRING) {
            StringPrimitive leftValue = (StringPrimitive) left.value();
            StringPrimitive rightValue = (StringPrimitive) right.value();
            String result = leftValue.value().substring(0, leftValue.value().length()-1) +
                    rightValue.value().substring(1);
            lastResult = new Value(PrimitiveType.STRING, new StringPrimitive(result));
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
                errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT ADD " +
                        currencyLeft.getName() + " TO " + currencyRight.getName() + "!"));
            }
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT ADD " +
                    left.valueType().name() + " TO " + right.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(SubtractionExpression subtractionExpression) throws Exception {
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
                errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT SUBTRACT " +
                        currencyLeft.getName() + " FROM " + currencyRight.getName() + "!"));
            }
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT SUBTRACT " +
                    left.valueType().name() + " FROM " + right.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(MultiplicationExpression multiplicationExpression) throws Exception {
        multiplicationExpression.left().accept(this);
        Value left = copyLastResult();
        multiplicationExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            if (leftValue.value() != 0 &&
                    CurrexLimits.INTEGER_MAX_VALUE / leftValue.value() < rightValue.value()) {
                errorHandler.handleInterpreterError(new OverflowError("INTEGER VALUE IS TOO BIG!"));
            }
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
                errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT MULTIPLY " +
                        currencyLeft.getName() + " BY " + currencyRight.getName() + "!"));
            }
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT MULTIPLY " +
                    left.valueType().name() + " BY " + right.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(DivisionExpression divisionExpression) throws Exception {
        divisionExpression.left().accept(this);
        Value left = copyLastResult();
        divisionExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.INTEGER && right.valueType() == PrimitiveType.INTEGER) {
            IntPrimitive leftValue = (IntPrimitive) left.value();
            IntPrimitive rightValue = (IntPrimitive) right.value();
            if (rightValue.value().equals(0)) {
                errorHandler.handleInterpreterError(new ZeroDivisionError("UNHANDLED DIVISION BY ZERO!"));
            }
            lastResult = new Value(PrimitiveType.INTEGER, new IntPrimitive(leftValue.value() / rightValue.value()));
        }
        else if ((left.valueType() == PrimitiveType.INTEGER || left.valueType() == PrimitiveType.FLOAT) &&
                (right.valueType() == PrimitiveType.INTEGER || right.valueType() == PrimitiveType.FLOAT)) {
            FloatPrimitive leftValue = (FloatPrimitive) left.value();
            FloatPrimitive rightValue = (FloatPrimitive) right.value();
            if (rightValue.value().equals(0.0)) {
                errorHandler.handleInterpreterError(new ZeroDivisionError("UNHANDLED DIVISION BY ZERO!"));
            }
            lastResult = new Value(PrimitiveType.FLOAT, new FloatPrimitive(leftValue.value() / rightValue.value()));
        }
        else if (left.valueType() == PrimitiveType.CURRENCY && right.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive currencyLeft = (CurrencyPrimitive) left.value();
            CurrencyPrimitive currencyRight = (CurrencyPrimitive) right.value();
            if (currencyRight.getValue().equals(BigDecimal.valueOf(0.0))) {
                errorHandler.handleInterpreterError(new ZeroDivisionError("UNHANDLED DIVISION BY ZERO!"));
            }
            if (currencyLeft.getName().equals(currencyRight.getName())) {
                BigDecimal currencyValue = currencyLeft.getValue().divide(currencyRight.getValue(), RoundingMode.HALF_DOWN);
                currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
                CurrencyPrimitive primitive = new CurrencyPrimitive(currencyValue, currencyLeft.getName());
                lastResult = new Value(PrimitiveType.CURRENCY, primitive);
            }
            else {
                errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT DIVIDE " +
                        currencyLeft.getName() + " BY " + currencyRight.getName() + "!"));
            }
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("CANNOT DIVIDE " +
                    left.valueType().name() + " BY " + right.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(CurrencyCastExpression currencyCastExpression) throws Exception {
        currencyCastExpression.left().accept(this);
        Value left = copyLastResult();
        currencyCastExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive leftValue = (CurrencyPrimitive) left.value();
            String rightValue = "";
            if (right.valueType() == PrimitiveType.STRING) {
                try {
                    rightValue = (String) right.value();
                } catch (ClassCastException e) {
                    errorHandler.handleInterpreterError(new InvalidCurrencyNameError("CURRENCY " +
                            right.value() + " IS NOT CORRECT"));
                }
            }
            else if (right.valueType() == PrimitiveType.CURRENCY) {
                CurrencyPrimitive rightCurrency = (CurrencyPrimitive) right.value();
                rightValue = rightCurrency.getName();
            }
            else {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CONVERTION VALUE CANNOT BE OF TYPE " + right.valueType().name()
                ));
            }
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(leftValue.getValue(), rightValue));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError(
                    "CURRENCY CASTING CANNOT BE APPLIED TO NON-CURRENCY VALUES!"));
        }
    }

    @Override
    public void visit(CurrencyConversionExpression currencyConversionExpression) throws Exception {
        currencyConversionExpression.left().accept(this);
        Value left = copyLastResult();
        currencyConversionExpression.right().accept(this);
        Value right = copyLastResult();
        if (left.valueType() == PrimitiveType.CURRENCY) {
            CurrencyPrimitive leftValue = (CurrencyPrimitive) left.value();
            String rightValue = "";
            if (right.valueType() == PrimitiveType.STRING) {
                try {
                    rightValue = (String) right.value();
                } catch (ClassCastException e) {
                    errorHandler.handleInterpreterError(new InvalidCurrencyNameError("CURRENCY " +
                            right.value() + " IS NOT CORRECT"));
                }
            }
            else if (right.valueType() == PrimitiveType.CURRENCY) {
                CurrencyPrimitive rightCurrency = (CurrencyPrimitive) right.value();
                rightValue = rightCurrency.getName();
            }
            else {
                errorHandler.handleInterpreterError(new InvalidCurrencyNameError(
                        "CONVERTION VALUE CANNOT BE OF TYPE " + right.valueType().name()
                ));
            }
            int conversionRateColumn = conversionTable.getColumnCurrencies().indexOf(rightValue);
            int conversionRateRow = conversionTable.getRowCurrencies().indexOf(leftValue.getName());
            Double conversionRate = conversionTable.getConversionTable().get(conversionRateRow).get(conversionRateColumn);
            BigDecimal currencyValue = leftValue.getValue().multiply(BigDecimal.valueOf(conversionRate));
            currencyValue = currencyValue.setScale(10, RoundingMode.HALF_DOWN);
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(currencyValue, rightValue));
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("CURRENCY CONVERSION CANNOT BE APPLIED TO NON-CURRENCY VALUES!"));
        }
    }

    @Override
    public void visit(NegationExpression negationExpression) throws Exception {
        negationExpression.expression().accept(this);
        Value result = copyLastResult();
        if (result.valueType() == PrimitiveType.BOOL) {
            BoolPrimitive boolValue = (BoolPrimitive) result.value();
            lastResult = new Value(PrimitiveType.BOOL, !boolValue.value());
        }
        else {
            errorHandler.handleInterpreterError(new IncompatibleTypesError("NEGATION NOT POSSIBLE FOR TYPE" +
                    result.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(MinusExpression minusExpression) throws Exception {
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
            errorHandler.handleInterpreterError(new IncompatibleTypesError("NEGATION NOT POSSIBLE FOR TYPE" +
                    result.valueType().name() + "!"));
        }
    }

    @Override
    public void visit(AccessExpression accessExpression) throws Exception {
        accessExpression.left().accept(this);
        Value left = copyLastResult();
        if (left.value().getClass() != CurrencyPrimitive.class) {
            errorHandler.handleInterpreterError(new InvalidVariableTypeError("METHODS ARE NOT APPLICABLE TO NON-CURRENCY VALUES!"));
        }
        FunctionCallExpression functionCall;
        if (accessExpression.right().getClass() != FunctionCallExpression.class) {
            errorHandler.handleInterpreterError(new InvalidMethodCallError("ONLY METHOD CALLS ARE ALLOWED!"));
        }
        else {
            functionCall = (FunctionCallExpression) accessExpression.right();
            FunctionDefinition function = functionDefinitions.get(functionCall.name());
            if (function == null) {
                errorHandler.handleInterpreterError(new FunctionDoesNotExistError(
                        "METHOD WITH NAME " + functionCall.name() + " DOES NOT EXIST!"
                ));
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
    public void visit(FunctionCallExpression functionCallExpression) throws Exception {
        if (!functionDefinitions.containsKey(functionCallExpression.name())) {
            if (functionCallExpression.name().equals(CurrexConfig.MAIN_FUNCTION_NAME)) {
                errorHandler.handleInterpreterError(new MainFunctionNotDefinedError("MAIN FUNCTION WAS NOT DEFINED!"));
            }
            errorHandler.handleInterpreterError(new FunctionDoesNotExistError("NO FUNCTION WITH THIS NAME ERROR"));
        }
        FunctionDefinition functionDefinition = functionDefinitions.get(functionCallExpression.name());
        if (functionDefinition.parameters().size() != functionCallExpression.arguments().size()) {
            errorHandler.handleInterpreterError(new InvalidFunctionCallError(
                    "INVALID NUMBER OF ARGUMENTS FOR FUNCTION " +
                            functionDefinition.name() +
                            " EXPECTED: " + functionDefinition.parameters().size() +
                            " BUT RECEIVED: " + functionCallExpression.arguments().size() + "!"
            ));
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
                errorHandler.handleInterpreterError(new InvalidVariableTypeError("INVALID TYPE PROVIDED FOR PARAMETER " + param.name()));
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
            errorHandler.handleInterpreterError(new InvalidReturnValueError("INVALID RETURN TYPE, EXPECTED " +
                    functionDefinition.returnType().name() + " BUT GOT " + lastResult.valueType().name() + " INSTEAD!"));
        }
        else if (lastResult.value() == null) {
            errorHandler.handleInterpreterError(new InvalidReturnValueError("NO RETURN VALUE SPECIFIED!"));
        }
        contextManager.removeCurrentContext();
    }

    @Override
    public void visit(IdentifierExpression identifierExpression) throws Exception {
        if (conversionTable.getColumnCurrencies().contains(identifierExpression.name())) {
            BigDecimal temporaryValue = BigDecimal.valueOf(0.0);
            lastResult = new Value(PrimitiveType.CURRENCY, new CurrencyPrimitive(temporaryValue, identifierExpression.name()));
            return;
        }
        Value value = contextManager.fetchVariable(identifierExpression.name());
        if (value == null) {
            errorHandler.handleInterpreterError(new VariableDoesNotExistError(
                    "VARIABLE " + identifierExpression.name() + " DOES NOT EXIST IN ANY CONTEXT!"
            ));
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
    public void visit(PrintFunction printFunction) throws Exception {
        Value valueToPrint = contextManager.fetchVariable("valueToPrint");
        printer.println(valueToPrint.value().toString());
    }

    @Override
    public void visit(GetBalanceMethod getBalanceMethod) throws Exception {
        CurrencyPrimitive balance = (CurrencyPrimitive) contextManager.fetchVariable("currencyBalance").value();
        lastResult = new Value(PrimitiveType.FLOAT, balance.getValue());
    }

    @Override
    public void visit(GetCurrencyMethod getCurrencyMethod) throws Exception {
        CurrencyPrimitive balance = (CurrencyPrimitive) contextManager.fetchVariable("currencyBalance").value();
        lastResult = new Value(PrimitiveType.STRING, balance.getName());
    }

    private Value copyLastResult() {
        Value lastResultCopy = lastResult;
        lastResult = null;
        return lastResultCopy;
    }
}
