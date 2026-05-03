package com.fyp.javaailangchain4j.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.SelectOne;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.javaailangchain4j.domain.Appointment;
import com.fyp.javaailangchain4j.mapper.AppointmentMapper;
import com.fyp.javaailangchain4j.service.AppointmentService;
import org.springframework.stereotype.Service;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 18:05:38
 * @Description
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper,Appointment> implements AppointmentService
{
	/**
	 * 根据预约信息查询单个预约记录
	 * 通过用户名、身份证号、科室、日期和时间进行精确匹配查询
	 *
	 * @param appointment 预约查询条件对象，包含用户名、身份证号、科室、日期和时间等查询字段
	 * @return 匹配的预约记录对象，如果未找到则返回null
	 */
	@Override
	public Appointment getOne(Appointment appointment)
	{
		if(appointment == null)
		{
			return null;
		}
		LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Appointment::getUsername,appointment.getUsername())
					.eq(Appointment::getIdCard,appointment.getIdCard())
					.eq(Appointment::getDepartment,appointment.getDepartment())
			        .eq(Appointment::getDate,appointment.getDate())
					.eq(Appointment::getTime,appointment.getTime());
		
		return baseMapper.selectOne(queryWrapper);
	}
}
