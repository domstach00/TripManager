package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.account.AccountDto;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TripDto {
    private String id;
    private String name;
    private String description;
    private int dayLength;
    private double summaryCost;
    private LocalDate lastUpdateDate;
    private LocalTime lastUpdateTime;
    private AccountDto lastUpdateBy;
}
