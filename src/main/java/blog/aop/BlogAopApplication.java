package blog.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BlogAopApplication {


	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(BlogAopApplication.class, args);
		SuperService s = ctx.getBean(SuperService.class);
		s.complexOperation(new ComplexObject());
	}
}
