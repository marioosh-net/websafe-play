package controllers;

import model.Message;
import model.Tag;
import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.clicks;

public class Clicks extends Controller {

	public static Html clicksHtml() {
		return clicks.render(Message.find.orderBy("clicks desc").setMaxRows(10).findList());
	}
	
	public static Result getClicks() {
		return ok(clicksHtml());
	}
}
