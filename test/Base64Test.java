import java.io.IOException;
import java.net.URL;
import org.junit.Test;
import controllers.Application;

public class Base64Test {
	
	@Test 
	public void test() throws IOException {
		System.out.println(Application.toBase64(new URL("file:///C:/users/muniek/Downloads/udana-nazwa.jpg")));
	}
}
