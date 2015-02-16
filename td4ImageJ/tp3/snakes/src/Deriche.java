/**
 * @author Thomas BOUDIER (Institut Pierre et Marie Curie)
 * @version 21/11/2006 (repris par Jacques-Olivier Lachaud)
 */


import ij.process.*;

class Deriche {

	/**
	 * Filter
	 *
	 * @param  ip      image
	 * @param  alphaD  alpha coefficient
	 * @return         filtered image
	 */
	public static ByteProcessor deriche
	(ImageProcessor ip, double alphaD,
			FloatProcessor ip_gx,
			FloatProcessor ip_gy  ) {
		int nmem;
		float[] nf_grx = null;
		float[] nf_gry = null;
		int[] a1 = null;
		float[] a2 = null;
		float[] a3 = null;
		float[] a4 = null;

		int icolonnes = 0;
		int icoll = 0;
		int lignes;
		int colonnes;
		int lig_1;
		int lig_2;
		int lig_3;
		int col_1;
		int col_2;
		int col_3;
		int jp1;
		int jm1;
		int im1;
		int ip1;
		int icol_1;
		int icol_2;
		int i;
		int j;
		float ad1;
		float ad2;
		float wd;
		float gzr;
		float gun;
		float an1;
		float an2;
		float an3;
		float an4;
		float an11;

		lignes = ip.getHeight();
		colonnes = ip.getWidth();
		nmem = lignes * colonnes;

		lig_1 = lignes - 1;
		lig_2 = lignes - 2;
		lig_3 = lignes - 3;
		col_1 = colonnes - 1;
		col_2 = colonnes - 2;
		col_3 = colonnes - 3;

		/*
		 *  alloc temporary buffers
		 */
		nf_grx = new float[nmem];
		nf_gry = new float[nmem];

		a1 = new int[nmem];
		a2 = new float[nmem];
		a3 = new float[nmem];
		a4 = new float[nmem];

		ad1 = (float) -Math.exp(-alphaD);
		ad2 = 0;
		an1 = 1;
		an2 = 0;
		an3 = (float) Math.exp(-alphaD);
		an4 = 0;
		an11 = 1;

		/*
		 *  FIRST STEP:  Y GRADIENT
		 */
		/*
		 *  x-smoothing
		 */
		for (i = 0; i < lignes; i++) {
			for (j = 0; j < colonnes; j++) {
				a1[i * colonnes + j] = (int) ip.getPixel(j, i);
			}
		}

		for (i = 0; i < lignes; ++i) {
			icolonnes = i * colonnes;
			icol_1 = icolonnes - 1;
			icol_2 = icolonnes - 2;
			a2[icolonnes] = an1 * a1[icolonnes];
			a2[icolonnes + 1] = an1 * a1[icolonnes + 1] +
			an2 * a1[icolonnes] - ad1 * a2[icolonnes];
			for (j = 2; j < colonnes; ++j) {
				a2[icolonnes + j] = an1 * a1[icolonnes + j] + an2 * a1[icol_1 + j] -
				ad1 * a2[icol_1 + j] - ad2 * a2[icol_2 + j];
			}
		}

		for (i = 0; i < lignes; ++i) {
			icolonnes = i * colonnes;
			icol_1 = icolonnes + 1;
			icol_2 = icolonnes + 2;
			a3[icolonnes + col_1] = 0;
			a3[icolonnes + col_2] = an3 * a1[icolonnes + col_1];
			for (j = col_3; j >= 0; --j) {
				a3[icolonnes + j] = an3 * a1[icol_1 + j] + an4 * a1[icol_2 + j] -
				ad1 * a3[icol_1 + j] - ad2 * a3[icol_2 + j];
			}
		}

		icol_1 = lignes * colonnes;

		for (i = 0; i < icol_1; ++i) {
			a2[i] += a3[i];
		}

		/*
		 *  FIRST STEP Y-GRADIENT : y-derivative
		 */
		/*
		 *  columns top - downn
		 */
		for (j = 0; j < colonnes; ++j) {
			a3[j] = 0;
			a3[colonnes + j] = an11 * a2[j] - ad1 * a3[j];
			for (i = 2; i < lignes; ++i) {
				a3[i * colonnes + j] = an11 * a2[(i - 1) * colonnes + j] -
				ad1 * a3[(i - 1) * colonnes + j] - ad2 * a3[(i - 2) * colonnes + j];
			}
		}

		/*
		 *  columns down top
		 */
		for (j = 0; j < colonnes; ++j) {
			a4[lig_1 * colonnes + j] = 0;
			a4[(lig_2 * colonnes) + j] = -an11 * a2[lig_1 * colonnes + j] -
			ad1 * a4[lig_1 * colonnes + j];
			for (i = lig_3; i >= 0; --i) {
				a4[i * colonnes + j] = -an11 * a2[(i + 1) * colonnes + j] -
				ad1 * a4[(i + 1) * colonnes + j] - ad2 * a4[(i + 2) * colonnes + j];
			}
		}

		icol_1 = colonnes * lignes;
		for (i = 0; i < icol_1; ++i) {
			a3[i] += a4[i];
		}

		for (i = 0; i < lignes; ++i) {
			for (j = 0; j < colonnes; ++j) {
				nf_gry[i * colonnes + j] = a3[i * colonnes + j];
			}
		}

		/*
		 *  SECOND STEP X-GRADIENT
		 */
		for (i = 0; i < lignes; ++i) {
			for (j = 0; j < colonnes; ++j) {
				a1[i * colonnes + j] = (int) (ip.getPixel(j, i));
			}
		}

		for (i = 0; i < lignes; ++i) {
			icolonnes = i * colonnes;
			icol_1 = icolonnes - 1;
			icol_2 = icolonnes - 2;
			a2[icolonnes] = 0;
			a2[icolonnes + 1] = an11 * a1[icolonnes];
			for (j = 2; j < colonnes; ++j) {
				a2[icolonnes + j] = an11 * a1[icol_1 + j] -
				ad1 * a2[icol_1 + j] - ad2 * a2[icol_2 + j];
			}
		}

		for (i = 0; i < lignes; ++i) {
			icolonnes = i * colonnes;
			icol_1 = icolonnes + 1;
			icol_2 = icolonnes + 2;
			a3[icolonnes + col_1] = 0;
			a3[icolonnes + col_2] = -an11 * a1[icolonnes + col_1];
			for (j = col_3; j >= 0; --j) {
				a3[icolonnes + j] = -an11 * a1[icol_1 + j] -
				ad1 * a3[icol_1 + j] - ad2 * a3[icol_2 + j];
			}
		}
		icol_1 = lignes * colonnes;
		for (i = 0; i < icol_1; ++i) {
			a2[i] += a3[i];
		}

		/*
		 *  on the columns
		 */
		/*
		 *  columns top down
		 */
		for (j = 0; j < colonnes; ++j) {
			a3[j] = an1 * a2[j];
			a3[colonnes + j] = an1 * a2[colonnes + j] + an2 * a2[j]
			                                                     - ad1 * a3[j];
			for (i = 2; i < lignes; ++i) {
				a3[i * colonnes + j] = an1 * a2[i * colonnes + j] + an2 * a2[(i - 1) * colonnes + j] -
				ad1 * a3[(i - 1) * colonnes + j] - ad2 * a3[(i - 2) * colonnes + j];
			}
		}

		/*
		 *  columns down top
		 */
		for (j = 0; j < colonnes; ++j) {
			a4[lig_1 * colonnes + j] = 0;
			a4[lig_2 * colonnes + j] = an3 * a2[lig_1 * colonnes + j] - ad1 * a4[lig_1 * colonnes + j];
			for (i = lig_3; i >= 0; --i) {
				a4[i * colonnes + j] = an3 * a2[(i + 1) * colonnes + j] + an4 * a2[(i + 2) * colonnes + j] -
				ad1 * a4[(i + 1) * colonnes + j] - ad2 * a4[(i + 2) * colonnes + j];
			}
		}

		icol_1 = colonnes * lignes;
		for (i = 0; i < icol_1; ++i) {
			a3[i] += a4[i];
		}

		for (i = 0; i < lignes; i++) {
			for (j = 0; j < colonnes; j++) {
				nf_grx[i * colonnes + j] = a3[i * colonnes + j];
			}
		}

		/*
		 *  SECOND STEP X-GRADIENT : the x-gradient is  done
		 */
		/*
		 *  THIRD STEP : NORM
		 */
		/*
		 *  computation of the magnitude
		 */
		for (i = 0; i < lignes; i++) {
			for (j = 0; j < colonnes; j++) {
				a2[i * colonnes + j] = nf_gry[i * colonnes + j];
			}
		}
		icol_1 = colonnes * lignes;
		for (i = 0; i < icol_1; ++i) {
			a2[i] = modul(a2[i], a3[i]);
		}

		byte res[] = new byte[nmem];
		float min = a2[0];
		float max = a2[0];
		for (i = 1; i < nmem; i++) {
			if (a2[i] > max) {
				max = a2[i];
			}
			if (a2[i] < min) {
				min = a2[i];
			}
		}

		for (i = 0; i < nmem; i++) {
			res[i] = (byte) (255.0 * (a2[i] - min) / (max - min));
		}

		// create gradient images.
		for (i = 0; i < lignes; i++) {
			for (j = 0; j < colonnes; j++) {
				ip_gx.setf( j, i, nf_grx[i * colonnes + j] );
				ip_gy.setf( j, i, nf_gry[i * colonnes + j] );
			}
		}


		ByteProcessor ip2 = new ByteProcessor(ip.getWidth(), ip.getHeight(), res, null);
		return ip2;
	}


	/**
	 *  modul
	 *
	 * @param  x  x
	 * @param  y  y
	 * @return    modul
	 */
	public static float modul(float x, float y) {
		return ((float) Math.sqrt(carre(x) + carre(y)));
	}


	/**
	 *  square
	 *
	 * @param  x  x
	 * @return    x square
	 */
	public static float carre(float x) {
		return (x * x);
	}


}

