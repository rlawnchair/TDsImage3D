import java.awt.*;

public class Path {

	private class ContourPoint{
		// repère x "vers le haut" et y "vers la droite"
		// (x,y) désigne le point demi-entier (x+1/2, y+1/2)
		int _x;
		int _y; 
		// informations géométriques calculées:

		// vecteur tangent
		int _tx;
		int _ty;

		// courbure : TODO
		double _k;

	}

	private int _n;       // nb de points de contour
	private ContourPoint[] _tabPts; // tableau des points de contour
	private int _xm; // abscisse minimum
	private int _ym; // ordonnée minimum
	private int _xM; // abscisse maximum
	private int _yM; // ordonnée maximum
	private final int _SIGMA = 3;
	private final int _M = 3*_SIGMA;


	Path(FreemanCode c){
		_n = c.getLength();
		_tabPts = new ContourPoint[_n];
		for(int i = 0; i<_n; i++)
			_tabPts[i] = new ContourPoint();
		computeCoordinates(c);
		computeTangents(c);
		computePathCurvature();
	}


	private void computeCoordinates(FreemanCode c){
		int x=(int)c.getP0().getX(), y=(int)c.getP0().getY();
		_xm=_xM=x;
		_ym=_yM=y;
		_tabPts[0]._x=x;
		_tabPts[0]._y=y;

		for (int i = 0; i < _n-1; i++){
			x += c.getDeltaX(i);
			if (x < _xm) 
				_xm = x;
			if (x > _xM)
				_xM = x;
			y += c.getDeltaY(i);
			if (y < _ym)
				_ym = y;
			if (y > _yM)
				_yM = y;
			_tabPts[i+1]._x=x;
			_tabPts[i+1]._y=y;
		}
	}
	private void computeTangents(FreemanCode c){
		DigitalTangent t;
		int[][] sym4 = {{1,1},{-1,1},{-1,-1},{1,-1}};

		for(int i = 0; i < _n; i++){
			t= new DigitalTangent(c, i);
			// calcul de symetries a partir des caracteristiques 
			// rapportees au 1er quadrant
			int a = t.getA();
			int b = t.getB();
			int quad = t.getQuadrant();
			_tabPts[i]._tx = sym4[quad][0]*b;
			_tabPts[i]._ty = sym4[quad][1]*a;
		}

	}

	private void computePathCurvature(){
		for(int i = 0; i < _n; ++i){
			computeCurvature(i);
		}
	}

	private void computeCurvature(int index){
		float ktmp = 0;
		int ind1;
		int ind2;

		double thetaInd = Math.atan2(_tabPts[index]._ty, _tabPts[index]._tx);
		double sin = Math.sin(thetaInd);
		double cos = Math.cos(thetaInd);
		
		double ind1Tx;
		double ind1Ty;
		double ind2Tx;
		double ind2Ty;
		
		for(int i = _M; i >= 1; --i){
			ind1 = (index-i+_n)%_n;
			ind2 = (index+i)%_n;
			
			ind1Tx = _tabPts[ind1]._tx * cos + _tabPts[ind1]._ty * -sin;
			ind1Ty = _tabPts[ind1]._tx * sin + _tabPts[ind1]._ty * cos;
			ind2Tx = _tabPts[ind2]._tx * cos + _tabPts[ind2]._ty * -sin;
			ind2Ty = _tabPts[ind2]._tx * sin + _tabPts[ind2]._ty * cos;

			ktmp += (Math.atan2(ind2Ty, ind2Tx) - Math.atan2(ind1Ty, ind1Tx)) * -computeGaussDValue(i);
		}

		_tabPts[index]._k = ktmp;
	}

	private double computeGaussDValue(int value){
		return (-value/(_SIGMA*_SIGMA*_SIGMA*Math.sqrt(2*Math.PI)))*Math.exp((-value*value)/(2*_SIGMA*_SIGMA));
	}

	public int getLength(){
		return _n;
	}


	public Point getPMin(){
		return new Point(_xm, _ym);
	}
	public Point getPMax(){
		return new Point(_xM, _yM);
	}

	public int getContourX(int i){
		return _tabPts[i]._x;
	}

	public int getContourY(int i){
		return _tabPts[i]._y;
	}
	public int getContourTX(int i){
		return _tabPts[i]._tx;
	}

	public int getContourTY(int i){
		return _tabPts[i]._ty;
	}

	public double getCurvature(int i){
		return _tabPts[i]._k;
	}
}

