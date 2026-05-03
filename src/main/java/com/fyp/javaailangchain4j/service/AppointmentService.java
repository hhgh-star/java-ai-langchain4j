package com.fyp.javaailangchain4j.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.javaailangchain4j.domain.Appointment;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 18:03:46
 * @Description
 */
public interface AppointmentService extends IService<Appointment>
{
	Appointment getOne(Appointment  appointment);
}
