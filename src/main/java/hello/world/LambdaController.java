package hello.world;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LambdaController {
	@GetMapping("/")
	public @ResponseBody HelloWorldBean getResponse() {
		HelloWorldBean bean = new HelloWorldBean();
		bean.setHello("world");
		return bean;
	}
}
