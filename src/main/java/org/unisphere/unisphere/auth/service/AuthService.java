package org.unisphere.unisphere.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.auth.domain.MemberRole;
import org.unisphere.unisphere.auth.oauth.extractor.OauthAttributeExtractor;
import org.unisphere.unisphere.log.LogLevel;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.domain.SocialMember;
import org.unisphere.unisphere.member.infrastructure.MemberRepository;
import org.unisphere.unisphere.member.infrastructure.SocialMemberRepository;

@Service
@Transactional
@Logging(level = LogLevel.DEBUG)
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final SocialMemberRepository socialMemberRepository;

	public Member createAndFindOauthSocialMember(OauthAttributeExtractor extractor) {
		return memberRepository.findByEmail(extractor.getEmail())
				.orElseGet(() -> {
					Member member = Member.of(
							extractor.getEmail(),
							extractor.getNickname() + UUID.randomUUID(),
							LocalDateTime.now(),
							MemberRole.MEMBER
					);
					memberRepository.save(member);
					SocialMember socialMember = SocialMember.of(
							member,
							extractor.getOauthId(),
							extractor.getOauthType()
					);
					socialMemberRepository.save(socialMember);
					return member;
				});
	}
}
