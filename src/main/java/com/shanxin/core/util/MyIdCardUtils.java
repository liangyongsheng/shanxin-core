package com.shanxin.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类（大陆，香港，澳门，台湾） <br/>
 * 注意证号里出现字母必须是大写，否则为非身份证，出现括号必须是英文括号，否则为非身份证<br/>
 * Z687485(2)（香港） 1000248(3)（澳门） F212345674（台湾）
 */
public class MyIdCardUtils {

	/** 中国公民身份证号码最小长度（大陆） */
	public static final int MAINLAND_IDCARD_MIN_LENGTH = 15;

	/** 中国公民身份证号码最大长度（大陆） */
	public static final int MAINLAND_IDCARD_ID_MAX_LENGTH = 18;

	/** 每位加权因子（大陆） */
	public static final int mainlandPower[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	/** 最低年限（大陆） */
	public static final int MAINLAND_MIN_YEAR = 1930;

	/** 城市编号（大陆） */
	public static Map<String, String> chinaCityCodes = new HashMap<String, String>();

	/** 香港身份首字母对应数字（香港） */
	public static Map<String, Integer> hkFirstCode = new HashMap<String, Integer>();

	/** 台湾身份首字母对应数字（台湾） */
	public static Map<String, Integer> twFirstCode = new HashMap<String, Integer>();

	static {
		chinaCityCodes.put("11", "北京");
		chinaCityCodes.put("12", "天津");
		chinaCityCodes.put("13", "河北");
		chinaCityCodes.put("14", "山西");
		chinaCityCodes.put("15", "内蒙古");
		chinaCityCodes.put("21", "辽宁");
		chinaCityCodes.put("22", "吉林");
		chinaCityCodes.put("23", "黑龙江");
		chinaCityCodes.put("31", "上海");
		chinaCityCodes.put("32", "江苏");
		chinaCityCodes.put("33", "浙江");
		chinaCityCodes.put("34", "安徽");
		chinaCityCodes.put("35", "福建");
		chinaCityCodes.put("36", "江西");
		chinaCityCodes.put("37", "山东");
		chinaCityCodes.put("41", "河南");
		chinaCityCodes.put("42", "湖北");
		chinaCityCodes.put("43", "湖南");
		chinaCityCodes.put("44", "广东");
		chinaCityCodes.put("45", "广西");
		chinaCityCodes.put("46", "海南");
		chinaCityCodes.put("50", "重庆");
		chinaCityCodes.put("51", "四川");
		chinaCityCodes.put("52", "贵州");
		chinaCityCodes.put("53", "云南");
		chinaCityCodes.put("54", "西藏");
		chinaCityCodes.put("61", "陕西");
		chinaCityCodes.put("62", "甘肃");
		chinaCityCodes.put("63", "青海");
		chinaCityCodes.put("64", "宁夏");
		chinaCityCodes.put("65", "新疆");
		chinaCityCodes.put("71", "台湾");
		chinaCityCodes.put("81", "香港");
		chinaCityCodes.put("82", "澳门");
		chinaCityCodes.put("91", "国外");
		twFirstCode.put("A", 10);
		twFirstCode.put("B", 11);
		twFirstCode.put("C", 12);
		twFirstCode.put("D", 13);
		twFirstCode.put("E", 14);
		twFirstCode.put("F", 15);
		twFirstCode.put("G", 16);
		twFirstCode.put("H", 17);
		twFirstCode.put("J", 18);
		twFirstCode.put("K", 19);
		twFirstCode.put("L", 20);
		twFirstCode.put("M", 21);
		twFirstCode.put("N", 22);
		twFirstCode.put("P", 23);
		twFirstCode.put("Q", 24);
		twFirstCode.put("R", 25);
		twFirstCode.put("S", 26);
		twFirstCode.put("T", 27);
		twFirstCode.put("U", 28);
		twFirstCode.put("V", 29);
		twFirstCode.put("X", 30);
		twFirstCode.put("Y", 31);
		twFirstCode.put("W", 32);
		twFirstCode.put("Z", 33);
		twFirstCode.put("I", 34);
		twFirstCode.put("O", 35);
		hkFirstCode.put("A", 1);
		hkFirstCode.put("B", 2);
		hkFirstCode.put("C", 3);
		hkFirstCode.put("D", 4);//
		hkFirstCode.put("E", 5);//
		hkFirstCode.put("F", 6);//
		hkFirstCode.put("G", 7);//
		hkFirstCode.put("H", 8);//
		hkFirstCode.put("I", 9);//
		hkFirstCode.put("J", 10);//
		hkFirstCode.put("K", 11);//
		hkFirstCode.put("L", 12);//
		hkFirstCode.put("M", 13);//
		hkFirstCode.put("N", 14);
		hkFirstCode.put("O", 15);
		hkFirstCode.put("P", 16);//
		hkFirstCode.put("Q", 17);//
		hkFirstCode.put("R", 18);
		hkFirstCode.put("S", 19);//
		hkFirstCode.put("T", 20);//
		hkFirstCode.put("U", 21);
		hkFirstCode.put("V", 22);//
		hkFirstCode.put("W", 23);
		hkFirstCode.put("X", 24);
		hkFirstCode.put("Y", 25);//
		hkFirstCode.put("Z", 26);
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值（大陆）
	 * 
	 * @param iArr
	 * @return 身份证编码。
	 */
	private static int getPowerSum(int[] iArr) {
		int sum = 0;
		if (mainlandPower.length == iArr.length) {
			for (int i = 0; i < iArr.length; i++) {
				for (int j = 0; j < mainlandPower.length; j++) {
					if (i == j)
						sum = sum + iArr[i] * mainlandPower[j];
				}
			}
		}
		return sum;
	}

	/**
	 * 将power和值与11取模获得余数进行校验码判断（大陆）
	 * 
	 * @param iSum
	 * @return 校验位
	 */
	private static String getCheckCodeFor18(int num) {
		String sCode = "";
		switch (num % 11) {
		case 10:
			sCode = "2";
			break;
		case 9:
			sCode = "3";
			break;
		case 8:
			sCode = "4";
			break;
		case 7:
			sCode = "5";
			break;
		case 6:
			sCode = "6";
			break;
		case 5:
			sCode = "7";
			break;
		case 4:
			sCode = "8";
			break;
		case 3:
			sCode = "9";
			break;
		case 2:
			sCode = "x";
			break;
		case 1:
			sCode = "0";
			break;
		case 0:
			sCode = "1";
			break;
		}
		return sCode;
	}

	/**
	 * 验证18位身份编码是否合法（大陆）
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard18(String idCard) {
		boolean rs = false;
		if (idCard == null || idCard.length() != MAINLAND_IDCARD_ID_MAX_LENGTH)
			return rs;

		// 出生在1900到2099年之间，若今年是2100年请修改
		if (!idCard.matches("[1-9][0-9]{5}(19|20)[0-9]{2}(0[1-9]|10|11|12)(0[1-9]|1[0-9]|2[0-9]|3[0-1])[0-9]{3}[0-9X]"))
			return rs;

		// 前17位
		String code17 = idCard.substring(0, 17);
		// 第18位
		String code18 = idCard.substring(17, MAINLAND_IDCARD_ID_MAX_LENGTH);
		if (isNum(code17)) {
			char[] cArr = code17.toCharArray();
			if (cArr != null) {
				int[] iCard = converCharToInt(cArr);
				int iSum17 = getPowerSum(iCard);
				// 获取校验位
				String val = getCheckCodeFor18(iSum17);
				if (val.length() > 0) {
					if (val.equalsIgnoreCase(code18))
						rs = true;
				}
			}
		}
		return rs;
	}

	/**
	 * 验证15位身份编码是否合法（大陆）
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 是否合法
	 */
	public static boolean validateIdCard15(String idCard) {
		if (idCard == null || idCard.length() != MAINLAND_IDCARD_MIN_LENGTH)
			return false;

		if (!idCard.matches("[1-9][0-9]{5}[0-9]{2}(0[1-9]|10|11|12)(0[1-9]|1[0-9]|2[0-9]|3[0-1])[0-9]{3}"))
			return false;

		if (!isNum(idCard))
			return false;

		String proCode = idCard.substring(0, 2);
		if (chinaCityCodes.get(proCode) == null)
			return false;

		String birthCode = idCard.substring(6, 12);
		Date birthDate = null;
		try {
			birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
		} catch (Throwable e) {
			return false;
		}

		Calendar cal = Calendar.getInstance();
		if (birthDate != null)
			cal.setTime(birthDate);
		else
			return false;

		if (!valiDate(cal.get(Calendar.YEAR), Integer.valueOf(birthCode.substring(2, 4)), Integer.valueOf(birthCode.substring(4, 6))))
			return false;

		return false;
	}

	/**
	 * 将15位身份证号码转换为18位（大陆）
	 * 
	 * @param idCard
	 *            15位身份编码
	 * @return 18位身份编码
	 */
	public static String conver15CardTo18(String idCard) {
		String idCard18 = "";
		if (!validateIdCard15(idCard))
			return null;

		if (!isNum(idCard))
			return null;

		// 获取出生年月日
		String birthday = idCard.substring(6, 12);
		Date birthDate = null;
		try {
			birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
		} catch (Throwable e) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		if (birthDate != null)
			cal.setTime(birthDate);
		else
			return null;

		// 获取出生年(完全表现形式,如：2010)
		String year = String.valueOf(cal.get(Calendar.YEAR));
		idCard18 = idCard.substring(0, 6) + year + idCard.substring(8);
		// 转换字符数组
		char[] cArr = idCard18.toCharArray();
		if (cArr != null) {
			int[] cards = converCharToInt(cArr);
			int sum17 = getPowerSum(cards);
			// 获取校验位
			String sVal = getCheckCodeFor18(sum17);
			if (sVal.length() > 0)
				idCard18 += sVal;
			else
				return null;
		}
		return idCard18;
	}

	/**
	 * 根据15或18位身份编号获取年龄（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 年龄
	 */
	public static int getAgeByIdCard(String idCard) {
		int age = 0;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return age;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return age;
		}
		String year = idCard.substring(6, 10);
		Calendar cal = Calendar.getInstance();
		int currYear = cal.get(Calendar.YEAR);
		age = currYear - Integer.valueOf(year);
		return age;
	}

	/**
	 * 根据15或18位身份编号获取生日（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 生日(yyyyMMdd)
	 */
	public static String getBirthdayByIdCard(String idCard) {
		String birthday = null;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return birthday;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return birthday;
		}
		birthday = idCard.substring(6, 14);
		return birthday;
	}

	/**
	 * 根据15或18位身份编号获取出生年份（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 年份(yyyy)
	 */
	public static int getYearByIdCard(String idCard) {
		int year = 0;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return year;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return year;
		}
		year = Integer.valueOf(idCard.substring(6, 10));
		return year;
	}

	/**
	 * 根据15或18位身份编号获取出生月份（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 月份(MM)
	 */
	public static int getMonthByIdCard(String idCard) {
		int month = 0;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return month;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return month;
		}
		month = Integer.valueOf(idCard.substring(10, 12));
		return month;
	}

	/**
	 * 根据15或18位身份编号获取出生日期（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 日期(dd)
	 */
	public static int getDayByIdCard(String idCard) {
		int day = 0;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return day;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return day;
		}
		day = Integer.valueOf(idCard.substring(12, 14));
		return day;
	}

	/**
	 * 根据15或18位身份编号获取性别（大陆）
	 * 
	 * @param idCard
	 *            身份编号
	 * @return 性别(M-男，F-女，N-未知)
	 */
	public static String getGenderByIdCard(String idCard) {
		String gender = "N";
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return gender;
		if (idCard.length() == MAINLAND_IDCARD_MIN_LENGTH) {
			idCard = conver15CardTo18(idCard);
			if (idCard == null)
				return gender;
		}
		String cardNumStr = idCard.substring(16, 17);
		if (Integer.parseInt(cardNumStr) % 2 != 0)
			gender = "M";
		else
			gender = "F";
		return gender;
	}

	/**
	 * 根据15或18位身份编号获取户籍省份（大陆）
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 省级编码。
	 */
	public static String getProvinceByIdCard(String idCard) {
		String province = null;
		if (!validateIdCard18(idCard) && !validateIdCard15(idCard))
			return province;
		province = chinaCityCodes.get(idCard.substring(0, 2));
		return province;
	}

	/**
	 * 验证10位身份编码是否合法，港澳台身份证是有排它性（港澳台）
	 * 
	 * @param idCard
	 *            身份编码
	 * @return 身份证信息数组：<br/>
	 *         [0] - 香港、澳门、台湾<br/>
	 *         [1] - 1、2、3(分别指港澳台)<br/>
	 *         [2] - 性别(男M,女F,未知N)<br/>
	 *         [3] - 是否合法(合法true,不合法false)<br/>
	 *         若不是身份证件号码则返回null
	 */
	public static String[] validateIdCard10(String idCard) {
		String[] info = new String[3];

		if (idCard == null || (idCard.length() != 10 && idCard.length() != 11 && idCard.length() != 2))
			return null;

		boolean isHk = validateHkCard(idCard);
		boolean isMc = isHk ? false : validateMcCard(idCard);
		boolean isTw = isHk || isMc ? false : validateTwCard(idCard);
		if (isTw == false && isMc == false && isHk == false)
			return null;

		if (isHk) {
			info[0] = "香港";
			info[1] = "1";
			info[2] = "N";
			info[3] = "true";
		}
		if (isMc) {
			info[0] = "澳门";
			info[1] = "2";
			info[2] = "N";
			info[3] = "true";
		}
		if (isTw) {
			info[0] = "台湾";
			info[1] = "3";
			String c = idCard.substring(1, 2);
			if (c.equals("1"))
				info[2] = "M";
			else if (c.equals("2"))
				info[2] = "F";
			else
				info[2] = "N";
			info[3] = "true";
		}
		return info;
	}

	/**
	 * 验证台湾身份证号码（台湾）
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateTwCard(String idCard) {
		boolean rs = false;
		if (idCard == null || idCard.length() != 10)
			return rs;

		if (!idCard.matches("[A-Z][0-9]{9}"))
			return rs;

		String start = idCard.substring(0, 1);
		String mid = idCard.substring(1, 9);
		String end = idCard.substring(9, 10);

		int flag = 8;
		char[] chars = mid.toCharArray();
		int startNum = twFirstCode.get(start);
		int sum = startNum / 10 + (startNum % 10) * 9;

		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * flag;
			flag--;
		}
		rs = (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end) ? true : false;
		return rs;
	}

	/**
	 * 验证澳门身份证号码[没完成验证功能]（澳门）
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateMcCard(String idCard) {
		boolean rs = false;
		if (idCard == null || idCard.length() != 10)
			return rs;

		if (!idCard.matches("[1|5|7][0-9]{6}\\([0-9A-Z]\\)"))
			return rs;

		// TODO 验证澳门身份证号码
		return true;
	}

	/**
	 * 验证香港身份证号码（香港）
	 * 
	 * @param idCard
	 *            身份证号码
	 * @return 验证码是否符合
	 */
	public static boolean validateHkCard(String idCard) {
		boolean rs = false;
		if (idCard == null || idCard.length() != 10)
			return rs;

		if (!idCard.matches("[A-Z][0-9]{6}\\([0-9A]\\)"))
			return rs;

		String card = idCard.replaceAll("[\\(\\)]", "");
		String start = card.substring(0, 1);
		String end = card.substring(7, 8);
		String mid = card.substring(1, 7);

		int flag = 7;
		char[] chars = mid.toCharArray();
		int startNum = hkFirstCode.get(start);
		int endNum = end.equals("A") ? 10 : Integer.valueOf(end);
		int sum = startNum * 8;

		for (char c : chars) {
			sum = sum + Integer.valueOf(c + "") * flag;
			flag--;
		}
		rs = sum % 11 == 0 ? true : (11 - sum % 11 == endNum) ? true : false;
		return rs;
	}

	// -------
	/**
	 * 验证身份证是否合法（大陆港澳台）
	 */
	public static boolean validateCard(String idCard) {
		String card = idCard == null ? null : idCard.trim();
		if (validateIdCard18(card))
			return true;
		if (validateIdCard15(card))
			return true;
		String[] cardval = validateIdCard10(card);
		if (cardval != null) {
			if (cardval[2].equalsIgnoreCase("true"))
				return true;
		}
		return false;
	}

	/*-------------*/
	/**
	 * 将字符数组转换成数字数组
	 * 
	 * @param ca
	 *            字符数组
	 * @return 数字数组
	 */
	public static int[] converCharToInt(char[] ca) {
		int len = ca.length;
		int[] iArr = new int[len];
		try {
			for (int i = 0; i < len; i++) {
				iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return iArr;
	}

	/**
	 * 数字验证
	 * 
	 * @param val
	 * @return 提取的数字。
	 */
	public static boolean isNum(String val) {
		return val == null || "".equals(val) ? false : val.matches("^[0-9]*$");
	}

	/**
	 * 验证小于当前日期 是否有效
	 * 
	 * @param year
	 *            待验证日期(年)
	 * @param month
	 *            待验证日期(月 1-12)
	 * @param date
	 *            待验证日期(日)
	 * @return 是否有效
	 */
	public static boolean valiDate(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		int yearNow = cal.get(Calendar.YEAR);
		int datePerMonth;
		if (year < MAINLAND_MIN_YEAR || year >= yearNow)
			return false;

		if (month < 1 || month > 12)
			return false;

		switch (month) {
		case 4:
		case 6:
		case 9:
		case 11:
			datePerMonth = 30;
			break;
		case 2:
			boolean dm = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
			datePerMonth = dm ? 29 : 28;
			break;
		default:
			datePerMonth = 31;
		}
		return (date >= 1) && (date <= datePerMonth);
	}
}