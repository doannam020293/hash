package vn.cicdata.common.time;



/**
 * @author thiendn2
 *
 * Created on Aug 17, 2016, 2:55:41 PM
 */
public enum TimeUnit {

  FIVE_MIN, ONE_HOUR, ONE_DAY, ONE_WEEK, ONE_MONTH, PEAK, PEAK_OTHER;

  public static byte getCQPrefix(TimeUnit tu) {
    switch (tu) {
      case FIVE_MIN:
        return 'm';
      case ONE_HOUR:
        return 'h';
      case ONE_DAY:
        return 'd';
      case ONE_WEEK:
        return 'w';
      case ONE_MONTH:
        return 'M';
      case PEAK:
        return 'p';
      case PEAK_OTHER:
        return 'P';
      default:
        return ' ';
    }
  }


  public static TimeUnit fromByteCqPrefix(byte cq) {
    switch (cq) {
      case 'm':
        return FIVE_MIN;
      case 'h':
        return ONE_HOUR;
      case 'd':
        return ONE_DAY;
      case 'w':
        return ONE_WEEK;
      case 'M':
        return ONE_MONTH;
      case 'p':
        return PEAK;
      case 'P':
        return PEAK_OTHER;
      default:
        return ONE_DAY;
    }
  }

  public static long getTimeMilis(TimeUnit tu) {
    switch (tu) {
      case FIVE_MIN:
        return 5 * 60 * 1000L;
      case ONE_HOUR:
        return 1 * 60 * 60 * 1000L;
      case ONE_DAY:
        return 24 * 60 * 60 * 1000L;
      case ONE_WEEK:
        return 7 * 24 * 60 * 60 * 1000L;
      case ONE_MONTH:
        return 30 * 24 * 60 * 60 * 1000L;
      case PEAK:
        return 24 * 60 * 60 * 1000L;
      case PEAK_OTHER:
        return 24 * 60 * 60 * 1000L;
      default:
        return 1 * 60 * 60 * 1000L;
    }
  }

  public static TimeUnit getTimeUnitFromString(String timeUnit) {
    switch (timeUnit) {
      case "5m":
        return TimeUnit.FIVE_MIN;
      case "1h":

      case "0hd":

      case "1hd":

      case "2hd":

      case "3hd":

      case "4hd":

      case "5hd":

      case "6hd":

      case "7hd":

      case "8hd":

      case "9hd":

      case "10hd":

      case "11hd":

      case "12hd":

      case "13hd":

      case "14hd":

      case "15hd":

      case "16hd":

      case "17hd":

      case "18hd":

      case "19hd":

      case "20hd":

      case "21hd":

      case "22hd":

      case "23hd":
        return TimeUnit.ONE_HOUR;
      case "1d":
        return TimeUnit.ONE_DAY;
      case "1w":
        return TimeUnit.ONE_WEEK;
      case "1mo":
        return TimeUnit.ONE_MONTH;
      case "1pe":
        return TimeUnit.PEAK;
      case "1po":
        return TimeUnit.PEAK_OTHER;
      default:
        return TimeUnit.ONE_DAY; // default TimeUnit
    }
  }
}
