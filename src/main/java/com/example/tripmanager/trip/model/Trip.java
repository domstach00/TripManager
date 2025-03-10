package com.example.tripmanager.trip.model;

import com.example.tripmanager.shared.model.AbstractAuditable;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.shared.model.common.Member;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = Trip.COLLECTION_NAME)
public class Trip extends AbstractAuditable {
    public static final String COLLECTION_NAME = "trips";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_DESCRIPTION = "description";
    public static final String FIELD_NAME_OWNER = "owner";
    public static final String FIELD_NAME_IS_PUBLIC = "isPublic";
    public static final String FIELD_NAME_IS_CLOSED = "isClosed";
    public static final String FIELD_NAME_IS_ARCHIVED = "isArchived";
    public static final String FIELD_NAME_MEMBERS = "members";

    private String name;
    private String description;
    @DocumentReference
    @Indexed
    private Account owner;
    private List<Member> members;

    private boolean isPublic;
    private boolean isClosed;
    private boolean isArchived;

    public Trip() {
    }

    public void deepCopyFrom(Trip that) {
        super.deepCopyFrom(that);
        setName(that.getName());
        setDescription(that.getDescription());
        setOwner(that.getOwner());
        setMembers(that.getMembers().stream()
                .map(member -> {
                    Member deepCopyMember = new Member();
                    deepCopyMember.deepCopyFrom(member);
                    return deepCopyMember;
                })
                .toList()
        );
        setPublic(that.isPublic());
        setClosed(that.isClosed());
        setArchived(that.isArchived());
        setDeleted(that.isDeleted());
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

    public List<Member> getMembers() {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
