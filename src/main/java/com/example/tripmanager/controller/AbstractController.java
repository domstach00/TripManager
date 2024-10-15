package com.example.tripmanager.controller;


import java.util.Objects;

public abstract class AbstractController {

    protected void throwErrorOnValidateIdsFromUrlAndBody(String idFromUrl, String idFromBody) {
        if (!Objects.equals(idFromUrl, idFromBody)) {
            throw new IllegalArgumentException("Id in path and given body does not match");
        }
    }
}
