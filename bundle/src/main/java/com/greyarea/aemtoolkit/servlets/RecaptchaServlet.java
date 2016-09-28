package com.greyarea.aemtoolkit.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.ComponentContext;
import org.apache.sling.commons.osgi.PropertiesUtil;

@Component(immediate = true, metatype = true, label = "Google reCAPTCHA verification")
@Service(value=javax.servlet.Servlet.class)
@Properties({@Property(name = "sling.servlet.paths", value = "/bin/servlet/verifyRecaptcha")})
public class RecaptchaServlet extends SlingAllMethodsServlet  {
	
	private static final long serialVersionUID = -6087689053766658045L;
	
	@Property(label = "Provide Secret Key", description = "Provide secret key", value="123456")
    public static final String PROP_SECRET_KEY = "prop.secretkey";
	
	String secretKey = "";
	 
	@Activate
	protected void activate (final ComponentContext componentContext) {
		
		@SuppressWarnings("unchecked")
		final Map<String, String> properties = (Map<String, String>) componentContext.getProperties();
		secretKey = PropertiesUtil.toString(properties.get(PROP_SECRET_KEY), "");
    
	}
	@Override
	protected void doPost (SlingHttpServletRequest request, SlingHttpServletResponse response) {	
			doGet(request, response);
	}
	@Override
    protected void doGet(final SlingHttpServletRequest req,final SlingHttpServletResponse resp){
    	String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
    	resp.setContentType("application/json");    	
    	JSONObject object = new JSONObject();
    	PrintWriter out = null;
        try {
            	String status = RequestHandler.verifyResponse(gRecaptchaResponse,secretKey) ? "success" : "failure";
                out = resp.getWriter();
				object.put("status", status);
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        out.print(object.toString()); 
       
       }
}
