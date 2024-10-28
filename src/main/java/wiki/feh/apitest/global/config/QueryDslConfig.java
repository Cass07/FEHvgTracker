package wiki.feh.apitest.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
@EnableJpaAuditing
public class QueryDslConfig {
	@PersistenceContext
	private EntityManager entityManager;

	@Bean
	@Qualifier(value = "QueryDslConfig")
	public JPAQueryFactory queryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
