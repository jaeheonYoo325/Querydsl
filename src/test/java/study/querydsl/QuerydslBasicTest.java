package study.querydsl;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;

import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import static study.querydsl.entity.QMember.*;

import java.util.List;

import study.querydsl.entity.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class QuerydslBasicTest {

	@Autowired
	EntityManager em;
	
	JPAQueryFactory queryFactory;
	
	@BeforeEach
	public void before() {
		queryFactory = new JPAQueryFactory(em);
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);
		
		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);
		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);
	}
	
	
	@Test
	public void startJPQL() {
		//member1을 찾아라.
		String qlString = 
				"select m from Member m " +
				"where m.username = :username";
		
		Member findMember = em.createQuery(qlString, Member.class)
				.setParameter("username", "member1")
				.getSingleResult();
		
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void startQuerydsl() {
		
		QMember member = QMember.member;
		//QMember m1 = new QMember("m1"); 같은 테이블을 조인할 경우에 사용.
		
		Member findMember = queryFactory
				.select(member)
				.from(member)
				.where(member.username.eq("member1")) //파라미터 바인딩 처리
				.fetchOne();
		
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void search() {
		
		Member findMember = queryFactory
				.selectFrom(member)
				.where(member.username.eq("member1")
						.and(member.age.eq(10)))
				.fetchOne();
		
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}
	
	@Test
	public void searchAndParam() {
		
		Member findMember = queryFactory
				.selectFrom(member)
				.where(
						member.username.eq("member1"),
						member.age.eq(10)
				)
				.fetchOne();
		
		Assertions.assertThat(findMember.getUsername()).isEqualTo("member1");
	}

	@Test
	public void resultFetch() {

//List
//		List<Member> fetch = queryFactory
//				.selectFrom(member)
//				.fetch();
		
//단 건		
//		Member fetchOne = queryFactory
//				.selectFrom(member)
//				.fetchOne();
		
//처음 한 건 조회		
//		Member fetchFirst = queryFactory
//				.selectFrom(member)
//				.fetchFirst();
		
//페이징에서 사용		
//		QueryResults<Member> results = queryFactory
//				.selectFrom(member)
//				.fetchResults();
//		
//		results.getTotal();
//		List<Member> content = results.getResults();

//count 쿼리로 변경		
		long count = queryFactory
				.selectFrom(member)
				.fetchCount();
		
	}
}
