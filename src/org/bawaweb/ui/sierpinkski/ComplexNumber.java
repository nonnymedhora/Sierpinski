/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.io.Serializable;
import java.util.Objects;



/**
 * @author Navroz
 *
 */
public class ComplexNumber implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 134567L;
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
					//System.out.println("C2 us "+c2);
					final int c2MinusIndex = c2.indexOf(minus);
					if (c2MinusIndex > 0) {
						this.real = Double.parseDouble(c2.substring(0, c2MinusIndex)) * -1;
						this.imaginary = Double.parseDouble(c2.substring(c2MinusIndex).replace(I, EMPTY));
					} else {
						this.real = Double.parseDouble(c.replace(I, EMPTY));
						this.imaginary = 0;
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
    

	public double absOLD() {
		//old
				return Math.hypot(real, imaginary);
	}

	public double abs() {
		//see abs (double x, double y) {
		// marksmath	Complex
		// 
		/***********************************
		//old
		return Math.hypot(real, imaginary);
		********************************/
		//  abs(z)  =  sqrt(norm(z))

        // Adapted from
        // "Numerical Recipes in Fortran 77: The Art of Scientific Computing"
        // (ISBN 0-521-43064-X)

		double absX = Math.abs(this.real);
		double absY = Math.abs(this.imaginary);

		if (absX == 0.0 && absY == 0.0) { // !!! Numerical Recipes, mmm?
			return 0.0;
		} else if (absX >= absY) {
			double d = this.imaginary / this.real;
			return absX * Math.sqrt(1.0 + d * d);
		} else {
			double d = this.real / this.imaginary;
			return absY * Math.sqrt(1.0 + d * d);
		}//endif
    }//end abs()
	
	public ComplexNumber conjugate() {
		return new ComplexNumber(this.real, -1.0 * this.imaginary);
	}
	
	
	public ComplexNumber sqroot (ComplexNumber s) {
		//from	Complex
        // with thanks to Jim Shapiro <jnshapi@argo.ecte.uswc.uswest.com>
        // adapted from "Numerical Recipies in C" (ISBN 0-521-43108-5)
        // by William H. Press et al

        double mag =  this.abs();
        double re = s.real;
        double im = s.imaginary;

        if (mag > 0.0) {
            if (re > 0.0) {
                double temp =  Math.sqrt(0.5 * (mag + re));

                re =  temp;
                im =  0.5 * im / temp;
            } else {
                double temp =  Math.sqrt(0.5 * (mag - re));

                if (im < 0.0) {
                    temp =  -temp;
                }//endif

                re =  0.5 * im / temp;
                im =  temp;
            }//endif
        } else {
            re =  0.0;
            im =  0.0;
        }//endif
        
        return new ComplexNumber(re,im);
	}
	
	public ComplexNumber sqroot () {
		return sqroot(this);
/*/////////////////////////////////////////////////
		//from	Complex
        // with thanks to Jim Shapiro <jnshapi@argo.ecte.uswc.uswest.com>
        // adapted from "Numerical Recipies in C" (ISBN 0-521-43108-5)
        // by William H. Press et al

        double mag =  this.abs();
        double re = this.real;
        double im = this.imaginary;

        if (mag > 0.0) {
            if (re > 0.0) {
                double temp =  Math.sqrt(0.5 * (mag + re));

                re =  temp;
                im =  0.5 * im / temp;
            } else {
                double temp =  Math.sqrt(0.5 * (mag - re));

                if (im < 0.0) {
                    temp =  -temp;
                }//endif

                re =  0.5 * im / temp;
                im =  temp;
            }//endif
        } else {
            re =  0.0;
            im =  0.0;
        }//endif
        
        return new ComplexNumber(re,im);
///////////////////////////////////////*/        
    }//end sqrt(Complex)


	
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
	public ComplexNumber sqrootOLD() {
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
	
	
	/**
	    * Returns the <i>principal</i> natural logarithm of a <tt>Complex</tt>
	    * number.
	    *
	    * <p>
	    * <pre>
	    *     log(z)  =  log(abs(z)) + <i><b>i</b></i> * arg(z)
	    * </pre>
	    * <p>
	    * There are infinitely many solutions, besides the principal solution.
	    * If <b>L</b> is the principal solution of <i>log(z)</i>, the others are of
	    * the form:
	    * <p>
	    * <pre>
	    *     <b>L</b> + (2*k*<b>PI</b>)*<i><b>i</b></i>
	    * </pre>
	    * <p>
	    * where k is any integer.
	    * <p>
	    * @return                  Principal <tt>Complex</tt> natural logarithm
	    * <p>
	    * @see                     Complex#exp()
	    **/

	    public ComplexNumber log () {
	        return  new ComplexNumber( Math.log(this.abs()), this.arg() );                      // principal value
	    }//end log()
	
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
    public ComplexNumber dividesOLD(ComplexNumber b) {
        ComplexNumber a = this;
        return a.times(b.reciprocal());
    }
    
    
    //////////////////////////////////
    public ComplexNumber divides (ComplexNumber z) {
        ComplexNumber result =  this;
        
        return  divides(result, z.real, z.imaginary);
    }//end div(Complex)



    public ComplexNumber divides (ComplexNumber z, double x, double y) {
        // Adapted from
        // "Numerical Recipes in Fortran 77: The Art of Scientific Computing"
        // (ISBN 0-521-43064-X)

        double zRe, zIm;
        double scalar;

        if (Math.abs(x) >= Math.abs(y)) {
            scalar =  1.0 / ( x + y*(y/x) );

            zRe =  scalar * (z.real + z.imaginary*(y/x));
            zIm =  scalar * (z.imaginary - z.real*(y/x));

        } else {
            scalar =  1.0 / ( x*(x/y) + y );

            zRe =  scalar * (z.real*(x/y) + z.imaginary);
            zIm =  scalar * (z.imaginary*(x/y) - z.real);
        }//endif

        
        return new ComplexNumber(zRe,zIm);
    }//end div(Complex,double,double)

    //////////////////////////////////
    
    /*//	from	Complex	markmaths
    //		div (Complex z, double x, double y) {
    public ComplexNumber divides(ComplexNumber b) {
    	// Adapted from
        // "Numerical Recipes in Fortran 77: The Art of Scientific Computing"
        // (ISBN 0-521-43064-X)

        double zRe, zIm;
        double scalar;
        
        double x = b.real;
        double y = b.imaginary;

        if (Math.abs(x) >= Math.abs(y)) {
            scalar =  1.0 / ( x + y*(y/x) );

            zRe =  scalar * (this.real + this.imaginary*(y/x));
            zIm =  scalar * (this.imaginary - this.real*(y/x));

        } else {
            scalar =  1.0 / ( x*(x/y) + y );

            zRe =  scalar * (this.real*(x/y) + this.imaginary);
            zIm =  scalar * (this.imaginary*(x/y) - this.real);
        }//endif

        return new ComplexNumber(zRe,zIm);
    }//end div(Complex,double,double)
    */
    
 // return a new Complex object whose value is the reciprocal of this
    public ComplexNumber reciprocalOLD() {
    	/*//////////////////OLD//////////////////////////////*/
		double scale = real * real + imaginary * imaginary;
		return new ComplexNumber(real / scale, -imaginary / scale);
		/*////////////////ends-old////////////////////////////////*/
    }
    
    public ComplexNumber reciprocal() {
    	double zRe, zIm;
        double scalar;

		if (Math.abs(this.real) >= Math.abs(this.imaginary)) {
			scalar = 1.0 / (this.real + this.imaginary * (this.imaginary / this.real));

			zRe = scalar;
			zIm = scalar * (-1.0 * this.imaginary / this.real);
		} else {
			scalar = 1.0 / (this.real * (this.real / this.imaginary) + this.imaginary);

			zRe = scalar * (this.real / this.imaginary);
			zIm = -scalar;
		} // endif

		return new ComplexNumber(zRe, zIm);
	}// end inv(Complex)
    
    public ComplexNumber reciprocal(ComplexNumber c) {
		return c;
    	//	returns 1/c
    	
    }
    
    
	public ComplexNumber power(double base, ComplexNumber exponent) {
		// return real(base).log().mul(exponent).exp();

		double re = Math.log(Math.abs(base));
		double im = Math.atan2(0.0, base);

		double re2 = (re * exponent.real) - (im * exponent.imaginary);
		double im2 = (re * exponent.imaginary) + (im * exponent.real);

		double scalar = Math.exp(re2);

		return new ComplexNumber(scalar * Math.cos(im2), scalar * Math.sin(im2));
	}//end pow(double,Complex)
    
    public ComplexNumber power(ComplexNumber power) {
    	/*	see	Complex [marksmath.org]
    	 *  Complex
    	 *		pow (Complex base, Complex exponent) {
    	 *		here	--	base=this
    	 * */
    	double re =  Math.log(this.abs());
        double im =  this.arg();

        double re2 =  (re*power.real) - (im*power.imaginary);
        double im2 =  (re*power.imaginary) + (im*power.real);

        double scalar =  Math.exp(re2);

        return  new ComplexNumber ( scalar * Math.cos(im2), scalar * Math.sin(im2) );
    }
    
    
	//	https://math.stackexchange.com/questions/3668/what-is-the-value-of-1i
	/*If a & b are real: then	(a+ib) = r(cosθ+isinθ)
			where: 
				r^2=a^2+b^2
			and	tanθ = (b/a)	==>	θ=Math.atan2(b,a)

			Then:
				(a+ib)^N	=	(r^N)*(cos(N*θ)+i*sin(N*θ))
				
	****************************************************/
    //following is incorrect --- remove power_New123
	public ComplexNumber power_New123(int power) {
		double a = this.real;
		double b = this.imaginary;

		double r = Math.sqrt((a * a) + (b * b));
		double θ = Math.toDegrees(Math.atan(b / a));
		//TODO	check answer with original method this.power_0123
		return new ComplexNumber(r * power * Math.cos(power * θ), r * power * Math.sin(power * θ));
	}

	public ComplexNumber powerOLD(int power) {
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
	
	public ComplexNumber power(int power) {
		return this.power(1.0*power);
	}
	public ComplexNumber power(double exponent) {
		//	returns this ComplexNumber raised to power of exponent
		//	see marksmath.org	Complex.java
		//			pow (double base, Complex exponent) {
		double re =  exponent * Math.log(this.abs());
        double im =  exponent * this.arg();

        double scalar =  Math.exp(re);

        return  new ComplexNumber( scalar * Math.cos(im), scalar * Math.sin(im) );		
	}
	


    /**
    * Returns the <i>principal</i> angle of a <tt>Complex</tt> number, in
    * radians, measured counter-clockwise from the real axis.  (Think of the
    * reals as the x-axis, and the imaginaries as the y-axis.)
    *
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>arg(z)</i>, the others are of
    * the form:
    * <p>
    * <pre>
    *     <b>A</b> + 2*k*<b>PI</b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * <tt>arg()</tt> always returns a <tt>double</tt> between
    * -<tt><b>PI</b></tt> and +<tt><b>PI</b></tt>.
    * <p>
    * <i><b>Note:</b><ul> 2*<tt><b>PI</b></tt> radians is the same as 360 degrees.
    * </ul></i>
    * <p>
    * <i><b>Domain Restrictions:</b><ul> There are no restrictions: the
    * class defines arg(0) to be 0
    * </ul></i>
    * <p>
    * @return                  Principal angle (in radians)
    * <p>
    * @see                     Complex#abs()
    * @see                     Complex#polar(double, double)
    **/

	public double arg() {
		return Math.atan2(this.imaginary, this.real);
	}// end arg()


/**
    * Returns a <tt>Complex</tt> from a size and direction.
    *
    * <p>
    * @param  r                Size
    * @param  theta            Direction (in <i>radians</i>)
    * <p>
    * @return                  <tt>Complex</tt> from Polar coordinates
    * <p>
    * @see                     Complex#abs()
    * @see                     Complex#arg()
    * @see                     Complex#cart(double, double)
    **/

	public ComplexNumber polar (double r, double theta) {
        if (r < 0.0) {
            theta +=  Math.PI;
            r      =  -r;
        }//endif

        theta =  theta % (2.0 * Math.PI);

        return  new ComplexNumber(r * Math.cos(theta), r * Math.sin(theta));
    }//end polar(double,double)

	public ComplexNumber sineOLD() {
		double sineR = Math.sin(this.real) * Math.cosh(this.imaginary);
		double sineI = Math.cos(this.real) * Math.sinh(this.imaginary);
		return new ComplexNumber(sineR,sineI);
	}
	

    /**
    * Returns the sine of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     sin(z)  =  ( exp(<i><b>i</b></i>*z) - exp(-<i><b>i</b></i>*z) ) / (2*<i><b>i</b></i>)
    * </pre>
    * <p>
    * @return                  The <tt>Complex</tt> sine
    * <p>
    * @see                     Complex#asin()
    * @see                     Complex#sinh()
    * @see                     Complex#cosec()
    * @see                     Complex#cos()
    * @see                     Complex#tan()
    **/

    public ComplexNumber sine () {
        ComplexNumber result;
            //  sin(z)  =  ( exp(i*z) - exp(-i*z) ) / (2*i)

            double scalar;
            double iz_re, iz_im;
            double _re1, _im1;
            double _re2, _im2;

            // iz:      i.mul(z) ...
            iz_re =  -1.0*this.imaginary;
            iz_im =   this.real;

            // _1:      iz.exp() ...
            scalar =  Math.exp(iz_re);
            _re1 =  scalar * Math.cos(iz_im);
            _im1 =  scalar * Math.sin(iz_im);

            // _2:      iz.neg().exp() ...
            scalar =  Math.exp(-iz_re);
            _re2 =  scalar * Math.cos(-iz_im);
            _im2 =  scalar * Math.sin(-iz_im);

            // _1:      _1.sub(_2) ...
            _re1 = _re1 - _re2;                                                // !!!
            _im1 = _im1 - _im2;                                                // !!!

            // result:  _1.div(2*i) ...
            result =  new ComplexNumber( 0.5*_im1, -0.5*_re1 );
            // ... result =  cart(_re1, _im1);
            //     div(result, 0.0, 2.0);
        return  result;
    }//end sin()


	
	//arcsin
	public ComplexNumber inverseSineOLD(){
		return new ComplexNumber(1.0, 0.0).divides(this.sineOLD());
	}
	
	public ComplexNumber inverseSine() {
		return this.cosec();
	}
	


    /**
    * Returns the <i>principal</i> arc sine of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     asin(z)  =  -<i><b>i</b></i> * log(<i><b>i</b></i>*z + sqrt(1 - z*z))
    * </pre>
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>asin(z)</i>, the others are
    * of the form:
    * <p>
    * <pre>
    *     k*<b>PI</b> + (-1)<sup><font size=-1>k</font></sup>  * <b>A</b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * @return                  Principal <tt>Complex</tt> arc sine
    * <p>
    * @see                     Complex#sin()
    * @see                     Complex#sinh()
    **/
    public ComplexNumber asin () {
        ComplexNumber result;
            //  asin(z)  =  -i * log(i*z + sqrt(1 - z*z))

            double _re1, _im1;

            // _1:      one.sub(z.mul(z)) ...
            _re1 =  1.0 - ( (real*real) - (imaginary*imaginary) );
            _im1 =  0.0 - ( (real*imaginary) + (imaginary*real) );

            // result:  _1.sqrt() ...
            result =  new ComplexNumber(_re1, _im1);
            result=this.sqroot(result);

            // _1:      z.mul(i) ...
            _re1 =  - imaginary;
            _im1 =  + real;

            // result:  _1.add(result) ...
            //result.real =  _re1 + result.real;//rem
            //result.imaginary =  _im1 + result.imaginary;//rem
            
            result = new ComplexNumber(_re1 + result.real, _im1 + result.imaginary);

            // _1:      result.log() ...
            _re1 =  Math.log(result.abs());
            _im1 =  result.arg();

            // result:  i.neg().mul(_1) ...
            //result.real =    _im1;//rem
            //result.imaginary =  - _re1;//rem
            result=new ComplexNumber(_im1, -1.0 * _re1);
        return  result;
    }//end asin()
	
	public ComplexNumber cosec() {
		ComplexNumber result;
		// cosec(z) = 1 / sin(z)

		double scalar;
		double iz_re, iz_im;
		double _re1, _im1;
		double _re2, _im2;

		// iz: i.mul(z) ...
		iz_re = -1.0*this.imaginary;
		iz_im = this.real;

		// _1: iz.exp() ...
		scalar = Math.exp(iz_re);
		_re1 = scalar * Math.cos(iz_im);
		_im1 = scalar * Math.sin(iz_im);

		// _2: iz.neg().exp() ...
		scalar = Math.exp(-iz_re);
		_re2 = scalar * Math.cos(-iz_im);
		_im2 = scalar * Math.sin(-iz_im);

		// _1: _1.sub(_2) ...
		_re1 = _re1 - _re2; // !!!
		_im1 = _im1 - _im2; // !!!

		// _result: _1.div(2*i) ...
		result = new ComplexNumber(0.5 * _im1, -0.5 * _re1);
		// result = cart(_re1, _im1);
		// div(result, 0.0, 2.0);

		// result: one.div(_result) ...
		return this.reciprocal(result);
	}// end cosec()



    /**
    * Returns the hyperbolic sine of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     sinh(z)  =  ( exp(z) - exp(-z) ) / 2
    * </pre>
    * <p>
    * @return                  The <tt>Complex</tt> hyperbolic sine
    * <p>
    * @see                     Complex#sin()
    * @see                     Complex#asinh()
    **/

    public ComplexNumber sinh () {
        ComplexNumber result;
            //  sinh(z)  =  ( exp(z) - exp(-z) ) / 2

            double scalar;
            double _re1, _im1;
            double _re2, _im2;

            // _1:      z.exp() ...
            scalar =  Math.exp(this.real);
            _re1 =  scalar * Math.cos(this.imaginary);
            _im1 =  scalar * Math.sin(this.imaginary);

            // _2:      z.neg().exp() ...
            scalar =  Math.exp(-1.0*this.real);
            _re2 =  scalar * Math.cos(-1.0*this.imaginary);
            _im2 =  scalar * Math.sin(-1.0*this.imaginary);

            // _1:      _1.sub(_2) ...
            _re1 = _re1 - _re2;                                                // !!!
            _im1 = _im1 - _im2;                                                // !!!

            // result:  _1.scale(0.5) ...
            result =  new ComplexNumber( 0.5 * _re1, 0.5 * _im1 );
        return  result;
    }//end sinh()




    /**
    * Returns the <i>principal</i> inverse hyperbolic sine of a
    * <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     asinh(z)  =  log(z + sqrt(z*z + 1))
    * </pre>
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>asinh(z)</i>, the others are
    * of the form:
    * <p>
    * <pre>
    *     k*<b>PI</b>*<b><i>i</i></b> + (-1)<sup><font size=-1>k</font></sup>  * <b>A</b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * @return                  Principal <tt>Complex</tt> inverse hyperbolic sine
    * <p>
    * @see                     Complex#sinh()
    **/

	public ComplexNumber asinh() {
		ComplexNumber result;
		// asinh(z) = log(z + sqrt(z*z + 1))

		double _re1, _im1;

		// _1: z.mul(z).add(one) ...
		_re1 = ((real * real) - (imaginary * imaginary)) + 1.0;
		_im1 = ((real * imaginary) + (imaginary * real)) + 0.0;

		// result: _1.sqrt() ...
		result = new ComplexNumber(_re1, _im1);
		result = this.sqroot(result);

		// result: z.add(result) ...
		// result.re = re + result.re; // !//rem
		// result.im = im + result.im; // !//rem
		result = new ComplexNumber(real + result.real, imaginary + result.imaginary);

		// _1: result.log() ...
		_re1 = Math.log(result.abs());
		_im1 = result.arg();

		// result: _1 ...
		// result.re = _re1;//rem
		// result.im = _im1;//rem
		result = new ComplexNumber(_re1, _im1);

		/*
		 * Many thanks to the mathematicians of aus.mathematics and sci.math,
		 * and to Zdislav V. Kovarik of the Department of Mathematics and
		 * Statistics, McMaster University and John McGowan <jmcgowan@inch.com>
		 * in particular, for their advice on the current naming conventions for
		 * "area/argumentus sinus hyperbolicus".
		 */

		return result;
	}// end asinh()
	
	public ComplexNumber cosineOLD() {
		// cos(x)*cosh(y) - isin(x)*sinh(y)
		double cosR = Math.cos(this.real) * Math.cosh(this.imaginary);
		double cosI = -Math.sin(this.real) * Math.sinh(this.imaginary);
		return new ComplexNumber(cosR, cosI);
	}
	

    /**
    * Returns the cosine of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     cos(z)  =  ( exp(<i><b>i</b></i>*z) + exp(-<i><b>i</b></i>*z) ) / 2
    * </pre>
    * <p>
    * @return                  The <tt>Complex</tt> cosine
    * <p>
    * @see                     Complex#acos()
    * @see                     Complex#cosh()
    * @see                     Complex#sec()
    * @see                     Complex#sin()
    * @see                     Complex#tan()
    **/

	public ComplexNumber cosine() {
		ComplexNumber result;
		// cos(z) = ( exp(i*z) + exp(-i*z) ) / 2

		double scalar;
		double iz_re, iz_im;
		double _re1, _im1;
		double _re2, _im2;

		// iz: i.mul(z) ...
		iz_re = -1.0*this.imaginary;
		iz_im = this.real;

		// _1: iz.exp() ...
		scalar = Math.exp(iz_re);
		_re1 = scalar * Math.cos(iz_im);
		_im1 = scalar * Math.sin(iz_im);

		// _2: iz.neg().exp() ...
		scalar = Math.exp(-iz_re);
		_re2 = scalar * Math.cos(-iz_im);
		_im2 = scalar * Math.sin(-iz_im);

		// _1: _1.add(_2) ...
		_re1 = _re1 + _re2; // !!!
		_im1 = _im1 + _im2; // !!!

		// result: _1.scale(0.5) ...
		result = new ComplexNumber(0.5 * _re1, 0.5 * _im1);
		return result;
	}// end cos()
	
	//arccos
	public ComplexNumber inverseCosineOLD(){
		return new ComplexNumber(1.0, 0.0).divides(this.cosine());
	}
	
	public ComplexNumber inverseCosine(){
		return this.sec();
	}
	
	/**
	    * Returns the secant of a <tt>Complex</tt> number.
	    *
	    * <p>
	    * <pre>
	    *     sec(z)  =  1 / cos(z)
	    * </pre>
	    * <p>
	    * <i><b>Domain Restrictions:</b><ul> sec(z) is undefined whenever z = (k + 1/2) * <tt><b>PI</b></tt><br>
	    * where k is any integer
	    * </ul></i>
	    * <p>
	    * @return                  The <tt>Complex</tt> secant
	    * <p>
	    * @see                     Complex#cos()
	    * @see                     Complex#cosec()
	    * @see                     Complex#cot()
	    **/

	public ComplexNumber sec() {
		ComplexNumber result;
		// sec(z) = 1 / cos(z)

		double scalar;
		double iz_re, iz_im;
		double _re1, _im1;
		double _re2, _im2;

		// iz: i.mul(z) ...
		iz_re = -1.0 * this.imaginary;
		iz_im = this.real;

		// _1: iz.exp() ...
		scalar = Math.exp(iz_re);
		_re1 = scalar * Math.cos(iz_im);
		_im1 = scalar * Math.sin(iz_im);

		// _2: iz.neg().exp() ...
		scalar = Math.exp(-iz_re);
		_re2 = scalar * Math.cos(-iz_im);
		_im2 = scalar * Math.sin(-iz_im);

		// _1: _1.add(_2) ...
		_re1 = _re1 + _re2;
		_im1 = _im1 + _im2;

		// result: _1.scale(0.5) ...
		result = new ComplexNumber(0.5 * _re1, 0.5 * _im1);

		// result: one.div(result) ...
		result = this.reciprocal(result);
		return result;
	}//end sec()
	
	/**
	 * Returns the hyperbolic cosine of a <tt>Complex</tt> number.
	 *
	 * <p>
	 * 
	 * <pre>
	 * cosh(z) = (exp(z) + exp(-z)) / 2
	 * </pre>
	 * <p>
	 * 
	 * @return The <tt>Complex</tt> hyperbolic cosine
	 *         <p>
	 * @see Complex#cos()
	 * @see Complex#acosh()
	 **/

	public ComplexNumber cosh() {
		ComplexNumber result;
		// cosh(z) = ( exp(z) + exp(-z) ) / 2

		double scalar;
		double _re1, _im1;
		double _re2, _im2;

		// _1: z.exp() ...
		scalar = Math.exp(this.real);
		_re1 = scalar * Math.cos(this.imaginary);
		_im1 = scalar * Math.sin(this.imaginary);

		// _2: z.neg().exp() ...
		scalar = Math.exp(-1.0 * this.real);
		_re2 = scalar * Math.cos(-1.0 * this.imaginary);
		_im2 = scalar * Math.sin(-1.0 * this.imaginary);

		// _1: _1.add(_2) ...
		_re1 = _re1 + _re2; // !!!
		_im1 = _im1 + _im2; // !!!

		// result: _1.scale(0.5) ...
		result = new ComplexNumber(0.5 * _re1, 0.5 * _im1);
		return result;
	}// end cosh()
	

    /**
    * Returns the <i>principal</i> arc cosine of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     acos(z)  =  -<i><b>i</b></i> * log( z + <i><b>i</b></i> * sqrt(1 - z*z) )
    * </pre>
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>acos(z)</i>, the others are
    * of the form:
    * <p>
    * <pre>
    *     2*k*<b>PI</b> +/- <b>A</b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * @return                  Principal <tt>Complex</tt> arc cosine
    * <p>
    * @see                     Complex#cos()
    * @see                     Complex#cosh()
    **/

    public ComplexNumber acos () {
        ComplexNumber result;
            //  acos(z)  =  -i * log( z + i * sqrt(1 - z*z) )

            double _re1, _im1;

            // _1:      one.sub(z.mul(z)) ...
            _re1 =  1.0 - ( (real*real) - (imaginary*imaginary) );
            _im1 =  0.0 - ( (real*imaginary) + (imaginary*real) );

            // result:  _1.sqrt() ...
            result =  new ComplexNumber(_re1, _im1);
            result=sqroot(result);

            // _1:      i.mul(result) ...
            _re1 =  - result.imaginary;
            _im1 =  + result.real;

            // result:  z.add(_1) ...
            //result.re =  real + _re1;//rem
            //result.im =  imaginary + _im1;//rem
            
            result = new ComplexNumber( real+_re1,imaginary+_im1);

            // _1:      result.log()
            _re1 =  Math.log(result.abs());
            _im1 =  result.arg();

            // result:  i.neg().mul(_1) ...
            //result.re =    _im1;//rem
            //result.im =  - _re1;//rem
            
            result = new ComplexNumber( _im1,- _re1);
        return  result;
    }//end acos()


    /**
	 * Returns the <i>principal</i> inverse hyperbolic cosine of a
	 * <tt>Complex</tt> number.
	 *
	 * <p>
	 * 
	 * <pre>
	 * acosh(z) = log(z + sqrt(z * z - 1))
	 * </pre>
	 * <p>
	 * There are infinitely many solutions, besides the principal solution. If
	 * <b>A</b> is the principal solution of <i>acosh(z)</i>, the others are of
	 * the form:
	 * <p>
	 * 
	 * <pre>
	 *     2*k*<b>PI</b>*<b><i>i</i></b> +/- <b>A</b>
	 * </pre>
	 * <p>
	 * where k is any integer.
	 * <p>
	 * 
	 * @return Principal <tt>Complex</tt> inverse hyperbolic cosine
	 *         <p>
	 * @see Complex#cosh()
	 **/

	public ComplexNumber acosh() {
		ComplexNumber result;
		// acosh(z) = log(z + sqrt(z*z - 1))

		double _re1, _im1;

		// _1: z.mul(z).sub(one) ...
		_re1 = ((real * real) - (imaginary * imaginary)) - 1.0;
		_im1 = ((real * imaginary) + (imaginary * real)) - 0.0;

		// result: _1.sqrt() ...
		result = new ComplexNumber(_re1, _im1);
		result=this.sqroot(result);

		// result: z.add(result) ...
		//result.re = re + result.re; // !rem
		//result.im = im + result.im; // !rem
		result = new ComplexNumber(real+result.real,imaginary+result.imaginary);

		// _1: result.log() ...
		_re1 = Math.log(result.abs());
		_im1 = result.arg();

		// result: _1 ...
		//result.re = _re1;//rem
		//result.im = _im1;//rem
		result= new ComplexNumber(_re1,_im1);
		return result;
	}//end acosh()



	public ComplexNumber tangentOLD(){
		return this.sine/*OLD*/().divides(this.cosine/*OLD*/());
	}
	


/**
    * Returns the tangent of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     tan(z)  =  sin(z) / cos(z)
    * </pre>
    * <p>
    * <i><b>Domain Restrictions:</b><ul> tan(z) is undefined whenever z = (k + 1/2) * <tt><b>PI</b></tt><br>
    * where k is any integer
    * </ul></i>
    * <p>
    * @return                  The <tt>Complex</tt> tangent
    * <p>
    * @see                     Complex#atan()
    * @see                     Complex#tanh()
    * @see                     Complex#cot()
    * @see                     Complex#sin()
    * @see                     Complex#cos()
    **/

	public ComplexNumber tangent() {
		// return this.sine().divides(this.cosine());

		ComplexNumber result;
		// tan(z) = sin(z) / cos(z)

		double scalar;
		double iz_re, iz_im;
		double _re1, _im1;
		double _re2, _im2;
		double _re3, _im3;

		double cs_re, cs_im;

		// sin() ...

		// iz: i.mul(z) ...
		iz_re = -1.0 * this.imaginary;
		iz_im = this.real;

		// _1: iz.exp() ...
		scalar = Math.exp(iz_re);
		_re1 = scalar * Math.cos(iz_im);
		_im1 = scalar * Math.sin(iz_im);

		// _2: iz.neg().exp() ...
		scalar = Math.exp(-iz_re);
		_re2 = scalar * Math.cos(-iz_im);
		_im2 = scalar * Math.sin(-iz_im);

		// _3: _1.sub(_2) ...
		_re3 = _re1 - _re2;
		_im3 = _im1 - _im2;

		// result: _3.div(2*i) ...
		result = new ComplexNumber(0.5 * _im3, -0.5 * _re3);
		// result = cart(_re3, _im3);
		// div(result, 0.0, 2.0);

		// cos() ...

		// _3: _1.add(_2) ...
		_re3 = _re1 + _re2;
		_im3 = _im1 + _im2;

		// cs: _3.scale(0.5) ...
		cs_re = 0.5 * _re3;
		cs_im = 0.5 * _im3;

		// result: result.div(cs) ...
		divides(result, cs_re, cs_im);
		return result;
	}// end tan()

	//arctan
	public ComplexNumber inverseTangentOLD(){
		return new ComplexNumber(1.0, 0.0).divides(this.tangent());
	}
	
	public ComplexNumber inverseTangent(){
		return this.cot();
		
	}
	
	/**
	    * Returns the cotangent of a <tt>Complex</tt> number.
	    *
	    * <p>
	    * <pre>
	    *     cot(z)  =  1 / tan(z)
	    * </pre>
	    * <p>
	    * <i><b>Domain Restrictions:</b><ul> cot(z) is undefined whenever z = k * <tt><b>PI</b></tt><br>
	    * where k is any integer
	    * </ul></i>
	    * <p>
	    * @return                  The <tt>Complex</tt> cotangent
	    * <p>
	    * @see                     Complex#tan()
	    * @see                     Complex#cosec()
	    * @see                     Complex#sec()
	    **/

	public ComplexNumber cot() {
		ComplexNumber result;
		// cot(z) = 1 / tan(z) = cos(z) / sin(z)

		double scalar;
		double iz_re, iz_im;
		double _re1, _im1;
		double _re2, _im2;
		double _re3, _im3;

		double sn_re, sn_im;

		// cos() ...

		// iz: i.mul(z) ...
		iz_re = -1.0 * this.imaginary;
		iz_im = this.real;

		// _1: iz.exp() ...
		scalar = Math.exp(iz_re);
		_re1 = scalar * Math.cos(iz_im);
		_im1 = scalar * Math.sin(iz_im);

		// _2: iz.neg().exp() ...
		scalar = Math.exp(-iz_re);
		_re2 = scalar * Math.cos(-iz_im);
		_im2 = scalar * Math.sin(-iz_im);

		// _3: _1.add(_2) ...
		_re3 = _re1 + _re2;
		_im3 = _im1 + _im2;

		// result: _3.scale(0.5) ...
		result = new ComplexNumber(0.5 * _re3, 0.5 * _im3);

		// sin() ...

		// _3: _1.sub(_2) ...
		_re3 = _re1 - _re2;
		_im3 = _im1 - _im2;

		// sn: _3.div(2*i) ...
		sn_re = 0.5 * _im3; // !!!
		sn_im = -0.5 * _re3; // !!!

		// result: result.div(sn) ...
		result = divides(result, sn_re, sn_im);
		return result;
	}//end cot()


    /**
    * Returns the hyperbolic tangent of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     tanh(z)  =  sinh(z) / cosh(z)
    * </pre>
    * <p>
    * @return                  The <tt>Complex</tt> hyperbolic tangent
    * <p>
    * @see                     Complex#tan()
    * @see                     Complex#atanh()
    **/

	public ComplexNumber tanh() {
		ComplexNumber result;
		// tanh(z) = sinh(z) / cosh(z)

		double scalar;
		double _re1, _im1;
		double _re2, _im2;
		double _re3, _im3;

		double ch_re, ch_im;

		// sinh() ...

		// _1: z.exp() ...
		scalar = Math.exp(this.real);
		_re1 = scalar * Math.cos(this.imaginary);
		_im1 = scalar * Math.sin(this.imaginary);

		// _2: z.neg().exp() ...
		scalar = Math.exp(-1.0 * this.real);
		_re2 = scalar * Math.cos(-1.0 * this.imaginary);
		_im2 = scalar * Math.sin(-1.0 * this.imaginary);

		// _3: _1.sub(_2) ...
		_re3 = _re1 - _re2;
		_im3 = _im1 - _im2;

		// result: _3.scale(0.5) ...
		result = new ComplexNumber(0.5 * _re3, 0.5 * _im3);

		// cosh() ...

		// _3: _1.add(_2) ...
		_re3 = _re1 + _re2;
		_im3 = _im1 + _im2;

		// ch: _3.scale(0.5) ...
		ch_re = 0.5 * _re3;
		ch_im = 0.5 * _im3;

		// result: result.div(ch) ...
		result = this.divides(result, ch_re, ch_im);
		return result;
	}//end tanh()


    /**
    * Returns the <i>principal</i> arc tangent of a <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     atan(z)  =  -<i><b>i</b></i>/2 * log( (<i><b>i</b></i>-z)/(<i><b>i</b></i>+z) )
    * </pre>
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>atan(z)</i>, the others are
    * of the form:
    * <p>
    * <pre>
    *     <b>A</b> + k*<b>PI</b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * <i><b>Domain Restrictions:</b><ul> atan(z) is undefined for z = + <b>i</b> or z = - <b>i</b>
    * </ul></i>
    * <p>
    * @return                  Principal <tt>Complex</tt> arc tangent
    * <p>
    * @see                     Complex#tan()
    * @see                     Complex#tanh()
    **/

	public ComplexNumber atan() {
        ComplexNumber result;
            //  atan(z)  =  -i/2 * log( (i-z)/(i+z) )

            double _re1, _im1;

            // result:  i.sub(z) ...
            result =  new ComplexNumber(- real, 1.0 - imaginary);

            // _1:      i.add(z) ...
            _re1 =  + real;
            _im1 =  1.0 + imaginary;

            // result:  result.div(_1) ...
            result=this.divides(result, _re1, _im1);

            // _1:      result.log() ...
            _re1 =  Math.log(result.abs());
            _im1 =  result.arg();

            // result:  half_i.neg().mul(_2) ...
            //result.re =   0.5*_im1;//rem
            //result.im =  -0.5*_re1;//rem
            result=new ComplexNumber(0.5*_im1,-0.5*_re1);
        return  result;
    }//end atan()



    /**
    * Returns the <i>principal</i> inverse hyperbolic tangent of a
    * <tt>Complex</tt> number.
    *
    * <p>
    * <pre>
    *     atanh(z)  =  1/2 * log( (1+z)/(1-z) )
    * </pre>
    * <p>
    * There are infinitely many solutions, besides the principal solution.
    * If <b>A</b> is the principal solution of <i>atanh(z)</i>, the others are
    * of the form:
    * <p>
    * <pre>
    *     <b>A</b> + k*<b>PI</b>*<b><i>i</i></b>
    * </pre>
    * <p>
    * where k is any integer.
    * <p>
    * <i><b>Domain Restrictions:</b><ul> atanh(z) is undefined for z = + 1 or z = - 1
    * </ul></i>
    * <p>
    * @return                  Principal <tt>Complex</tt> inverse hyperbolic tangent
    * <p>
    * @see                     Complex#tanh()
    **/

	public ComplexNumber atanh() {
        ComplexNumber result;
            //  atanh(z)  =  1/2 * log( (1+z)/(1-z) )

            double _re1, _im1;

            // result:  one.add(z) ...
            result =  new ComplexNumber(1.0 + real, + imaginary);

            // _1:      one.sub(z) ...
            _re1 =  1.0 - real;
            _im1 =  - imaginary;

            // result:  result.div(_1) ...
            result=this.divides(result, _re1, _im1);

            // _1:      result.log() ...
            _re1 =  Math.log(result.abs());
            _im1 =  result.arg();

            // result:  _1.scale(0.5) ...
            //result.re =  0.5 * _re1;//rem
            //result.im =  0.5 * _im1;//rem
            result= new ComplexNumber(0.5*_re1,0.5*_im1);
        return  result;
    }//end atanh()
	
	

	public Object clone() {
		try {
			return (Object) (super.clone());
		} catch (java.lang.CloneNotSupportedException e) {
			return null; // This cannot happen: there would have to be a serious
							// internal error in the Java runtime if this
							// codepath happens!
		} // endtry
	}// end clone()



	public boolean isNaN() {
		boolean isNan = Double.isNaN(this.real) || Double.isNaN(this.imaginary);
		boolean isInfinite = Double.isInfinite(this.real) || Double.isInfinite(this.imaginary);
		return isNan || isInfinite;
	}
	
	
	public static void main(String args[]) {
		ComplexNumber cn = new ComplexNumber(5.6, 3.5);
		final ComplexNumber cn2 = new ComplexNumber(2.1,0.3);
		
		System.out.println("Number is: "+cn+", power is -3");

		System.out.println("newPowerMethod == " + cn.power(-3));

		System.out.println("oldPowerMethod == " + cn.powerOLD(-3));//_New123(3));
		System.out.println("from marksPow(double), 3.0 == " + cn.power(-3.0));
		System.out.println("Diff__Pow is "+ (cn.power(-3).minus(cn.power(-3.0))));
		System.out.println("------------------------");

		System.out.println("Abs__OLD is "+ cn.absOLD());
		System.out.println("Abs__NEW_marksmath is "+cn.abs());
		System.out.println("Diff__Abs is "+ (cn.absOLD()-(cn.abs())));
		System.out.println("------------------------");

		System.out.println("Sine__OLD is "+ cn.sineOLD());
		System.out.println("Sine__NEW_marksmath is "+cn.sine());
		System.out.println("Diff__Sine is "+ (cn.sineOLD().minus(cn.sine())));
		System.out.println("------------------------");
		
		System.out.println("CoSine__OLD is "+ cn.cosineOLD());
		System.out.println("COSine__NEW_marksmath is "+cn.cosine());
		System.out.println("Diff__COSine is "+ (cn.cosineOLD().minus(cn.cosine())));
		System.out.println("------------------------");
		
		System.out.println("Tangent__OLD is "+ cn.tangentOLD());
		System.out.println("Tangent_new is "+cn.tangent());
		System.out.println("Tan is "+cn.tan());
		System.out.println("Diff__Tan is "+ (cn.tangentOLD().minus(cn.tangent())));
		System.out.println("------------------------");
		
		System.out.println("Reciprocal__OLD is "+ cn.reciprocalOLD());
		System.out.println("Reciprocal__NEW_marksmath is "+cn.reciprocal());
		System.out.println("Diff__Reciprocal is "+ (cn.reciprocalOLD().minus(cn.reciprocal())));
		System.out.println("------------------------");
		
		System.out.println("Sqroot__OLD is "+ cn.sqrootOLD());
		System.out.println("Sqroot__NEW_marksmath is "+cn.sqroot());
		System.out.println("Diff__SqRoot is "+ (cn.sqrootOLD().minus(cn.sqroot())));
		System.out.println("------------------------");
		

		
		System.out.println("Divides__OLD is "+ cn.dividesOLD(cn2));
		System.out.println("Divides__NEW_marksmath is "+cn.divides(cn2));
		System.out.println("Diff__DIVIDES is "+ (cn.dividesOLD(cn2).minus(cn.divides(cn2))));
		System.out.println("------------------------");
		

		System.out.println("InverseSine__OLD is "+ cn.inverseSineOLD());
		System.out.println("InverseSine__NEW_marksmath is "+cn.inverseSine());
		System.out.println("Diff__Sine is "+ (cn.inverseSineOLD().minus(cn.inverseSine())));
		System.out.println("------------------------");
	
		
	}
}

