--show databases;

USE playdata;

-- 회원 리스트
DROP TABLE IF EXISTS member;

CREATE TABLE member (
       member_id          	VARCHAR(40)  
       pw               	VARCHAR(40) NOT NULL,
       name         		VARCHAR(40) NOT NULL,
       constraint pk_id_member primary key (member_id)
);
