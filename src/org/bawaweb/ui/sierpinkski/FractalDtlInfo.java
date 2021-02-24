/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.util.Arrays;

/**
 * @author Navroz
 *
 */
public class FractalDtlInfo {
	
	
	private FractalBase fBase;

	private String name;

	private double x_min;
	private double y_min;
	private double x_max;
	private double y_max;

	private double xC = 0.0;
	private double yC = 0.0;
	private double scaleSize;

	private double rotation = 0.0;
	private int magnification;
	private int power;
	private boolean useDiff = false;
	
	// z + C others are "Multiply","Divide","Subtract"
	private String pxConstOperation = "Plus";

	private String pxXTransformation = "None";
	private String pxYTransformation = "None";
	private String pixXYOperation = "Plus";
	private String useFuncPixel = "None";
	private String useFuncConst = "None";
	private boolean running = false;
	private String colorChoice;
	private double  bound = 2.0;
	private boolean reversePixelCalculation = false;
	
	private int[] rgbDivisors = FractalBase.FRST_SIX_PRIMES;
	private int[] rgbStartVals = FractalBase.POW2_4_200;
	
	//	maximum iterations to check for Mandelbrot, Julia & Poly
	private int maxIter = 255;
	private int areaSize = 599;
	
	private boolean isComplexNumConst;
	private ComplexNumber complex;
	private boolean isConstFuncApplied = false;
	private boolean applyCustomFormula = false;
	
	public FractalDtlInfo(String n) {
		this.name = n;
	}


	public static void main(String[] args) {
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getX_min() {
		return x_min;
	}


	public void setX_min(double x_min) {
		this.x_min = x_min;
	}


	public double getY_min() {
		return y_min;
	}


	public void setY_min(double y_min) {
		this.y_min = y_min;
	}


	public double getX_max() {
		return x_max;
	}


	public void setX_max(double x_max) {
		this.x_max = x_max;
	}


	public double getY_max() {
		return y_max;
	}


	public void setY_max(double y_max) {
		this.y_max = y_max;
	}


	public double getxC() {
		return xC;
	}


	public void setxC(double xC) {
		this.xC = xC;
	}


	public double getyC() {
		return yC;
	}


	public void setyC(double yC) {
		this.yC = yC;
	}


	public double getScaleSize() {
		return scaleSize;
	}


	public void setScaleSize(double scaleSize) {
		this.scaleSize = scaleSize;
	}


	public double getRotation() {
		return rotation;
	}


	public void setRotation(double rotation) {
		this.rotation = rotation;
	}


	public int getMagnification() {
		return magnification;
	}


	public void setMagnification(int magnification) {
		this.magnification = magnification;
	}


	public int getPower() {
		return power;
	}


	public void setPower(int power) {
		this.power = power;
	}


	public boolean isUseDiff() {
		return useDiff;
	}


	public void setUseDiff(boolean useDiff) {
		this.useDiff = useDiff;
	}


	public String getPxConstOperation() {
		return pxConstOperation;
	}


	public void setPxConstOperation(String pxConstOperation) {
		this.pxConstOperation = pxConstOperation;
	}


	public String getPxXTransformation() {
		return pxXTransformation;
	}


	public void setPxXTransformation(String pxXTransformation) {
		this.pxXTransformation = pxXTransformation;
	}


	public String getPxYTransformation() {
		return pxYTransformation;
	}


	public void setPxYTransformation(String pxYTransformation) {
		this.pxYTransformation = pxYTransformation;
	}


	public String getPixXYOperation() {
		return pixXYOperation;
	}


	public void setPixXYOperation(String pixXYOperation) {
		this.pixXYOperation = pixXYOperation;
	}


	public String getUseFuncPixel() {
		return useFuncPixel;
	}


	public void setUseFuncPixel(String useFuncPixel) {
		this.useFuncPixel = useFuncPixel;
	}


	public String getUseFuncConst() {
		return useFuncConst;
	}


	public void setUseFuncConst(String useFuncConst) {
		this.useFuncConst = useFuncConst;
	}


	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}


	public String getColorChoice() {
		return colorChoice;
	}


	public void setColorChoice(String colorChoice) {
		this.colorChoice = colorChoice;
	}


	public double getBound() {
		return bound;
	}


	public void setBound(double bound) {
		this.bound = bound;
	}


	public boolean isReversePixelCalculation() {
		return reversePixelCalculation;
	}


	public void setReversePixelCalculation(boolean reversePixelCalculation) {
		this.reversePixelCalculation = reversePixelCalculation;
	}


	public int[] getRgbDivisors() {
		return rgbDivisors;
	}


	public void setRgbDivisors(int[] rgbDivisors) {
		this.rgbDivisors = rgbDivisors;
	}


	public int[] getRgbStartVals() {
		return rgbStartVals;
	}


	public void setRgbStartVals(int[] rgbStartVals) {
		this.rgbStartVals = rgbStartVals;
	}


	public int getMaxIter() {
		return maxIter;
	}


	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}


	public int getAreaSize() {
		return areaSize;
	}


	public void setAreaSize(int areaSize) {
		this.areaSize = areaSize;
	}


	public boolean isComplexNumConst() {
		return isComplexNumConst;
	}


	public void setComplexNumConst(boolean isComplexNumConst) {
		this.isComplexNumConst = isComplexNumConst;
	}


	public ComplexNumber getComplex() {
		return complex;
	}


	public void setComplex(ComplexNumber complex) {
		this.complex = complex;
	}


	public boolean isConstFuncApplied() {
		return isConstFuncApplied;
	}


	public void setConstFuncApplied(boolean isConstFuncApplied) {
		this.isConstFuncApplied = isConstFuncApplied;
	}


	public boolean isApplyCustomFormula() {
		return applyCustomFormula;
	}


	public void setApplyCustomFormula(boolean applyCustomFormula) {
		this.applyCustomFormula = applyCustomFormula;
	}


	public FractalBase getfBase() {
		return fBase;
	}


	public void setfBase(FractalBase fBase) {
		this.fBase = fBase;
	}
	
	
	@Override
	public String toString() {
		return "FractalDtlInfo [fBase=" + fBase + ", name=" + name + ", x_min=" + x_min + ", y_min=" + y_min
				+ ", x_max=" + x_max + ", y_max=" + y_max + ", xC=" + xC + ", yC=" + yC + ", scaleSize=" + scaleSize
				+ ", rotation=" + rotation + ", magnification=" + magnification + ", power=" + power + ", useDiff="
				+ useDiff + ", pxConstOperation=" + pxConstOperation + ", pxXTransformation=" + pxXTransformation
				+ ", pxYTransformation=" + pxYTransformation + ", pixXYOperation=" + pixXYOperation + ", useFuncPixel="
				+ useFuncPixel + ", useFuncConst=" + useFuncConst + ", running=" + running + ", colorChoice="
				+ colorChoice + ", bound=" + bound + ", reversePixelCalculation=" + reversePixelCalculation
				+ ", rgbDivisors=" + Arrays.toString(rgbDivisors) + ", rgbStartVals=" + Arrays.toString(rgbStartVals)
				+ ", maxIter=" + maxIter + ", areaSize=" + areaSize + ", isComplexNumConst=" + isComplexNumConst
				+ ", complex=" + complex + ", isConstFuncApplied=" + isConstFuncApplied + ", applyCustomFormula="
				+ applyCustomFormula + "]";
	}



}
