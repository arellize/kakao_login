/*
CREATE TABLE member (
       member_id          	VARCHAR(40)  
       pw               	VARCHAR(40) NOT NULL,
       name         		VARCHAR(40) NOT NULL,
       constraint pk_id_member primary key (member_id)
);
*/





package model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.entity.Member;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class MemberDTO {
	
	
	private String id;
	private String pw;
	private String name;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("1. 회원 ID : ");
		builder.append(id);
		builder.append("  2. PW : ");
		builder.append(pw);
		builder.append("  3. 이름 : ");
		builder.append(name);
		return builder.toString();
	}
	
	public Member toEntity() {
		return Member.builder().id(id).pw(pw).name(name).build();
	}
	
	
	
	
	
	

}
