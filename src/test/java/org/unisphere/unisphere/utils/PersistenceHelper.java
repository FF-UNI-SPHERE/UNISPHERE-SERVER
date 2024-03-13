package org.unisphere.unisphere.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class PersistenceHelper {

	private final EntityManager em;

	private PersistenceHelper(EntityManager em) {
		this.em = em;
	}

	public static PersistenceHelper start(EntityManager em) {
		return new PersistenceHelper(em);
	}

	@SafeVarargs
	public final <E> PersistenceHelper persist(E... entities) {
		for (E entity : entities) {
			this.em.persist(entity);
		}
		return this;
	}

	public PersistenceHelper and() {
		return this;
	}

	public PersistenceHelper flush() {
		this.em.flush();
		return this;
	}

	public PersistenceHelper clear() {
		this.em.clear();
		return this;
	}

	public void flushAndClear() {
		this.flush().clear();
	}

	public <E> List<E> persistAndReturn(Collection<E> entities) {
		for (Object entity : entities) {
			this.em.persist(entity);
		}
		return new ArrayList<>(entities);
	}

	@SafeVarargs
	public final <E> List<E> persistAndReturn(E... entities) {
		for (Object entity : entities) {
			this.em.persist(entity);
		}
		return Arrays.stream(entities).collect(Collectors.toList());
	}

	public <E> E persistAndReturn(E entity) {
		this.em.persist(entity);
		return entity;
	}
}
