package controllers;

import java.util.List;
import java.util.concurrent.Callable;
import model.Message;
import play.api.templates.Html;
import play.libs.Akka;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.clicks;

public class Clicks extends Controller {

	public static Result getClicks() {

		return async(Akka.future(new Callable<List<Message>>() {
			@Override
			public List<Message> call() throws Exception {
				return Message.find.orderBy("clicks desc").setMaxRows(10).findList();
			}

		}).map(new Function<List<Message>, Result>() {

			@Override
			public Result apply(List<Message> a) throws Throwable {
				// TODO Auto-generated method stub
				return ok(clicks.render(a));
			}
		}));
	}

	public static Html clicksHtml() {
		return Akka.future(new Callable<Html>() {
			@Override
			public Html call() throws Exception {
				return clicks.render(Message.find.orderBy("clicks desc").setMaxRows(10).findList());
			}
		}).get();
	}
	
}
