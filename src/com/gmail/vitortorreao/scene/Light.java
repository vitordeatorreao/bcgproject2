package com.gmail.vitortorreao.scene;

import java.awt.Color;

import com.gmail.vitortorreao.math.Vector;

/**
 * This class implements a Light Element.
 * A light is composed of the following elements:
 * 
 * <p>
 * This code is available through the 
 * <a href="http://www.gnu.org/licenses/gpl-2.0.html">GNU GPL v2.0</a> license.
 * <br>
 * You can acess the full project at 
 * <a href="https://github.com/vitordeatorreao/bcgproject1">GitHub</a>.
 * @author	<a href="https://github.com/vitordeatorreao/">V&iacute;tor de 
 * 			Albuquerque Torre&atilde;o</a>
 * @version 1.0
 * @since 1.0
 */
public class Light {
	
	/**
	 * Environment Light's Color
	 */
	private float[] iAmb;
	
	/**
	 * Light's color
	 */
	private Color iL;
	
	/**
	 * Object's environmental reflection coefficient
	 */
	private double kA;
	
	/**
	 * Object's specular reflection coefficient
	 */
	private double kS;
	
	/**
	 * Featured specular size
	 * A.K.A. Eta
	 */
	private float n;
	
	/**
	 * Diffuse reflection coefficient
	 */
	private Vector kD;
	
	/**
	 * Object's surface diffuse color
	 */
	private Vector oD;
	
	/**
	 * Origin of Light
	 */
	private float[] pL;
	
	/**
	 * Light's diffuse color
	 */
	private float[] iDiffuse;
	
	/**
	 * Light's specular color
	 */
	private float[] iSpecular;
	
	/**
	 * Material's environment color
	 */
	private float[] mAmb;
	
	/**
	 * Material's diffuse color
	 */
	private float[] mDiffuse;
	
	/**
	 * Material's specular color
	 */
	private float[] mSpecular;
	
	/**
	 * Material's emissive color
	 */
	private float[] mEmissive;
	
	public Light(float[] iAmb, float[] iDiffuse, float[] iSpecular,
			float[] mAmb, float[] mDiffuse, float[] mSpecular, float[] mEmissive,
			float eta, float[] pL) {
		
		this.iAmb		= iAmb;
		this.pL			= pL;
		this.n			= eta;
		this.iDiffuse	= iDiffuse;
		this.iSpecular	= iSpecular;
		this.mAmb		= mAmb;
		this.mDiffuse	= mDiffuse;
		this.mSpecular	= mSpecular;
		this.mEmissive	= mEmissive;
	}
	
	public Light(float[] iAmb, Color iLocal, double kA, 
			double kS, float n, Vector kD, Vector oD, float[] pL) {
		this.iAmb	= iAmb;
		this.iL = iLocal;
		this.kA		= kA;
		this.kS		= kS;
		this.n		= n;
		this.kD		= kD;
		this.oD		= oD;
		this.pL		= pL;
	}

	public float[] getiAmb() {
		return iAmb;
	}

	public Color getiL() {
		return iL;
	}

	public double getkA() {
		return kA;
	}

	public double getkS() {
		return kS;
	}

	public float getN() {
		return n;
	}

	public Vector getkD() {
		return kD;
	}

	public Vector getoD() {
		return oD;
	}

	public float[] getpL() {
		return pL;
	}
	
	public float[] getiDiffuse() {
		return iDiffuse;
	}

	public void setiDiffuse(float[] iDiffuse) {
		this.iDiffuse = iDiffuse;
	}

	public float[] getiSpecular() {
		return iSpecular;
	}

	public void setiSpecular(float[] iSpecular) {
		this.iSpecular = iSpecular;
	}

	public float[] getmAmb() {
		return mAmb;
	}

	public void setmAmb(float[] mAmb) {
		this.mAmb = mAmb;
	}

	public float[] getmDiffuse() {
		return mDiffuse;
	}

	public void setmDiffuse(float[] mDiffuse) {
		this.mDiffuse = mDiffuse;
	}

	public float[] getmSpecular() {
		return mSpecular;
	}

	public void setmSpecular(float[] mSpecular) {
		this.mSpecular = mSpecular;
	}

	public float[] getmEmissive() {
		return mEmissive;
	}

	public void setmEmissive(float[] mEmissive) {
		this.mEmissive = mEmissive;
	}

	public String toString() {
		String s = "{\n";
		s += "Iamb = "+this.iAmb.toString()+"\n";
		s += "Il = "+this.iL.toString()+"\n";
		s += "Ka = "+this.kA+"\n";
		s += "Ks = "+this.kS+"\n";
		s += "N (or eta) = "+this.n+"\n";
		s += "Kd = "+this.kD.toString()+"\n";
		s += "Od = "+this.oD.toString()+"\n";
		s += "Pl = "+this.pL.toString()+"\n";
		s += "}\n";
		return s;
	}
}
