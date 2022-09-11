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
	        System.out.println("連接成功");
	        System.out.println("服務正在運行: "+jedis.ping());
			SpringApplication.run(DemoApplication.class, args);
		}catch(Exception e) {
			System.out.print(e.toString());
		}
		
		
	}

}
