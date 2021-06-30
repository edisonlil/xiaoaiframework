package com.xiaoaiframework.util.base;

import java.sql.Time;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * JDK1.8 本地时间工具类
 * @author edison
 */
public final class LocalDateTimeUtil {

    private LocalDateTimeUtil(){}

    /**
     * 获取当前时间
     * @return
     */
    public static LocalDateTime now(){
        return LocalDateTime.now();
    }

    /**
     * 获取当前时间
     * @param currentTimeMillis
     * @return
     */
    public static LocalDateTime getLocalDateTime(Long currentTimeMillis) {
        if(currentTimeMillis == null){return null;}
        return dateToLocalDateTime(new Date(currentTimeMillis));
    }

    /**
     * 获取当前时间
     * @return
     */
    public static LocalTime getLocalTime(Long currentTimeMillis) {
        if(currentTimeMillis == null){return null;}
        return new Time(currentTimeMillis).toLocalTime();
    }

    /**
     * 获取某天开始的时间
     * @param localDateTime
     * @return
     */
    public static LocalDateTime beginOfDay(LocalDateTime localDateTime){
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取某天结束的时间
     * @param localDateTime
     * @return
     */
    public static LocalDateTime endOfDay(LocalDateTime localDateTime){
        return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
    }

    /**
     * 获取今天开始的时间
     * @param
     * @return
     */
    public static LocalDateTime nowBeginOfDay(){
        return LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取今天结束的时间
     * @return
     */
    public static LocalDateTime nowEndOfDay(){
        return LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX);
    }

    /**
     * 获取到下一天开始
     * @return
     */
    public static LocalDateTime nextBeginOfDay(){
        return offsetDays(beginOfDay(LocalDateTime.now()),1);
    }

    /**
     * 获取到下一天结束
     * @return
     */
    public static LocalDateTime nextEndOfDay(){
        return offsetDays(endOfDay(LocalDateTime.now()),1);
    }



    /**
     * 向指定毫秒数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetMillis(LocalDateTime localDateTime,Integer offset){
        LocalTime localTime = localDateTime.toLocalTime();
        return LocalDateTime.of(localDateTime.toLocalDate(),localTime.plus(offset, ChronoUnit.MILLIS));
    }

    /**
     * 向指定秒数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetSeconds(LocalDateTime localDateTime,Integer offset){
        LocalTime localTime = localDateTime.toLocalTime();
        return LocalDateTime.of(localDateTime.toLocalDate(),localTime.plus(offset, ChronoUnit.SECONDS));
    }

    /**
     * 向指定分钟数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetMinutes(LocalDateTime localDateTime,Integer offset){
        LocalTime localTime = localDateTime.toLocalTime();
        return LocalDateTime.of(localDateTime.toLocalDate(),localTime.plus(offset, ChronoUnit.MINUTES));
    }

    /**
     * 向指定小时数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetHours(LocalDateTime localDateTime,Integer offset){
        LocalTime localTime = localDateTime.toLocalTime();
        return LocalDateTime.of(localDateTime.toLocalDate(),localTime.plus(offset, ChronoUnit.HOURS));
    }

    /**
     * 向指定天数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetDays(LocalDateTime localDateTime,Integer offset){
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(localDate.plus(offset,ChronoUnit.DAYS),localDateTime.toLocalTime());
    }

    /**
     * 向指定周数(7天为一个单位)偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetWeeks(LocalDateTime localDateTime,Integer offset){
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(localDate.plus(offset,ChronoUnit.WEEKS),localDateTime.toLocalTime());
    }

    /**
     * 向指定月数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetMonths(LocalDateTime localDateTime,Integer offset){
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(localDate.plus(offset,ChronoUnit.MONTHS),localDateTime.toLocalTime());
    }

    /**
     * 向指定年数偏移
     * @param localDateTime
     * @param offset
     * @return
     */
    public static LocalDateTime offsetYears(LocalDateTime localDateTime,Integer offset){
        LocalDate localDate = localDateTime.toLocalDate();
        return LocalDateTime.of(localDate.plus(offset,ChronoUnit.YEARS),localDateTime.toLocalTime());
    }


    /**
     * 计算两个时间相差的毫秒数
     * @param begin
     * @param end
     * @return
     */
    public static Long betweenMillis(LocalDateTime begin,LocalDateTime end){
        return Duration.between(begin,end).toMillis();
    }

    /**
     * 计算两个时间相差秒数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenSeconds(LocalDateTime begin,LocalDateTime end){
        return Duration.between(begin,end).toSeconds();
    }

    /**
     * 计算两个时间相差分数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenMinutes(LocalDateTime begin,LocalDateTime end){
        return Duration.between(begin,end).toMinutes();
    }

    /**
     * 计算两个时间相差小时数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenHours(LocalDateTime begin,LocalDateTime end){
        return Duration.between(begin,end).toHours();
    }

    /**
     * 计算两个时间相差天数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenDays(LocalDateTime begin,LocalDateTime end){
        return Duration.between(begin,end).toDays();
    }


    /**
     * 获得今天所剩的毫秒数
     * @return
     */
    public static Long getResidueMillis(){
        return betweenMillis(LocalDateTime.now(),nowEndOfDay());
    }

    /**
     * 获得今天所剩的秒数
     * @return
     */
    public static Long getResidueSeconds(){
        return betweenSeconds(LocalDateTime.now(),LocalDateTime.MAX);
    }

    /**
     * 获得今天所剩的分钟数
     * @return
     */
    public static Long getResidueMinutes(){
        return betweenMinutes(LocalDateTime.now(),LocalDateTime.MAX);
    }

    /**
     * 获得今天所剩的小时数
     * @return
     */
    public static Long getResidueHours(){
        return betweenHours(LocalDateTime.now(),LocalDateTime.MAX);
    }



    /**
     * 获取今天是周几
     * @return
     */
    public static int getToDayOfWeekValue(){
        return LocalDate.now().getDayOfWeek().getValue();
    }

    /**
     * 获取今天是周几
     * @return
     */
    public static DayOfWeek getToDayOfWeek(){
        return LocalDate.now().getDayOfWeek();
    }

    /**
     * 获取指定时间是周几
     * @param localDateTime
     * @return
     */
    public static int getWeekValue(LocalDateTime localDateTime){
        return localDateTime.toLocalDate().getDayOfWeek().getValue();
    }

    /**
     * 获取指定时间是周几
     * @param localDateTime
     * @return
     */
    public static DayOfWeek getWeek(LocalDateTime localDateTime){
        return localDateTime.toLocalDate().getDayOfWeek();
    }

    /**
     * Date转 LocalDateTime
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        //An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        //A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant,zoneId);
    }

    /**
     * Date转 LocalDate
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        //An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        Instant instant = date.toInstant();
        //A time-zone ID, such as {@code Europe/Paris}.(时区)
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDate.ofInstant(instant,zoneId);
    }

    /**
     * LocalDateTime转换为Date
     * @param localDateTime
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        //Combines this date-time with a time-zone to create a  ZonedDateTime.
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

}

