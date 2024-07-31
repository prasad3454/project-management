package com.proj;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
//@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
//@EnableJpaRepositories(basePackages = "com.proj.repository")
public class ProjectManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(DataSource dataSource) {
//		return args -> {
//			try (Connection connection = dataSource.getConnection()) {
//				System.out.println("Successfully connected to the database");
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		};
//	}
}
