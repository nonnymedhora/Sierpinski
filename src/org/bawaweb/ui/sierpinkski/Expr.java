/**
 * 
 */
package org.bawaweb.ui.sierpinkski;


/**
 * @author Navroz
 *
 */	
///////////////////////////////////////////
// https://math.hws.edu › javanotes › source › chapter8 › Expr
/*
 * An object belonging to the class Expr is a mathematical expression that
 * can involve:
 * 
 * -- real numbers such as 2.7, 3, and 12.7e-12 -- the variable Z / z --
 * arithmetic operators +, -, *, /, and ^ , where the last of these
 * represents raising to a power -- the mathematical functions sin, cos,
 * tan, sec, csc, cot, arcsin, arccos, arctan, exp, ln, log2, log10, abs,
 * and sqrt, where ln is the natural log, log2 is the log to the base 2,
 * log10 is the log to the base 10, abs is absolute value, and sqrt is the
 * square root -- parentheses
 * 
 * Some examples would be: x^2 + x + 1 sin(2.3*x-7.1) - cos(7.1*x-2.3) x - 1
 * 42 exp(x^2) - 1
 * 
 * The trigonometric functions work with radians, not degrees. The parameter
 * of a function must be enclosed in parentheses, so that "sin x" is NOT
 * allowed.
 * 
 * An Expr is constructed from a String that contains its definition. If an
 * error is found in this definition, the constructor will throw an
 * IllegalArgumentException with a message that describes the error and the
 * position of the error in the string. After an Expr has been constructed,
 * its defining string can be retrieved using the method getDefinition().
 * 
 * The main operation on an Expr object is to find its value, given a value
 * for the variable x. The value is computed by the value() method. If the
 * expression is undefined at the given value of x, then the number returned
 * by value() method will be the special "non-a-number number", Double.NaN.
 * (The boolean-valued function Double.isNaN(v) can be used to test whether
 * the double value v is the special NaN value. For technical reasons, you
 * can't just use the == operator to test for this value.)
 * 
 */

public class Expr {

	private String type = "Complex";

	// ----------------- public interface
	// ---------------------------------------
	public Expr() {
	}

	/**
	 * Construct an expression, given its definition as a string. This will
	 * throw an IllegalArgumentException if the string does not contain a
	 * legal expression.
	 */
	public Expr(String definition) {
		parse(definition);
	}

	/**
	 * 
	 * @param definition
	 *            ==
	 * @param expssionType
	 *            == "Complex","Real"
	 * 
	 *            Complex ==> f(Z,i) Real ==> f(x,y,z) [Real number domain
	 *            only. tuple solvers for ODEs]
	 */
	public Expr(String definition, String expssionType) {
		this.type = expssionType;
		this.parse(definition);

	}

	/**
	 * Computes the value of this expression, when the variable x has a
	 * specified value. If the expression is undefined for the specified
	 * value of x, then Double.NaN is returned.
	 * 
	 * @param x
	 *            the value to be used for the variable x in the expression
	 * @return the computed value of the expression
	 */
	public ComplexNumber value(ComplexNumber x) {
		return eval(x);
	}

	/**
	 * Computes the value of this expression, when the variable x has a
	 * specified value. If the expression is undefined for the specified
	 * value of x, then Double.NaN is returned.
	 * 
	 * @param x
	 *            the value to be used for the variable x in the expression
	 * @return the computed value of the expression
	 */
	public double value(final double x) {
		return this.eval(x, 0.0, 0.0);
	}

	public double value(final double x, final double y, final double z) {
		return eval(x, y, z);
	}

	/**
	 * Return the original definition string of this expression. This is the
	 * same string that was provided in the constructor.
	 */
	public String toString() {
		return definition;
	}

	// ------------------- private implementation details
	// ----------------------------------

	private String definition; // The original definition of the expression,
								// as passed to the constructor.

	private byte[] code; // A translated version of the expression,
							// containing
							// stack operations that compute the value of
							// the expression.

	private ComplexNumber[] stack; // A stack to be used during the
	private double[] realStack; // evaluation of
								// the expression.

	private ComplexNumber[] constants; // An array containing all the
	private double[] realConstants; // constants
	// found in the expression.

	private static final byte // values for code array; values >= 0 are
								// indices into constants array
	PLUS = -1, MINUS = -2, TIMES = -3, DIVIDE = -4, POWER = -5, SIN = -6, COS = -7, TAN = -8, COT = -9, SEC = -10,
			CSC = -11, ARCSIN = -12, ARCCOS = -13, ARCTAN = -14, EXP = -15, LN = -16, LOG10 = -17, LOG2 = -18,
			ABS = -19, SQRT = -20, UNARYMINUS = -21,
			// x,y,z = f(real)
			// Z,i = f(Complex_
			VARIABLE_X = -22, VARIABLE_Y = -23, VARIABLE_Z = -24, VARIABLE_I = -25;

	private String[] functionNames = {
			// names of standard functions, used during parsing
			"sin", "cos", "tan", "cot", "sec", "csc", "arcsin", "arccos", "arctan", "exp", "ln", "log10", "log2",
			"abs", "sqrt" };

	/*private String[] mathConstants = { "PI", "E" }; // Mathematical
													// Constants*/	
	private double eval(final Tuple3d t3d) { 
		return this.eval(t3d.x,t3d.y,t3d.z);
	}
	
	private double eval(final double variable_x, final double variable_y, final double variable_z) { 
		// evaluate this expression for this value of these variables
		try {
			int top = 0;
			for (int i = 0; i < codeSize; i++) {
				if (code[i] >= 0)
					realStack[top++] = realConstants[code[i]];
				else if (code[i] >= POWER) {
					double y = realStack[--top];
					double x = realStack[--top];
					double ans = Double.NaN;
					switch (code[i]) {
					case PLUS:
						ans = x + y;
						break;
					case MINUS:
						ans = x - y;
						break;
					case TIMES:
						ans = x * y;
						break;
					case DIVIDE:
						ans = x / y;
						break;
					case POWER:
						ans = Math.pow(x, y);
						break;
					}
					if (Double.isNaN(ans))
						return ans;
					realStack[top++] = ans;
				} else if (code[i] == VARIABLE_X || code[i] == VARIABLE_Y || code[i] == VARIABLE_Z) {
					if (code[i] == VARIABLE_X)
						realStack[top++] = variable_x;
					else if (code[i] == VARIABLE_Y)
						realStack[top++] = variable_y;
					else if (code[i] == VARIABLE_Z)
						realStack[top++] = variable_z;
				} else {
					double x = realStack[--top];
					double ans = Double.NaN;
					switch (code[i]) {
					case SIN:
						ans = Math.sin(x);
						break;
					case COS:
						ans = Math.cos(x);
						break;
					case TAN:
						ans = Math.tan(x);
						break;
					case COT:
						ans = Math.cos(x) / Math.sin(x);
						break;
					case SEC:
						ans = 1.0 / Math.cos(x);
						break;
					case CSC:
						ans = 1.0 / Math.sin(x);
						break;
					case ARCSIN:
						if (Math.abs(x) <= 1.0)
							ans = Math.asin(x);
						break;
					case ARCCOS:
						if (Math.abs(x) <= 1.0)
							ans = Math.acos(x);
						break;
					case ARCTAN:
						ans = Math.atan(x);
						break;
					case EXP:
						ans = Math.exp(x);
						break;
					case LN:
						if (x > 0.0)
							ans = Math.log(x);
						break;
					case LOG2:
						if (x > 0.0)
							ans = Math.log(x) / Math.log(2);
						break;
					case LOG10:
						if (x > 0.0)
							ans = Math.log(x) / Math.log(10);
						break;
					case ABS:
						ans = Math.abs(x);
						break;
					case SQRT:
						if (x >= 0.0)
							ans = Math.sqrt(x);
						break;
					case UNARYMINUS:
						ans = -x;
						break;
					}
					if (Double.isNaN(ans))
						return ans;
					realStack[top++] = ans;

				}
			}
		} catch (Exception e) {
			return Double.NaN;
		}
		if (Double.isInfinite(realStack[0]))
			return Double.NaN;
		else
			return realStack[0];
	}

	private ComplexNumber eval(ComplexNumber x2) { 
		// evaluate this expression for this value of the variable
		try {
			int top = 0;
			for (int index = 0; index < codeSize; index++) {
				if (code[index] >= 0)
					stack[top++] = constants[code[index]];
				else if (code[index] >= POWER) {
					ComplexNumber y = stack[--top];
					ComplexNumber x = stack[--top];
					ComplexNumber ans = new ComplexNumber(Double.NaN);
					switch (code[index]) {
					case PLUS:
						ans = x.plus(y);
						break;
					case MINUS:
						ans = x.minus(y);
						break;
					case TIMES:
						ans = x.times(y);
						break;
					case DIVIDE:
						ans = x.divides(y);
						break;
					case POWER:
						ans = x.power((int) y.real);// Math.pow(x, y);
						break;
					}
					if (Double.isNaN(ans.real))
						return ans;
					stack[top++] = ans;
				} else if (code[index] == VARIABLE_Z) {
					stack[top++] = x2;
				} else if (code[index] == VARIABLE_I) {
					stack[top++] = ComplexNumber.i;
				} else {
					ComplexNumber x = stack[--top];
					ComplexNumber ans = new ComplexNumber(Double.NaN);
					switch (code[index]) {
					case SIN:
						ans = x.sine();// Math.sin(x);
						break;
					case COS:
						ans = x.cosine();// Math.cos(x);
						break;
					case TAN:
						ans = x.tangent();// Math.tan(x);
						break;
					case COT:
						ans = x.cosine().divides(x.sine()); // Math.cos(x) /
															// Math.sin(x);
						break;
					case SEC:
						ans = x.cosine().reciprocal();// 1.0 / Math.cos(x);
						break;
					case CSC:
						ans = x.sine().reciprocal(); // 1.0 / Math.sin(x);
						break;
					case ARCSIN:
						// if (Math.abs(x.real) <= 1.0)
						ans = x.inverseSine();// Math.asin(x);
						break;
					case ARCCOS:
						// if (Math.abs(x.real) <= 1.0)
						ans = x.inverseCosine();// Math.acos(x);
						break;
					case ARCTAN:
						ans = x.inverseTangent();// Math.atan(x);
						break;
					case EXP:
						ans = x.exp();// Math.exp(x);
						break;
					case LN:
						// if (x.real > 0.0)
						ans = x.ln();// Math.log(x);
						break;
					/*
					 * case LOG2: if (x > 0.0) ans = Math.log(x) /
					 * Math.log(2); break; case LOG10: if (x > 0.0) ans =
					 * Math.log(x) / Math.log(10); break;
					 */
					case ABS:
						ans = new ComplexNumber(x.abs());// Math.abs(x);
						break;
					case SQRT:
						// if (x.real >= 0.0)
						ans = x.sqroot();// Math.sqrt(x);
						break;
					case UNARYMINUS:
						ans = new ComplexNumber(-1.0).times(x);
						break;
					}
					if (Double.isNaN(ans.real) || Double.isNaN(ans.imaginary))
						return ans;
					stack[top++] = ans;

				}
			}
		} catch (Exception e) {
			return new ComplexNumber(Double.NaN);
		}
		if (Double.isInfinite(stack[0].real) || Double.isInfinite(stack[0].imaginary))
			return new ComplexNumber(Double.NaN);
		else
			return stack[0];
	}

	private int pos = 0, constantCt = 0, codeSize = 0; // data for use
														// during parsing

	void error(String message) {
		// called when an error occurs during parsing
		throw new IllegalArgumentException("Parse error:  " + message + "  (Position in data = " + pos + ".)");
	}

	private int computeStackUsage() { // call after code[] is computed
		int s = 0; // stack size after each operation
		int max = 0; // maximum stack size seen
		for (int i = 0; i < codeSize; i++) {
			if (code[i] >= 0 || code[i] == VARIABLE_Z || code[i] == VARIABLE_I || code[i] == VARIABLE_X
					|| code[i] == VARIABLE_Y) {
				s++;
				if (s > max)
					max = s;
			} else if (code[i] >= POWER)
				s--;
		}
		return max;
	}

	private void parse(String definition) {
		if (this.type.equals("Complex")) {
			this.parseComplex(definition);
		} else {
			this.parseReal(definition);
		}
	}

	private void parseReal(String definition) {
		// Parse the definition and produce all
		// the data that represents the expression
		// internally; can throw IllegalArgumentException
		if (definition == null || definition.trim().equals(""))
			error("No data provided to Expr constructor");
		this.definition = definition;
		code = new byte[definition.length()];
		realConstants = new double[definition.length()];
		parseExpression();
		skip();
		if (next() != 0)
			error("Extra data found after the end of the expression.");
		int stackSize = computeStackUsage();
		realStack = new double[stackSize];
		byte[] c = new byte[codeSize];
		System.arraycopy(code, 0, c, 0, codeSize);
		code = c;
		double[] A = new double[constantCt];
		System.arraycopy(realConstants, 0, A, 0, constantCt);
		realConstants = A;
	}

	private void parseComplex(String definition) {
		// Parse the definition and produce all the data that represents the
		// expression internally; can throw IllegalArgumentException
		if (definition == null || definition.trim().equals(""))
			error("No data provided to Expr constructor");
		this.definition = definition;
		code = new byte[definition.length()];
		constants = new ComplexNumber[definition.length()];
		parseExpression();
		skip();
		if (next() != 0)
			error("Extra data found after the end of the expression.");
		int stackSize = computeStackUsage();
		stack = new ComplexNumber[stackSize];
		byte[] c = new byte[codeSize];
		System.arraycopy(code, 0, c, 0, codeSize);
		code = c;
		ComplexNumber[] A = new ComplexNumber[constantCt];
		System.arraycopy(constants, 0, A, 0, constantCt);
		constants = A;
	}

	private char next() {
		// return next char in data or 0 if data is all used up
		if (pos >= definition.length())
			return 0;
		else
			return definition.charAt(pos);
	}

	private void skip() { // skip over white space in data
		while (Character.isWhitespace(next()))
			pos++;
	}

	// remaining routines do a standard recursive parse of the expression
	private void parseExpression() {
		boolean neg = false;
		skip();
		if (next() == '+' || next() == '-') {
			neg = (next() == '-');
			pos++;
			skip();
		}
		parseTerm();
		if (neg)
			code[codeSize++] = UNARYMINUS;
		skip();
		while (next() == '+' || next() == '-') {
			char op = next();
			pos++;
			parseTerm();
			code[codeSize++] = (op == '+') ? PLUS : MINUS;
			skip();
		}
	}

	private void parseTerm() {
		parseFactor();
		skip();
		while (next() == '*' || next() == '/') {
			char op = next();
			pos++;
			parseFactor();
			code[codeSize++] = (op == '*') ? TIMES : DIVIDE;
			skip();
		}
	}

	private void parseFactor() {
		parsePrimary();
		skip();
		while (next() == '^') {
			pos++;
			parsePrimary();
			code[codeSize++] = POWER;
			skip();
		}
	}

	private void parsePrimary() {
		skip();
		char ch = next();
		if (ch == 'z' || ch == 'Z' || ch == 'i' || ch == 'I' || ch == 'x' || ch == 'X' || ch == 'y' || ch == 'Z') {
			pos++;

			if (ch == 'z' || ch == 'Z') {
				code[codeSize++] = VARIABLE_Z;
			} else if (ch == 'i' || ch == 'I') {
				code[codeSize++] = VARIABLE_I;
			} else if (ch == 'x' || ch == 'X') {
				code[codeSize++] = VARIABLE_X;
			} else if (ch == 'y' || ch == 'Y') {
				code[codeSize++] = VARIABLE_Y;
			}
		} else if (Character.isLetter(ch))
			parseWord();
		else if (Character.isDigit(ch) || ch == '.')
			parseNumber();
		else if (ch == '(') {
			pos++;
			parseExpression();
			skip();
			if (next() != ')')
				error("Expected a right parenthesis.");
			pos++;
		} else if (ch == ')')
			error("Unmatched right parenthesis.");
		else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^')
			error("Operator '" + ch + "' found in an unexpected position.");
		else if (ch == 0)
			error("Unexpected end of data in the middle of an expression.");
		else
			error("Illegal character '" + ch + "' found in data.");
	}

	private void parseWord() {
		String w = "";
		while (Character.isLetterOrDigit(next())) {
			w += next();
			pos++;
		}
		w = w.toLowerCase();

		/*
		 * for(int i = 0; i < mathConstants.length; i++) {
		 * if(w.equals(mathConstants[i])) { skip(); parseExpression();
		 * skip(); } }
		 */

		for (int i = 0; i < functionNames.length; i++) {
			if (w.equals(functionNames[i])) {
				skip();
				if (next() != '(')
					error("Function name '" + w + "' must be followed by its parameter in parentheses.");
				pos++;
				parseExpression();
				skip();
				if (next() != ')')
					error("Missing right parenthesis after parameter of function '" + w + "'.");
				pos++;
				code[codeSize++] = (byte) (SIN - i);
				return;
			}
		}
		error("Unknown word '" + w + "' found in data.");
	}

	private void parseNumber() {
		String w = "";
		while (Character.isDigit(next())) {
			w += next();
			pos++;
		}
		if (next() == '.') {
			w += next();
			pos++;
			while (Character.isDigit(next())) {
				w += next();
				pos++;
			}
		}
		if (w.equals("."))
			error("Illegal number found, consisting of decimal point only.");
		if (next() == 'E' || next() == 'e') {
			w += next();
			pos++;
			if (next() == '+' || next() == '-') {
				w += next();
				pos++;
			}
			if (!Character.isDigit(next()))
				error("Illegal number found, with no digits in its exponent.");
			while (Character.isDigit(next())) {
				w += next();
				pos++;
			}
		}

		if (this.type.equals("Complex")) {
			ComplexNumber d = new ComplexNumber(Double.NaN);
			try {
				d = new ComplexNumber(Double.valueOf(w).doubleValue());
			} catch (Exception e) {
			}
			if (Double.isNaN(d.real))
				error("Illegal number '" + w + "' found in data.");
			code[codeSize++] = (byte) constantCt;
			constants[constantCt++] = d;
		} else {
			double d = Double.NaN;
			try {
				d = Double.valueOf(w).doubleValue();
			} catch (Exception e) {
			}
			if (Double.isNaN(d))
				error("Illegal number '" + w + "' found in data.");
			code[codeSize++] = (byte) constantCt;
			realConstants[constantCt++] = d;
		}
	}

} // end class Expr


