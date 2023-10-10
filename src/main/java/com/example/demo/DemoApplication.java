package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class DemoApplication {
	public static Jedis jedis = new Jedis("localhost");

	public static void main(String[] args) {
		try {
			// If Redis setting password, Need use auth
			// jedis.auth("123456");
			SpringApplication.run(DemoApplication.class, args);
		} catch (Exception e) {
			System.out.print(e.toString());
		}

	}

}
