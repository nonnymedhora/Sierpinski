/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

/**
 * @author Navroz
 *
 */
public class MultiClrJuliaGenerator extends MultiClrFractalGenerator {

	public MultiClrJuliaGenerator(String folderPath, FractalDtlInfo fD) {
		super(folderPath, fD);
		System.out.println(folderPath);
		System.out.println(fD);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	int getIterBlowOutCount(ComplexNumber z, FractalDtlInfo opt) {
		return this.julia(z, opt);
	}

	private int julia(ComplexNumber z0, FractalDtlInfo opt) {

		ComplexNumber z = z0;
		final ComplexNumber zC = opt.getComplex();//new ComplexNumber(opt.getRealConstant(), opt.getImagConstant());
		final int maxIter = opt.getMaxIter();
		final double boundary = opt.getBound();
		for (int t = 0; t < maxIter; t++) {
			if (z.abs() > boundary)
				return t;
			z = getZValue(zC, z, opt);
		}
		return maxIter;
	}

}
