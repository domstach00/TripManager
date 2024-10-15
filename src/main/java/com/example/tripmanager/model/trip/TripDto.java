package com.example.tripmanager.model.trip;

import com.example.tripmanager.model.AbstractAuditableDto;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.common.MemberDto;

import java.util.List;

public class TripDto extends AbstractAuditableDto {
    private String name;
    private String description;
    private AccountDto owner;
    private List<MemberDto> members;
    private Boolean isPublic;
    private Boolean isClosed;
    private Boolean isArchived;

    public TripDto() {
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

    public AccountDto getOwner() {
        return owner;
    }

    public void setOwner(AccountDto owner) {
        this.owner = owner;
    }


    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }

    public Boolean isPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean isClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Boolean isArchived() {
        return isArchived;
    }

    public void setArchived(Boolean archived) {
        isArchived = archived;
    }
}
