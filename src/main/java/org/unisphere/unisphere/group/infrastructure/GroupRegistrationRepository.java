package org.unisphere.unisphere.group.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.unisphere.unisphere.group.domain.GroupRegistration;
import org.unisphere.unisphere.group.domain.GroupRole;

public interface GroupRegistrationRepository extends JpaRepository<GroupRegistration, Long> {

	@Query("select gr from group_registration gr where gr.member.id = :memberId")
	Page<GroupRegistration> findAllByMemberId(Long memberId, Pageable pageable);

	@Query("select gr from group_registration gr where gr.group.id = :groupId")
	Page<GroupRegistration> findAllByGroupId(Long groupId, Pageable pageable);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.member.id = :memberId")
	Optional<GroupRegistration> findByGroupIdAndMemberId(Long groupId, Long memberId);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.member.id = :memberId and gr.registeredAt is not null")
	Optional<GroupRegistration> findByGroupIdAndMemberIdAndRegisteredAtNotNull(Long groupId,
			Long memberId);

	@Query("select gr from group_registration gr where gr.role = :role")
	Optional<GroupRegistration> findByRole(GroupRole role);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.role = :role and gr.registeredAt != null order by gr.registeredAt asc")
	Optional<GroupRegistration> findByGroupIdAndRoleAndRegisteredAtNotNullOrderByRegisteredAtAsc(
			Long groupId,
			GroupRole role
	);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.role in :roles and gr.registeredAt != null")
	List<GroupRegistration> findAllByGroupIdAndRoleAndRegisteredAtNotNull(Long groupId,
			List<GroupRole> roles);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.registeredAt is null")
	Page<GroupRegistration> findAllByGroupIdAndRegisteredAtNull(Long groupId, Pageable pageable);

	@Query("select gr from group_registration gr where gr.group.id = :groupId and gr.member.id = :memberId and gr.registeredAt is null")
	Optional<GroupRegistration> findByGroupIdAndMemberIdAndRegisteredAtNull(Long groupId,
			Long memberId);
}
