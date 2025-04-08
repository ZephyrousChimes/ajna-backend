package org.dexteradei.whitegold;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class WhitegoldApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(WhitegoldApplication.class, args);
	}

}
