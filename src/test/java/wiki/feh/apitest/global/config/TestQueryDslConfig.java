package wiki.feh.apitest.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestConfiguration
public class TestQueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	@Qualifier(value = "TestQueryDslConfig")
	public JPAQueryFactory queryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
