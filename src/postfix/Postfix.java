/* *******************************************************************
 * Copyright (c) 2021 Universidade Federal de Pernambuco (UFPE).
 * 
 * This file is part of the Compilers course at UFPE.
 * 
 * During the 1970s and 1980s, Hewlett-Packard used RPN in all 
 * of their desktop and hand-held calculators, and continued to 
 * use it in some models into the 2020s. In computer science, 
 * reverse Polish notation is used in stack-oriented programming languages 
 * such as Forth, STOIC, PostScript, RPL and Joy.
 *  
 * Contributors: 
 *     Henrique Rebelo      initial design and implementation 
 *     http://www.cin.ufpe.br/~hemr/
 * ******************************************************************/

package postfix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import postfix.ast.AstPrinter;
import postfix.ast.Expr;
import postfix.interpreter.Interpreter;
import postfix.lexer.LexError;
import postfix.lexer.Scanner;
import postfix.lexer.Token;
import postfix.lexer.TokenType;
import postfix.parser.Parser;
import postfix.parser.ParserError;

/**
 * @author Henrique Rebelo
 */
public class Postfix {

	public static String resposta = ""; // Será usada para ids estáticos ou dinâmicos.

	private static final Interpreter interpreter = new Interpreter(new HashMap<String, String>());
	private static boolean hasError = false;
	private static boolean debugging = false;

	/**
	 * Running Postfix Interpreter
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		args = new String [1];
//		args [0] = "../StackerPrograms/program/Calc1.stk";
//		args [1] = "../StackerPrograms/program/Calc2.stk";

		debugging = false; // for interpretation phases
		run(args, debugging);
	}

	/**
	 * run by Interpretation the given program as 
	 * a source file path
	 * 
	 * @param sourceFilePath to be interpreted
	 */
	private static void runFile(String sourceFilePath) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(sourceFilePath));
		String sourceProgram = 
				new String(bytes, Charset.defaultCharset());
		run(sourceProgram);

		// Indicate an error in the exit code.
		if (hasError) System.exit(65); // exiting with non-zero code as should be
	}

	/**
	 * Prompt where you can enter and execute code one 
	 * line at a time.
	 * 
	 * @throws IOException
	 */
	private static void runPrompt() throws IOException {
		InputStreamReader input = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(input);

		System.out.print("Deseja criar seus próprios ids?\nResposta [s/n]: ");
		resposta = reader.readLine();

		if(resposta.charAt(0) == 's' || resposta.charAt(0) == 'S'){
			System.out.println("Regras para criar ids: " +
					"\n\t1. Ids precisam começar com uma letra minúscula" +
					"\n\t2. Após a primeira letra, letras maiúsculas e números são aceitos" +
					"\n\t3. Digite os ids como nos exemplos a seguir:" +
					"\n\t   Exemplos:" +
					"\n\t\t abc1=10 | aBC12=11 | numero=14" +
					"\n\t   ou seja, sem espaços entre o id e o seu valor");

			System.out.print("\nQuantidade de ids (é necessário pelo menos 1 id): ");
			int n = Integer.parseInt(reader.readLine());

			for (int i = 0; i < n; i++) {
				String attr = reader.readLine();
				interpreter.id.put(attr.split("=")[0], attr.split("=")[1]);
			}
		}
		else{
			System.out.println("Existem apenas dois ids estáticos, y = 10 e x = 15.");
		}

		System.out.println("A execução principal de RPNStacker começa a seguir...");

		for (;;) { 
			System.out.print("> ");
			String line = reader.readLine();
			if (line == null) break;
			run(line);
			hasError = false;
		}
	}

	/**
	 * run by Interpretation the given program [as a String]
	 * 
	 * @param source to be interpreted
	 */
	private static void run(String source) {
		try {
			Scanner scanner = new Scanner(source);
			List<Token> tokens = scanner.scan();

			// debugging for tokens
			if(debugging) {
				printTokens(tokens);
			}

			Parser parser = new Parser(tokens);
			Expr expr = parser.parse();

			// debugging for parsing/ast
			if(debugging) {
				printAST(expr);
			}

			// Caso a resposta no começo da execução do programa seja "y", inicia um id estático.
			if (resposta.charAt(0) == 'n') {
				interpreter.id.put("y", "10");
				interpreter.id.put("x", "15");
			}

			printTokens(tokens);
			System.out.println(interpreter.interp(expr));
		} catch (LexError e) {
			error("Lex", e.getMessage());
			hasError = true;
		}	
		catch (ParserError e) {
			error("Parser", e.getMessage());
			hasError = true;
		}	
	}

	// -------------------------------------------------------------
	// HELPERS METHODS
	// -------------------------------------------------------------
	private static void run(String[] args, boolean debugging) throws IOException {
		Postfix.debugging = debugging;
		if(args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				runFile(args[i]);
			}
		}
		else {
			runPrompt();
		}
	}

	private static void printAST(Expr expr) {
		System.out.println("ast: "+new AstPrinter().print(expr));
		System.out.println();
	}

	private static void printTokens(List<Token> tokens) {
		for (Token token : tokens) {
			// Como agora variávies são aceitas, pode ocorrer do usuário digitar uma variável que não existe
			// por esse motivo faço a checagem no if abaixo
			if(token.type == TokenType.ID && !interpreter.id.containsKey(token.lexeme)){
				throw new LexError("Unexpected character: " + token.lexeme);
			}
			System.out.println(token);
		}
		System.out.println();
	}

	private static void error(String kind, String message) {
		report(kind, message);
	}

	private static void report(String kind, String message) {
		System.err.println(
				"[" + kind + "] Error: " + message);
		hasError = true;
	}
}
