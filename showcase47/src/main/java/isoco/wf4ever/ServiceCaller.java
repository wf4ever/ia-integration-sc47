package isoco.wf4ever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ServiceCaller {

	
	public ServiceCaller() {
		//ClientConfig config = new DefaultClientConfig();
		//Client client = Client.create(config);
		//service = client.resource(getBaseURI());
	}
	
	public ArrayList<String> getResult(String ro, String minim, String purpose) throws IOException{
		String urlBase = "http://andros.zoo.ox.ac.uk:8080/evaluate/checklist";
		//String urlBase="http://andros.zoo.ox.ac.uk:8080/evaluate/checklist?RO=http%3A%2F%2Fandros.zoo.ox.ac.uk%2Fworkspace%2Fwf4ever-ro-catalogue%2Fv0.1%2Fsimple-requirements%2F&minim=http%3A%2F%2Fandros.zoo.ox.ac.uk%2Fworkspace%2Fwf4ever-ro-catalogue%2Fv0.1%2Fsimple-requirements%2Fsimple-requirements-minim.rdf&purpose=Runnable";
		String urlRO= "?RO="+ro;
		String urlMinim= "&minim="+minim;
		String urlTarget= "";
		String urlPurpose= "&purpose="+purpose;
		String urlComplete=urlBase+urlRO+urlMinim+urlTarget+urlPurpose;
		URL url = new URL(urlComplete);

        //make connection
        URLConnection urlc = url.openConnection();
        urlc.setRequestProperty("Accept", "application/rdf+xml");
        
        //use post mode
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);

        //get result
        /*BufferedReader br = new BufferedReader(new InputStreamReader(urlc
            .getInputStream()));
        String l = null;
        while ((l=br.readLine())!=null) {
            result=result+l+"\n";
        }
        br.close();*/
        
        RdfReader reader=new RdfReader();
        ArrayList<String> output=reader.rdfRead(urlComplete);
        

		return output;
	}
	
}
