package org.unisphere.unisphere.utils.entity;

public interface TestEntity<E, ID> {

	E asEntity();

	E asMockEntity(ID id);
}
