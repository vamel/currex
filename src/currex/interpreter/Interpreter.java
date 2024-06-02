package currex.interpreter;

import currex.structure.components.Block;
import currex.structure.components.FunctionDefinition;
import currex.structure.components.Parameter;
import currex.structure.components.Program;
import currex.structure.expressions.*;
import currex.structure.primitives.*;
import currex.structure.statements.*;
import currex.visitor.Visitor;

public class Interpreter implements Interpretable, Visitor {



    @Override
    public void run(Program program) {
        visit(program);
    }

    @Override
    public void visit(Program program) {

    }

    @Override
    public void visit(Block block) {

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

    }

    @Override
    public void visit(SubtractionExpression subtractionExpression) {

    }

    @Override
    public void visit(MultiplicationExpression multiplication) {

    }

    @Override
    public void visit(DivisionExpression divisionExpression) {

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

    }

    @Override
    public void visit(IntPrimitive intPrimitive) {

    }

    @Override
    public void visit(FloatPrimitive floatPrimitive) {

    }

    @Override
    public void visit(StringPrimitive stringPrimitive) {

    }

    @Override
    public void visit(BoolPrimitive boolPrimitive) {

    }

    @Override
    public void visit(CurrencyPrimitive currencyPrimitive) {

    }
}
