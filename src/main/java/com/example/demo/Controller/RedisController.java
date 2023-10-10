package com.example.demo.Controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.DemoApplication;
import com.example.demo.Model.RedisModel;

@RestController
@RequestMapping("/api")
public class RedisController {
	Map<String, Object> map = new HashMap<>();

	@GetMapping("/{key}")
	public Map<String, Object> getOneData(@PathVariable("key") String key) {
		map.clear();
		if (DemoApplication.jedis.get(key) == null) {
			map.put("msg", "no data");
			return map;
		} else {
			map.put(key, DemoApplication.jedis.get(key));
			return map;
		}
	}

	@GetMapping("/all")
	public Map<String, Object> getAllData(@RequestParam Map<String, String> allParams) {
		Set<String> keys = DemoApplication.jedis.keys("*");
		Iterator<String> k = keys.iterator();
		while (k.hasNext()) {
			String key = k.next();
			map.put(key, DemoApplication.jedis.get(key).replaceAll("\r\n", ""));
		}
		return map;
	}

	@PostMapping("/data")
	public ResponseEntity<Map<String, Object>> createRedisData(@RequestBody RedisModel request) {
		map.clear();
		DemoApplication.jedis.set(request.getKey(), request.getValue());
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(request.getKey())
				.toUri();
		map.put("msg", "success");
		map.put(request.getKey(), request.getValue());
		return ResponseEntity.created(location).body(map);
	}

	@PutMapping("/{key}")
	public Map<String, Object> updateRedisData(
			@PathVariable("key") String key, @RequestBody RedisModel request) {
		map.clear();
		if (DemoApplication.jedis.get(key) == null) {
			map.put("msg", "no data");
			return map;
		} else {
			DemoApplication.jedis.set(key, request.getValue());
			map.put("msg", "success update");
			map.put(key, DemoApplication.jedis.get(key));
			return map;
		}
	}

	@DeleteMapping("/{key}")
	public Map<String, Object> deleteProduct(@PathVariable("key") String key) {
		map.clear();
		if (DemoApplication.jedis.get(key) == null) {
			map.put("msg", "no data");
			return map;
		} else {
			DemoApplication.jedis.del(key);
			map.put("msg", "success del");
			return map;
		}
	}
}
