package com.exemple.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.exemple.dao.UserRepository;


@RestController()
public class UserController
{

	  private int calculateAge(LocalDate birthdate) {
	        LocalDate currentDate = LocalDate.now();
	        return Period.between(birthdate, currentDate).getYears();
	    }
	
	  
	
	  
	@Autowired
	private UserRepository repository;
	
	

	    public UserController(UserRepository userRepository) {
	        this.repository = userRepository;
	    }
	
	
	@PostMapping("/saveUsers")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            // si l'utilisateur existe déjà
            Optional<User> existingUser = repository.findByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("L'utilisateur existe déjà.");
            }

            //  si l'utilisateur est majeur
            int age = calculateAge(user.getBirthdate());
            if (age < 18) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur doit être majeur.");
            }

            // z si le pays de résidence est la France
            if (!"France".equalsIgnoreCase(user.getCountry())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur doit résider en France.");
            }
            repository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur enregistré avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de l'enregistrement de l'utilisateur.");
        }
    }
	
	   @GetMapping("/{username}")
	    public ResponseEntity<?> getUserDetails(@PathVariable String username) {
	        try {
	            Optional<User> user = repository.findByUsername(username);

	            if (user.isPresent()) {
	                return ResponseEntity.ok(user.get());
	            } else {
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue lors de la récupération des détails de l'utilisateur.");
	        }
	    }
	
}
