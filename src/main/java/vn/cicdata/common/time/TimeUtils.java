package vn.cicdata.common.time;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author namdd9 06/10/2018
 */
public class TimeUtils {

  public static final long FIVE_SEC_IN_MILIS = 5 * 1000L;
  public static final long TWENTY_SEC_IN_MILIS = 20 * 1000L;
  public static final long ONE_MIN_IN_MILIS = 1 * 60 * 1000L;
  public static final long FIVE_MIN_IN_MILIS = 5 * 60 * 1000L;
  public static final long ONE_HOUR_IN_MILIS = 60 * 60 * 1000L;
  public static final long SEVEN_HOUR_IN_MILIS = 7 * 60 * 60 * 1000L;
  public static final long ONE_DAY_IN_MILIS = 24 * 60 * 60 * 1000L;
  public static final long ONE_WEEK_IN_MILIS = 7 * 24 * 60 * 60 * 1000L;

  public static int getFiveSecondsId(long time) {
    return (int) (time / (5 * 1000));
  }

  public static int getOneMinutesId(long time) {
    return (int) (time / (1 * 60 * 1000));
  }

  public static int getFiveMinutesId(long time) {
    return (int) (time / (5 * 60 * 1000));
  }

  public static int getHourId(long time) {
    return (int) (time / (1 * 60 * 60 * 1000));
  }

  public static int getDayId(long time) {
    return (int) (time / (24 * 60 * 60 * 1000));
  }

  public static long getTwentySecRoundTime(long time) {
    return time - (time % TWENTY_SEC_IN_MILIS);
  }

  public static long getFiveMinRoundTime(long time) {
    return time - (time % FIVE_MIN_IN_MILIS);
  }

  public static long getFiveSecRoundTime(long time) {
    return time - (time % FIVE_SEC_IN_MILIS);
  }

  public static long getOneMinRoundTime(long time) {
    return time - (time % ONE_MIN_IN_MILIS);
  }

  public static long getHourRoundTime(long time) {
    return time - (time % ONE_HOUR_IN_MILIS);
  }

  public static synchronized long getDayRoundTime(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTimeInMillis();
  }
  public static synchronized String getTodayString() {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd");
    return sdf.format(new Date(System.currentTimeMillis()));
  }



  public static synchronized int getWeekOfYear(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    calendar.setMinimalDaysInFirstWeek(4);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    return calendar.get(Calendar.WEEK_OF_YEAR);
  }

  public static synchronized long getWeekRoundTime(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  public static Map<String, List<Long>> getWeekListOfMonth(long time) {
    long start = getMonthRoundTime(time);
    long end = getMonthLastDay(start);
    Map<Long, List<Long>> weekMap = new HashMap<>();
    for (long t = start; t <= end; t += ONE_DAY_IN_MILIS) {
      long weekRoundTime = getWeekRoundTime(t);
      List<Long> lst = weekMap.get(weekRoundTime);
      if (lst == null) {
        lst = new ArrayList();
        weekMap.put(weekRoundTime, lst);
      }
      lst.add(t);
    }

    Map<String, List<Long>> res = new HashMap<>();
    for (Map.Entry<Long, List<Long>> entry : weekMap.entrySet()) {
      long key = entry.getKey();
      List<Long> value = entry.getValue();

      if (value.size() < 7) {
        List<Long> lstTime = res.get("day");
        if (lstTime == null) {
          lstTime = new ArrayList();
          res.put("day", lstTime);
        }
        lstTime.addAll(value);
      } else {
        List<Long> lstTime = res.get("week");
        if (lstTime == null) {
          lstTime = new ArrayList();
          res.put("week", lstTime);
        }
        lstTime.add(key);
      }
    }
    return res;
  }

  public static synchronized long getMonthRoundTime(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    calendar.set(Calendar.DATE, 1);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  public static int convertTime(int timeId, TIME_CONVERT_MODE mode) {
    switch (mode) {
      case FIVE_MIN_ID_2_HOUR_ID:
        return timeId / 12;
      case FIVE_MIN_ID_2_DAY_ID:
        return timeId / (12 * 24);
      case HOUR_ID_2_DAY_ID:
        return timeId / 24;
      default:
        return 0;
    }
  }

  public static long convertTimeVer2(long time, TIME_CONVERT_MODE mode) {
    switch (mode) {
      case FIVE_MIN_ID_2_HOUR_ID:
        return getHourRoundTime(time);
      case FIVE_MIN_ID_2_DAY_ID:
        return getDayRoundTime(time);
      case HOUR_ID_2_DAY_ID:
        return getDayRoundTime(time);
      default:
        return 0;
    }
  }

  /**
   * Convert timeId with change timezone, add SEVEN_HOURS
   */
  public static int convertTimeWithZone(int timeId, TIME_CONVERT_MODE mode) {
    switch (mode) {
      case FIVE_MIN_ID_2_HOUR_ID:
        return getHourId(timeId * FIVE_MIN_IN_MILIS + SEVEN_HOUR_IN_MILIS);
      case FIVE_MIN_ID_2_DAY_ID:
        return getDayId(timeId * FIVE_MIN_IN_MILIS + SEVEN_HOUR_IN_MILIS);
      case HOUR_ID_2_DAY_ID:
        return getDayId(timeId * ONE_HOUR_IN_MILIS + SEVEN_HOUR_IN_MILIS);
      default:
        return 0;
    }
  }

  /**
   * get last week monday
   */
  public static long getLastWeekMonday(long timeId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeId - 7 * ONE_DAY_IN_MILIS);
    calendar.setFirstDayOfWeek(Calendar.MONDAY);
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  /**
   * get first day of last month
   */
  public static long getLastMonthFirstDay(long timeId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeId);
    calendar.add(Calendar.MONTH, -1);
    calendar.set(Calendar.DATE, 1);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  /**
   * get first day of next month
   */
  public static long getNextMonthFirstDay(long timeId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeId);
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DATE, 1);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  /**
   * get last day of last month
   */
  public static long getLastMonthLastDay(long timeId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeId);
    calendar.set(Calendar.DATE, 1);
    calendar.add(Calendar.DATE, -1);
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  /**
   * get last day of this month
   */
  public static long getMonthLastDay(long timeId) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeId);
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    return getDayRoundTime(calendar.getTimeInMillis());
  }

  public static List<Long> getMonthList(long fromTime, long toTime) {
    List<Long> res = new ArrayList<>();
    long fromRoundTime = getMonthRoundTime(fromTime);
    while (fromRoundTime <= toTime) {
      res.add(fromRoundTime);
      fromRoundTime = getNextMonthFirstDay(fromRoundTime);
    }
    return res;
  }

  public static List<Long> getWeekList(long fromTime, long toTime) {
    SortedSet<Long> res = new TreeSet<>();
    long fromRoundTime = TimeUtils.getDayRoundTime(fromTime);
    long toRoundTime = TimeUtils.getDayRoundTime(toTime) + TimeUtils.ONE_DAY_IN_MILIS;
    for (long i = fromRoundTime; i <= toRoundTime; i += TimeUtils.ONE_DAY_IN_MILIS) {
      res.add(getWeekRoundTime(i));
    }
    return new ArrayList<>(res);
  }

  /**
   * get list of day from range
   */
  public static List<Long> getDaysList(long fromTime, long toTime, int hour) {
    List<Long> res = new ArrayList<>();
    long count = (toTime - fromTime) / ONE_DAY_IN_MILIS + 1;
    for (long i = 0; i < count; i++) {
      long temp = getDayRoundTime(fromTime) + i * ONE_DAY_IN_MILIS + hour * ONE_HOUR_IN_MILIS;
      res.add(temp);
    }
    return res;
  }

  /**
   * get list of hour from range
   */
  public static List<Long> getHoursList(long day) {
    List<Long> res = new ArrayList<>();
    for (long i = 0; i < 23; i++) {
      long temp = getDayRoundTime(day) + i * ONE_HOUR_IN_MILIS;
      res.add(temp);
    }
    return res;
  }

  /**
   * get list of time from range for getkqi
   */
  public static List<Long> getTimeList(long fromTime, long toTime, TimeUnit timeUnit, int hour) {

    // Calculate for case of hour in days
    if (hour >= 0) {
      return getDaysList(fromTime, toTime, hour);
    } else {
      long unitTimeInMilis;
      List<Long> res = new ArrayList<>();
//			if(timeUnit == TimeUnit.FIVE_MIN)
//			{
//				unitTimeInMilis =  TimeUnit.getTimeMilis(TimeUnit.ONE_HOUR);
//			}
//			else
//			{
//				unitTimeInMilis = TimeUnit.getTimeMilis(timeUnit);
//			}
      unitTimeInMilis = TimeUnit.getTimeMilis(timeUnit);

      long fromRoundTime = TimeUtils.getHourRoundTime(fromTime);
      long toRoundTime = TimeUtils.getHourRoundTime(toTime) + TimeUtils.ONE_HOUR_IN_MILIS;

      for (long i = fromRoundTime; i < toRoundTime; i = i + unitTimeInMilis) {
        res.add(i);
      }
      return res;
    }

  }

  /**
   * get list of time from range for getkqi
   */
  public static List<Long> getTimeList(long fromTime, long toTime, TimeUnit timeUnit) {
    long unitTimeInMilis;
    List<Long> res = new ArrayList<>();
    unitTimeInMilis = TimeUnit.getTimeMilis(timeUnit);

    long fromRoundTime = TimeUtils.getHourRoundTime(fromTime);
    long toRoundTime = TimeUtils.getHourRoundTime(toTime) + TimeUtils.ONE_HOUR_IN_MILIS;

    for (long i = fromRoundTime; i < toRoundTime; i = i + unitTimeInMilis) {
      res.add(i);
    }
    return res;
  }

  public static enum TIME_CONVERT_MODE {

    FIVE_MIN_ID_2_HOUR_ID,
    FIVE_MIN_ID_2_DAY_ID,
    HOUR_ID_2_DAY_ID
  }


}

