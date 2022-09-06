package model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.NamedQuery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter   
@Setter  
@Builder

@NamedQuery(name="Member.findAll", query="select m from Member m")
@NamedQuery(name="Member.findIdName", query="select m from Member m where m.id = :id and m.name =:name")
@NamedQuery(name="Member.findName", query="select m from Member m where m.name =:name")
@NamedQuery(name="Member.findId", query="select m from Member m where m.id =:id")


@Entity

public class Member { 

	@Id
	@Column(name = "member_id")
	private String id;

	private String pw;

	private String name;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("회원 ID :  ");
		builder.append(id);
		builder.append("  비밀번호 :  ");
		builder.append(pw);
		builder.append("  이름 : ");
		builder.append(name);
		return builder.toString();
	}
	
	
}
