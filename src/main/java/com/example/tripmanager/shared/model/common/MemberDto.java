package com.example.tripmanager.shared.model.common;

public class MemberDto {
    private String memberId;
    private String memberRole;

    public MemberDto() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }
}
