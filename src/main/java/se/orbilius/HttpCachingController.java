package se.orbilius;

import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.WebContentGenerator;

@RestController
public class HttpCachingController extends WebContentGenerator{

    private final AtomicLong counter = new AtomicLong();

	
	@RequestMapping("/hello")
	public GoodStuff hello(HttpServletResponse response) {
		response.setHeader("Cache-Control", "max-age=10");
		return new GoodStuff(counter.getAndIncrement(), "a new name");
	}

}
