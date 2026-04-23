package com.baarsch_bytes.studentRegDemo.exception; // Update to match your package

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

// Import the MockMvc request builders and matchers statically for cleaner code
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


    // --- DUMMY COMPONENTS FOR ISOLATED TESTING ---

    @RestController
    class DummyController {
        // A fake endpoint that requires a valid DTO
        @PostMapping("/dummy-endpoint")
        public String dummyEndpoint(@Valid @RequestBody DummyDto dto) {
            return "Success!";
        }
    }

    class DummyDto {
        // A fake validation rule with a specific message
        @NotBlank(message = "This test field cannot be left blank")
        private String testField;

        public String getTestField() { return testField; }
        public void setTestField(String testField) { this.testField = testField; }
    }


// 1. Tell Spring to only load the web layer, and only for our DummyController
@WebMvcTest(controllers = DummyController.class)
// 2. Explicitly import your GlobalExceptionHandler so Spring wires it up
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void handleValidationExceptions_ReturnsBadRequestWithFieldErrorsMap() throws Exception {

        // 3. Simulate an HTTP POST request sending an empty JSON object
        // Because "testField" is missing, the @NotBlank validation will instantly fail
        mockMvc.perform(post("/dummy-endpoint")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))

                // 4. Assert that your GlobalExceptionHandler caught it and returned a 400 Bad Request
                .andExpect(status().isBadRequest())

                // 5. Assert that the JSON map returned contains the exact key and value we expect
                // jsonPath "$." means the root of the JSON object
                .andExpect(jsonPath("$.testField").value("This test field cannot be left blank"));
    }
}