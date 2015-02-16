import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.plugin.filter.*;

public class Binarize_3D_Image implements PlugInFilter {
	private ImagePlus imp;
	private int w;
	private int h;
	private int nbs;
	
	public void run(ImageProcessor ip){
		
		GenericDialog gd = new GenericDialog( "Threshold", IJ.getInstance() );
		gd.addSlider( "threshold", 0.0, 255.0, 20.0 );
		gd.showDialog();
		if ( gd.wasCanceled() ) {
			IJ.error( "PlugIn cancelled" );
			return;
		}
		
		int T = (int) gd.getNextNumber();

		for (int i=1; i<=nbs; i++){
			imp.setSlice(i);
			for (int x = 0; x < w; x++)
				for(int y = 0; y < h; y++)
					if(ip.getPixel(x, y) < T)
						ip.putPixel(x, y, 0);
					else
						ip.putPixel(x, y, 255);			
		}	
	}

	
	public int setup(String arg, ImagePlus imp){
		this.imp = imp;
		this.w = imp.getWidth();
		this.h = imp.getHeight();
	    this.nbs = imp.getStackSize();
	        
		if (arg.equals("about")){
			IJ.showMessage("Binarisation de l'image");
			return DONE;
		}
		return DOES_8G;
	}
}

