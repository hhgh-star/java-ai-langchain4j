package com.fyp.javaailangchain4j.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 17:49:44
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment
{
	@TableId(type = IdType.AUTO)
	private Long id;
	private String username;
	private String idCard;
	private String department;
	private String date;
	private String time;
	private String doctorName;
}
