/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

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
	double xR; // A value of x for which f(x,y,z) is to be calculated.
	double yR = 0.0; // in case its for lesser dimension
	double zR = 0.0;

	double valR; // The value of f(x,y,z) for the specified value of x,y,z.

	public FunctionEvaluator() {
	}
	
	public double evaluate(final String funcString, final Tuple3d t3d) {
		return this.evaluate(funcString,t3d.x,t3d.y,t3d.z);
	}
		

	public double evaluate(final String funcString, final double x, final double y, final double z) {
		if (funcString.length() == 0) {
			System.out.println("Err___line");
			return 0.0;
		}

		try {
			expression = new Expr(funcString, "Real");
		} catch (IllegalArgumentException e) {
			// An error was found in the input. Print an error message
			expression.error("Error!  The definition of f(x) is not valid.");
			System.out.println(e.getMessage());
		}
		
		/*
		 * If complexNumber is not a legal complex number, print an error
		 * message. Otherwise, compute f(x) and return the result.
		 */
		if(x==(Double.NaN)||y==(Double.NaN)||z==(Double.NaN)){
			System.out.println("Err___line2");
			return Double.NaN;
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
		if (Double.isNaN(valR))
			System.out.println("f("+x+","+y+"," + z + ") is undefined.");
		else {
			/* System.out.println("f(" + z + ") = " + val); */}
		return valR;

	}

	public ComplexNumber evaluate(final String funcString, final ComplexNumber z0) {
		// System.out.print("\nf(z) = " + funcString.trim());
		if (funcString.length() == 0) {
			System.out.println("Err___line");
			return null;
		}

		try {
			expression = new Expr(funcString);
		} catch (IllegalArgumentException e) {
			// An error was found in the input. Print an error message
			expression.error("Error!  The definition of f(x) is not valid.");
			System.out.println(e.getMessage());
		}

		/*
		 * If complexNumber is not a legal complex number, print an error
		 * message. Otherwise, compute f(x) and return the result.
		 */
		// System.out.print("\nz = ");
		if (z0 == null) {
			System.out.println("Err___line2");
			return null;
		}
		try {
			z = z0;
		} catch (NumberFormatException e) {
			System.out.println("\"" + z0 + "\" is not a legal complexnumber.");
		}
		val = expression.value(z);
		if (Double.isNaN(val.real))
			System.out.println("f(" + z + ") is undefined.");
		else {
			/* System.out.println("f(" + z + ") = " + val); */}
		return val;
	}
}

