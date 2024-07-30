package engines;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Koneksi {
	
	public static String connect(String sUrl)
	{
		String hasil = "";
		
		try {
			URL url = new URL(sUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream in = connection.getInputStream();
			
			StringBuilder builder = new StringBuilder();
			int huruf;
			
			while((huruf = in.read()) != -1)
			{
				builder.append((char)huruf);
			}
			hasil = builder.toString();
			in.close();
				
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return hasil;
	}
}
