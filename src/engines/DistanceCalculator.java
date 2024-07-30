package engines;

public class DistanceCalculator {
	
	static final int R = 6371;
	
	public static double getDistance(double lat1, double lon1, double lat2, double lon2)
	{
		double dLat = toRad(lat2-lat1);
		double dLon = toRad(lon2-lon1);
		double radlat1 = toRad(lat1);
		double radlat2 = toRad(lat2);

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(radlat1) * Math.cos(radlat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return R * c;
	}
	
	public static double toRad(double value)
	{
		return value * Math.PI / 180;
	}
}
