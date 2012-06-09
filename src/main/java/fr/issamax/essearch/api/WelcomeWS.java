package fr.issamax.essearch.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class WelcomeWS {

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	String welcome() {
		String helloMsg = "Welcome to the es-search API.<br/>Use <a href=\"scan/\">/scan</a> to scan dirs.";
		return helloMsg;
	}
}