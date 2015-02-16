import ij.*;
import ij.process.*;
import java.awt.*;
import ij.plugin.filter.*;
import ij.gui.*;

public class Snake_Viewer implements PlugInFilter {
	private Snake snake;
    private ImagePlus visu;
    private ImageProcessor ip;
    
    public int setup(String arg, ImagePlus imp) {
    	return DOES_8G;
    }
    
    private void calculVisu(int zl) {
    	ImageProcessor ipc = ip.convertToRGB();
    	ipc.setColor(Color.red);
    	int n = snake.getNbPts();
    	for(int i = 0; i < n; i++){
    	    ipc.drawLine((int)(snake.getX(i)+0.5), (int)(snake.getY(i)+0.5),
    		       (int)(snake.getX((i+1)%n)+0.5), (int)(snake.getY((i+1)%n)+0.5));
    	}
    	ipc.setColor(Color.blue);
    	for(int i = 0; i < n; i++){
    	    ipc.fillOval((int)(snake.getX(i)-1), (int)(snake.getY(i)-1),3, 3);
    	}
    	ImageProcessor ipcz = ipc.resize(zl*ipc.getWidth());
    	visu.setProcessor(ipcz);
        }

    
    public void run( ImageProcessor ip ) {
    	this.ip = ip;
    	this.visu = NewImage.createRGBImage("Evolution", ip.getWidth(), ip.getHeight(), 1, 0);
    	
    	GenericDialog gd = new GenericDialog( "Snake parameters", IJ.getInstance() );
    	gd.addNumericField( "alpha", 0.01, 2, 4, "" );
    	gd.addNumericField( "beta", 0.5, 2, 4, "" );
    	gd.addNumericField( "gamma", 2, 2, 4, "" );
    	gd.addNumericField( "lambda", 0.005, 3, 4, "" );
    	gd.addNumericField( "nb itérations", 100, 0, 4, "" );
    	String[] ZLChoice = {"1", "2", "4", "8"};
    	gd.addChoice("zoom level", ZLChoice, "1"); 

    	
      	gd.showDialog();
    	if ( gd.wasCanceled() ) {
    		IJ.error( "PlugIn cancelled" );
    		return;
    	}
    	double alpha = (double)gd.getNextNumber();	
      	double beta = (double)gd.getNextNumber();	
      	double gamma = (double)gd.getNextNumber();	
      	double lambda = (double)gd.getNextNumber();	
      	int nb_iter = (int)gd.getNextNumber();
      	int zl = (int)Math.pow(2, gd.getNextChoiceIndex());
      	
	    snake = new Snake();
	    // initialisation du contour pour l'image sphere.png
	    snake.initRectangle( 10.0, 10.0, 90.0, 90.0, 5 );
	    snake.initParametres(ip, alpha, beta, gamma, lambda);
	    
	    // Visualisation du carré du gradient
	    FloatProcessor sg = snake.getSGImage();
	    ImagePlus sgim = NewImage.createFloatImage( "gradient", sg.getWidth(), sg.getHeight(), 1, 0 );
	    sgim.setProcessor(sg);
	    sg.resetMinAndMax();
	    sgim.show();
	    
	    calculVisu(zl);
	    visu.show();
	    
      	for (int iter = 0; iter < nb_iter; iter ++){
      		snake.iteration();
      		try{
      			Thread.sleep(50);
      		}
      		catch(InterruptedException e){}
      		
      		if (iter % 10 == 0)
      			calculVisu(zl);
      	}
 	}
}

