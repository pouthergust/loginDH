package com.example.login.dao;

import java.util.List;

public interface IDao<T> {

	public T salvar(T user);
	public Boolean consultar(T user);
	public List<T> buscarTodos(); 
}
