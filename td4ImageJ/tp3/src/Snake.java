import ij.*;
import ij.gui.*;
import ij.process.*;
import java.util.*;
import Jama.*;

/**
 * Cette classe modelise un snake, ou contour actif, de nb de points fixe.
 * La methode 'iteration' effectue une iteration de la deformation.
 */
public class Snake {

	private double[] X; // vecteur des abscisses des points de contrôle
	private double[] Y; // vecteur des ordonnées des points de contrôle
	private ImageProcessor image;
	private FloatProcessor SGImage; // image de la norme du gradient au carré
	private double alpha, beta, gamma, lambda; // coefficients des différentes forces (elastique, torsion, viscosité)
	private int N = 0;
	private Matrix M;

	/**
	 * Calcule l'image de la norme du gradient au carre de [ip].
	 *
	 * @param ip une image en niveaux de gris.
	 * @return une image de contours.
	 */
	static FloatProcessor squaredGradientImage(ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		FloatProcessor sg = new FloatProcessor(w, h);
		FloatProcessor gx = new FloatProcessor(w, h);
		FloatProcessor gy = new FloatProcessor(w, h);

		ByteProcessor gn = 
			Deriche.deriche( ip, 0.5, gx, gy );
		for(int x=0; x<w; x++)
			for(int y=0; y<h; y++){
				float xg = gx.getf(x, y);
				float yg = gy.getf(x, y);
				sg.setf(x, y, xg*xg+yg*yg);
			}
		return sg;
	}

	/**
	 * Constructeur. Le snake est invalide.
	 * @see initRectangle,initParametres
	 */
	public Snake(){
		N = 0;
	}


	/**
	 * Initialise le snake comme un rectangle avec [nb] points par
	 * cote. (Repere image, y vers le bas).
	 *
	 * @param x1 coordonnee x point haut-gauche.
	 * @param y1 coordonnee y point haut-gauche.
	 * @param x2 coordonnee x point bas-droite.
	 * @param y2 coordonnee y point bas-droite.
	 * @param nb nombre de points par cote.
	 */
	public void initRectangle(double x1, double y1, double x2, double y2, int nb){
		N = 4*nb;
		X = new double[N];
		Y = new double[N];
		int j = 0;

		for(int i=0; i <=nb; i++){
			X[j] = x1;
			Y[j] = y1+((y2-y1)*i)/nb;
			j++;
		}
		for(int i=1; i <=nb; i++){
			X[j] = x1+((x2-x1)*i)/nb;	
			Y[j] = y2;
			j++;
		}

		for(int i=1; i <=nb; i++){
			X[j] = x2;
			Y[j] = y2-((y2-y1)*i)/nb;
			j++;
		}
		for(int i=1; i <nb; i++){
			X[j] = x2-((x2-x1)*i)/nb;
			Y[j] = y1;
			j++;
		}
	}

	/**
	 * Initialisee les parametres du snake.
	 *
	 * @param ip l'image dont on veut extraire une composante
	 * suivant l'information de contour.
	 *
	 * @param alpha coefficient d'elasticite
	 * @param beta coefficient de rigidite
	 * @param gamma coefficient de frottement
	 * @param lambda influence des forces images.
	 */
	public void initParametres(ImageProcessor ip, double alpha, double beta, double gamma, double lambda ){
		if ( N == 0 ) throw new RuntimeException( "Snake sans geometrie.");

		this.image = ip;
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.lambda = lambda;
		SGImage = squaredGradientImage(image);
		// TODO : calcul de la matrice M
	}

	/**
	 * @return le nombre de points du snake.
	 */
	public int getNbPts(){
		return N;
	}

	/**
	 * @param i un indice entre 0 et 'getNbPts()'.
	 * @return la coordonnee X[i].
	 */
	public double getX(int i){
		return X[i];
	}

	/**
	 * @param i un indice entre 0 et 'getNbPts()'.
	 * @return la coordonnee Y[i].
	 */
	public double getY(int i){
		return Y[i];
	}


	/**
	 * @return l'image de contour utilisee dans les energies du snake.
	 */
	public FloatProcessor getSGImage(){
		return SGImage;
	}

	/**
	 * Multiplie a gauche la matrice [A] par le vecteur [V] (de taille
	 * compatible).
	 *
	 * @param A une matrice avec m lignes et n colonnes
	 * @param V un vecteur de taille n (n lignes)
	 * @return un vecteur de taille m (m lignes).
	 */
	private double[] mult(Matrix A, double[] V){
		int n = V.length;
		int m = A.getRowDimension();
		double[] res = new double[m];
		for(int i=0; i<m; i++){
			res[i] = 0.0;
			for(int j=0; j<n; j++)
				res[i] += A.get(i, j)*V[j];
		}
		return res;
	}

	
	/**
	 * Methode calculant les vecteurs 'X' et 'Y' au temps t+1 a partir
	 * de la position du snake au temps t.
	 */
	public void iteration(){
	    // TODO
	}

}

