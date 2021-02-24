/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

/**
 * @author Navroz
 *	
 */
public class MultiClrMandelbrotGenerator extends MultiClrFractalGenerator {

	public MultiClrMandelbrotGenerator(String folderPath, FractalDtlInfo fD) {
		super(folderPath, fD);
		System.out.println(folderPath);
		System.out.println(fD);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	int getIterBlowOutCount(ComplexNumber z, FractalDtlInfo opt) {
		return this.mand(z,opt);
	}
	
	// return number of iterations to check if 
	// c = a + ib is in Mandelbrot set
	private int mand(ComplexNumber z0, FractalDtlInfo fDInf) {
		ComplexNumber z = z0;
		final int maxIter = fDInf.getMaxIter();
		final double boundary = fDInf.getBound();
		for (int t = 0; t < maxIter; t++) {
			if (z.abs() > boundary)
				return t;
			z = this.getZValue(z0, z, fDInf);
		}
		return maxIter;
	}

}
