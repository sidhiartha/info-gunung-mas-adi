package models;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.ResultSet;

import controller.OntoBahasaReader;

public class OntoBahasa
{

	public static enum KategoriSintaks
	{
		ADJEKTIVA, ADVERBIA, ARTIKULA, FRASA_NOMINAL, FRASA_PREPOSISIONAL,
		FRASA_VERBAL, NOMINA, NULL, NUMERALIA, PREPOSISI,
		PRONOMINA_INTEROGATIV, VERBA, KLAUSA, KALIMAT
	}

	String subject;
	String nilai;
	String shadowNilai;
	KategoriSintaks kategori;
	List<String> tipe0;
	List<KategoriSintaks> kat0;
	List<String> tipe1;
	List<KategoriSintaks> kat1;
	List<String> tipe2;
	List<KategoriSintaks> kat2;
	List<String> tipeS;
	List<KategoriSintaks> katS;
	String tipeSemantik;
	String nilaiSemantik;
	List<String> vars;

	OntoBahasaReader reader;

	public OntoBahasa(OntoBahasaReader reader)
	{
		// TODO Auto-generated constructor stub
		this.reader = reader;
	}

	public OntoBahasa(OntoBahasa other)
	{
		subject = other.subject;
		nilai = other.nilai;
		kategori = other.kategori;
		tipe0 = other.tipe0 != null ? new ArrayList<String>(other.tipe0) : null;
		kat0 =
				other.kat0 != null ? new ArrayList<KategoriSintaks>(other.kat0)
						: null;
		tipe1 = other.tipe1 != null ? new ArrayList<String>(other.tipe1) : null;
		kat1 =
				other.kat1 != null ? new ArrayList<KategoriSintaks>(other.kat1)
						: null;
		tipe2 = other.tipe2 != null ? new ArrayList<String>(other.tipe2) : null;
		kat2 =
				other.kat2 != null ? new ArrayList<KategoriSintaks>(other.kat2)
						: null;
		tipeS = other.tipeS != null ? new ArrayList<String>(other.tipeS) : null;
		katS =
				other.katS != null ? new ArrayList<KategoriSintaks>(other.katS)
						: null;
		nilaiSemantik = other.nilaiSemantik;
		tipeSemantik = other.tipeSemantik;

		vars = other.vars != null ? new ArrayList<String>(other.vars) : null;

		reader = other.reader;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getSubject()
	{
		return subject;
	}

	public String getNilai()
	{
		return nilai;
	}

	public void setNilai(String nilai)
	{
		this.nilai = nilai;
	}

	public String getShadowNilai()
	{
		return shadowNilai;
	}

	public void setShadowNilai(String shadowNilai)
	{
		this.shadowNilai = shadowNilai;
	}

	public KategoriSintaks getKategori()
	{
		return kategori;
	}

	public void setKategori(KategoriSintaks kategori)
	{
		this.kategori = kategori;
	}

	public void addTipe0(String tipe0)
	{
		if (this.tipe0 == null)
		{
			this.tipe0 = new ArrayList<String>();
		}
		this.tipe0.add(tipe0);
	}

	public void addKat0(KategoriSintaks kat0)
	{
		if (this.kat0 == null)
		{
			this.kat0 = new ArrayList<KategoriSintaks>();
		}
		this.kat0.add(kat0);
	}

	public void addTipe1(String tipe1)
	{
		if (this.tipe1 == null)
		{
			this.tipe1 = new ArrayList<String>();
		}
		this.tipe1.add(tipe1);
	}

	public void addKat1(KategoriSintaks syntaks)
	{
		if (kat1 == null)
		{
			kat1 = new ArrayList<KategoriSintaks>();
		}
		kat1.add(syntaks);
	}

	public void addTipe2(String tipe2)
	{
		if (this.tipe2 == null)
		{
			this.tipe2 = new ArrayList<String>();
		}
		this.tipe2.add(tipe2);
	}

	public void addKat2(KategoriSintaks kat2)
	{
		if (this.kat2 == null)
		{
			this.kat2 = new ArrayList<KategoriSintaks>();
		}
		this.kat2.add(kat2);
	}

	public void addTipeS(String tipeS)
	{
		if (this.tipeS == null)
		{
			this.tipeS = new ArrayList<String>();
		}
		this.tipeS.add(tipeS);
	}

	public void addKatS(KategoriSintaks katS)
	{
		if (this.katS == null)
		{
			this.katS = new ArrayList<KategoriSintaks>();
		}
		this.katS.add(katS);
	}

	public void addVars(String var)
	{
		if (vars == null)
		{
			vars = new ArrayList<String>();
		}

		if (!vars.contains(var))
		{
			vars.add(var);
		}
	}

	public void addVars(List<String> vars)
	{
		if (vars == null)
		{
			return;
		}
		for (String var : vars)
		{
			addVars(var);
		}
	}

	public List<String> getVars()
	{
		return vars;
	}

	public void setTipe0(List<String> tipe0)
	{
		if (tipe0 != null)
		{
			this.tipe0 = new ArrayList<String>(tipe0);
		} else
		{
			this.tipe0 = tipe0;
		}
	}

	public void setKat0(List<KategoriSintaks> kat0)
	{
		if (kat0 != null)
		{
			this.kat0 = new ArrayList<OntoBahasa.KategoriSintaks>(kat0);
		} else
		{
			this.kat0 = kat0;
		}
	}

	public void setTipe1(List<String> tipe1)
	{
		if (tipe1 != null)
		{
			this.tipe1 = new ArrayList<String>(tipe1);
		} else
		{
			this.tipe1 = tipe1;
		}
	}

	public void setKat1(List<KategoriSintaks> kat1)
	{
		if (kat1 != null)
		{
			this.kat1 = new ArrayList<OntoBahasa.KategoriSintaks>(kat1);
		} else
		{
			this.kat1 = kat1;
		}
	}

	public void setTipe2(List<String> tipe2)
	{
		if (tipe2 != null)
		{
			this.tipe2 = new ArrayList<String>(tipe2);
		} else
		{
			this.tipe2 = tipe2;
		}
	}

	public void setKat2(List<KategoriSintaks> kat2)
	{
		if (kat2 != null)
		{
			this.kat2 = new ArrayList<OntoBahasa.KategoriSintaks>(kat2);
		} else
		{
			this.kat2 = kat2;
		}
	}

	public void setTipeS(List<String> tipeS)
	{
		if (tipeS != null)
		{
			this.tipeS = new ArrayList<String>(tipeS);
		} else
		{
			this.tipeS = tipeS;
		}
	}

	public void setKatS(List<KategoriSintaks> katS)
	{
		if (katS != null)
		{
			katS = new ArrayList<OntoBahasa.KategoriSintaks>(katS);
		} else
		{
			this.katS = katS;
		}
	}

	public void setReader(OntoBahasaReader reader)
	{
		this.reader = reader;
	}

	public String getTipeSemantik()
	{
		return tipeSemantik;
	}

	public void setTipeSemantik(String tipeSemantik)
	{
		this.tipeSemantik = tipeSemantik;
	}

	public String getNilaiSemantik()
	{
		return nilaiSemantik;
	}

	public void setNilaiSemantik(String nilaiSemantik)
	{
		this.nilaiSemantik = nilaiSemantik;
	}

	public List<String> getTipe0()
	{
		return tipe0;
	}

	public List<KategoriSintaks> getKat0()
	{
		return kat0;
	}

	public List<String> getTipe1()
	{
		return tipe1;
	}

	public List<KategoriSintaks> getKat1()
	{
		return kat1;
	}

	public List<String> getTipe2()
	{
		return tipe2;
	}

	public List<KategoriSintaks> getKat2()
	{
		return kat2;
	}

	public List<String> getTipeS()
	{
		return tipeS;
	}

	public List<KategoriSintaks> getKatS()
	{
		return katS;
	}

	public String getFormatedVars()
	{
		String formated = "";
		if (vars != null)
		{
			for (String var : vars)
			{
				formated += var + " ";
			}
		}

		return formated;
	}

	public void queryOtherDetail()
	{
		String kategori =
				this.kategori == null ? null : this.kategori.name()
						.toLowerCase();
		String nilaiSemantik = this.nilaiSemantik;
		String query = reader.createKat0Type0Query(subject);
		ResultSet rs = reader.runRawQuery(query);
		reader.resultSetKat0Tipe0(rs, this);
		rs = null;

		query = reader.createKat1Type1Query(subject);
		//System.out.println("query kat1 " + query);
		rs = reader.runRawQuery(query);
		reader.resultSetKat1Tipe1(rs, this);
		rs = null;

		query = reader.createKat2Type2Query(subject);
		//System.out.println("query kat2 " + query);
		rs = reader.runRawQuery(query);
		reader.resultSetKat2Tipe2(rs, this);
		rs = null;

		query = reader.createKatSTypeSQuery(subject);
		//System.out.println("query katS " + query);
		rs = reader.runRawQuery(query);
		reader.resultSetKatSTipeS(rs, this);
	}

	public OntoBahasaReader getReader()
	{
		return reader;
	}

	@Override
	public OntoBahasa clone()
	{
		return new OntoBahasa(this);
	}

	@Override
	public String toString()
	{
		return "OntoBahasa [subject=" + subject + ", nilai=" + nilai
				+ ", kategori=" + kategori + ", tipe0=" + tipe0 + ", kat0="
				+ kat0 + ", tipe1=" + tipe1 + ", kat1=" + kat1 + ", tipe2="
				+ tipe2 + ", kat2=" + kat2 + ", tipeS=" + tipeS + ", katS="
				+ katS + ", tipeSemantik=" + tipeSemantik + ", nilaiSemantik="
				+ nilaiSemantik + "]\n";
	}
}
