package engines;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.XML;

public class XmlToJsonConverter {

	public static JSONObject convert(String xml)
	{
		JSONObject jsonObject = XML.toJSONObject(xml);
		return jsonObject;
	}
	
	public static void main(String[] args) {
		String url = "https://dl.dropboxusercontent.com/u/48648026/merbabu_full.kml";
		String hasil = Koneksi.connect(url);
		System.out.println(hasil);
		System.out.println();
		JSONObject object = XmlToJsonConverter.convert(hasil);
		String json = object.toString(2);
		System.out.println(json);
		try {
			File jsonFile = new File("merbabuJson.txt");
			jsonFile.createNewFile();
			FileWriter writer = new FileWriter(jsonFile);
			writer.write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
