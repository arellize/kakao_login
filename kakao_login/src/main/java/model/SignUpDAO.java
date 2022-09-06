package model;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;

import org.modelmapper.ModelMapper;

import model.dto.MemberDTO;
import model.entity.Member;
import model.util.PublicCommon;

public class SignUpDAO {

	public static boolean addMember(String id, String pw, String name) throws Exception, RollbackException {
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		Member memberEntity = null;

		boolean result = false;

		try {
			tx.begin();

			ModelMapper modelMapper = new ModelMapper();
			memberEntity = modelMapper.map(new MemberDTO(id, pw, name), Member.class);

			em.persist(memberEntity);
			tx.commit();

			result = true;
		} catch (RollbackException e) {
			tx.rollback();
			throw e;
		} catch (Exception e) {
			tx.rollback();
			throw e;
		} finally {
			em.close();
			em = null;
		}
		return result;
	}

	public static void notExistMember(String id) throws Exception {
		MemberDTO member = getMember(id);
		if (member == null) {
			throw new Exception("검색하는 id가 미존재합니다.");
		}
	}

	public static boolean checkIdDuplicate(String id) throws Exception, NoResultException {

		List<MemberDTO> member = getId(id);
		System.out.println(member);
		
		if (member.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean updatePassword(String id, String pw) throws Exception {
		notExistMember(id);
		return SignUpDAO.updateInfo(id, pw);
	}

	public static boolean updateInfo(String id, String pw) throws SQLException {
		EntityManager em = PublicCommon.getEntityManager();
		em.getTransaction().begin();
		boolean result = false;

		try {
			em.find(Member.class, id).setPw(pw);

			em.getTransaction().commit();

			result = true;
		} catch (Exception e) {
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return result;
	}

	public static MemberDTO getMember(String name) throws Exception { // getInfo와 중복
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		Member member = null;
		MemberDTO memberDTO = null;

		try {

			member = em.createNamedQuery("Member.findName", Member.class).setParameter("name", name).getSingleResult();

			ModelMapper modelMapper = new ModelMapper();
			memberDTO = modelMapper.map(member, MemberDTO.class);

			return memberDTO;

		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}
	}

	public static List<MemberDTO> getId(String id) throws Exception, NoResultException { // getInfo와 중복
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		List<Member> list = null;
		List<MemberDTO> listDTO = null;

		try {
			tx.begin();

			list = em.createNamedQuery("Member.findId", Member.class).setParameter("id", id).getResultList();

			ModelMapper modelMapper = new ModelMapper();
			listDTO = list.stream().map(d -> modelMapper.map(d, MemberDTO.class)).collect(Collectors.toList());

		} catch (NoResultException s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}
		return listDTO;
	}

	public static MemberDTO getPw(String id, String name) throws Exception { // getInfo와 중복
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		Member member = null;
		MemberDTO memberDTO = null;

		try {

			member = em.createNamedQuery("Member.findIdName", Member.class).setParameter("id", id)
					.setParameter("name", name).getSingleResult();

			ModelMapper modelMapper = new ModelMapper();
			memberDTO = modelMapper.map(member, MemberDTO.class);

			return memberDTO;

		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}
	}

	public static List<MemberDTO> getAllMembers() throws Exception {
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		List<Member> list = null;
		List<MemberDTO> listDTO = null;

		try {
			tx.begin();

			list = em.createNamedQuery("Member.findAll", Member.class).getResultList();

			ModelMapper modelMapper = new ModelMapper();
			listDTO = list.stream().map(d -> modelMapper.map(d, MemberDTO.class)).collect(Collectors.toList());

		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}
		return listDTO;
	}

	public static boolean removeMember(String id) throws Exception {
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		boolean result = false;

		try {
			em.remove(em.find(Member.class, id));

			result = true;
			tx.commit();
		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}
		return result;
	}

	public static MemberDTO loginMember(String id, String pw) throws Exception, NoResultException {
		EntityManager em = PublicCommon.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		Member member = null;
		MemberDTO memberDTO = null;

		try {
			member = em.createNamedQuery("Member.findId", Member.class).setParameter("id", id).getSingleResult();

			if (member.getPw().equals(pw)) {

				ModelMapper modelMapper = new ModelMapper();
				memberDTO = modelMapper.map(member, MemberDTO.class);

				return memberDTO;

			} else {
				throw new Exception("id 또는 pw를 확인하세요");
			}
		} catch (NoResultException s) {
			throw new NoResultException("id 또는 pw를 확인하세요");
		} catch (Exception s) {
			tx.rollback();
			s.printStackTrace();
			throw s;
		} finally {
			em.close();
			em = null;
		}

	}

}