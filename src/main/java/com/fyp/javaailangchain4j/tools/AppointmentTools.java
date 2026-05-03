package com.fyp.javaailangchain4j.tools;

import com.fyp.javaailangchain4j.domain.Appointment;
import com.fyp.javaailangchain4j.service.AppointmentService;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 18:54:28
 * @Description
 */
@Component
public class AppointmentTools
{
	@Resource
	private AppointmentService appointmentService;
	
	/**
	 * 预约挂号
	 * 根据参数先执行工具方法queryDepartment查询是否可预约，并直接给用户回答是否可预约
	 * 如果用户提供了完整信息（包括时间），直接执行预约并返回结果
	 * 如果用户遗漏了时间参数，不假设时间也不报错，调用此工具时返回当前系统时间并向用户确认
	 *
	 * @param appointment 预约信息，包含用户名、身份证号、科室、日期、时间等
	 * @return 预约结果信息：预约成功返回成功提示，已有预约返回冲突提示，失败返回失败提示，或返回确认时间的提示信息
	 */
	@Tool(name = "Book Appointment",value = """
		When user provides appointment information, follow these steps:
		    1. First call queryDepartment to check slot availability for the requested department, date and time.
		          - If no slots available, inform user and do not proceed with booking.
		          - If slots are available, continue to step 2.
		    2. Check if doctor name is provided in the appointment request:
		          - If doctor name IS provided: Use the specified doctor for booking.
		           - If doctor name is NOT provided:
		           * Refer to the medical documents in resources/documents/ directory to select an appropriate doctor.
		           * For 口腔科 (Stomatology): Choose from doctors like 赵继志 (expert in oral tumors and plastic surgery), 赖钦声 (expert in oral maxillofacial surgery), or 万阔 (expert in painless dental treatment).
		           * For 神经内科 (Neurology): Choose from doctors like 彭斌 (expert in cerebrovascular diseases), 崔丽英 (expert in motor neuron diseases), or 朱以诚 (expert in cerebrovascular and rare diseases).
		           * Select a doctor based on the department and patient's needs mentioned in the conversation.
		           * Inform the user which doctor has been selected and their specialty.
		   3. Ask user to confirm all appointment details including:
		         - Username (姓名)
		         - ID card number (身份证号)
		         - Department (科室)
		         - Date (日期)
		         - Time (时间 - morning or afternoon)
		         - Doctor name (医生 - either user-specified or AI-selected)
		   4. Only proceed with booking after user confirms ALL the details.
		   5. Required parameters: username, ID card, department, date, time.
		          - If any required parameter is missing, especially time, ask user to provide the missing information.
		          - If time is missing, explicitly ask user to specify the appointment time (上午 or 下午).
		          - Do not proceed with booking until all required parameters are provided and confirmed by user.
		   6. After successful booking, return confirmation message with full appointment details including the assigned doctor.
                  """)
	public String bookAppoinment(Appointment appointment){
		Appointment appointmentDB = appointmentService.getOne(appointment);
		if(appointmentDB == null)
		{
			appointment.setId(null);
			if(appointmentService.save(appointment))
			{
				return "预约成功,并返回预约详情";
			}
			else {
				return "预约失败,请重新预约";
			}
		}
		return "您在相同的科室和时间段已有预约";
	}
	
	
	
	/**
	 * 取消预约
	 * 根据参数查询预约是否存在，如果存在则删除预约记录并返回取消成功，否则返回取消失败
	 *
	 * @param appointment 预约信息，用于查询需要取消的预约记录
	 * @return 取消结果信息：取消成功返回成功提示，预约不存在或取消失败返回相应提示
	 */
	@Tool(name = "Cancel Appointment",value = """
        Check if appointment exists based on parameters.
         If exists, delete the record and return success message. Otherwise, return failure message.")
		""")
	public String cancelAppoinment(Appointment appointment){
		Appointment appointmentDB = appointmentService.getOne(appointment);
		if(appointmentDB != null)
		{
			if(appointmentService.removeById(appointmentDB))
			{
				return "取消成功";
			}
			else {
				return "取消失败,请重新取消";
			}
		}
		return "您没有该预约,请核对预约科室和时间";
	}
	
	/**
	 * 查询号源
	 * 根据科室名称、日期、时间和医生查询是否有号源，并返回给用户
	 *
	 * @param name 科室名称
	 * @param date 日期
	 * @param time 时间，可选值：上午、下午
	 * @param doctorName 医生名称（可选参数）
	 * @return 是否有号源：有号源返回true，否则返回false
	 */
	@Tool(name = "Query Available Slots",value = """
		Check slot availability based on department name, date, time and doctor. Return availability status.")
		""")
	public boolean queryDepartment(
				@P(value = "科室名称") String name,
				@P(value = "日期") String date,
				@P(value = "时间,可选值：上午、下午") String time,
				@P(value = "医生名称",required = false) String doctorName
	){
		System.out.println("查询是否有号源");
		System.out.println("科室名称:"+name);
		System.out.println("日期:"+date);
		System.out.println("时间:"+time);
		System.out.println("医生名称:"+doctorName);
		//TODO 维护医生的排班信息：
		//如果没有指定医生名字，则根据其他条件查询是否有可以预约的医生（有返回true，否则返回false）
		
		//如果指定了医生名字，则判断医生是否有排班（没有排版返回false）
		//如果有排班，则判断医生排班时间段是否已约满（约满返回false，有空闲时间返回true）
		return true;
	}
}
