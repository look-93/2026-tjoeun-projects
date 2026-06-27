package com.moit.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {
	private int memberId;
	private String  loginId;
	private String  mobile;
	private String  nickname;
	private String  email;
	private String  password;
	private String  profileUrl;
	
	private int memberTypeId;
	private int statusId;
	
	private String createdAt;
	private String updatedAt;
	private String deleteYn;
}


/*
mysql> desc members;
+----------------+--------------+------+-----+-------------------+-----------------------------------------------+
| Field          | Type         | Null | Key | Default           | Extra                                         |
+----------------+--------------+------+-----+-------------------+-----------------------------------------------+
| member_id      | int          | NO   | PRI | NULL              | auto_increment                                |
| login_id       | varchar(50)  | NO   | UNI | NULL              |                                               |
| mobile         | varchar(20)  | NO   | UNI | NULL              |                                               |
| nickname       | varchar(50)  | NO   | UNI | NULL              |                                               |
| email          | varchar(100) | NO   | UNI | NULL              |                                               |
| password       | varchar(255) | NO   |     | NULL              |                                               |
| profile_url    | varchar(255) | YES  |     | NULL              |                                               |
| member_type_id | int          | NO   | MUL | 1                 |                                               |
| status_id      | int          | NO   | MUL | 1                 |                                               |
| created_at     | datetime     | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED                             |
| updated_at     | datetime     | NO   |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED on update CURRENT_TIMESTAMP |
| delete_yn      | char(1)      | NO   |     | N                 |                                               |
+----------------+--------------+------+-----+-------------------+-----------------------------------------------+
14 rows in set (0.00 sec)

*/