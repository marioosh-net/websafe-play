import java.util.Arrays;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class OtherTests {
	Logger log = Logger.getAnonymousLogger();
	
	public static void main(String[] args) {
		new OtherTests();
	}
	
	
	public OtherTests() {
		System.out.println(Arrays.asList("asdasda,      asdsad    sa,     asdsada".split("\\s*\\,\\s*")));
		System.exit(0);
		
		String input =	" #icons a {"+
    "background: url (\"htt://ocdn.eu/images/pulscms/MjY7MDA_/c1e716db8254977f77bd6463542b02a4.png\") no-repeat scroll -7px 0;"+
    "background: url('http://ocdn.eu/images/pulscms/MjY7MDA_/c1e716db8254977f77bd6463542b02a4.png') no-repeat scroll -7px 0;"+
    "text-indent: -999px;\n"+
    "float: left;\n"+
  "}\n"+
  ".mourning #icons a {\n"+
  "  background-image: url('http://ocdn.eu/images/pulscms/YjA7MDA_/7cc6bdd54a386a99083d8e7e57372d1e.png');;;;;\n " +
  "asdasdas\n"+
  "}";

		StringBuffer sb = new StringBuffer();
		
		String regex = "(url\\s{0,}\\(\\s{0,}['\"]{0,1})([^\\)'\"]*)(['\"]{0,1}\\))";
		// String regex = "(url\\s{0,}\\(\\s{0,})([^\\)]*)(\\))";
		log.info(input.replaceAll(regex, "$1"+"URL"+"$2$3"));
		Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		Matcher m = p.matcher(input);
		while(m.find()) {
			if(!m.group(2).startsWith("http")) {
				m.appendReplacement(sb, m.group(1)+"__URL"+Matcher.quoteReplacement(m.group(2))+"URL__"+m.group(3));
			}
		}
		m.appendTail(sb);
		
		log.info(sb.toString());
		
	}
}
