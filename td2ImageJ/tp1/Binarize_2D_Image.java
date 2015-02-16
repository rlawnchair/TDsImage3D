import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class Binarize_2D_Image implements PlugInFilter {
	ImagePlus imp;
    
    /**
     * Set ups this binarization filter. Process only 8 bits grey-level images.
     */
	public int setup(String arg, ImagePlus imp) {
		if ( arg == "about" ) {
			IJ.showMessage( "about Binarize_2D_Image ...", 
			"First example of an ImageJ plugin.");
			return DONE;
		}
		this.imp = imp;
		return DOES_8G;
	}
    
    public void run( ImageProcessor ip ) {
	// the user gives a threshold value 
	GenericDialog gd = new GenericDialog( "Threshold", IJ.getInstance() );
	gd.addSlider( "threshold", 0.0, 255.0, 20.0 );
	gd.showDialog();
	if ( gd.wasCanceled() ) {
	    IJ.error( "PlugIn cancelled" );
	    return;
	}
	int T = (int) gd.getNextNumber();

	// creation of the output image
	// it would also be possible to directly modify the initial image  
	ImagePlus binary = 
	    NewImage.createByteImage( "Binarization of " + imp.getTitle(),
				      ip.getWidth(), ip.getHeight(), 1, 0 );

	ImageProcessor bip = binary.getProcessor();
	for ( int y = 0; y < ip.getHeight(); ++y ) {
	    for ( int x = 0; x < ip.getWidth(); ++x )
		// stupid thresholding
		if ( ip.get( x, y ) < T )
		    bip.set( x, y, 0 );
		else
		    bip.set( x, y, 255 );
	    IJ.showProgress( y / (double) ip.getHeight() );
	}

	// displays it in a window.
	binary.show();
	// forces the redisplay.
	binary.updateAndDraw();
    }
}			      

