/*
Further Improvements:

1) Reduce the number of parameters took by the constructor by removing the hashing algorithm.
This for now is hardcoded, however it could be extracted by the token! "alg" field of the header
2) Exception is too generic: build/use an appropriate one 

Useful doc:
About the code: https://www.baeldung.com/java-jwt-token-decode
About the validation process: https://www.freecodecamp.org/news/how-to-sign-and-validate-json-web-tokens/#:~:text=Crypto%2FSignature%20Segment%20of%20a%20JWT&text=JWTs%20are%20signed%20so%20they,the%20key%20used%20could%20differ.

*/

package com.mycompany.app;

import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

public class JWT {
    private String decodedHeader;
    private String decodedPayload;
    private String encodedHeader;
    private String encodedPayload;
    private String signature;
    private String secretKey;

    /**
     * 
     * @param token     the encoded Json Web Token dot-separated
     * @param secretKey the key used to hash header and payload
     */
    public JWT(String token, String secretKey) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        this.encodedHeader = chunks[0];
        this.encodedPayload = chunks[1];
        this.decodedHeader = new String(decoder.decode(chunks[0]));
        this.decodedPayload = new String(decoder.decode(chunks[1]));
        this.signature = chunks[2];
        this.secretKey = secretKey;
    }

    /**
     * 
     * @return True if the Hash256 of header and payload is equal to the signature
     */
    public boolean validate() {
        String tokenWithoutSignature = this.encodedHeader + "." + this.encodedPayload;
        SignatureAlgorithm sa = SignatureAlgorithm.HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            return false;
        } else {
            return true;
        }
    }

    public String getHeader() {
        return decodedHeader;
    }

    public String getPayload() {
        return decodedPayload;
    }

    public String getSignature() {
        return signature;
    }
}
