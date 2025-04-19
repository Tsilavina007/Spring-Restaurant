package hei.spring.todo.dao;

import hei.spring.todo.service.exception.ServerException;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class CustomDataSource {
	private final String url = System.getenv("DATASOURCE_URL");
	private final String user = System.getenv("DATASOURCE_USER");
	private final String password = System.getenv("DATASOURCE_PASSWORD");
	private final String jdbcUrl;

	public CustomDataSource() {
		jdbcUrl = url;
	}
	// public CustomDataSource() {
	// 	jdbcUrl = "jdbc:postgresql://" + host + ":" + defaultPort + "/" + database;
	// }
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(jdbcUrl, user, password);
		} catch (SQLException e) {
			throw new ServerException(e);
		}

	}
}
