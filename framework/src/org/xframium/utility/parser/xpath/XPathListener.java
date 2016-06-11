// Generated from XPath.g4 by ANTLR 4.5.3
package org.xframium.utility.parser.xpath;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link XPathParser}.
 */
public interface XPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link XPathParser#main}.
	 * @param ctx the parse tree
	 */
	void enterMain(XPathParser.MainContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#main}.
	 * @param ctx the parse tree
	 */
	void exitMain(XPathParser.MainContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#locationPath}.
	 * @param ctx the parse tree
	 */
	void enterLocationPath(XPathParser.LocationPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#locationPath}.
	 * @param ctx the parse tree
	 */
	void exitLocationPath(XPathParser.LocationPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#absoluteLocationPathNoroot}.
	 * @param ctx the parse tree
	 */
	void enterAbsoluteLocationPathNoroot(XPathParser.AbsoluteLocationPathNorootContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#absoluteLocationPathNoroot}.
	 * @param ctx the parse tree
	 */
	void exitAbsoluteLocationPathNoroot(XPathParser.AbsoluteLocationPathNorootContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#relativeLocationPath}.
	 * @param ctx the parse tree
	 */
	void enterRelativeLocationPath(XPathParser.RelativeLocationPathContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#relativeLocationPath}.
	 * @param ctx the parse tree
	 */
	void exitRelativeLocationPath(XPathParser.RelativeLocationPathContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#step}.
	 * @param ctx the parse tree
	 */
	void enterStep(XPathParser.StepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#step}.
	 * @param ctx the parse tree
	 */
	void exitStep(XPathParser.StepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#axisSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAxisSpecifier(XPathParser.AxisSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#axisSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAxisSpecifier(XPathParser.AxisSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#nodeTest}.
	 * @param ctx the parse tree
	 */
	void enterNodeTest(XPathParser.NodeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#nodeTest}.
	 * @param ctx the parse tree
	 */
	void exitNodeTest(XPathParser.NodeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(XPathParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(XPathParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#abbreviatedStep}.
	 * @param ctx the parse tree
	 */
	void enterAbbreviatedStep(XPathParser.AbbreviatedStepContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#abbreviatedStep}.
	 * @param ctx the parse tree
	 */
	void exitAbbreviatedStep(XPathParser.AbbreviatedStepContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(XPathParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(XPathParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(XPathParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#primaryExpr}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(XPathParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(XPathParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(XPathParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#unionExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void enterUnionExprNoRoot(XPathParser.UnionExprNoRootContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#unionExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void exitUnionExprNoRoot(XPathParser.UnionExprNoRootContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#pathExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void enterPathExprNoRoot(XPathParser.PathExprNoRootContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#pathExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void exitPathExprNoRoot(XPathParser.PathExprNoRootContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#filterExpr}.
	 * @param ctx the parse tree
	 */
	void enterFilterExpr(XPathParser.FilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#filterExpr}.
	 * @param ctx the parse tree
	 */
	void exitFilterExpr(XPathParser.FilterExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(XPathParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(XPathParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(XPathParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(XPathParser.AndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpr(XPathParser.EqualityExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#equalityExpr}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpr(XPathParser.EqualityExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpr(XPathParser.RelationalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#relationalExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpr(XPathParser.RelationalExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpr(XPathParser.AdditiveExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#additiveExpr}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpr(XPathParser.AdditiveExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpr(XPathParser.MultiplicativeExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpr(XPathParser.MultiplicativeExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#unaryExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExprNoRoot(XPathParser.UnaryExprNoRootContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#unaryExprNoRoot}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExprNoRoot(XPathParser.UnaryExprNoRootContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#qName}.
	 * @param ctx the parse tree
	 */
	void enterQName(XPathParser.QNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#qName}.
	 * @param ctx the parse tree
	 */
	void exitQName(XPathParser.QNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(XPathParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(XPathParser.FunctionNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void enterVariableReference(XPathParser.VariableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void exitVariableReference(XPathParser.VariableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#nameTest}.
	 * @param ctx the parse tree
	 */
	void enterNameTest(XPathParser.NameTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#nameTest}.
	 * @param ctx the parse tree
	 */
	void exitNameTest(XPathParser.NameTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link XPathParser#nCName}.
	 * @param ctx the parse tree
	 */
	void enterNCName(XPathParser.NCNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link XPathParser#nCName}.
	 * @param ctx the parse tree
	 */
	void exitNCName(XPathParser.NCNameContext ctx);
}
