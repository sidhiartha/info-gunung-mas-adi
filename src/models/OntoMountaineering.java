package models;

import java.util.ArrayList;
import java.util.List;

public class OntoMountaineering
{
	private String type;
	private String name;
	private String label;
	private String desk;
	private boolean hasMap;
	private List<String> maps;
//	private String pm;
	private List<String> pmId;
	
	public OntoMountaineering()
	{
		// TODO Auto-generated constructor stub
	}

	public OntoMountaineering(String type)
	{
		super();
		this.type = type;
	}

//	public void setType(String type)
//	{
//		this.type = type;
//	}
//
//	public String getType()
//	{
//		return type;
//	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	public boolean isHasMap()
	{
		return hasMap;
	}

	public void setHasMap(boolean hasMap)
	{
		this.hasMap = hasMap;
	}

	public void setDesk(String desk)
	{
		this.desk = desk;
	}

	public String getDesk()
	{
		return desk;
	}

	public void appendDesk(String nama, String desk)
	{
		if (this.desk == null)
		{
			this.desk = "";
		}
		this.desk += "<b>" + nama + "</b>: " + desk + "<br ><br >";
	}

	public void addDesk(String desk)
	{
		if (this.desk == null)
		{
			this.desk = "";
		}
		this.desk +=  desk + "<br><br>";
	}

	
	public void setMaps(List<String> maps)
	{
		this.maps = maps;
	}

	public List<String> getMaps()
	{
		return maps;
	}

	public void addMap(String map)
	{
		if (maps == null)
		{
			maps = new ArrayList<String>();
		}
		maps.add(map);
	}

	public void setpmId(List<String> pmId)
	{
		this.pmId = pmId;
	}

	public List<String> getPm()
	{
		return pmId;
	}

	public void addPm(String pm)
	{
		if (pmId == null)
		{
			pmId = new ArrayList<String>();
		}

		pmId.add(pm);
	}
	
//	public void setPm(String pm)
//	{
//		this.pm = pm;
//	}
//
//	public String getPm()
//	{
//		return pm;
//	}
	
	public boolean isEmpty()
	{
		return this.desk == null;
	}
	
	@Override
	public String toString()
	{
		return desk;
	}
}
