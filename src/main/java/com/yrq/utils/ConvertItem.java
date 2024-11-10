package com.yrq.utils;

import com.yrq.entity.ResidentEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ConvertItem {

    public static String convertGender(String gender){

        if(gender.equals("0")){
            gender="男";
        }
        else if(gender.equals("1")){
            gender="女";
        }
        return gender;
    }
    public static String convertResidentState(String state){

        if(state.equals("0")){
            state="正常";
        }
        else if(state.equals("1")){
            state="通勤人员";
        }
        else if(state.equals("2")){
            state="黄码";
        }
        else if(state.equals("3")){
            state="红码";
        }
        return state;
    }
    public static String convertRoomState(String state){

        if(state.equals("0")){
            state="户主";
        }
        else if(state.equals("1")){
            state="租借";
        }
        return state;
    }
    public static void convertResident(ResidentEntity resident){
        try{
            resident.setGender(convertGender(resident.getGender()));
            resident.setState(convertResidentState(resident.getState()));
            resident.setHousingState(convertRoomState(resident.getHousingState()));
        }
        catch (NullPointerException e){

        }
    }
    public static void convertResident(List<ResidentEntity> residents){
        residents.forEach(resident->{
            resident.setGender(convertGender(resident.getGender()));
            resident.setState(convertResidentState(resident.getState()));
            resident.setHousingState(convertRoomState(resident.getHousingState()));
        });
    }
    public static Integer convertAge(Date date){
        if(date==null) return null;
        Calendar birth=Calendar.getInstance();
        birth.setTime(date);

        int year=birth.get(Calendar.YEAR);;
        int month=birth.get(Calendar.MONTH) + 1;
        int day=birth.get(Calendar.DAY_OF_MONTH);

        System.out.println(year+"   "+month+"   "+day);

		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayNow = cal.get(Calendar.DAY_OF_MONTH);
		int age;

		age=yearNow-year;
		System.out.println(dayNow+"   "+monthNow);
		if(age>=0){
			if(monthNow<month){
				age--;
			}
			else if(monthNow==month&&dayNow<day){
				age--;
			}
		}
		else{
			age=-1;
		}
		return age;
    }
    public static String convertPosition(String position){

        switch (position){
            case "0":return "管理员";
            case "1":return "网格员";
            case "2":return "网格长";
            case "3":return "片长";
            case "4":return "保安队长";
            case "5":return "保安";
            default:return null;
        }
    }
}
