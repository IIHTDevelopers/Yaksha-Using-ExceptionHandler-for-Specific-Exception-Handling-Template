package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import com.yaksha.assignment.controller.ProductController;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testGetProductById_ProductNotFound() throws Exception {
		// Simulate the case where product id is "notfound" and
		// ResourceNotFoundException is thrown
		RequestBuilder requestBuilder = get("/products/notfound").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		String responseContent = mockMvc.perform(requestBuilder).andReturn().getResponse().getContentAsString();

		// Assert that the response content matches the expected error message
		yakshaAssert(currentTest(), responseContent.equals("Product with ID notfound not found.") ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testGetProductById_ValidationException() throws Exception {
		// Simulate the case where product id is "invalid" and ValidationException is
		// thrown
		RequestBuilder requestBuilder = get("/products/invalid").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		String responseContent = mockMvc.perform(requestBuilder).andReturn().getResponse().getContentAsString();

		// Assert that the response content matches the expected error message
		yakshaAssert(currentTest(), responseContent.equals("Invalid product ID: invalid") ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testGetProductById_GenericException() throws Exception {
		// Simulate the case where product id is "generic" and GenericException is
		// thrown
		RequestBuilder requestBuilder = get("/products/generic").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		String responseContent = mockMvc.perform(requestBuilder).andReturn().getResponse().getContentAsString();

		// Assert that the response content matches the expected error message
		yakshaAssert(currentTest(), responseContent.equals("A generic error occurred.") ? "true" : "false",
				businessTestFile);
	}

	@Test
	public void testGetProductById_Success() throws Exception {
		// Simulate the case where the product is retrieved successfully (ID = "10")
		RequestBuilder requestBuilder = get("/products/10").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		String responseContent = mockMvc.perform(requestBuilder).andReturn().getResponse().getContentAsString();

		// Assert that the response content matches the expected success response
		yakshaAssert(currentTest(), responseContent.equals("Product with ID 10") ? "true" : "false", businessTestFile);
	}
}
