package isoco.wf4ever;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.IClusterable;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	private Label label;
	
	//private static final List<String> TYPES = Arrays.asList("Replayability", "Repeatability");

    public HomePage(final PageParameters parameters) {
    	label = new Label("version", "");
		add(label);
        // TODO Add your page's components here
		formulario();
    }
    
    public void formulario(){
    	final Input input = new Input();

        setDefaultModel(new CompoundPropertyModel<Input>(input));

        // Add a FeedbackPanel for displaying our messages
        //FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
        //add(feedbackPanel);

        // Add a form with an onSumbit implementation that sets a message
        Form<?> form = new Form("form")
        {
            @Override
            protected void onSubmit()
            {
                //info("Results for: " + input);
                showResults(input.text,input.minim,input.purpose);
            }
        };
        add(form);
        

        // add a simple text field that uses Input's 'text' property. Nothing
        // can go wrong here
        form.add(new TextField<String>("text"));
        form.add(new TextField<String>("minim"));
        form.add(new TextField<String>("purpose"));
        //form.add(new RadioChoice<String>("type", TYPES));

    }
    
    private static class Input implements IClusterable
    {
        
        /** some plain text. */
        public String text = "http://...";
        /** some plain text. */
        public String minim = "...rdf";
        /** some plain text. */
        public String purpose = "R...";
        
        /** the selected site. */
        //public String type = TYPES.get(0);
       
        
        //
        @Override
        public String toString()
        {
            return "RO uri = '" + text+ "', " + "Type: '" + purpose + "'.";
        }
        
    }
    
    private void showResults(String text, String minim, String purpose){
    	this.remove(label);
    	ServiceCaller sc=new ServiceCaller();
    	try {
    		String out="";
    		ArrayList<String> output = sc.getResult(text,minim,purpose);
    		for (String s: output){
    			out=out+s+"<br/>";
    		}
			label= (Label) new Label("version", "<h4>"+out+"</h4>").setEscapeModelStrings(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	add(label);
    }

	
	
}
