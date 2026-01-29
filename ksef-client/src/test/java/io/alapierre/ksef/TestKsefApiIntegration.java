package io.alapierre.ksef;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import pl.akmf.ksef.sdk.client.model.auth.AuthenticationChallengeResponse;

import static org.junit.Assert.*;

/**
 * Integration test that verifies JavaTimeModule works with real KSeF API calls.
 * This test makes an actual HTTP request to the KSeF test API to ensure Jackson
 * dependencies are properly configured and available.
 */
public class TestKsefApiIntegration {

    private static final String KSEF_TEST_API_BASE = "https://api-test.ksef.mf.gov.pl/v2";
    private static final String AUTH_CHALLENGE_ENDPOINT = "/auth/challenge";
    
    @Test
    public void testAuthChallengeEndpoint() throws Exception {
        // Create ObjectMapper with JavaTimeModule - same configuration as DefaultKsefClient
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Create HTTP client with timeout configuration
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build();

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build()) {

            // Make GET request to auth/challenge endpoint
            String url = KSEF_TEST_API_BASE + AUTH_CHALLENGE_ENDPOINT;
            HttpGet httpGet = new HttpGet(url);
            
            System.out.println("Making request to: " + url);
            
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println("Response status code: " + statusCode);
                
                // Get response body
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("Response body: " + responseBody);
                
                // Verify response is successful (200 OK)
                assertEquals("Expected HTTP 200 OK", 200, statusCode);
                assertNotNull("Response body should not be null", responseBody);
                assertFalse("Response body should not be empty", responseBody.trim().isEmpty());
                
                // Try to deserialize the response using Jackson with JavaTimeModule
                // This verifies that JavaTimeModule is properly loaded and working
                AuthenticationChallengeResponse authResponse = 
                    objectMapper.readValue(responseBody, AuthenticationChallengeResponse.class);
                
                assertNotNull("Deserialized response should not be null", authResponse);
                assertNotNull("Challenge should not be null", authResponse.getChallenge());
                assertNotNull("Timestamp should not be null", authResponse.getTimestamp());
                
                System.out.println("Challenge received: " + authResponse.getChallenge());
                System.out.println("Timestamp: " + authResponse.getTimestamp());
                System.out.println("✓ JavaTimeModule successfully loaded and working with KSeF API");
                
            } catch (java.net.UnknownHostException | java.net.SocketTimeoutException | 
                     java.net.ConnectException e) {
                // Network errors - skip test gracefully
                System.err.println("⚠ Network error connecting to KSeF API (this is OK in CI/offline environments): " + 
                    e.getClass().getSimpleName() + ": " + e.getMessage());
                System.out.println("Test skipped due to network unavailability");
                // Don't fail the test if network is unavailable
                org.junit.Assume.assumeTrue(false);
            }
        }
    }

    @Test
    public void testObjectMapperCanDeserializeAuthChallengeResponse() throws Exception {
        // This test verifies ObjectMapper can deserialize AuthenticationChallengeResponse
        // even without network connectivity
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Sample response from KSeF API (based on expected format)
        String sampleJson = "{\"challenge\":\"20250129-CR-1234567890ABCDEF-12\",\"timestamp\":\"2025-01-29T11:23:45.123Z\"}";
        
        AuthenticationChallengeResponse response = 
            objectMapper.readValue(sampleJson, AuthenticationChallengeResponse.class);
        
        assertNotNull("Response should not be null", response);
        assertNotNull("Challenge should be deserialized", response.getChallenge());
        assertNotNull("Timestamp should be deserialized", response.getTimestamp());
        
        System.out.println("✓ ObjectMapper with JavaTimeModule correctly deserializes AuthenticationChallengeResponse");
    }
}
