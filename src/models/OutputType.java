package models;

public class OutputType
{

	public String deskripsiStr;

	public String mapJson;

	public String pmIdJson;
	
	public String GmapJson;
	
	public String JmapJson;
	
	public String gIdJson;
	
	public String jIdJson;
	
	public String oIdJson;
	
	public String OmapJson;

	public int elevasiInt;

	public String lokasiStr;

	public int jarakInt;

	public String labelStr;

	public String tipeStr;
	
	public String cuacaNama;
	
	public String cuacaFile;
	
	public String telepon;

	public OutputType()
	{
		deskripsiStr = "";
		lokasiStr = "";
		mapJson = "";
		pmIdJson = "";
		GmapJson = "";
		JmapJson = "";
		gIdJson = "";
		jIdJson = "";
		oIdJson = "";
		OmapJson = "";
		elevasiInt = 0;
		labelStr = "";
		jarakInt = 0;
		cuacaFile = "";
		cuacaNama = "";
		telepon = "";
	}

	@Override
	public boolean equals(Object obj)
	{
		// TODO Auto-generated method stub
		if (!(obj instanceof OutputType))
		{
			return false;
		}
		OutputType other = (OutputType) obj;

//		if (deskripsiStr.equals(other.deskripsiStr)
//				|| lokasiStr.equals(other.lokasiStr)
//				|| mapJson.equals(other.mapJson)
//				|| pmIdJson.equals(other.pmIdJson)
//				|| GmapJson.equals(other.GmapJson)
//				|| JmapJson.equals(other.JmapJson)
//				|| gIdJson.equals(other.gIdJson)
//				|| jIdJson.equals(other.jIdJson)
//				|| oIdJson.equals(other.oIdJson)
//				|| OmapJson.equals(other.OmapJson)
//				|| elevasiInt == other.elevasiInt
//				|| labelStr.equals(other.labelStr)
//				|| jarakInt == other.jarakInt)
//		{
//			return true;
//		}
		
//		if(labelStr.equals(other.labelStr))
//		{
//			return true;
//		}
		
		if(cuacaFile.equals(other.cuacaFile) && cuacaNama.equals(other.cuacaNama))
		{
			return true;
		}

		return false;
	}
}
