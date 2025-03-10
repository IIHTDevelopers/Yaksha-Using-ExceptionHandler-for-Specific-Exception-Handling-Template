package com.yaksha.assignment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yaksha.assignment.exception.GenericException;
import com.yaksha.assignment.exception.ResourceNotFoundException;
import com.yaksha.assignment.exception.ValidationException;

@RestController
public class ProductController {

	@GetMapping("/products/{id}")
	public String getProduct(@PathVariable String id) {
		if (id.equals("notfound")) {
			throw new ResourceNotFoundException("Product with ID " + id + " not found.");
		} else if (id.equals("generic")) {
			throw new GenericException("A generic error occurred.");
		} else if (id.equals("invalid")) {
			throw new ValidationException("Invalid product ID: " + id);
		}
		return "Product with ID " + id;
	}
}
