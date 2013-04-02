package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Message;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Util;
import org.apache.commons.io.IOUtils;
import play.Logger;
import play.data.Form;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import com.avaje.ebean.Ebean;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Form.form(Message.class), Message.find.orderBy("timestamp desc").findList()));
    }
    
    public static Result post() {
    	Form<Message> m = Form.form(Message.class);
    	Form f = m.bindFromRequest();
    	if(f.hasErrors()) {
    		ValidationError e = f.error("");
    		flash("error", e.message());
    		f.fill(new Message());
    		return badRequest(index.render(f, Message.find.all()));
    		//return badRequest(f.errors().values()+"");
    	}
    	Message l = m.bindFromRequest().get();
    	try {
			add(l);
			flash("info", "elem added");
		} catch (IOException e) {
			flash("error", "ERROR: " + e);
			e.printStackTrace();
		}
    	return redirect("/");
    }

    public static String toBase64(URL url) throws IOException {
		URLConnection c = url.openConnection();
		InputStream in = c.getInputStream();
		// return "data:"+c.getContentType()+";base64,"+org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(IOUtils.toByteArray(in));
		return "data:"+c.getContentType()+";base64,"+org.apache.commons.codec.binary.Base64.encodeBase64String(IOUtils.toByteArray(in));
		/*
		File f = File.createTempFile(UUID.randomUUID().toString(), "");
		FileOutputStream o = new FileOutputStream(f);
		IOUtils.copy(in, o);
		o.close();	
		return f;
		*/
    }
    
	private static void add(Message l) throws IOException {
		Logger.info("adding...");
		URL url = new URL(l.getUrl());
		URLConnection c = url.openConnection();
		InputStream in = c.getInputStream();
		l.setContentType(c.getContentType());		
		
		File f = File.createTempFile(UUID.randomUUID().toString(), "");
		Logger.info(f.getAbsolutePath());
		FileOutputStream o = new FileOutputStream(f);
		IOUtils.copy(in, o);
		o.close();
		
		/*
		if(c.getContentType().startsWith("text/html")) {
			List<Message> m = parse(f, l.getUrl());
		}
		*/
		
		if(c.getContentType().startsWith("text/html")) {
			String s = process(f, l.getUrl());
			l.setData(s.getBytes());
		} else {
			in = new FileInputStream(f);
			l.setData(IOUtils.toByteArray(in));
		}
		
		l.save();
		in.close();
	}
	
	// lista powiazanych plikow, plik zrodlowy bedzie musial miec zmienione odnosniki
	private static List<Message> parse(File f, String sourceUrlString) {
		return null;
	}
	
	private static String process(File f, String sourceUrlString) {
		try {
			final URL sourceUrl=new URL(sourceUrlString);
			Source source = new Source(f);
			OutputDocument outputDocument = new OutputDocument(source);
			StringBuilder sb=new StringBuilder();
			
			/**
			 * replace <link rel="stylesheet" href="<path>" .../>
			 * with <style type="text/css"> code </style>
			 */
			List linkStartTags = source.getAllStartTags(HTMLElementName.LINK);
			int j = 0;
			for (Iterator i=linkStartTags.iterator(); i.hasNext();) {
			    StartTag startTag=(StartTag)i.next();
			    Attributes attributes=startTag.getAttributes();
			    String rel=attributes.getValue("rel");
			    if (!"stylesheet".equalsIgnoreCase(rel)) continue;
			    String href=attributes.getValue("href");
			    if (href==null) continue;
			    String styleSheetContent;
			    try {
			      styleSheetContent = Util.getString(new InputStreamReader(new URL(sourceUrl,href).openStream()));
			      styleSheetContent = processCss(styleSheetContent, new URL(new URL(sourceUrlString),href));
			      // Message m = new Message();
			    } catch (Exception ex) {
			      continue; // don't convert if URL is invalid
			    }
			    sb.setLength(0);
			    sb.append("<style");
			    Attribute typeAttribute=attributes.get("type");
			    if (typeAttribute!=null) sb.append(' ').append(typeAttribute);
			    sb.append(">\n").append(styleSheetContent).append("\n</style>");
			    outputDocument.replace(startTag,sb);
			    
			    Logger.info("REPLACED css: "+href);
			    j++;
			  }
			
			Logger.info("REPLACED css count: "+j);
			
			/**
			 * replace <img src="<path>" .../>
			 * with base64 <img src="data:image/jpg;base64,..."/>
			 */
			j = 0;
			List imgStartTags = source.getAllStartTags(HTMLElementName.IMG);
			for (Iterator i=imgStartTags.iterator(); i.hasNext();) {
			    StartTag startTag=(StartTag)i.next();
			    Attributes attributes=startTag.getAttributes();
			    final String src=attributes.getValue("src");
			    if (src==null) continue;
			    
			    // Logger.info("img: "+src);
			    // toBase64(src);
			    
			    outputDocument.replace(attributes, new HashMap<String, String>(){{
			    	// put("src","http://www.copywriting.pl/wp-content/uploads/2011/09/udana-nazwa.jpg");
			    	put("src",toBase64(src.startsWith("http") ? new URL(src) : new URL(sourceUrl, src)));
			    }});
			    
			    Logger.info("REPLACED img src: "+src);
			    j++;
			}
			Logger.info("REPLACED img src count: "+j);
			
			/**
			 * replace <script src="<path>" .../>
			 * with <script> js code </script>
			 */
			List js = source.getAllStartTags(HTMLElementName.SCRIPT);
			j = 0;
			for (Iterator i=linkStartTags.iterator(); i.hasNext();) {
			    StartTag startTag=(StartTag)i.next();
			    Attributes attributes=startTag.getAttributes();
			    String src=attributes.getValue("src");
			    if (src==null) continue;
			    String jsText;
			    try {
			    	jsText = Util.getString(new InputStreamReader(new URL(sourceUrl,src).openStream()));
			    } catch (Exception ex) {
			      continue; // don't convert if URL is invalid
			    }
			    sb.setLength(0);
			    sb.append("<script");
			    Attribute typeAttribute=attributes.get("type");
			    if (typeAttribute!=null) sb.append(' ').append(typeAttribute);
			    sb.append(">\n").append(jsText).append("\n</script>");
			    outputDocument.replace(startTag,sb);
			    
			    Logger.info("REPLACED js: "+src);
			    j++;
			  }
			
			Logger.info("REPLACED js count: "+j);			
			
		    			
			return outputDocument.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * replace all url() with base64
	 * @param input
	 * @return
	 */
	private static String processCss(String input, URL baseUrl) {

		//StringBuffer sb = new StringBuffer(input.length());
		StringBuffer sb = new StringBuffer();
		
		// (url\s{0,}\(\s{0,}['"]{0,1})([^\)'"]*)(['"]{0,1}\))
		String regex = "(url\\s{0,}\\(\\s{0,}['\"]{0,1})([^\\'\")]*)(['\"]{0,1}\\))";
		// input.replaceAll(regex, "$1"+"URL"+"$2$3");
		// return input;
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(input);
		while(m.find()) {
			try {
				URL url;
				if(m.group(2).startsWith("http")) {
					url = new URL(m.group(2)); 
				} else if(m.group(2).startsWith("data:")) {
					url = null;
				} else {
					url = new URL(baseUrl, m.group(2));
				}
				if(url != null) {
					Logger.warn(m.group() + " => " + url.toString());
					try {
						String b64 = toBase64(url);
						m.appendReplacement(sb, Matcher.quoteReplacement(m.group(1)+b64+m.group(3)));
					} catch (IOException e) {
						Logger.error(e.toString());
					}
				}
			} catch (MalformedURLException e) {
				Logger.error(e.toString());
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	public static Result open(Long id) {
		Message m = Ebean.find(Message.class, id);//Message.find.byId(id);
		if(m == null) {
			return badRequest("no data");
		}
		try {
			response().setContentType(m.getContentType());
			// response().setHeader("Content-Disposition", "inline; filename=\"myfile.txt\"");
			return m.getData() != null ? ok(IOUtils.toBufferedInputStream(new ByteArrayInputStream(m.getData()))) : ok("no data");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return badRequest("no data");
	}
    
}
