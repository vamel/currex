package currex.visitor;

import currex.interpreter.builtin.GetBalanceMethod;
import currex.interpreter.builtin.GetCurrencyMethod;
import currex.interpreter.builtin.PrintFunction;
import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;

public interface Visitor {
    void visit(Program program) throws Exception;
    void visit(Block block) throws Exception;
    void visit(FunctionDefinition functionDefinition) throws Exception;
    void visit(Parameter parameter);

    void visit(DeclarationStatement declarationStatement) throws Exception;
    void visit(AssignmentStatement assignmentStatement) throws Exception;
    void visit(ReturnStatement returnStatement) throws Exception;
    void visit(WhileStatement whileStatement) throws Exception;
    void visit(IfStatement ifStatement) throws Exception;
    void visit(ElseStatement elseStatement) throws Exception;

    void visit(OrExpression orExpression) throws Exception;
    void visit(AndExpression andExpression) throws Exception;
    void visit(GreaterExpression greaterExpression) throws Exception;
    void visit(LesserExpression lesserExpression) throws Exception;
    void visit(EqualExpression equalExpression) throws Exception;
    void visit(NotEqualExpression notEqualExpression) throws Exception;
    void visit(GreaterOrEqualExpression greaterOrEqualExpression) throws Exception;
    void visit(LesserOrEqualExpression lesserOrEqualExpression) throws Exception;
    void visit(AdditionExpression additionExpression) throws Exception;
    void visit(SubtractionExpression subtractionExpression) throws Exception;
    void visit(MultiplicationExpression multiplicationExpression) throws Exception;
    void visit(DivisionExpression divisionExpression) throws Exception;
    void visit(CurrencyCastExpression currencyCastExpression) throws Exception;
    void visit(CurrencyConversionExpression currencyConversionExpression) throws Exception;
    void visit(NegationExpression negationExpression) throws Exception;
    void visit(MinusExpression minusExpression) throws Exception;
    void visit(AccessExpression accessExpression) throws Exception;
    void visit(FunctionCallExpression functionCallExpression) throws Exception;
    void visit(IdentifierExpression identifierExpression) throws Exception;

    void visit(IntPrimitive intPrimitive);
    void visit(FloatPrimitive floatPrimitive);
    void visit(StringPrimitive stringPrimitive);
    void visit(BoolPrimitive boolPrimitive);
    void visit(CurrencyPrimitive currencyPrimitive);

    void visit(PrintFunction printFunction) throws Exception;
    void visit(GetBalanceMethod getBalanceMethod) throws Exception;
    void visit(GetCurrencyMethod getCurrencyMethod) throws Exception;
}
