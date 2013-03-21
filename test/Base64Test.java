import java.io.IOException;
import org.junit.Test;
import controllers.Application;

public class Base64Test {
	
	@Test 
	public void test() throws IOException {
		System.out.println(Application.toBase64("file:///C:/users/muniek/Downloads/udana-nazwa.jpg"));
	}
}
