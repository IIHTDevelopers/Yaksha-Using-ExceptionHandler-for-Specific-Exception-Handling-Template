package com.yaksha.assignment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MasterData {
	
	// Convert an object to a JSON string
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Disable writing dates as timestamps
			final String jsonContent = mapper.writeValueAsString(obj); // Convert object to JSON string
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle exceptions during JSON conversion
		}
	}

	// Generate a random string of the given size (not used directly in this class)
	public static String randomStringWithSize(int size) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < size; i++) {
			s.append("A");
		}
		return s.toString(); // Return the generated string
	}
}
