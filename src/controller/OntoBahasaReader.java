package controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;
import models.OntoBahasa;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import java.io.File;

public class OntoBahasaReader {

    final static String SOURCE = "/WEB-INF/ontologi/bahasa.owl";
    final static String FORMAT = "RDF/XML";
    final static String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    final static String OWL = "http://www.w3.org/2002/07/owl#";
    final static String XSD = "http://www.w3.org/2001/XMLSchema#";
    final static String RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    final static String BHS = "http://www.semanticweb.org/ontologies/bahasa_v1#";
    final static String PREFIX_QUERY = "PREFIX rdf: <" + RDF + "> "
            + "PREFIX owl: <" + OWL + "> " + "PREFIX xsd: <" + XSD + "> "
            + "PREFIX rdfs: <" + RDFS + "> " + "PREFIX bhs: <" + BHS + "> ";

    final String SUBJECT = "subject";
    final String NILAI = "word";
    final String KATEGORI = "category";
    final String TIPE_0 = "argType0";
    final String KAT_0 = "argCat0";
    final String TIPE_1 = "argType1";
    final String KAT_1 = "argCat1";
    final String TIPE_2 = "argType2";
    final String KAT_2 = "argCat2";
    final String TIPE_S = "stripType";
    final String KAT_S = "stripKat";
    final String TIPE_SEMANTIK = "type";
    final String NILAI_SEMANTIK = "semanticValue";
    final String QPART = "qpart";
    final String LABEL = "label";

    OntModel model1;

    public OntoBahasaReader(ServletContext context) {
        System.out.println("server context " + context);
        InputStream is = context.getResourceAsStream(SOURCE);

        System.out.println("input stream " + is);

        model1 = ModelFactory
                .createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        model1.read(is, FORMAT);
    }

    public String createFullQueryTest(String pertanyaan) {
        String query =
                PREFIX_QUERY + " SELECT " + " WHERE {"
                        + " ?" + SUBJECT + " bhs:" + NILAI + " ?" + NILAI + " ."
                        + " ?" + SUBJECT + " bhs:" + KATEGORI + " ?" + KATEGORI + " ."
                        + " ?" + SUBJECT + " bhs:" + NILAI_SEMANTIK + " ?" + NILAI_SEMANTIK + " ."
                        + " ?" + SUBJECT + " bhs:" + TIPE_SEMANTIK + " ?" + TIPE_SEMANTIK + " ."
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + TIPE_0 + " ?" + TIPE_0 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + KAT_0 + " ?" + KAT_0 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + TIPE_1 + " ?" + TIPE_1 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + KAT_1 + " ?" + KAT_1 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + TIPE_2 + " ?" + TIPE_2 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + KAT_2 + " ?" + KAT_2 + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + TIPE_S + " ?" + TIPE_S + " ." + " } "
                        + "OPTIONAL " + "{" + " ?" + SUBJECT + " bhs:" + KAT_S + " ?" + KAT_S + " ." + " } "
                        + " FILTER ( ?" + NILAI + " = \"" + pertanyaan + "\"^^xsd:string) ." + " }";
        return query;
    }

    public String createFullQuery(String pertanyaan) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT * " + " WHERE { ?" + SUBJECT
                        + " bhs:" + NILAI + " ?" + NILAI + " ." + " ?"
                        + SUBJECT + " bhs:" + KATEGORI + " ?" + KATEGORI + " ."
                        + " ?" + SUBJECT + " bhs:" + NILAI_SEMANTIK + " ?"
                        + NILAI_SEMANTIK + " ." + " ?" + SUBJECT + " bhs:"
                        + TIPE_SEMANTIK + " ?" + TIPE_SEMANTIK + " ." + " ?"
                        + SUBJECT + " bhs:" + TIPE_0 + " ?" + TIPE_0 + " ."
                        + " ?" + SUBJECT + " bhs:" + KAT_0 + " ?" + KAT_0
                        + " ." + " ?" + SUBJECT + " bhs:" + TIPE_1 + " ?"
                        + TIPE_1 + " ." + " ?" + SUBJECT + " bhs:" + KAT_1
                        + " ?" + KAT_1 + " ." + " ?" + SUBJECT + " bhs:"
                        + TIPE_2 + " ?" + TIPE_2 + " ." + " ?" + SUBJECT
                        + " bhs:" + KAT_2 + " ?" + KAT_2 + " ." + " ?"
                        + SUBJECT + " bhs:" + TIPE_S + " ?" + TIPE_S + " ."
                        + " ?" + SUBJECT + " bhs:" + KAT_S + " ?" + KAT_S
                        + " ." + " FILTER ( ?" + NILAI + " = \"" + pertanyaan
                        + "\"^^xsd:string) ." + " }";
//						+ " ." + " FILTER(regex(?NILAI, \"" + pertanyaan + "\", \"i\")) . " + "}";
        return query;
    }

    //	FILTER(regex(?LB1, \"" + nama + "\", \"i\")) } . "
    public String createSimpleQuery(String pertanyaan) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + NILAI
                        + " ?" + KATEGORI + " ?" + NILAI_SEMANTIK + " ?"
                        + TIPE_SEMANTIK + " WHERE { ?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + KATEGORI + " ?" + KATEGORI + " ." + " ?"
                        + SUBJECT + " bhs:" + NILAI_SEMANTIK + " ?"
                        + NILAI_SEMANTIK + " ." + " ?" + SUBJECT + " bhs:"
                        + TIPE_SEMANTIK + " ?" + TIPE_SEMANTIK + " ."
                        + " FILTER ( ?" + NILAI + "= \"" + pertanyaan
                        + "\"^^xsd:string) ." + " }";
//		+ " FILTER (regex(?" + NILAI + ", \"" + pertanyaan + "\",\"i\" )) ." //diubah ke regex untuk handling hurup besar/kecil 26-4-2015
//		+ " }";
        return query;
    }

    public String createKat0Type0Query(String subject) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + TIPE_0
                        + " ?" + KAT_0 + " WHERE { " + "?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + TIPE_0 + " ?" + TIPE_0 + " ." + " ?"
                        + SUBJECT + " bhs:" + KAT_0 + " ?" + KAT_0 + " ."
                        + " ?" + SUBJECT + " rdfs:" + LABEL + " ?" + LABEL
                        + " ." + " FILTER ( ?" + LABEL + " =\"" + subject
                        + "\"^^xsd:string) ." + " }";
        return query;
    }

    public String createKat1Type1Query(String subject) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + TIPE_1
                        + " ?" + KAT_1 + " WHERE { ?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + TIPE_1 + " ?" + TIPE_1 + " ." + " ?"
                        + SUBJECT + " bhs:" + KAT_1 + " ?" + KAT_1 + " ."
                        + " ?" + SUBJECT + " rdfs:" + LABEL + " ?" + LABEL
                        + " ." + " FILTER ( ?" + LABEL + " =\"" + subject
                        + "\"^^xsd:string) ." + " }";
        return query;
    }

    public String createKat2Type2Query(String subject) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + TIPE_2
                        + " ?" + KAT_2 + " WHERE { ?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + TIPE_2 + " ?" + TIPE_2 + " ." + " ?"
                        + SUBJECT + " bhs:" + KAT_2 + " ?" + KAT_2 + " ."
                        + " ?" + SUBJECT + " rdfs:" + LABEL + " ?" + LABEL
                        + " ." + " FILTER (?" + LABEL + " =\"" + subject
                        + "\"^^xsd:string) ." + " }";
        return query;
    }

    public String createKatSTypeSQuery(String subject) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + TIPE_S
                        + " ?" + KAT_S + " WHERE { ?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + TIPE_S + " ?" + TIPE_S + " ." + " ?"
                        + SUBJECT + " bhs:" + KAT_S + " ?" + KAT_S + " ."
                        + " ?" + SUBJECT + " rdfs:" + LABEL + " ?" + LABEL
                        + " ." + " FILTER (?" + LABEL + " = \"" + subject
                        + "\"^^xsd:string) ." + " }";
        return query;
    }

    public String createQueryLogicQueryOnParA(String nilaiSemantik) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + QPART
                        + " WHERE { ?"
                        + SUBJECT + " bhs:" + QPART + "?" + QPART + " ."
                        + " ?" + SUBJECT + " bhs:parA bhs:" + nilaiSemantik + " ." + " }";
        return query;
    }

    public String createQueryTemplate(String label) {
        String query =
                PREFIX_QUERY + " SELECT DISTINCT ?" + SUBJECT + " ?" + NILAI
                        + " ?" + KATEGORI + " ?" + NILAI_SEMANTIK + " ?"
                        + TIPE_SEMANTIK + " WHERE { ?" + SUBJECT + " bhs:"
                        + NILAI + " ?" + NILAI + " ." + " ?" + SUBJECT
                        + " bhs:" + KATEGORI + " ?" + KATEGORI + " ." + " ?"
                        + SUBJECT + " bhs:" + NILAI_SEMANTIK + " ?"
                        + NILAI_SEMANTIK + " ." + " ?" + SUBJECT + " bhs:"
                        + TIPE_SEMANTIK + " ?" + TIPE_SEMANTIK + " ." + " ?"
                        + SUBJECT + " rdfs:" + LABEL + "?" + LABEL + " ."
                        + " FILTER (regex(?" + LABEL + ", \"" + label + "\",\"i\" )) ."
                        + " }";
        return query;
    }

    public String getQueryLogic(OntoBahasa bahasa) {
        String hasil = "";
        ResultSet rs =
                runRawQuery(createQueryLogicQueryOnParA(bahasa
                        .getNilaiSemantik()));

        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            String q = qs.get(QPART).asNode().getLiteralValue().toString();
            if (q.contains("{NAME}")) {
                String shadow = bahasa.getShadowNilai();
                shadow =
                        ("" + shadow.charAt(0)).toUpperCase()
                                + shadow.substring(1);
                q = q.replace("{NAME}", shadow);
            }
            hasil += q + " ";
        }

        return hasil;
    }

    public String getQueryLogic(List<List<OntoBahasa>> bahasas,
                                List<String> nilaiSemantiks) {
        String hasil = "";

        for (String nilaiSemantik : nilaiSemantiks) {
            ResultSet rs =
                    runRawQuery(createQueryLogicQueryOnParA(nilaiSemantik));

            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                String q = qs.get(QPART).asNode().getLiteralValue().toString();
                if (q.contains("{NAME}")) {
                    q = replaceQuery(bahasas, nilaiSemantik, q);
                }

                hasil += q;
            }
        }
        // System.out.println("hasil = " + hasil);
        return hasil;
    }

    public String replaceQuery(List<List<OntoBahasa>> bahasas,
                               String nilaiSemantik, String query) {
        for (List<OntoBahasa> bhsas : bahasas) {
            for (OntoBahasa bahasa : bhsas) {
                if (bahasa.getNilaiSemantik().contains(nilaiSemantik)) {
                    String shadow = bahasa.getShadowNilai();
                    shadow =
                            ("" + shadow.charAt(0)).toUpperCase()
                                    + shadow.substring(1);
                    return query.replace("{NAME}", shadow);
                }
            }
        }
        return query;
    }

    public ResultSet runRawQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model1);
        ResultSet results = qe.execSelect();

        return results;
    }

    public List<OntoBahasa> simpleResultSetToListBahasa(ResultSet rs) {
        List<OntoBahasa> bahasas = new ArrayList<OntoBahasa>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            OntoBahasa ob = new OntoBahasa(this);
            ob.setSubject(qs.get(SUBJECT).asNode().getLocalName());
            ob.setNilai(qs.get(NILAI).asNode().getLiteralValue().toString());
            ob.setNilaiSemantik(qs.get(NILAI_SEMANTIK).asNode().getLocalName());
            ob.setTipeSemantik(qs.get(TIPE_SEMANTIK).asNode().getLocalName());

            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                if (qs.get(KATEGORI).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.setKategori(syntaks);
                    break;
                }
            }
            bahasas.add(ob);
            //System.out.println(ob);
        }

        return bahasas;
    }

    public List<OntoBahasa> fullResultSetToListBahasa(ResultSet rs) {
        List<OntoBahasa> bahasas = new ArrayList<OntoBahasa>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            OntoBahasa ob = new OntoBahasa(this);
            ob.setSubject(qs.get(SUBJECT).asNode().getLocalName());
            ob.setNilai(qs.get(NILAI).asNode().getLiteralValue().toString());
            ob.setNilaiSemantik(qs.get(NILAI_SEMANTIK).asNode().getLocalName());
            ob.setTipeSemantik(qs.get(TIPE_SEMANTIK).asNode().getLocalName());
            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                if (qs.get(KATEGORI).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.setKategori(syntaks);
                    break;
                }
            }
            bahasas.add(ob);
            //System.out.println(ob);
        }
        return bahasas;
    }

    public List<String> resultSetToString(ResultSet rs) {
        List<String> strings = new ArrayList<String>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            strings.add(qs.get(NILAI).asNode().getLiteralValue().toString());
        }

        return strings;
    }

    public void resultSetKat0Tipe0(ResultSet rs, OntoBahasa ob) {
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();

            if (!qs.get(SUBJECT).asNode().getLocalName()
                    .equals(ob.getSubject())) {
                return;
            }
            ob.addTipe0(qs.get(TIPE_0).asNode().getLocalName());

            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                if (qs.get(KAT_0).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.addKat0(syntaks);
                    break;
                }
            }
            // System.out.println(ob);
        }
    }

    public void resultSetKat1Tipe1(ResultSet rs, OntoBahasa ob) {
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            if (!qs.get(SUBJECT).asNode().getLocalName()
                    .equals(ob.getSubject())) {
                return;
            }
            ob.addTipe1(qs.get(TIPE_1).asNode().getLocalName());

            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                if (qs.get(KAT_1).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.addKat1(syntaks);
                    break;
                }
            }
            // System.out.println(ob);
        }
    }

    public void resultSetKat2Tipe2(ResultSet rs, OntoBahasa ob) {
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            if (!qs.get(SUBJECT).asNode().getLocalName()
                    .equals(ob.getSubject())) {
                return;
            }
            ob.addTipe2(qs.get(TIPE_2).asNode().getLocalName());

            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                if (qs.get(KAT_2).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.addKat2(syntaks);
                    break;
                }
            }
            // System.out.println(ob);
        }
    }

    public void resultSetKatSTipeS(ResultSet rs, OntoBahasa ob) {
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            if (!qs.get(SUBJECT).asNode().getLocalName()
                    .equals(ob.getSubject())) {
                return;
            }
            ob.addTipeS(qs.get(TIPE_S).asNode().getLocalName());

            for (OntoBahasa.KategoriSintaks syntaks : OntoBahasa.KategoriSintaks
                    .values()) {
                // System.out.println("\t" +
                // qs.get(KAT_S).asNode().getLocalName()
                // + "\n\t" + syntaks.name());
                if (qs.get(KAT_S).asNode().getLocalName()
                        .equalsIgnoreCase(syntaks.name())) {
                    ob.addKatS(syntaks);
                    break;
                }
            }

            // System.out.println("arg strip = " + ob);
        }
    }

    public String convertResultSetToString(ResultSet rs) {
        return ResultSetFormatter.asText(rs);
    }

    public List<String> getAllKata() {
        String query = PREFIX_QUERY + " SELECT DISTINCT ?" + NILAI
                + " WHERE { ?" + SUBJECT + " bhs:word ?" + NILAI + " ." + " }";
        ResultSet rs = runRawQuery(query);
        return resultSetToString(rs);
    }

    public List<OntoBahasa> queryTemplate(String kata) {
        // TODO Auto-generated method stub
        String query = createQueryTemplate("Template");
        //System.out.println("query template bahasa= " + query + "\n");
        ResultSet rs = runRawQuery(query);
        List<OntoBahasa> bahasas = simpleResultSetToListBahasa(rs);
        for (OntoBahasa bahasa : bahasas) {
            bahasa.setShadowNilai(kata);
        }
        //System.out.println("shadow nilai "+bahasas);
        return bahasas;
    }

}
