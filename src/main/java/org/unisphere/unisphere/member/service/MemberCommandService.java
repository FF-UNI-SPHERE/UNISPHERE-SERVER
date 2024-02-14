package org.unisphere.unisphere.member.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.unisphere.unisphere.annotation.Logging;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.member.infrastructure.MemberRepository;

@Service
@RequiredArgsConstructor
@Logging
@Transactional
public class MemberCommandService {

	private final MemberRepository memberRepository;

	/**
	 * 아바타 정보 수정
	 *
	 * @param member   수정할 회원
	 * @param nickname 닉네임
	 * @param imageUrl 이미지 URL
	 */
	public void updateAvatar(Member member, String nickname, String imageUrl) {
		member.updateAvatar(nickname, imageUrl);
		memberRepository.save(member);
	}
}
