package com.yrq;

import com.yrq.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {
//	@Resource
//	JwtUtils jwtUtils;
//
//	@Test
//	void tockenTest() {
//		Object o="213";
//		System.out.println(o);
//		String str=o.toString();
//		System.out.println(str);
//	}
//
//	@Test
//
//	void testAge(){
//
//		Calendar tt=Calendar.getInstance();
//		tt.set(Calendar.YEAR,2001);
//		tt.set(Calendar.MONTH,7);
//		tt.set(Calendar.DAY_OF_MONTH,4);
//
//		Date date=tt.getTime();
//
//		Calendar birth=Calendar.getInstance();
//		birth.setTime(date);
//
//		int year=birth.get(Calendar.YEAR);;
//		int month=birth.get(Calendar.MONTH) + 1;
//		int day=birth.get(Calendar.DAY_OF_MONTH);
//		System.out.println(year+"   "+month+"   "+day);
////		Calendar cal = Calendar.getInstance();
////		int yearNow = cal.get(Calendar.YEAR);
////		int monthNow = cal.get(Calendar.MONTH) + 1;
////		int dayNow = cal.get(Calendar.DAY_OF_MONTH);
////		int age;
////		age=yearNow-year;
////		System.out.println(dayNow+"   "+monthNow);
////		if(age>=0){
////			if(monthNow<month){
////				age--;
////			}
////			else if(monthNow==month&&dayNow<day){
////				age--;
////			}
////		}
////		else{
////			age=-1;
////		}
////		System.out.println("年龄："+age);
//	}

}
