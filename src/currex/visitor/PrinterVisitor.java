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

public class PrinterVisitor implements Visitor {

    private int identation;
    private String ident;

    public PrinterVisitor() {
        this.identation = 0;
        this.ident = "\t";
    }

    @Override
    public void visit(Program program) throws Exception {
        System.out.println(ident.repeat(identation) +
                "<Program>");
        for (FunctionDefinition functionDefinition: program.functionDefinitions().values()) {
            functionDefinition.accept(this);
        }
    }

    @Override
    public void visit(Block block) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<Block>");
        for (Statement statement: block.statementList()) {
            statement.accept(this);
        }
        decreaseIdendation();
    }

    @Override
    public void visit(FunctionDefinition functionDefinition) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<FunctionDefinition return_type=" + functionDefinition.returnType() +
                " name=" + functionDefinition.name() + ">");
        for (Parameter parameter: functionDefinition.parameters()) {
            parameter.accept(this);
        }
        functionDefinition.block().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(Parameter parameter) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<Parameter type=" + parameter.type() +
                " name=" + parameter.name() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(DeclarationStatement declarationStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<DeclarationStatement type=" + declarationStatement.type() +
                " name=" + declarationStatement.name() + ">");
        declarationStatement.expression().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(AssignmentStatement assignmentStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<AssignmentStatement>");
        assignmentStatement.left().accept(this);
        assignmentStatement.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(ReturnStatement returnStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<ReturnStatement>");
        returnStatement.expression().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(WhileStatement whileStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<WhileStatement>");
        whileStatement.expression().accept(this);
        whileStatement.block().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(IfStatement ifStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<IfStatement>");
        for (ElseStatement elseStatement: ifStatement.conditionalStatements()) {
            elseStatement.accept(this);
        }
        decreaseIdendation();
    }

    @Override
    public void visit(ElseStatement elseStatement) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<ElseStatement>");
        if (elseStatement.expression() != null) {
            elseStatement.expression().accept(this);
        }
        elseStatement.block().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(OrExpression orExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<OrExpression>");
        orExpression.left().accept(this);
        orExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(AndExpression andExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<AndExpression>");
        andExpression.left().accept(this);
        andExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(GreaterExpression greaterExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<GreaterExpression>");
        greaterExpression.left().accept(this);
        greaterExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(LesserExpression lesserExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<LesserExpression>");
        lesserExpression.left().accept(this);
        lesserExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(EqualExpression equalExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<EqualExpression>");
        equalExpression.left().accept(this);
        equalExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(NotEqualExpression notEqualExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<NotEqualExpression>");
        notEqualExpression.left().accept(this);
        notEqualExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(GreaterOrEqualExpression greaterOrEqualExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<GreaterOrEqualExpression>");
        greaterOrEqualExpression.left().accept(this);
        greaterOrEqualExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(LesserOrEqualExpression lesserOrEqualExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<LesserOrEqualExpression>");
        lesserOrEqualExpression.left().accept(this);
        lesserOrEqualExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(AdditionExpression additionExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<AdditionExpression>");
        additionExpression.left().accept(this);
        additionExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(SubtractionExpression subtractionExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<SubtractionExpression>");
        subtractionExpression.left().accept(this);
        subtractionExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(MultiplicationExpression multiplication) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<MultiplicationExpression>");
        multiplication.left().accept(this);
        multiplication.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(DivisionExpression divisionExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<DivisionExpression>");
        divisionExpression.left().accept(this);
        divisionExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(CurrencyCastExpression currencyCastExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<CurrencyCastExpression>");
        currencyCastExpression.left().accept(this);
        currencyCastExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(CurrencyConversionExpression currencyConversionExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<CurrencyConversionExpression>");
        currencyConversionExpression.left().accept(this);
        currencyConversionExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(NegationExpression negationExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<NegationExpression>");
        negationExpression.expression().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(MinusExpression minusExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<MinusExpression>");
        minusExpression.expression().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(AccessExpression accessExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<AccessExpression>");
        accessExpression.left().accept(this);
        accessExpression.right().accept(this);
        decreaseIdendation();
    }

    @Override
    public void visit(FunctionCallExpression functionCallExpression) throws Exception {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<FunctionCallExpression name=" + functionCallExpression.name() + ">");
        for (Expression argument: functionCallExpression.arguments()) {
            argument.accept(this);
        }
        decreaseIdendation();
    }

    @Override
    public void visit(IdentifierExpression identifierExpression) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<Identifier name=" + identifierExpression.name() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(IntPrimitive intPrimitive) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<IntPrimitive value=" + intPrimitive.value() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(FloatPrimitive floatPrimitive) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<FloatPrimitive value=" + floatPrimitive.value() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(StringPrimitive stringPrimitive) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<StringPrimitive value=" + stringPrimitive.value() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(BoolPrimitive boolPrimitive) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<BoolPrimitive value=" + boolPrimitive.value() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(CurrencyPrimitive currencyPrimitive) {
        increaseIdentation();
        System.out.println(ident.repeat(identation) +
                "<CurrencyPrimitive value=" + currencyPrimitive.getValue() +
                " type=" + currencyPrimitive.getName() + ">");
        decreaseIdendation();
    }

    @Override
    public void visit(PrintFunction printFunction) {
        increaseIdentation();
        System.out.println("<PrintStatement>");
        decreaseIdendation();
    }

    @Override
    public void visit(GetBalanceMethod getBalanceMethod) {
        increaseIdentation();
        System.out.println("<GetBalance method>");
        decreaseIdendation();
    }

    @Override
    public void visit(GetCurrencyMethod getCurrencyMethod) {
        increaseIdentation();
        System.out.println("<GetCurrency method>");
        decreaseIdendation();
    }

    private void increaseIdentation() {
        identation++;
    }

    private void decreaseIdendation() {
        identation--;
    }
}
