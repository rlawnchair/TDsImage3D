import java.awt.*;
import java.io.*;

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

        // courbure
        double _c;
    }

    private int _n;       // nb de points de contour
    private ContourPoint[] _tabPts; // tableau des points de contour
    private int _xm; // abscisse minimum
    private int _ym; // ordonnée minimum
    private int _xM; // abscisse maximum
    private int _yM; // ordonnée maximum

    Path(FreemanCode c){
        _n = c.getLength();
        _tabPts = new ContourPoint[_n];
        for(int i = 0; i<_n; i++)
            _tabPts[i] = new ContourPoint();
        computeCoordinates(c);
        computeTangents(c);
        computeCurvature(c);
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

    private void computeCurvature(FreemanCode c){
        int sigma = 3;
        int m = 3 * sigma;

        for (int i = 0; i < _n; i++){
            double txB, tyB, txF, tyF;
            double thetaB, thetaF;
            double sum = 0;
            double thetai = Math.atan2(_tabPts[i]._ty, _tabPts[i]._tx);

            for (int k = 1; k <= m; k++) {
                txB = getContourTX(circ(i - k));
                tyB = getContourTY(circ(i - k));
                txF = getContourTX(circ(i + k));
                tyF = getContourTY(circ(i + k));

                // rotation d'angle theta(i)
                txB = txB * Math.cos(thetai) + txB * Math.sin(thetai);
                tyB = tyB * -Math.sin(thetai) + tyB * Math.cos(thetai);
                txF = txF * Math.cos(thetai) + txF * Math.sin(thetai);
                tyF = tyF * -Math.sin(thetai) + tyF * Math.cos(thetai);

                thetaB = Math.atan2(tyB, txB);
                thetaF = Math.atan2(tyF, txF);

                sum += -G(k, sigma) * (thetaF - thetaB);
            }

            _tabPts[i]._c = sum;
        }
    }


    private double G(int x, int sigma){
        return (-x / (Math.pow(sigma, 3) * Math.sqrt(2 * Math.PI)))
                * Math.exp(-(Math.pow(x, 2)) / (2 * Math.pow(sigma, 2)));
    }

    private int circ(int a){
        if (a < 0)
            return a + _n;
        return a % _n;
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
}
