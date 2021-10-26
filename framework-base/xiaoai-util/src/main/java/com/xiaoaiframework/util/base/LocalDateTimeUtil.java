package com.xiaoaiframework.util.base;

import com.xiaoaiframework.util.math.MathUtil;

import java.sql.Time;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.zone.ZoneRules;
import java.util.Date;
import java.util.Objects;

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
        return ChronoUnit.MILLIS.between(begin,end);
    }

    /**
     * 计算两个时间相差秒数
     * @param begin
     * @param end
     * @return
     */
    public static Long betweenSeconds(LocalDateTime begin,LocalDateTime end){
        return ChronoUnit.SECONDS.between(begin,end);
    }

    /**
     * 计算两个时间相差分数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenMinutes(LocalDateTime begin,LocalDateTime end){
        return ChronoUnit.MILLIS.between(begin,end);
    }

    /**
     * 计算两个时间相差小时数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenHours(LocalDateTime begin,LocalDateTime end){
        return ChronoUnit.HOURS.between(begin,end);
    }

    /**
     * 计算两个时间相差天数
     * @param begin
     * @param end
     * @since 1.9+
     * @return
     */
    public static Long betweenDays(LocalDateTime begin,LocalDateTime end){
        return ChronoUnit.DAYS.between(begin,end);
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
        return betweenSeconds(LocalDateTime.now(),nowEndOfDay());
    }

    /**
     * 获得今天所剩的分钟数
     * @return
     */
    public static Long getResidueMinutes(){
        return betweenMinutes(LocalDateTime.now(),nowEndOfDay());
    }

    /**
     * 获得今天所剩的小时数
     * @return
     */
    public static Long getResidueHours(){
        return betweenHours(LocalDateTime.now(),nowEndOfDay());
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
        return localDateTimeOfInstant(instant,zoneId);
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
        return localDateOfInstant(instant,zoneId);
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


    /**
     *
     * @param instant
     * @param zone
     * @return
     */
    public static LocalDate localDateOfInstant(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zone, "zone");
        ZoneRules rules = zone.getRules();
        ZoneOffset offset = rules.getOffset(instant);
        long localSecond = instant.getEpochSecond() + (long)offset.getTotalSeconds();
        long localEpochDay = MathUtil.floorDiv(localSecond, 86400);
        return ofEpochDay(localEpochDay);
    }


    public static LocalDateTime localDateTimeOfInstant(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zone, "zone");
        ZoneRules rules = zone.getRules();
        ZoneOffset offset = rules.getOffset(instant);
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano(), offset);
    }


    public static LocalDateTime ofEpochSecond(long epochSecond, int nanoOfSecond, ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        ChronoField.NANO_OF_SECOND.checkValidValue((long)nanoOfSecond);
        long localSecond = epochSecond + (long)offset.getTotalSeconds();
        long localEpochDay = Math.floorDiv(localSecond, 86400);
        int secsOfDay = Math.floorMod(localSecond, 86400);
        LocalDate date = LocalDate.ofEpochDay(localEpochDay);
        LocalTime time = LocalTime.ofNanoOfDay((long)secsOfDay * 1000000000L + (long)nanoOfSecond);
        return LocalDateTime.of(date, time);
    }


    /**
     *
     * @param epochDay
     * @return
     */
    public static LocalDate ofEpochDay(long epochDay) {
        ChronoField.EPOCH_DAY.checkValidValue(epochDay);
        long zeroDay = epochDay + 719528L;
        zeroDay -= 60L;
        long adjust = 0L;
        long yearEst;
        if (zeroDay < 0L) {
            yearEst = (zeroDay + 1L) / 146097L - 1L;
            adjust = yearEst * 400L;
            zeroDay += -yearEst * 146097L;
        }

        yearEst = (400L * zeroDay + 591L) / 146097L;
        long doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
        if (doyEst < 0L) {
            --yearEst;
            doyEst = zeroDay - (365L * yearEst + yearEst / 4L - yearEst / 100L + yearEst / 400L);
        }

        yearEst += adjust;
        int marchDoy0 = (int)doyEst;
        int marchMonth0 = (marchDoy0 * 5 + 2) / 153;
        int month = (marchMonth0 + 2) % 12 + 1;
        int dom = marchDoy0 - (marchMonth0 * 306 + 5) / 10 + 1;
        yearEst += (long)(marchMonth0 / 10);
        int year = ChronoField.YEAR.checkValidIntValue(yearEst);
        return LocalDate.of(year, month, dom);
    }

}

