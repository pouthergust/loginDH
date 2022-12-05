package com.example.login.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.login.dao.impl.UserDAOH2;
import com.example.login.model.UserModel;
import com.example.login.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserDAOH2 userIDao;
	
	public UserServiceImpl(UserDAOH2 userIDao) {
		this.userIDao = userIDao;
	}

	@Override
	public UserModel salvar(UserModel user) {
		return userIDao.salvar(user);
	}

	@Override
	public Boolean consultar(UserModel user) {
		return userIDao.consultar(user);
	}

	@Override
	public List<UserModel> buscarTodos() {
		return userIDao.buscarTodos();
	}
}
