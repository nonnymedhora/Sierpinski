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
	double xR; // A value of x for which f(x,y,z) is to be calculated.
	double yR = 0.0; // in case its for lesser dimension
	double zR = 0.0;

	double valR; // The value of f(x,y,z) for the specified value of x,y,z.

	public FunctionEvaluator() {
	}
	
	public double evaluate(final String funcString, final Tuple3d t3d) {
		return this.evaluate(funcString,
					new BigDecimal(String.valueOf(t3d.x)).doubleValue(),
					new BigDecimal(String.valueOf(t3d.y)).doubleValue(),
							new BigDecimal(String.valueOf(t3d.z)).doubleValue()
			);
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
	
	
	
	
	public static void main(String[] args) {
		FunctionEvaluator funcEval = new FunctionEvaluator();
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
		String dx_dt = "sin(-2.24 * y) - cos(0.43 * x)";
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
		}
		
		
	}
}

