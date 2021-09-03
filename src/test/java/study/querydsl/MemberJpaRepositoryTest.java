package study.querydsl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import study.querydsl.entity.Member;
import study.querydsl.repository.MemberJpaRepository;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

	@Autowired
	EntityManager em;
	
	@Autowired MemberJpaRepository memberJpaRepository;
	
	
	@Test
	public void basicJPQLTest() {
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);
		
		Member findMember = memberJpaRepository.findById(member.getId()).get();
		Assertions.assertThat(findMember).isEqualTo(member);
		
		List<Member> result1 = memberJpaRepository.findAll();
		Assertions.assertThat(result1).containsExactly(member);
		
		List<Member> result2 = memberJpaRepository.findByUsername("member1");
		Assertions.assertThat(result2).containsExactly(member);
	}

	@Test
	public void basicQuerydslTest() {
		Member member = new Member("member1", 10);
		memberJpaRepository.save(member);
		
		Member findMember = memberJpaRepository.findById(member.getId()).get();
		Assertions.assertThat(findMember).isEqualTo(member);
		
		List<Member> result1 = memberJpaRepository.findAll_Querydsl();
		Assertions.assertThat(result1).containsExactly(member);
		
		List<Member> result2 = memberJpaRepository.findByUsername_Querydsl("member1");
		Assertions.assertThat(result2).containsExactly(member);
		
	}
}
