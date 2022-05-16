package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.expression.spel.ast.QualifiedIdentifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate){
	return args -> {
		Address address = new Address(
				"United States",
				"PA",
				"19301"
		);
		String email="sjs7477@psu.edu";
	Student student = new Student(
			"Shreyas",
			"Sambasivam",
			email,
			Gender.Male,
			address,
			List.of("Computer Science","Mathematics"),
			BigDecimal.TEN,
			LocalDateTime.now()
	);

		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(email));
		List<Student> students= mongoTemplate.find(query,Student.class);
		if(students.size() > 1){
			throw new IllegalStateException("Found many students with same email "+email);
		}
		if(students.isEmpty()){
			System.out.println("Inserting student "+student);
			repository.insert(student);
		}
		else{
			System.out.println("Student already exists");
		}
	};
	}

}
