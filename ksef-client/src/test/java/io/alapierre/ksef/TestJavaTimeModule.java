package io.alapierre.ksef;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Test to verify that JavaTimeModule is available and working correctly
 */
public class TestJavaTimeModule {

    @Test
    public void testJavaTimeModuleAvailable() {
        // This test verifies that JavaTimeModule class is available and can be instantiated
        JavaTimeModule module = new JavaTimeModule();
        assertNotNull("JavaTimeModule should be instantiable", module);
    }

    @Test
    public void testObjectMapperWithJavaTimeModule() throws Exception {
        // This test verifies that ObjectMapper can be configured with JavaTimeModule
        // This is the same configuration used in DefaultKsefClient
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        assertNotNull("ObjectMapper should be created successfully", mapper);
    }

    @Test
    public void testSerializeLocalDateTime() throws Exception {
        // Test that LocalDateTime can be serialized with JavaTimeModule
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        LocalDateTime now = LocalDateTime.of(2026, 1, 29, 12, 0, 0);
        String json = mapper.writeValueAsString(now);
        
        assertNotNull("Serialized JSON should not be null", json);
        assertTrue("JSON should contain date information", json.contains("2026"));
    }

    @Test
    public void testDeserializeLocalDateTime() throws Exception {
        // Test that LocalDateTime can be deserialized with JavaTimeModule
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        String json = "\"2026-01-29T12:00:00\"";
        LocalDateTime dateTime = mapper.readValue(json, LocalDateTime.class);
        
        assertNotNull("Deserialized LocalDateTime should not be null", dateTime);
        assertEquals("Year should be 2026", 2026, dateTime.getYear());
        assertEquals("Month should be January", 1, dateTime.getMonthValue());
    }
}
