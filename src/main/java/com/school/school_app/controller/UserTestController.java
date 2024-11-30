package com.school.school_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/method")
public class UserTestController {
	
	@GetMapping("/public")
	public ResponseEntity<String> userPublic(){
		return ResponseEntity.ok("Hola desde una url publica");
	}
	@GetMapping("/public-secured")
	public ResponseEntity<String> userSecured(){
		return ResponseEntity.ok("GET Hola desde una url asegurada. Debes tener permisos!");
	}
	
	@GetMapping("/public-create")
	public ResponseEntity<String> userSecuredCreate(){
		return ResponseEntity.ok("Hola desde una url asegurada. Debes tener permisos de CREATE!");
	}

}
