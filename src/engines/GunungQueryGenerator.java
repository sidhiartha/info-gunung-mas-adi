package engines;

public class GunungQueryGenerator extends SparqlQueryGenerator {
	
	public static final int SAME = 0;
	public static final int MORE = 1;
	public static final int LESS = 2;
	public static final int SAME_OR_MORE = 3;
	public static final int SAME_OR_LESS = 4;
	public static final int NOT_SAME = 5;
	
	public static enum Objects {
		KAWAH, JALUR, PUNCAK;
	};
	
	public static final String NAMA = "nama";
	public static final String TINGGI = "ketinggian";
	public static final String JALUR = "jalur";
	public static final String KAWAH = "kawah";
	public static final String ALAMAT = "alamat";
	public static final String PUNCAK = "puncak";
	public static final String TIPE = "tipe";
	public static final String JLH_PUNCAK = "jumlah_puncak";
	public static final String JLH_JALUR = "jumlah_jalur";
	public static final String JLH_KAWAH = "jumlah_kawah";
	public static final String LOKASI = "lokasi";
	public static final String UTARA = "utara";
	public static final String TIMUR = "timur";
	public static final String SELATAN = "selatan";
	public static final String BARAT = "barat";
	
	public GunungQueryGenerator() {
		// TODO Auto-generated constructor stub
		super();
		select += "?" + NAMA + " ";
		where += "?" + NAMA + " rdf:type mount:Gunung . ";
	}
	
	public void addAskKetinggian(int tinggi, int mode)
	{
		addAskKetinggian();
		where += "FILTER(?" + TINGGI;
		switch(mode)
		{
		case SAME:
			where += " = ";
			break;
		case MORE:
			where += " > ";
			break;
		case LESS:
			where += " < ";
			break;
		case SAME_OR_MORE:
			where += " >= ";
			break;
		case SAME_OR_LESS:
			where += " <= ";
			break;
		case NOT_SAME:
			where += " != ";
			break;
		}
		where += tinggi + ") . ";
	}
	
	public void addAskKetinggian()
	{
		if(!select.contains("?" + TINGGI))
			select += "?" + TINGGI + " ";
		
		where += "?" + NAMA + " mount:ketinggian ?" + TINGGI + " . ";
	}
	
	public void addAskJumlahObject(Objects o)
	{
		switch (o) {
		case JALUR:
			select += "(count(?" + JALUR + ") as ?" + JLH_JALUR + ") ";
			where += "?" + NAMA + " mount:hasJalurPendakian ?"+ JALUR + " . ";
			break;

		case KAWAH:
			select += "(count(?" + KAWAH + ") as ?" + JLH_KAWAH + ") ";
			where += "?" + NAMA + " mount:hasKawah ?"+ KAWAH + " . ";
			break;
			
		case PUNCAK:
			select += "(count(?" + PUNCAK + ") as ?" + JLH_PUNCAK + ") ";
			where += "?" + NAMA + " mount:hasPuncak ?"+ PUNCAK + " . ";
			break;
		}
		options += " GROUP BY ?" + NAMA + " ";
	}
	
	public void addAskLokasi()
	{
		select += "?" + LOKASI + " ?" + UTARA + " ?" + TIMUR + " ?" + SELATAN 
				+ " ?" + BARAT + " ";
		where += "?" + NAMA + " mount:LocatedIn ?" + LOKASI + " . ";
		where += "?" + NAMA + " mount:north ?" + UTARA + " . ";
		where += "?" + NAMA + " mount:east ?" + TIMUR + " . ";
		where += "?" + NAMA + " mount:south ?" + SELATAN + " . ";
		where += "?" + NAMA + " mount:west ?" + BARAT + " . ";
	}
	
	public void addAskBerada(String lokasi)
	{
		
	}
}
