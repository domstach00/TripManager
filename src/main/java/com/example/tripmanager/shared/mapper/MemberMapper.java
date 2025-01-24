package com.example.tripmanager.shared.mapper;

import com.example.tripmanager.account.exception.AccountNotFoundException;
import com.example.tripmanager.account.model.Account;
import com.example.tripmanager.shared.model.common.Member;
import com.example.tripmanager.shared.model.common.MemberDto;
import com.example.tripmanager.account.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberMapper {
    public static MemberDto toDto(Member member) {
        if (Objects.isNull(member)) {
            return null;
        }
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getAccountId());
        memberDto.setMemberRole(member.getMemberRoleStr());
        return memberDto;
    }

    public static List<MemberDto> toDto(List<Member> memberList) {
        if (Objects.isNull(memberList)) {
            return null;
        }
        return memberList.stream().map(MemberMapper::toDto).toList();
    }

    public static Member createFromDto(MemberDto memberDto, final AccountService accountService) {
        if (Objects.isNull(memberDto)) {
            return null;
        }
        Member member = new Member();
        Account account = accountService.getAccountById(member.getAccountId()).orElseThrow(AccountNotFoundException::new);
        // TODO validate if account can be assigned to given Trip
        member.setAccountId(account.getId());
        member.setMemberRole(Member.getRole(memberDto.getMemberRole()));
        return member;
    }

    public static List<Member> createFromDto(List<MemberDto> memberDtos, final AccountService accountService) {
        if (Objects.isNull(memberDtos)) {
            return new ArrayList<>();
        }
        return memberDtos.stream()
                .map(memberDto -> createFromDto(memberDto, accountService))
                .toList();
    }
}
