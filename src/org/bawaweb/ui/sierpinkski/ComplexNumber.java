/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.util.Objects;



/**
 * @author Navroz
 *
 */
public class ComplexNumber {
	
//	used in complex number
	final String START_BRACKET = "(";
	final String END_BRACKET = ")";
	final String WHITESPACE = " ";
	final String EMPTY = "";
	final String PIPE = "\\|";
	
	public static final ComplexNumber i = new ComplexNumber(0.0, 1.0);
	
//	final ComplexNumber DUMMY = new ComplexNumber(Double.NaN,Double.NaN);
//	ComplexNumber Z = DUMMY;

	
	final String I = "i";
	public final double real;		// the real part
	public final double imaginary;	// the imaginary part
	
	// return the real or imaginary part
	public double real(){
		return real;
	}
	
	public double imaginary(){
		return imaginary;
	}

	
	public ComplexNumber(double r) {
		super();
		this.real = r;
		this.imaginary = 0.0;
	}
	
	
	/**
	 * @param r	-	real part
	 * @param i	-	imaginary part
	 */
	public ComplexNumber(double r, double i) {
		super();
		this.real = r;
		this.imaginary = i;
	}
	
	
	//convert	0.2-7.5i	//	5.7	//	-1.1i	//-4.3-0.7i
	//3.1+i	//-1.8+0.8i	//	-0.8
	//	1.1i
	//	NO	leading	plus	SO	+0.8+1.5i	invalid
	//						but	0.8+1.5i	valid
	//	No stars for i		SO	1.1*i		invalid
	//						but	1.1i		valid
	public ComplexNumber(String c) {
//		System.out.println("c[1] is "+c);
		c = c.replaceAll(WHITESPACE,EMPTY);
//		System.out.println("c[2] is "+c);
		
		final String plus = "+";
		final String minus = "-";
		final String[] EMPTYARR = new String[] {};

		int i_index = c.indexOf(I);					//System.out.println("i_index="+i_index);
		final int plusIndex = c.indexOf(plus);		//System.out.println("plusIndex"+plusIndex);
		final int minusIndex = c.indexOf(minus);	//System.out.println("minusIndex="+minusIndex);

		boolean hasPlus = plusIndex > -1;			//System.out.println("hasPlus is "+hasPlus);
		boolean hasMinus = minusIndex > -1;			//System.out.println("hasMinus is "+hasMinus);

		int plusCount = 0;
		for (int i = 0; i < c.length(); i++) {
			if (plus.equals(String.valueOf(c.charAt(i)))) {
				plusCount += 1;
			}
		}											//System.out.println("plusCount is "+plusCount);

		int minusCount = 0;
		for (int i = 0; i < c.length(); i++) {
			if (minus.equals(String.valueOf(c.charAt(i)))) {
				minusCount += 1;
			}
		}											//System.out.println("minusCount is "+minusCount);

		if (i_index == -1) {
			this.real = Double.parseDouble(c);
			this.imaginary = 0.0;
		} else if (minusCount == 0) {
			if (plusCount == 1) {
				String realPart = c.substring(0, plusIndex);
				String imaginaryPart = c.substring(plusIndex);
				this.real = Double.parseDouble(realPart);
				if (plusIndex + 1 == i_index/* && imaginaryPart.equals(I)*/) {
					this.imaginary = 1.0;
				} else {
					this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
				}
			} else {
System.out.println("WHYr v here?");
this.real=Double.NaN;this.imaginary=Double.NaN;
			}
		} else {
			if (plusCount == 1) {
				String realPart = c.substring(0, plusIndex);
				String imaginaryPart = c.substring(plusIndex);
				this.real = Double.parseDouble(realPart);
				if (plusIndex + 1 == i_index && imaginaryPart.equals(I)) {
					this.imaginary = 1.0;
				} else {
					this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
				}
			} else {
				if (!c.startsWith(minus)) {
					String realPart = c.substring(0, minusIndex);
					String imaginaryPart = c.substring(minusIndex);
					this.real = Double.parseDouble(realPart);
					if (minusIndex + 1 == i_index && imaginaryPart.equals(I)) {
						this.imaginary = 1.0;
					} else {
						this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
					} 
				} else {
					String c2 = c.substring(1,c.length()-1);	//remove leading minus
					System.out.println("C2 us "+c2);
					final int c2MinusIndex = c2.indexOf(minus);
					if (c2MinusIndex>0) {
						this.real = Double.parseDouble(c2.substring(0, c2MinusIndex)) * -1;
						this.imaginary = Double.parseDouble(c2.substring(c2MinusIndex).replace(I, EMPTY));
					}else{
						this.real = Double.parseDouble(c.replace(I,EMPTY));
						this.imaginary=0;
					}
				}
			}
		}
	}
	
	
	// return a new Complex object whose value is (this * b)
    public ComplexNumber times(ComplexNumber b) {
    	ComplexNumber a = this;
        double real = a.real * b.real - a.imaginary * b.imaginary;
        double imag = a.real * b.imaginary + a.imaginary * b.real;
        return new ComplexNumber(real, imag);
    }
    
 // return a new Complex object whose value is (this + b)
    public ComplexNumber plus(ComplexNumber b) {
    	ComplexNumber a = this;             // invoking object
        double real = a.real + b.real;
        double imag = a.imaginary + b.imaginary;
        return new ComplexNumber(real, imag);
    }
    
 // return a new Complex object whose value is (this - b)
    public ComplexNumber minus(ComplexNumber b) {
    	ComplexNumber a = this;             // invoking object
        double real = a.real - b.real;
        double imag = a.imaginary - b.imaginary;
        return new ComplexNumber(real, imag);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (imaginary == 0.0 && real != 0.0)
			return real + "";
		if (real == 0.0 && imaginary != 0.0)
			return imaginary + "i";
		if (real < 0.0) {
			if (imaginary < 0.0)
				return real + " - " + (-1*imaginary) + "i";
			else
				return real + " + " + imaginary + "i";
		} else if (real > 0.0) {
			if (imaginary < 0.0)
				return real + " - " + (-1*imaginary) + "i";
			else
				return real + " + " + imaginary + "i";
		}
		return real + " + " + imaginary + "i";
//		return "ComplexNumber [real=" + real + ", imaginary=" + imaginary + "]";
	}
	
	// See Section 3.3.
    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        ComplexNumber that = (ComplexNumber) x;
        return (this.real == that.real) && (this.imaginary == that.imaginary);
    }

    // See Section 3.3.
    public int hashCode() {
        return Objects.hash(real, imaginary);
    }

	public double abs() {
		return Math.hypot(real, imaginary);
	}
	
	
	/**
	 * https://math.stackexchange.com/questions/44406/how-do-i-get-the-square-root-of-a-complex-number
	 * 
	 * z=c+d*i				c=real,	d=imaginary
	 * 
	 * sqroot(z)=a+b*i
	 * 
	 * a=root((c+root(c^2+d^2))/2) 
	 * b=d/|d|*root((-c+root(c^2+d^2))/2)
	 * 
	 * d/|d|	==> b has same sign as d
	 * 
	 * @return
	 */
	public ComplexNumber sqroot() {
//		System.out.println("Real==="+real+"---Img:=="+imaginary);
		double a = Math.sqrt(Math.abs(real) + ( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
		double b = Math.sqrt( Math.abs(real /** (-1.0)*/) + Math.sqrt( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
//System.out.println("a==="+a);
//System.out.println("b==="+b);
		if (real == 0.0 && imaginary != 0.0) {
//			System.out.println("returning___new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));"+new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary))));
			return new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));
		}
		if (imaginary > 0.0) {
//			System.out.println("returning___new ComplexNumber(a, b);"+new ComplexNumber(a, b));
			return new ComplexNumber(a, b);
		} else if (imaginary < 0.0) {
//			System.out.println("returning___new ComplexNumber(a, b * (-1.0));"+new ComplexNumber(a,( Math.abs(b) * (-1.0))));
		
			return new ComplexNumber(a,( Math.abs(b) * (-1.0)));
		}
//		System.out.println("returning___new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);"+new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0));
		return new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);
	}
	
	public ComplexNumber curoot(){
		return sqroot();	//for-now
	}

	
	/**https://en.wikipedia.org/wiki/Complex_logarithm
	//
	//	z=x+y*i
	//	log(z)=ln(root(x^2+y^2))+atan2(y,x)*i
	 * 
	 * @return
	 */
	public ComplexNumber ln() {
		double a = Math.log(Math.sqrt(Math.pow(real, 2.0) + Math.pow(imaginary, 2.0)));
		double b = Math.atan2(imaginary, real);
		return new ComplexNumber(a, b);
	}
	
	
	/*public final ComplexNumber one = new ComplexNumber(1.0, 0.0);*/

    // return a new Complex object whose value is the complex exponential of this
	// return e ^ z
    public ComplexNumber exp() {
        return new ComplexNumber(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
    }

    // return a new Complex object whose value is the complex sine of this
    public ComplexNumber sin() {
        return new ComplexNumber(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
    }

    // return a new Complex object whose value is the complex cosine of this
    public ComplexNumber cos() {
        return new ComplexNumber(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
    }

    // return a new Complex object whose value is the complex tangent of this
    public ComplexNumber tan() {
        return sin().divides(cos());
    }
    

    // return a / b
    public ComplexNumber divides(ComplexNumber b) {
        ComplexNumber a = this;
        return a.times(b.reciprocal());
    }
 // return a new Complex object whose value is the reciprocal of this
    public ComplexNumber reciprocal() {
		double scale = real * real + imaginary * imaginary;
		return new ComplexNumber(real / scale, -imaginary / scale);
    }
    
    public ComplexNumber power(ComplexNumber power) {
    	/*
    	 * 
    	 * */
    	return this;			//4now
    }
    
    
	//	https://math.stackexchange.com/questions/3668/what-is-the-value-of-1i
	/*If a & b are real: then	(a+ib) = r(cosθ+isinθ)
			where: 
				r^2=a^2+b^2
			and	tanθ = (b/a)	==>	θ=Math.atan2(b,a)

			Then:
				(a+ib)^N	=	(r^N)*(cos(N*θ)+i*sin(N*θ))
				
	****************************************************/		
	public ComplexNumber power_New123(int power) {
		double a = this.real;
		double b = this.imaginary;

		double r = Math.sqrt((a * a) + (b * b));
		double θ = Math.toDegrees(Math.atan(b / a));
		//TODO	check answer with original method this.power_0123
		return new ComplexNumber(r * power * Math.cos(power * θ), r * power * Math.sin(power * θ));
	}

	public ComplexNumber power(int power) {
		//TODO	l8r, try e^(iπ///θ) method
		// instead of multiplication route
		//	1=e^(2πi)	or		1=e^(0+2Kπi)	or
		//	naively	1^i	=	[e^(2πi)]^i	=	e^(2πi*i)	=	e^(-2π)
		//	https://math.stackexchange.com/questions/3668/what-is-the-value-of-1i
		/*If a & b are real: then	(a+ib) = r(cosθ+isinθ)
				where: 
					r^2=a^2+b^2
				and	tanθ = (b/a)	==>	θ=Math.atan2(b,a)

				Then:
					(a+ib)^N	=	(r^N)*(cos(N*θ)+i*sin(N*θ))
					
		****************************************************/
		
		
		if (power > 0) {
			final ComplexNumber a = this;
			ComplexNumber powered = a;
			for (int i = 1; i < power; i++) {
				powered = powered.times(a);
			}
			return powered;
		} else if (power < 0) {
			final ComplexNumber a = this;
			ComplexNumber powered = a.reciprocal();
			int powAbs = Math.abs(power);
			for (int i = 1; i < powAbs; i++) {
				powered = powered.times(powered);
			}
			return powered;

		}	
		//power==0
		return new ComplexNumber(1.0,0.0);	//	^0==1
	}

	public ComplexNumber sine() {
		double sineR = Math.sin(this.real) * Math.cosh(this.imaginary);
		double sineI = Math.cos(this.real) * Math.sinh(this.imaginary);
		return new ComplexNumber(sineR,sineI);
	}
	
	//arcsin
	public ComplexNumber inverseSine(){
		return new ComplexNumber(1.0, 0.0).divides(this.sine());
	}
	
	public ComplexNumber cosine() {
		// cos(x)*cosh(y) - isin(x)*sinh(y)
		double cosR = Math.cos(this.real) * Math.cosh(this.imaginary);
		double cosI = -Math.sin(this.real) * Math.sinh(this.imaginary);
		return new ComplexNumber(cosR, cosI);
	}
	
	//arccos
	public ComplexNumber inverseCosine(){
		return new ComplexNumber(1.0, 0.0).divides(this.cosine());
	}
	
	public ComplexNumber tangent(){
		return this.sine().divides(this.cosine());
	}
	
	//arctan
	public ComplexNumber inverseTangent(){
		return new ComplexNumber(1.0, 0.0).divides(this.tangent());
	}

	public boolean isNaN() {
		return Double.isNaN(this.real) || Double.isNaN(this.imaginary) || Double.isInfinite(this.real)
				|| Double.isInfinite(this.imaginary);
	}
	
	
	public static void main(String args[]) {
		ComplexNumber cn = new ComplexNumber(5.6, 3.5);
		
		System.out.println("Number is: "+cn+", power is 3");

		System.out.println("newPowerMethod == " + cn.power(3));

		System.out.println("oldPowerMethod == " + cn.power_New123(3));
	}
}

