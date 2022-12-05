package com.example.login.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.example.login.dao.ConfiguracaoJDBC;
import com.example.login.dao.IDao;
import com.example.login.model.UserModel;

@Repository
public class UserDAOH2 implements IDao<UserModel> {
	
	private ConfiguracaoJDBC configJDBC;
	final static Logger log = LogManager.getLogger(UserDAOH2.class);
	
	public UserDAOH2(ConfiguracaoJDBC configJDBC) {
		this.configJDBC = configJDBC;
		this.createTable();
	}

	@Override
	public UserModel salvar(UserModel user) {
		log.info("Registrando um novo usuario: " + user.getUsername());
		Connection conn = configJDBC.conectarComBancoDeDados();
		PreparedStatement statement = null;
		String query = String.format(
				"INSERT INTO users(username, password)" + 
				"VALUES (?, ?)");
		
		try {
			if (this.consultar(user)) {
				statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				
				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword());
				int affected = statement.executeUpdate();
				
				if (affected == 1) {
					ResultSet keys = statement.getGeneratedKeys();
					keys.next();
					user.setId(keys.getInt(1));
				} else {
					System.err.println("No rows affected");
		            return null;
				}
				log.info("Usuario cadastrado com sucesso!");
				
				statement.close();
				conn.close();
			} else {
				log.error("Usuario já cadastrado!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public Boolean consultar(UserModel user) {
		log.info("Pesquisando pelo usuario: " + user.getUsername());
		Connection conn = configJDBC.conectarComBancoDeDados();
		Statement statement = null;
		String query = String.format(
				"SELECT * FROM users WHERE username='%s'", user.getUsername()
				);
		
		try {
			statement = (Statement) conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			if (resultSet.next()) {
				return false;
			} else {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public List<UserModel> buscarTodos() {
		log.info("Pesquisando pelos usuarios...");
		Connection conn = configJDBC.conectarComBancoDeDados();
		Statement statement = null;
		String query = String.format("SELECT * FROM users");
		
		List<UserModel> usersList = new ArrayList<>();
		
		try {
			statement = (Statement) conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				UserModel um = new UserModel(resultSet.getString(2), resultSet.getString(3));
				um.setId(resultSet.getInt(1));
				
				usersList.add(um);
			}
			
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usersList;
	}
	
	private void createTable() {
		Connection conn = configJDBC.conectarComBancoDeDados();
		Statement statement = null;
		String sqlCreateTable = """
				DROP TABLE IF EXISTS users;
				CREATE TABLE users ( 
					id INT PRIMARY KEY AUTO_INCREMENT,
					username VARCHAR(150) NOT NULL,
					password VARCHAR(150) NOT NULL
				);
				""";
		
		try {
			statement = (Statement) conn.createStatement();
			statement.execute(sqlCreateTable);
			
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Tabela criada");
	}

}
