package org.unisphere.unisphere.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unisphere.unisphere.member.domain.SocialMember;

public interface SocialMemberRepository extends JpaRepository<SocialMember, Long> {

}
