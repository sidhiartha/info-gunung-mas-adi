package engines;

import org.json.JSONArray;
import org.json.JSONObject;

public class CuacaChecker {

	private final String BASE_URL = "http://data.bmkg.go.id/";
	
	private final String TANGGAL = "Tanggal";
	private final String ISI = "Isi";
	private final String ROW = "Row";
	private final String KOTA = "Kota";
	private final String MULAI = "Mulai";
	private final String PUKUL_MULAI = "MulaiPukul";
	private final String SAMPAI = "Sampai";
	private final String PUKUL_SAMPAI = "SampaiPukul";
	private final String LINTANG = "Lintang";
	private final String BUJUR = "Bujur";
	private final String BALAI = "Balai";
	private final String PROPINSI = "Propinsi";
	private final String CUACA = "Cuaca";
	private final String SUHU_MIN = "SuhuMin";
	private final String SUHU_MAX = "SuhuMax";
	private final String KELEMBAPAN_MIN = "KelembapanMin";
	private final String KELEMBAPAN_MAX = "KelembapanMax";
	private final String KECEPATAN_ANGIN = "KecepatanAngin";
	private final String ARAH_ANGIN = "ArahAngin";
	
	private JSONObject cuacaJson;
	private String kota;
	
	private String tglMulai;
	private String jamMulai;
	private String tglSelesai;
	private String jamSelesai;
	
	private double lintang;
	private double bujur;
	private String balai;
	private String propinsi;
	private String cuaca;
	private double suhuMin;
	private double suhuMax;
	private double kelembapanMin;
	private double kelembapanMax;
	private double kecepatanAngin;
	private String arahAngin;
	
	public CuacaChecker() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void readData(String url, String kota)
	{
		this.kota = kota;
		System.out.println(url);
		String xml = Koneksi.connect(BASE_URL + url);
		if(xml.isEmpty() || xml.length() == 0)
		{
			xml = Koneksi.connect(BASE_URL + url);
			System.out.println("Use " + BASE_URL + url + " and got " + xml);
		}
		if(!xml.equals(""))
		{
			cuacaJson = XmlToJsonConverter.convert(xml).getJSONObject(CUACA);
		}
		
		JSONObject tanggalObject = cuacaJson.getJSONObject(TANGGAL);
		tglMulai = tanggalObject.getString(MULAI);
		jamMulai = tanggalObject.getInt(PUKUL_MULAI) + ":00";
		tglSelesai = tanggalObject.getString(SAMPAI);
		jamSelesai = tanggalObject.getInt(PUKUL_SAMPAI) + ":00";
		
		JSONObject isiObject = cuacaJson.getJSONObject(ISI);
		JSONArray rowArray = isiObject.getJSONArray(ROW);
		
		for(int i = 0; i < rowArray.length(); i++)
		{
			JSONObject object = rowArray.getJSONObject(i);
			if(object.getString(KOTA).equals(kota))
			{
				arahAngin = object.getString(ARAH_ANGIN);
				cuaca = object.getString(CUACA);
				propinsi = object.getString(PROPINSI);
				bujur = object.getDouble(BUJUR);
				lintang = object.getDouble(LINTANG);
				if(!cuaca.equals("-"))
				{
					kelembapanMax = object.getDouble(KELEMBAPAN_MAX);
					suhuMax = object.getDouble(SUHU_MAX);
					kecepatanAngin = object.getDouble(KECEPATAN_ANGIN);
					kelembapanMin = object.getDouble(KELEMBAPAN_MIN);
					suhuMin = object.getDouble(SUHU_MIN);
				}
				balai = object.getString(BALAI);
				
			}
		}
	}

	public String getKota() {
		return kota;
	}

	public String getTglMulai() {
		return tglMulai;
	}

	public String getJamMulai() {
		return jamMulai;
	}

	public String getTglSelesai() {
		return tglSelesai;
	}

	public String getJamSelesai() {
		return jamSelesai;
	}

	public double getLintang() {
		return lintang;
	}

	public double getBujur() {
		return bujur;
	}

	public String getBalai() {
		return balai;
	}

	public String getPropinsi() {
		return propinsi;
	}

	public String getCuaca() {
		return cuaca;
	}

	public double getSuhuMin() {
		return suhuMin;
	}

	public double getSuhuMax() {
		return suhuMax;
	}

	public double getKelembapanMin() {
		return kelembapanMin;
	}

	public double getKelembapanMax() {
		return kelembapanMax;
	}

	public double getKecepatanAngin() {
		return kecepatanAngin;
	}

	public String getArahAngin() {
		return arahAngin;
	}
}
