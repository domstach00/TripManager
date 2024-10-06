package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.account.AccountDto;
import lombok.Data;

@Data
public class TripDto {
    private String id;
    private String name;
    private String description;
    private int dayLength;
    private double summaryCost;
    private String lastModifiedTime;
    private AccountDto lastModifiedBy;
}
