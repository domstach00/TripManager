package com.example.tripmanager.shared.token.model.token;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Token.COLLECTION_NAME)
public class PasswordResetToken extends Token {

}
