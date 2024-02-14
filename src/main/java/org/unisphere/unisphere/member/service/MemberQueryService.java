package org.unisphere.unisphere.member.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.exception.ExceptionStatus;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.infrastructure.MemberRepository;

@Service
@RequiredArgsConstructor
@Logging
public class MemberQueryService {

	private final MemberRepository memberRepository;

	/**
	 * 회원을 가져옵니다.
	 *
	 * @param memberId 회원의 ID
	 * @return 회원 객체를 반환합니다.
	 */
	public Member getMember(Long memberId) {
		Optional<Member> member = memberRepository.findById(memberId);
		return member.orElseThrow(ExceptionStatus.NOT_FOUND_MEMBER::toServiceException);
	}
}
