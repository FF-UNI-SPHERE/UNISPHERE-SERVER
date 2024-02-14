package org.unisphere.unisphere.group.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.unisphere.unisphere.group.domain.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query("select g from group_entity g where g.name = :name")
	Optional<Group> findByName(String name);
}
