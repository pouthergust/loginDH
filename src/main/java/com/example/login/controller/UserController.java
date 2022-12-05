package com.example.login.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.model.UserModel;
import com.example.login.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService us;

	public UserController(IUserService us) {
		this.us = us;
	}
	
	@PostMapping
	public UserModel salvar(@RequestBody UserModel user) {
		return this.us.salvar(user);
	}
	
	@GetMapping("/all")
	public List<UserModel> buscarTodos() {
		return this.us.buscarTodos();
	}
}
