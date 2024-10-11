package com.example.tripmanager.model.common;

import com.example.tripmanager.model.AbstractEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;

public class Member {
    public static final String FIELD_ACCOUNT_ID = "accountId";
    public static final String FIELD_MEMBER_ROLE = "memberRole";
    @Indexed
    private ObjectId accountId;
    private MemberRole memberRole = MemberRole.STANDARD_USER;


    public enum MemberRole {
        ADMINISTRATOR,
        STANDARD_USER,
        READ_ONLY_USER;

    }

    public static MemberRole getRole(String role) {
        if (Objects.isNull(role) || role.isBlank()) {
            return null;
        }
        return MemberRole.valueOf(role.toUpperCase());
    }

    public String getAccountId() {
        return AbstractEntity.toString(this.accountId);
    }

    public void setAccountId(String accountId) {
        this.accountId = AbstractEntity.toObjectId(accountId);
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    public String getMemberRoleStr() {
        if (Objects.isNull(memberRole)) {
            return null;
        }
        return memberRole.name();
    }

    public void setMemberRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
