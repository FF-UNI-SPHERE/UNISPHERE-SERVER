package org.unisphere.unisphere.group.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.unisphere.unisphere.group.domain.GroupRegistration;

public interface GroupRegistrationRepository extends JpaRepository<GroupRegistration, Long> {

	@Query("select gr from group_registration gr where gr.member.id = :memberId")
	Page<GroupRegistration> findAllByMemberId(Long memberId, Pageable pageable);
}
