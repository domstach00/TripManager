package com.example.tripmanager.model;

import com.example.tripmanager.model.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Trip {
    @Id
    private String id;
    private String name;
    private String description;
    private int dayLength;
    private LocalDate lastUpdateDate;
    private LocalTime lastUpdateTime;
    @DocumentReference
    private User lastUpdateBy;

    @DocumentReference
    private User createdBy;
    private LocalDate createdDate;
    private LocalTime createdTime;

    private boolean isPublic;
    private boolean isClosed;

    @DocumentReference
    @Indexed
    private List<User> allowedUsers;

}
