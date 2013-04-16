package controllers;

import java.util.List;
import java.util.concurrent.Callable;
import model.Message;
import model.Tag;
import play.api.templates.Html;
import play.libs.Akka;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.clicks;
import views.html.tags;

public class Tags extends Controller {

	public static Result getTags() {

		return async(Akka.future(new Callable<List<Tag>>() {
			@Override
			public List<Tag> call() throws Exception {
				return Tag.find.all();
			}

		}).map(new Function<List<Tag>, Result>() {

			@Override
			public Result apply(List<Tag> list) throws Throwable {
				return ok(tags.render(list));
			}
		}));
	}

	public static Html tagsHtml() {
		return Akka.future(new Callable<Html>() {
			@Override
			public Html call() throws Exception {
				return tags.render(Tag.find.all());
			}
		}).get();
	}	
}
