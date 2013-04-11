package controllers;

import model.Tag;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.tags;

public class Tags extends Controller {

	public static Html tagsHtml() {
		return tags.render(Tag.find.all());
	}
	
	public static Result getTags() {
		return ok(tagsHtml());
	}
}
