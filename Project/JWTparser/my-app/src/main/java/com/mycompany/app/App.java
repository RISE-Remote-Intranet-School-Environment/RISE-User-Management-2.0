package com.mycompany.app;

public class App {
    public static void main(String[] args) throws Exception {

        // Go to https://jwt.io/#encoded-jwt and insert a key 
        // Do not select "secret base64 encoded. The secret is here supposed to be clear"

        String keyExample = "test";
        String JWTexample = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.5mhBHqs5_DTLdINd9p5m7ZJ6XD0Xc55kIaCRY5r6HRA";

        JWT tokenTest = new JWT(JWTexample, keyExample);
        System.out.println(tokenTest.validate());
        System.out.println(tokenTest.getHeader());
        System.out.println(tokenTest.getSignature());
        System.out.println(tokenTest.getPayload());
    }

}
