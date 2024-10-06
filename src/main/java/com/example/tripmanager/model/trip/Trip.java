package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.AbstractAuditable;
import com.example.tripmanager.model.account.Account;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class Trip extends AbstractAuditable {
    private String name;
    private String description;
    private int dayLength;

    private boolean isPublic;
    private boolean isClosed;

    @DocumentReference
    @Indexed
    private List<Account> allowedAccounts;


    public void addAllowedAccount(Account account) {
        if (!getAllowedAccounts().contains(account)) {
            this.allowedAccounts.add(account);
        }
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

    public int getDayLength() {
        return dayLength;
    }

    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
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

    public List<Account> getAllowedAccounts() {
        if (this.allowedAccounts == null) {
            this.allowedAccounts = new ArrayList<>();
        }
        return allowedAccounts;
    }

    public void setAllowedAccounts(List<Account> allowedAccounts) {
        this.allowedAccounts = allowedAccounts;
    }
}
