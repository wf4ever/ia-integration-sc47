package isoco.wf4ever;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelCon;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.function.library.bnode;

public class RdfReader {
	
	ArrayList<String> output;
	
	public ArrayList<String> rdfRead(String url){
		output=new ArrayList<String>();
		
		
        Model model = ModelFactory.createDefaultModel();
        model.read(url);
        
        /*InputStream in=null;
		try {
			in = new ByteArrayInputStream(rdf.getBytes("UTF-8"));
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/  
        
        /*//nodes
        output.add("Show nodes:");
        NodeIterator iteraciones = model.listObjects();
        while (iteraciones.hasNext()){
        	RDFNode r = iteraciones.nextNode();
        	output.add(r.toString());
        }
        
        //statements
        output.add(" ");
        output.add("Show statements:");
        StmtIterator iter;
        Statement stmt;
        iter = model.listStatements();
        while (iter.hasNext())
            {
            stmt = iter.next();
            Property predicate;
            Resource subject;
            RDFNode obj;
            subject = stmt.getSubject();
            output.add("Subject = " + subject.getURI());
            predicate = stmt.getPredicate();
            output.add("Predicate = " +predicate.getLocalName());
            obj = stmt.getObject();
            output.add("Object = " + obj.toString());
            }
        
       */
        modelExtraction(model);
                    
		return output;   
	}
	
	private void modelExtraction(Model model){
		output.add("<enc>General information</enc><br/>");
		getHeaders(model);
		output.add("<br/><hr><enc>Checklists</enc>");
		getAchieved(model);
		output.add("<br/><hr><enc>Rules</enc>");
		getEvaluations(model);
	}

	
	private void getEvaluations(Model model) {
		
		getRules(model, "<br/><ach>Satisfied requirements:</ach>", "http://purl.org/minim/minim#satisfied");
		getRules(model, "<br/><nach>Requirements needed to achieve Minimally Satisfied Checklist (MUST):</nach>", "http://purl.org/minim/minim#missingMust");
		getRules(model, "<br/><nach>Requirements needed to achieve Nominally Satisfied Checklist (SHOULD):</nach>", "http://purl.org/minim/minim#missingShould");
		getRules(model, "<br/><nach>Requirements needed to achieve Fully Satisfied Checklist (MAY)</nach>:", "http://purl.org/minim/minim#missingMay");

	}
	
	private void getRules(Model model, String message, String property){
		Property p = model.getProperty(property);
		Property p2 = model.getProperty("http://purl.org/minim/minim#tryRequirement");
		NodeIterator n = model.listObjectsOfProperty(p);
		boolean used=false;
		while (n.hasNext()){
			if (!used){output.add(message);used=true;}
        	RDFNode r = n.nextNode();
        	NodeIterator n2=model.listObjectsOfProperty((Resource)r, p2);
        	addToOutput(n2);
        }
	}

	//worst method ever!
	private void getAchieved(Model model) {
		boolean keepOk=true;
		output.add("<br/><ach>Satisfied checklists</ach>");
		
		//Minimal
		Property p = model.getProperty("http://purl.org/minim/minim#minimallySatisfies");
		if (!model.contains(null, p)){
			output.add("NONE");
			output.add("<br/><nach>Non satisfied checklists</nach>"); keepOk=false;
		}
		output.add("Minimally Satisfied");	

		//Nominal
		p = model.getProperty("http://purl.org/minim/minim#nominallySatisfies");
		if (!model.contains(null, p)&& keepOk==true){
			output.add("<br/><anch>Non satisfied checklists</nach>"); keepOk=false;
		}
		output.add("Nominaly Satisfied");
		
		//Full
		p = model.getProperty("http://purl.org/minim/minim#fullySatisfies");
		if (!model.contains(null, p)&& keepOk==true){
			output.add("<br/><nach>Non satisfied checklists</nach>"); keepOk=false;
		}
		output.add("Fully Satisfied");
		
		//fin
		if (keepOk==true){
			output.add("<br/><nach>Non satisfied checklists</nach>");
			output.add("NONE");
		}
		
		
		
	}

	private void getHeaders(Model model) {
		output.add("<eval>Target of the evaluation:</eval>");
		Property p = model.getProperty("http://purl.org/minim/minim#testedTarget");
		NodeIterator n= model.listObjectsOfProperty(p);
		addToOutput(n);
		
		output.add("<eval>Evaluation constraint:</eval>");
		p = model.getProperty("http://purl.org/minim/minim#testedConstraint");
		n= model.listObjectsOfProperty(p);
		addToOutput(n);
		
		output.add("<eval>Checking the purpose of the RO:</eval>");
		p = model.getProperty("http://purl.org/minim/minim#testedPurpose");
		n= model.listObjectsOfProperty(p);
		addToOutput(n);
		
	}

	private void addToOutput(NodeIterator n) {
		while (n.hasNext()){
        	RDFNode r = n.nextNode();
        	output.add(r.toString());
        }
		
	}

}
