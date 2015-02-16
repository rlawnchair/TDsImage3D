import ij.process.FloatProcessor;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;

public class Watershed { /* member class not found */
	class Pixel {
	} /* member class not found */

	class Voisins {
	}

	public Watershed(FloatProcessor image_altitude, float epsilon) {
		system.out.println((new StringBuilder("epsilon : ")).append(epsilon)
				.toString());
		i_alt = image_altitude;
		EPSILON = epsilon;
	}

	public IntImage calculeWatershed() {
		if (i_alt == null)
			return null;
		int w = i_alt.getWidth();
		int h = i_alt.getHeight();
		i_label = new IntImage(w, h);
		i_dist = new IntImage(w, h);
		int label = 0;
		linkedlist fifo = new linkedlist();
		system.out.println("Initialisation labels et distance ...");
		system.out.println("Tri des pixels ...");
		Pixel t_pixels[] = new Pixel[w * h];
		Pixel pixel_fictif = new Pixel(-1, -1);
		int idx = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				t_pixels[idx] = new Pixel(x, y);
				t_pixels[idx].setLabel(-1);
				t_pixels[idx].setD(0);
				idx++;
			}
		}
		arrays.sort(t_pixels);
		system.out.println("Traitement de chaque pixel ...");
		for (int i = 0; i < t_pixels.length;) {
			int old_i = i;
			float altitude;
			for (altitude = t_pixels[i].altitude(); i < t_pixels.length
					&& t_pixels[i].altitude() <= altitude + EPSILON; i++) {
				Pixel p = t_pixels[i];
				p.setLabel(-3);
				for (Voisins N = new Voisins(p); N.current() != null; N.next()) {
					Pixel q = N.current();
					if (q.label() < 0 && q.label() != -2)
						continue;
					p.setD(1);
					fifo.addLast(p);
					break;
				}
			}
			system.out.println((new StringBuilder(string.valueOf(i - old_i)))
					.append(" pixels d'altitude ").append(altitude).toString());
			int d_cur = 1;
			fifo.addLast(pixel_fictif);
			do {
				Pixel p = (Pixel) fifo.removeFirst();
				if (p == pixel_fictif) {
					if (fifo.isEmpty())
						break;
					fifo.addLast(pixel_fictif);
					d_cur++;
					p = (Pixel) fifo.removeFirst();
				}
				for (Voisins N = new Voisins(p); N.current() != null; N.next()) {
					Pixel q = N.current();
					if (q.d() < d_cur && (q.label() >= 0 || q.label() == -2)) {
						if (q.label() >= 0) {
							if (p.label() == -3 || p.label() == -2)
								p.setLabel(q.label());
							else if (p.label() != q.label())
								p.setLabel(-2);
						} else if (p.label() == -3)
							p.setLabel(-2);
					} else if (q.label() == -3 && q.d() == 0) {
						q.setD(d_cur + 1);
						fifo.addLast(q);
					}
				}
			} while (true);
			for (int j = old_i; j < i; j++) {
				Pixel p = t_pixels[j];
				p.setD(0);
				if (p.label() == -3) {
					p.setLabel(label);
					fifo.addLast(p);
					while (!fifo.isEmpty()) {
						Pixel p1 = (Pixel) fifo.removeFirst();
						for (Voisins N = new Voisins(p1); N.current() != null; N
								.next()) {
							Pixel p2 = N.current();
							if (p2.label() == -3) {
								p2.setLabel(label);
								fifo.addLast(p2);
							}
						}
					}
					label++;
				}
			}
		}
		return i_label;
	}

	public static IntImage fermeWatershed(IntImage iws) {
		int w = iws.getWidth();
		int h = iws.getHeight();
		IntImage io = new IntImage(w, h);
		int nv = 4;
		int vx[] = { 1, 0, -1, 0 };
		int vy[] = { 0, -1, 0, 1 };
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int lbl = iws.getPixel(x, y);
				int nlbl = lbl;
				if (lbl >= 0) {
					for (int j = 0; j < 4; j++) {
						int px = x + vx[j];
						int py = y + vy[j];
						int vlbl = iws.getPixel(px, py);
						if (vlbl == -2 || vlbl >= lbl)
							continue;
						nlbl = -2;
						break;
					}
				} else {
					nlbl = -2;
				}
				io.setPixel(x, y, nlbl);
			}
		}
		return io;
	}

	public static IntImage ouvreWatershed(IntImage iws) {
		int w = iws.getWidth();
		int h = iws.getHeight();
		IntImage io = new IntImage(w, h);
		int nv = 4;
		int vx[] = { 1, 0, -1, 0 };
		int vy[] = { 0, -1, 0, 1 };
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int lbl = iws.getPixel(x, y);
				int nlbl = lbl;
				if (lbl == -2) {
					for (int j = 0; j < 4; j++) {
						int px = x + vx[j];
						int py = y + vy[j];
						int vlbl = iws.getPixel(px, py);
						if (vlbl <= nlbl)
							continue;
						nlbl = vlbl;
						break;
					}
				}
				io.setPixel(x, y, nlbl);
			}
		}
		return io;
	}

	public static final int MASK = -3;
	public static final int WATERSHED = -2;
	public static final int UNLABELLED = -1;
	public static final int FIRST_LABEL = 0;
	static final int nv = 4;
	static int vx[] = { 1, 0, -1, 0 };
	static int vy[] = { 0, -1, 0, 1 };
	public static float EPSILON = 5F;
	FloatProcessor i_alt;
	IntImage i_label;
	IntImage i_dist;
}
