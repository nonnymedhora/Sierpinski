/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.math.BigDecimal;

/**
 * @author Navroz
 *
 */
public class FunctionEvaluator {

	String line; // A line of input read from the user.
	Expr expression; // The definition of the function f(z).

	ComplexNumber z; // A value of z for which f(z) is to be calculated.
	ComplexNumber val; // The value of f(z) for the specified value of x.

	String type = "Complex"; // other is "Real"

	/////////// for Real /////////////////
	double xR = 0.0; // A value of x for which f(x,y,z) is to be calculated.
	double yR = 0.0; // in case its for lesser dimension
	double zR = 0.0;

	double valR; // The value of f(x,y,z) for the specified value of x,y,z.

	public FunctionEvaluator() {
	}
	
	public double evaluate(String funcString, final Tuple3d t3d) {
		if (t3d == null) {
			return Double.NaN;
		}
		if (t3d.x == Double.NaN || t3d.y == Double.NaN || t3d.z == Double.NaN) {
			return Double.NaN;
		}
		if (funcString.length() == 0) {
			System.out.println("Err___line");
			return 0.0;
		}

		funcString = this.stripWhitespaces(funcString.toLowerCase());
		funcString = funcString.replaceAll("pi", String.valueOf(Math.PI));
		funcString = funcString.replaceAll("π", String.valueOf(Math.PI));
		
		if (!(t3d.x == Double.NaN || t3d.y == Double.NaN || t3d.z == Double.NaN)) {
			return this.evaluate(funcString, new BigDecimal(String.valueOf(t3d.x)).doubleValue(),
					new BigDecimal(String.valueOf(t3d.y)).doubleValue(),
					new BigDecimal(String.valueOf(t3d.z)).doubleValue());
		}
		return  Double.NaN;
	}
		

	public double evaluate(String funcString, final double x, final double y, final double z) {
		/*
		 * If number is not a legal number, print an error
		 * message. Otherwise, compute f(x) and return the result.
		 */
		if (x == (Double.NaN) || y == (Double.NaN) || z == (Double.NaN)) {
			/*System.out.println("Err___line2");*/
			return Double.NaN;
		}
		if (funcString == null || funcString.length() == 0) {
			/*System.out.println("Err___line");*/
			return 0.0;
		}
		
		funcString = this.stripWhitespaces(funcString.toLowerCase());
		funcString = funcString.replaceAll("pi", String.valueOf(Math.PI));
		funcString = funcString.replaceAll("π", String.valueOf(Math.PI));
		
		try {
			if (funcString.indexOf('i') == -1) {
				expression = new Expr(funcString, "Real");
			} else {
				return 0.0;// expression = new Expr(this.processForI(funcString),"Complex");
			}
		} catch (IllegalArgumentException e) {
			// An error was found in the input. Print an error message
			expression.error("Error!  The definition of f(x) is not valid.");
			System.out.println(e.getMessage());
		}
		
		try {
			xR = x;
			yR = y;
			zR = z;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.out.println("\"" +  "\" is not a legal number.");
		}
		
		valR = expression.value(xR,yR,zR);
		
//		if (Double.isNaN(valR))
//			System.out.println("f("+x+","+y+"," + z + ") is undefined.");
//		else {
//			/* System.out.println("f(" + z + ") = " + val); */}
		return valR;

	}

	public ComplexNumber evaluate(String funcString, final ComplexNumber z0) {
		//	System.out.println("\nf(z) = " + funcString.trim());
		if (funcString.length() == 0) {
			/*System.out.println("Err___line");*/
			return null;
		}

		/*
		 * If complexNumber is not a legal complex number, print an error
		 * message. Otherwise, compute f(x) and return the result.
		 */
		if (z0 == null || z0.isNaN()) {
			/*System.out.println("Err___line2");*/
			return new ComplexNumber(Double.NaN, Double.NaN);
		}
		
		funcString = this.stripWhitespaces(funcString.toLowerCase());
		funcString = funcString.replaceAll("pi", String.valueOf(Math.PI));
		funcString = funcString.replaceAll("π", String.valueOf(Math.PI));

		if (funcString.indexOf("i") > -1) {		//	funcString contains 'i'
			funcString = this.processForI(funcString);
		}
/*System.out.println("After--processfuncString = "+funcString);*/
		try {
			expression = new Expr(funcString);
		} catch (IllegalArgumentException e) {
			// An error was found in the input. Print an error message
			expression.error("Error!  The definition of f(x) is not valid.");
			System.out.println(e.getMessage());
		}
		
		try {
			z = z0;
		} catch (NumberFormatException e) {
			System.out.println("\"" + z0 + "\" is not a legal complexnumber.");
		}
		
		val = expression.value(z);
		
//		if ( Double.isNaN(val.real) || Double.isNaN(val.imaginary) )
//			/*System.out.println("f(" + z + ") is undefined.");*/
//		else {
//			/* System.out.println("f(" + z + ") = " + val); */}
		return val;
	}

	private String processForI(String aString) {
		String[] iSplits = aString.split("i");
		String funcString = "";
		for (int i = 0; i < iSplits.length; i++) {
			String aSplit = iSplits[i];
			funcString += aSplit;
			if ((aSplit.length() == 0) && (i == 0) && funcString.equals(aSplit)) {
				funcString += "i*";
				continue;
			}

			if (aSplit.length() != 1 && i != iSplits.length - 1) {
				boolean isPrvsCharDigit = this.isLastCharDigit(funcString);
				boolean isPrvsCharCloseBrace = this.isLastCharCloseBrace(funcString);
				if (isPrvsCharDigit || isPrvsCharCloseBrace) {
					funcString += "*i";
					continue;
				}
				boolean isPrvsCharOperator = this.isLastCharOperator(funcString);
				if (isPrvsCharOperator) {
					funcString += "i";
				}
			} else {
				if (i != iSplits.length - 1) {
					boolean isPrvsCharDigit = this.isLastCharDigit(funcString);
					boolean isPrvsCharCloseBrace = this.isLastCharCloseBrace(funcString);
					if (isPrvsCharDigit || isPrvsCharCloseBrace) {

						funcString += "*i";
						continue;

					}
					boolean isPrvsCharOperator = this.isLastCharOperator(funcString);
					if (isPrvsCharOperator) {
						funcString += "i";
					}
				}
			}

		}
		return funcString;
	}

	private String processForIString(String aString) {
		String iFuncString = "";
		// there may be more than 1 'i'
		int numberOfI = this.getNumIs(aString);
		int[] indexOfIArr = this.getIndexIs(aString, numberOfI);
 
		for (int i = 0; i < indexOfIArr.length; i++) {
			int iIndex = indexOfIArr[i];
			if (i == 0) {
				iFuncString += iFuncString + aString.substring(0, iIndex);
			} else {
				iFuncString += iFuncString + aString.substring(indexOfIArr[i - 1], iIndex);
			}

			boolean isPrvsCharDigit = this.isLastCharDigit(iFuncString);			//	this.isPrvsCharDigit(aString, iIndex);			
			boolean isPrvsCharCloseBrace = this.isLastCharCloseBrace(iFuncString);
			if( isPrvsCharDigit || isPrvsCharCloseBrace ) {
				iFuncString += "*";
				continue;
			}
			
			boolean isNextCharOpenBrace = aString.charAt(iIndex+1)=='(';
			//????
			/*
			
			boolean isPrvsCharOperator = this.isLastCharOperator(iFuncString);		// 	this.isPrvsCharOperator(aString, iIndex);
			
			boolean isSurroundCharBrace = this.isSurroundCharBrace(aString,iIndex);*/
		}
		return iFuncString;
	}

	private boolean isLastCharCloseBrace(String aString) {
		char ch = aString.charAt(aString.length() - 1);
		return ch == ')';
	}

	private boolean isLastCharOperator(String aString) {
		char ch = aString.charAt(aString.length() - 1);
		return (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^');
	}

	private boolean isLastCharDigit(String aString) {
		return Character.isDigit(aString.charAt(aString.length() - 1));
	}

	private String processForIssssss(String funcString) {
		// there may be more than 1 'i'
		int numberOfI = this.getNumIs(funcString);
		int[] indexOfIArr = null;
		
		if (numberOfI > 1) { // more than one 'i'
			indexOfIArr = this.getIndexIs(funcString, numberOfI);

		} else { // only 1 i in function
			boolean isPrvsCharDigit;
			boolean isPrvsCharOperator;
			boolean isSurroundCharBrace;
			if (!(funcString.indexOf('i') == 0)) { 
				// function contains a single 'i', but doesn't start with 'i'
				isPrvsCharDigit = this.isPrvsCharDigit(funcString, funcString.indexOf("i"));
				isPrvsCharOperator = this.isPrvsCharOperator(funcString, funcString.indexOf("i"));
				isSurroundCharBrace=this.isSurroundCharBrace(funcString,funcString.indexOf("i"));

				// is previous character:
				// an operator(+,-,/,*,^),
				// a brace '(' or ')'
				// or a digit
				if (isPrvsCharDigit) {
					// previous character to 'i' is a digit
					funcString = funcString.replaceAll("i","*i");
				} else if(isPrvsCharOperator){
					// previous character to 'i' is an operator	(+,-,*,/)
					/*funcString = funcString.replaceAll("i", "(1.0*i)");*/
				} else if (isSurroundCharBrace) {
					boolean isPrvsCharOpenBrace = this.isPrvsCharOpenBrace(funcString, funcString.indexOf("i"));
					boolean isPrvsCharCloseBrace = this.isPrvsCharOpenBrace(funcString, funcString.indexOf("i"));

					boolean isNextCharOpenBrace = this.isNextCharOpenBrace(funcString, funcString.indexOf("i"));
					boolean isNextCharCloseBrace = this.isNextCharOpenBrace(funcString, funcString.indexOf("i"));

					// funcString = funcString.replaceAll("i", "(1.0*i)");
				}
			} else {
				// function string starts with 'i', and has only 1 'i'
				funcString = funcString.replaceAll("i","*(1.0*i)");
			}

		}
		return funcString;
	}
	
	private String stripWhitespaces(String aString) {
		String retStr = "";
		for (int i = 0; i < aString.length(); i++) {
			char ch = aString.charAt(i);
			if (!Character.isWhitespace(ch)) {
				retStr += ch;
			}
		}
		return retStr;
	}

	private boolean isSurroundCharBrace(String functionString, int startIndex) {
		return this.isPrvsCharOpenBrace(functionString, startIndex)
				|| this.isPrvsCharCloseBrace(functionString, startIndex)
				|| this.isNextCharOpenBrace(functionString, startIndex)
				|| this.isNextCharCloseBrace(functionString, startIndex);
	}

	private boolean isPrvsCharOperator(String aString, int startIndex) {
		if (startIndex == 0)
			return false;
		int index = startIndex - 1;
		while (index >= 0) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index -= 1;
				continue;
			}
			if (ch=='+'||ch=='-'||ch=='*'||ch=='/'||ch=='^')
				return true;		//	ch is in '+','-','*','/','^'
			else					//	
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}
	
	private boolean isNextCharOpenBrace(String aString, int startIndex) {
		if (startIndex == aString.length()-1)
			return false;
		int index = startIndex - 1;
		while (index <= aString.length()) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index += 1;
				continue;
			}
			if (ch=='(')
				return true;		//	ch is in '('
			else					//	
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}
	
	private boolean isNextCharCloseBrace(String aString, int startIndex) {
		if (startIndex == aString.length()-1)
			return false;
		int index = startIndex - 1;
		while (index <= aString.length()) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index += 1;
				continue;
			}
			if (ch==')')
				return true;		//	ch is in ')'
			else					//	
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}
	
	private boolean isNextCharBrace(String aString, int startIndex) {
		if (startIndex == aString.length()-1)
			return false;
		int index = startIndex - 1;
		while (index <= aString.length()) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index += 1;
				continue;
			}
			if (ch=='('||ch==')')
				return true;		//	ch is in '(',')'
			else					//	
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}
	
	private boolean isPrvsCharBrace(String aString, int startIndex) {
		if (startIndex == 0)
			return false;
		int index = startIndex - 1;
		while (index >= 0) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index -= 1;
				continue;
			}
			if (ch=='('||ch==')')
				return true;		//	ch is in '(',')'
			else					//	
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}
	
	private boolean isPrvsCharOpenBrace(String aString, int startIndex) {
		if (startIndex == 0)
			return false;
		int index = startIndex - 1;
		while (index >= 0) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index -= 1;
				continue;
			}
			if (ch == '(')
				return true; 	// ch is in '(',')'
			else 				//
				return false; 	// no dots . checked/evaluated - so no ".i"

		}
		return false;
	}
	
	private boolean isPrvsCharCloseBrace(String aString, int startIndex) {
		if (startIndex == 0)
			return false;
		int index = startIndex - 1;
		while (index >= 0) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index -= 1;
				continue;
			}
			if (ch == ')')
				return true; 	// ch is in ')'
			else 				//
				return false; 	// no dots . checked/evaluated - so no ".i"

		}
		return false;
	}
	
	private boolean isPrvsCharDigit(String aString, int startIndex) {
		// previous to i
		// can be 	operator		(+-*/), 
		//			braces			(),		
		//			mathConstants	π/(P),E	etc		==	no need! 4 this
		if (startIndex == 0)
			return false;
		int index = startIndex - 1;
		while (index >= 0) {
			char ch = aString.charAt(index);
			if (Character.isWhitespace(ch)) {
				index -= 1;
				continue;
			}
			if (Character.isDigit(ch))
				return true;		//[0-9]
			else					//	ch is in '(','+','-','*','/','^',')'
				return false;		//	no dots . checked/evaluated		-	so no ".i"
			
		}
		return false;
	}

	private int[] getIndexIs(String aString, int numI) {
		int indexes[] = new int[numI];
		int current = 0;
		for (int strIndex = 0; strIndex < aString.length(); strIndex++) {
			if (aString.charAt(strIndex) == 'i' || aString.charAt(strIndex) == 'I') {
				indexes[current] = strIndex;
				current += 1;
			}
		}
		System.out.println("numI==indexes.Length  " + (indexes.length == numI));
		return indexes;
	}

	/**
	 * Returns the number of i / I characterin aString
	 * @param aString
	 * @return	numI
	 */
	private int getNumIs(String aString) {
		int numI = 0;
		for (int index = 0; index < aString.length(); index++) {
			if (aString.charAt(index) == 'i') {
				numI += 1;
			}
		}
		return numI;
	}

	/*public ComplexNumber evaluate(String function, ComplexNumber z0, ComplexNumber compConst) {
		function = function.replaceAll("P", String.valueOf(Math.PI))
				.replaceAll("p", String.valueOf(Math.PI));
		function = function.replaceAll("π", String.valueOf(Math.PI));
		
		if (function.indexOf("i")>-1||function.indexOf("I")>-1) {
			function = function.replaceAll("i", "(1.0*i)");
			function = function.replaceAll("I", "(1.0*i)");
		}
		
		function = function.replaceAll("c", "(" + compConst.toString().replaceAll("i", "*i") + ")")
				.replaceAll("C", "(" + compConst.toString().replaceAll("i", "*i") + ")");

		return this.evaluate(function, z0);
	}*/
	
	
	public static void main(String[] args) {
		FunctionEvaluator funcEval = new FunctionEvaluator();
		String formula=null;
		ComplexNumber compConst = new ComplexNumber(5,-1.5);		
		ComplexNumber zz = new ComplexNumber(3,4);
		/*//1
		String dx_dt = "x*(y+z)";
		String dy_dt = "y*(z+x)";
		String dz_dt = "z*(x+y)";

		double x = 10.1;
		double y = 15.1;
		double z = 0.1;*/
		
		//2	DeJong
		/** a = -2.24, b = 0.43, c = -0.65, d = -2.43 
		 * a = 2.01, b = -2.53, c = 1.61, d = -0.33 
		 * a = -2, b = -2, c = -1.2, d = 2*/
		/*String dx_dt = "sin(-2.24 * y) - cos(0.43 * x)";
		String dy_dt = "sin(-0.65 * x) - cos(-2.43 * y)";
		

		double x = 10.1;
		double y = 15.1;
		double z = 0;

		Tuple3d t3d = new Tuple3d(x, y, z);
		System.out.println("Start" + t3d);

		for (int i = 0; i < 50; i++) {
			x = funcEval.evaluate(dx_dt, t3d);
			y = funcEval.evaluate(dy_dt, t3d);
//			z = funcEval.evaluate(dz_dt, t3d);

			t3d = new Tuple3d(x, y, z);
			System.out.println(i + " => " + t3d);
		}*/
		
		
		/*//3
		String formula = "(z-C)/(z+C)";
		ComplexNumber compConst = new ComplexNumber(5,-1.5);		
		ComplexNumber zz = new ComplexNumber(3,4);
		System.out.println("z is: "+zz);
		System.out.println("C is: "+compConst);
		System.out.println("Formula is:-- "+formula);
		System.out.println("Result is: "+funcEval.evaluate(formula,zz,compConst));*/

		//4		NoNoNo
	/*	formula="z+((π/2)*(1.0+0.6i))";
		System.out.println("z is: "+zz);
		System.out.println("C is: "+compConst);
		System.out.println("Formula is:-- "+formula);
		System.out.println("Result is: "+funcEval.evaluate(formula,zz));*/
		
		/*//5
		formula="exp(z-i)";						//"i((z-i)/(z+i))+1.56i";
		System.out.println("Formula is:-- "+formula);
		String[] iSplits = formula.split("i");
		for(int i = 0; i < iSplits.length; i++){
			System.out.println(""+i+"  ["+iSplits[i]+"]  (isNull=="+(iSplits[i]==null)+")"+"  length=="+iSplits[i].length());
		}
		
		System.out.println("Processed 4 i\n"+funcEval.processForI(formula));
		
		System.out.println("z is: "+zz);
		System.out.println("C is: "+compConst);
		System.out.println("Formula is:-- "+formula);
		System.out.println("Result is: "+funcEval.evaluate(formula,zz));*/
		
		/*//6
		formula = "(1/sqrt(2*Pi))*exp(((-Z)^2)/2)";
		zz = new ComplexNumber("100.0 + 8.597662771285476i");
		System.out.println("(6)z is: "+zz);
		System.out.println("C is: "+compConst);
		System.out.println("Formula is:-- "+formula);
		System.out.println("Result is: "+funcEval.evaluate(formula,zz));
		
			f(z)	(1/sqrt(2*Pi))*exp(((-Z)^2)/2)
		 * f(100.0 + 8.597662771285476i) is undefined.
f(100.0 + 8.564273789649416i) is undefined.
f(100.0 + 8.530884808013356i) is undefined.
f(100.0 + 8.497495826377296i) is undefined.
f(100.0 + 8.464106844741234i) is undefined.*/
		
		//7
		formula = "5*(y-x)+cos(y)";
		Tuple3d tuple3d = new Tuple3d(10.0,15.0,0.0);
		System.out.println("(7)t3d is: "+tuple3d);
		System.out.println("Formula is:-- "+formula);
		/*System.out.println("Processed 4 i\n"+funcEval.processForI(formula));*/
		System.out.println("Result is: "+funcEval.evaluate(formula,tuple3d));
		
		
	}

}

