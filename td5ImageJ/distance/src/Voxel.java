public class Voxel {
	
	//Position du Voxel dans l'espace
	private int m_posx;
	private int m_posy;
	private int m_posz;
	
	//Distances
	private int m_dx;
	private int m_dy;
	private int m_dz;
	private int m_d2;
	
	private boolean m_infinite; //Voxel à traiter

	public Voxel(int x, int y, int z, Boolean background) {
		if (background) {
			m_dx = 0;
			m_dy = 0;
			m_dz = 0;
			m_d2 = 0;
			m_infinite = false;
		} else {
			m_dx = -1;
			m_dy = -1;
			m_dz = -1;
			m_d2 = Integer.MAX_VALUE;
			m_infinite = true;
		}
		m_posx = x;
		m_posy = y;
		m_posz = z;
	}

	public int getD2() {
		return m_d2;
	}

	public int getDx() {
		return m_dx;
	}

	public int getDy() {
		return m_dy;
	}

	public int getDz() {
		return m_dz;
	}

	/**
	 * Mise a jour des distances
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setVecD(int dx, int dy, int dz) {
		this.m_dx = dx;
		this.m_dy = dy;
		this.m_dz = dz;
		this.m_infinite = false;
	}

	/**
	 * Mise a jour du calcul de distance au carré
	 * @param d2
	 */
	public void setD2(int d2) {
		this.m_d2 = d2;
	}

	/**
	 * Doit être traité
	 * @return
	 */
	public boolean isInfinite() {
		return this.m_infinite;
	}

	
	public int getPosx() {
		return m_posx;
	}
	
	public void setPosx(int m_posx) {
		this.m_posx = m_posx;
	}

	public int getPosy() {
		return m_posy;
	}

	public void setPosy(int m_posy) {
		this.m_posy = m_posy;
	}

	public int getPosz() {
		return m_posz;
	}

	public void setPosz(int m_posz) {
		this.m_posz = m_posz;
	}
}
