package com.xiaoaifreamwork.util.base;

import com.xiaoaiframework.util.base.LocalDateTimeUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试JDK1.8 本地时间工具类
 * @author lee
 */
public class LocalDateTimeUtilTest {

    /**
     * 获取当前时间
     */
    @Test
    public void getLocalDateTime(){
       System.out.println(LocalDateTimeUtil.getLocalDateTime(System.currentTimeMillis()));
    }

    /**
     * 获取当前时间
     */
    @Test
    public void getLocalTime(){
        System.out.println(LocalDateTimeUtil.getLocalTime(System.currentTimeMillis()));
    }

    /**
     * 获取某天开始的时间
     */
    @Test
    public void beginOfDay(){
        System.out.println(LocalDateTimeUtil.beginOfDay(LocalDateTime.now()));
    }

    /**
     * 获取某天结束的时间
     */
    @Test
    public void endOfDay(){
        System.out.println(LocalDateTimeUtil.endOfDay(LocalDateTime.now()));
    }

    /**
     * 获取今天开始的时间
     */
    @Test
    public void nowBeginOfDay(){
        System.out.println(LocalDateTimeUtil.nowBeginOfDay(LocalDateTime.now()));
    }

    /**
     * 获取今天结束的时间
     */
    @Test
    public void nowEndOfDay(){
        System.out.println(LocalDateTimeUtil.nowEndOfDay(LocalDateTime.now()));
    }

    /**
     * 获取到下一天开始
     */
    @Test
    public void nextBeginOfDay(){
        System.out.println(LocalDateTimeUtil.nextBeginOfDay());
    }

    /**
     * 获取到下一天结束
     */
    @Test
    public void nextEndOfDay(){
        System.out.println(LocalDateTimeUtil.nextEndOfDay());
    }

    /**
     * 向指定毫秒数偏移
     */
    @Test
    public void offsetMillis(){
        System.out.println(LocalDateTimeUtil.offsetMillis(LocalDateTime.now(),1));
    }

    /**
     * 向指定秒数偏移
     */
    @Test
    public void offsetSeconds(){
        System.out.println(LocalDateTimeUtil.offsetSeconds(LocalDateTime.now(),1));
    }

    /**
     * 向指定分钟数偏移
     */
    @Test
    public void offsetMinutes(){
        System.out.println(LocalDateTimeUtil.offsetMinutes(LocalDateTime.now(),1));
    }

    /**
     * 向指定小时数偏移
     */
    @Test
    public void offsetHours(){
        System.out.println(LocalDateTimeUtil.offsetHours(LocalDateTime.now(),1));
    }

    /**
     * 向指定天数偏移
     */
    @Test
    public void offsetDays(){
        System.out.println(LocalDateTimeUtil.offsetDays(LocalDateTime.now(),1));
    }

    /**
     * 向指定周数(7天为一个单位)偏移
     */
    @Test
    public void offsetWeeks(){
        System.out.println(LocalDateTimeUtil.offsetWeeks(LocalDateTime.now(),1));
    }

    /**
     * 向指定月数偏移
     */
    @Test
    public void offsetMonths(){
        System.out.println(LocalDateTimeUtil.offsetMonths(LocalDateTime.now(),1));
    }

    /**
     * 向指定年数偏移
     */
    @Test
    public void offsetYears(){
        System.out.println(LocalDateTimeUtil.offsetYears(LocalDateTime.now(),1));
    }

    /**
     * 计算两个时间相差的毫秒数
     */
    @Test
    public void betweenMillis(){

        LocalDateTime localDateTime =  LocalDateTime.parse("2020-11-11T16:17:00");

        System.out.println(LocalDateTimeUtil.betweenMillis(localDateTime,LocalDateTime.now()));
    }

    /**
     * 计算两个时间相差秒数
     */
    @Test
    public void betweenSeconds(){

        LocalDateTime localDateTime =  LocalDateTime.parse("2020-11-11T16:17:00");

        System.out.println(LocalDateTimeUtil.betweenSeconds(localDateTime,LocalDateTime.now()));
    }

    /**
     * 计算两个时间相差分数
     */
    @Test
    public void betweenMinutes(){

        LocalDateTime localDateTime =  LocalDateTime.parse("2020-11-11T16:17:00");

        System.out.println(LocalDateTimeUtil.betweenMinutes(localDateTime,LocalDateTime.now()));
    }

    /**
     * 计算两个时间相差小时数
     */
    @Test
    public void betweenHours(){

        LocalDateTime localDateTime =  LocalDateTime.parse("2020-11-11T16:17:00");

        System.out.println(LocalDateTimeUtil.betweenHours(localDateTime,LocalDateTime.now()));
    }

    /**
     * 计算两个时间相差天数
     */
    @Test
    public void betweenDays(){

        LocalDateTime localDateTime =  LocalDateTime.parse("2020-10-11T16:17:00");

        System.out.println(LocalDateTimeUtil.betweenDays(localDateTime,LocalDateTime.now()));
    }

    /**
     * 获得今天所剩的毫秒数
     */
    @Test
    public void getResidueMillis(){
        System.out.println(LocalDateTimeUtil.getResidueMillis());
    }

    /**
     * 获得今天所剩的秒数
     */
    @Test
    public void getResidueSeconds(){
        System.out.println(LocalDateTimeUtil.getResidueSeconds());
    }

    /**
     * 获得今天所剩的分数
     */
    @Test
    public void getResidueMinutes(){
        System.out.println(LocalDateTimeUtil.getResidueMinutes());
    }

    /**
     * 获得今天所剩的小时数
     */
    @Test
    public void getResidueHours(){
        System.out.println(LocalDateTimeUtil.getResidueHours());
    }

    /**
     * 获取今天是周几
     */
    @Test
    public void getToDayOfWeekValue(){
        System.out.println(LocalDateTimeUtil.getToDayOfWeekValue());
    }

    /**
     * 获取今天是周几
     */
    @Test
    public void getToDayOfWeek(){
        System.out.println(LocalDateTimeUtil.getToDayOfWeek());
    }

    /**
     * 获取指定时间是周几
     */
    @Test
    public void getWeekValue(){
        LocalDateTime localDateTime =  LocalDateTime.parse("2020-10-11T16:17:00");
        System.out.println(LocalDateTimeUtil.getWeekValue(localDateTime));
    }

    /**
     * 获取指定时间是周几
     */
    @Test
    public void getWeek(){
        LocalDateTime localDateTime =  LocalDateTime.parse("2020-10-11T16:17:00");
        System.out.println(LocalDateTimeUtil.getWeek(localDateTime));
    }

    /**
     *  Date转 LocalDateTime
     */
    @Test
    public void dateToLocalDateTime(){
        Date date = new Date();
        System.out.println(LocalDateTimeUtil.dateToLocalDateTime(date));
    }

    /**
     *  Date转 LocalDate
     */
    @Test
    public void dateToLocalDate(){
        Date date = new Date();
        System.out.println(LocalDateTimeUtil.dateToLocalDate(date));
    }

    /**
     *  LocalDateTime转换为Date
     */
    @Test
    public void localDateTimeToDate(){
        LocalDateTime localDateTime =  LocalDateTime.parse("2020-10-11T16:17:00");
        System.out.println(LocalDateTimeUtil.localDateTimeToDate(localDateTime));
    }







}
