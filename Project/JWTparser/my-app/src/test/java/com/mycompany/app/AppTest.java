package com.mycompany.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test to validate the JWT class' methods
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testJWT() {
        String keyExample = "test";
        String encodedJWTexample = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.5mhBHqs5_DTLdINd9p5m7ZJ6XD0Xc55kIaCRY5r6HRA";
        JWT testJWT = new JWT(encodedJWTexample, keyExample);

        // Validate the JWT
        assertTrue("Failed to validate JWT", testJWT.validate());

        // Validate the Header of the token
        String expectedHeader = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        assertTrue("Failed to decode the header of JWT", expectedHeader.equals(testJWT.getHeader()));

        String expectedPayload = "{\"sub\":\"1234567890\",\"name\":\"John Doe\",\"iat\":1516239022}";
        assertTrue("Failed to decode the payload of JWT", expectedPayload.equals(testJWT.getPayload()));

        String wrongKey = "tEst";
        JWT compromisedJwt = new JWT(encodedJWTexample, wrongKey);
        assertFalse("Failed because validated invalid JWT", compromisedJwt.validate());
    }
}
