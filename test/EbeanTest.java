import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.*;
import java.util.List;
import junit.framework.Assert;
import model.Message;
import model.Tag;
import org.junit.Test;
import play.Logger;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.TxRunnable;

public class EbeanTest {

    @Test
    public void test() {
        running(fakeApplication(), new Runnable() {
        	@Override
        	public void run() {
        		Long id = Ebean.createSqlQuery("select nextval('message_seq') as seq, currval('message_seq')").findUnique().getLong("seq");
        		Logger.info("next id: "+id);
        		Assert.assertEquals(true, id instanceof Long && id != null);
        		
        		/*
        		Ebean.execute(new TxRunnable() {
        			public void run() {
                		Message m = new Message("http://wp.pl", "text/html", null);
                		m.setTags("tag1,     tag2");
                		for(Tag t: m.getTags()) {
                			t.save();
                		}
                		m.save();
        			}
        		});
        		*/
        		
        		Message m = Message.find.byId(233L);
        		Logger.info(m+"");
        		
        		List<Message> l = Message.find.all();
        		for(Message m1: l) {
        			if(m1.getId() == 233) {
        				Logger.info(m1+"");
        			}
        		}
        	}
        });
    }
  
}
