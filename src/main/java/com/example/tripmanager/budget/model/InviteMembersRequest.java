package com.example.tripmanager.budget.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class InviteMembersRequest {
    private static final int MAX_INVITATION = 20;

    @NotEmpty(message = "Id list cannot be empty")
    @Size(max = MAX_INVITATION, message = "Cannot invite more then " + MAX_INVITATION + " invitations at a time")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
