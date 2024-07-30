package controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;
import models.OntoMountaineering;
import models.OutputType;

//import com.hp.hpl.jena.graph.Node;
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

public class MountaineeringReader {

    final String SOURCE = "/WEB-INF/ontologi/mountainering_new.owl";

    final String FORMAT = "RDF/XML";
    final String MOUNT = "http://www.semanticweb.org/ontologies/mountaineering#";
    final String PREFIX_QUERY = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX owl: <http://www.w3.org/2002/07/owl#> "
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
            + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX mount: <" + MOUNT + "> ";
    final String SUBJECT = "?subject";
    List<String> selected;

    OntModel model1;

    public MountaineeringReader(ServletContext context) {
        InputStream is = context.getResourceAsStream(SOURCE);

        model1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

        model1.read(is, FORMAT);
    }

    public ResultSet runRawQuery(String queryString) {
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, model1);
        ResultSet results = qe.execSelect();
        return results;
    }

    //query untuk key word
    public String getQueryDetailMount(String nama) {
//		query = PREFIX_QUERY + " SELECT DISTINCT * WHERE { " + query + " }";
        String query = " {?VAR1 rdfs:label ?LB1 . FILTER(regex(?LB1, \""
                + nama + "\", \"i\")) } . "
//				+ " ?VAR1 mount:label ?label ."
//				+ " ?VAR1 rdf:type ?tipe . filter(!strstarts(str(?tipe),str(owl:))) ."
                + " OPTIONAL {?VAR1 mount:description ?desk } ."
                + " OPTIONAL {?VAR1 mount:mapFile ?map } ."
                + " OPTIONAL {?VAR1 mount:pmId ?pmId } .";
        //System.out.println("query detail mount = " + query);
        return query;
    }

    public List<String> resultSetToString(ResultSet rs) {
        List<String> strings = new ArrayList<String>();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            strings.add(qs.get("nama").asNode().getLiteralValue().toString());
        }
        return strings;
    }

    public List<String> getAllKata() {
        String query = PREFIX_QUERY + "SELECT DISTINCT ?nama "
                + "WHERE { ?subject mount:name ?nama . }";
        ResultSet rs = runRawQuery(query);
        return resultSetToString(rs);
    }

    public List<OutputType> queryFinalAnswer(String query) {
        query = PREFIX_QUERY + " SELECT DISTINCT ?label ?desk ?lokasi ?map ?pmId ?gmap ?jmap ?omap ?gid ?jid ?oid ?elevasi ?jarak ?cuacaFile ?cuacaNama ?telepon WHERE { " + query + " }";
        System.out.println("final QUERY = " + query);
        ResultSet rs = runRawQuery(query);
        String label = "";
        String tipe = "";
        String desk = "";
        String lokasi = "";
        String map = "";
        String pmId = "";
        String gmap = "";
        String jmap = "";
        String omap = "";
        String gid = "";
        String jid = "";
        String oid = "";
        int elevasi = 0;
        int jarak = 0;
        String telepon = "";

        List<OutputType> listItemHasil = new ArrayList<OutputType>();

        while (rs.hasNext()) {
            QuerySolution qs = rs.nextSolution();
            label = "";
            tipe = "";
            desk = "";
            lokasi = "";
            map = "";
            pmId = "";
            gmap = "";
            jmap = "";
            omap = "";
            gid = "";
            jid = "";
            oid = "";
            elevasi = 0;
            jarak = 0;
            telepon = "";

            OutputType outpuType = new OutputType();

            if (qs.getLiteral("cuacaNama") != null) {
                String cuacaNama = qs.getLiteral("cuacaNama").getString();
                //System.out.println("Hasil cuaca nama : " + cuacaNama);
                outpuType.cuacaNama = cuacaNama;
            }

            if (qs.getLiteral("cuacaFile") != null) {
                String cuacaFile = qs.getLiteral("cuacaFile").getString();
                //System.out.println("Hasil cuaca file : " + cuacaFile);
                outpuType.cuacaFile = cuacaFile;
            }

            if (qs.getLiteral("label") != null) {
                label = qs.getLiteral("label").getString();
                //System.out.println("Hasil label : " + label);
                outpuType.labelStr = label;
            }

            if (qs.getLiteral("desk") != null) {
                desk = qs.getLiteral("desk").getString();
                //System.out.println("Hasil desk : " + desk);
                outpuType.deskripsiStr = desk;
            }

            if (qs.getLiteral("lokasi") != null) {
                lokasi = qs.getLiteral("lokasi").getString();
                //System.out.println("Hasil lokasi : " + lokasi);
                outpuType.lokasiStr = lokasi;
            }

            if (qs.getLiteral("elevasi") != null) {
                elevasi = qs.getLiteral("elevasi").getInt();
                //System.out.println("Hasil elevasi : " + elevasi);
                outpuType.elevasiInt = elevasi;
            }

            if (qs.getLiteral("jarak") != null) {
                jarak = qs.getLiteral("jarak").getInt();
                //System.out.println("Hasil jarak : " + jarak);
                outpuType.jarakInt = jarak;
            }

            if (qs.getLiteral("telepon") != null) {
                telepon = qs.getLiteral("telepon").getString();
                //System.out.println("Hasil telepon : " + telepon);
                outpuType.telepon = telepon;
            }

            //handling output map dan pm
            if (qs.getLiteral("omap") != null) {
                omap = qs.getLiteral("omap").getString();
                //System.out.println("Hasil omap : " + omap);
                outpuType.OmapJson = omap;
            } else if (qs.getLiteral("omap") == null && qs.getLiteral("jmap") != null) {
                jmap = qs.getLiteral("jmap").getString();
                //System.out.println("Hasil jmap : " + jmap);
                outpuType.JmapJson = jmap;
            } else if (qs.getLiteral("jmap") == null && qs.getLiteral("gmap") != null) {
                gmap = qs.getLiteral("gmap").getString();
                //System.out.println("Hasil gmap : " + gmap);
                outpuType.GmapJson = gmap;
            } else if (qs.getLiteral("gmap") == null && qs.getLiteral("map") != null) {
                map = qs.getLiteral("map").getString();
                //System.out.println("Hasil map : " + map);
                outpuType.mapJson = map;
            }
            if (qs.getLiteral("oid") != null) {
                oid = qs.getLiteral("oid").getString();
                //System.out.println("Hasil oId : " + oid);
                outpuType.oIdJson = oid;
            } else if (qs.getLiteral("oid") == null && qs.getLiteral("jid") != null) {
                jid = qs.getLiteral("jid").getString();
                //System.out.println("Hasil jId : " + jid);
                outpuType.jIdJson = jid;
            } else if (qs.getLiteral("jid") == null && qs.getLiteral("gid") != null) {
                gid = qs.getLiteral("gid").getString();
                //System.out.println("Hasil gId : " + gid);
                outpuType.gIdJson = gid;
            } else if (qs.getLiteral("gid") == null && qs.getLiteral("pmId") != null) {
                pmId = qs.getLiteral("pmId").getString();
                //System.out.println("Hasil pmId : " + pmId);
                outpuType.pmIdJson = pmId;
            }

//			if (!listItemHasil.contains(outpuType))
            {
                listItemHasil.add(outpuType);
            }
        }
        return listItemHasil;
    }

}
