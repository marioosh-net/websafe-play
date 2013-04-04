package controllers;

import play.libs.Comet;
import play.libs.F.Callback0;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Test extends Controller {

	public static Result test() {
		return ok(test.render());
	}

	public static Result comet() {
		Comet comet = new Comet("parent.log") {
			public void onConnected() {
				sendMessage("comet message 1");
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
				sendMessage("comet message 2");
				close();
			}
		};
		return ok(comet);
	}

	public static Result chunks() {
		Chunks<String> chunks = new StringChunks() {
			public void onReady(Chunks.Out<String> out) {
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
				out.write("log('comet message 1');");
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
				out.write("log('comet message 2');");
				out.close();
			}
		};
		return ok(chunks);
	}

}
