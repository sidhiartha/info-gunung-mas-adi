package engines;

public class SparqlQueryGenerator {
	
	protected String select;
	protected String where;
	protected String options;
	
	public SparqlQueryGenerator() {
		// TODO Auto-generated constructor stub
		select = "SELECT DISTINCT ";
		where = "WHERE { ";
		options = "";
	}
	
	public String getFinalQuery()
	{
		return select + where + " }" + options;
	}
}
