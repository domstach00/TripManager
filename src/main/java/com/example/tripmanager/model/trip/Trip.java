package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.AbstractAuditable;
import com.example.tripmanager.model.account.Account;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document
public class Trip extends AbstractAuditable {
    private String name;
    private String description;
    @DocumentReference
    @Indexed
    private Account owner;

    private boolean isPublic;
    private boolean isClosed;
    private boolean isArchived;
    private boolean isDeleted;

    @DocumentReference
    @Indexed
    private List<Account> members; // TODO change Account to ObjectId

    public Trip() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public List<Account> getMembers() {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        return members;
    }

    public void setMembers(List<Account> members) {
        this.members = members;
    }
}
