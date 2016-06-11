// Generated from XPath.g4 by ANTLR 4.5.3
package org.xframium.utility.parser.xpath;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link XPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface XPathVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link XPathParser#main}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMain(XPathParser.MainContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#locationPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocationPath(XPathParser.LocationPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#absoluteLocationPathNoroot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbsoluteLocationPathNoroot(XPathParser.AbsoluteLocationPathNorootContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#relativeLocationPath}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelativeLocationPath(XPathParser.RelativeLocationPathContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#step}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStep(XPathParser.StepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#axisSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAxisSpecifier(XPathParser.AxisSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#nodeTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNodeTest(XPathParser.NodeTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(XPathParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#abbreviatedStep}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbbreviatedStep(XPathParser.AbbreviatedStepContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(XPathParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#primaryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpr(XPathParser.PrimaryExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(XPathParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#unionExprNoRoot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnionExprNoRoot(XPathParser.UnionExprNoRootContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#pathExprNoRoot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathExprNoRoot(XPathParser.PathExprNoRootContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#filterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilterExpr(XPathParser.FilterExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(XPathParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(XPathParser.AndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#equalityExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpr(XPathParser.EqualityExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#relationalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpr(XPathParser.RelationalExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#additiveExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpr(XPathParser.AdditiveExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#multiplicativeExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpr(XPathParser.MultiplicativeExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#unaryExprNoRoot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExprNoRoot(XPathParser.UnaryExprNoRootContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#qName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQName(XPathParser.QNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(XPathParser.FunctionNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReference(XPathParser.VariableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#nameTest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNameTest(XPathParser.NameTestContext ctx);
	/**
	 * Visit a parse tree produced by {@link XPathParser#nCName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNCName(XPathParser.NCNameContext ctx);
}
