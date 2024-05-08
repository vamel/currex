package currex.visitor;

import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;

public interface Visitor {
    void visit(Parameter parameter);
    void visit(Block block);
    void visit(FunctionDefinition functionDefinition);
    void visit(Program program);

    void visit(DeclarationStatement declarationStatement);
    void visit(AssignmentStatement assignmentStatement);
    void visit(ReturnStatement returnStatement);
    void visit(WhileStatement whileStatement);
    void visit(IfStatement ifStatement);
    void visit(ElseStatement elseStatement);

    void visit(OrExpression orExpression);
    void visit(AndExpression andExpression);
    void visit(GreaterExpression greaterExpression);
    void visit(LesserExpression lesserExpression);
    void visit(EqualExpression equalExpression);
    void visit(NotEqualExpression notEqualExpression);
    void visit(GreaterOrEqualExpression greaterOrEqualExpression);
    void visit(LesserOrEqualExpression lesserOrEqualExpression);
    void visit(AdditionExpression additionExpression);
    void visit(SubtractionExpression subtractionExpression);
    void visit(MultiplicationExpression multiplication);
    void visit(DivisionExpression divisionExpression);
    void visit(CurrencyCastExpression currencyCastExpression);
    void visit(CurrencyConversionExpression currencyConversionExpression);
    void visit(NegationExpression negationExpression);
    void visit(AccessExpression accessExpression);
    void visit(FunctionCallExpression functionCallExpression);
    void visit(IdentifierExpression identifierExpression);

    void visit(BasicExpression simpleExpression);
    void visit(IntPrimitive intPrimitive);
    void visit(FloatPrimitive floatPrimitive);
    void visit(StringPrimitive stringPrimitive);
    void visit(BoolPrimitive boolPrimitive);
    void visit(CurrencyPrimitive currencyPrimitive);
}
