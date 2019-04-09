package vn.cicdata.common.submit;

import vn.cicdata.common.io.Config;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Submit {
    /**
     * args(0): file config, args(1): file submit
     * */
    public static void main(String[] args) throws URISyntaxException, IOException, ParseException, InterruptedException {

        String currentPath = getCurrentPath();
        Logger logger = Logger.getLogger(Submit.class.getName());
        FileHandler fh;
        fh = new FileHandler("log.log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        Properties config = new Properties();
        config.load(Config.class.getClassLoader().getResourceAsStream(args[0]));
        String startDateS = config.getProperty("input.start.date");
        System.out.println("start running: "  + startDateS);
        String endDateS = config.getProperty("input.end.date");
        String inputPath = config.getProperty("input.path.rawdata.cdr");


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startDateS);
        Date endDate = sdf.parse(endDateS);
        Calendar cStart = Calendar.getInstance();
        cStart.setTime(startDate);
        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(endDate);


        while (cStart.before(cEnd)) { //add one day to date
            String dateString = sdf.format(cStart.getTime());
            String inputPathFull = inputPath + "/date=" + dateString;
            logger.info("start read path: " + inputPathFull);
            ProcessBuilder pb = new ProcessBuilder(currentPath + "/bin/" + args[1],args[0] + " " + dateString);
            Process p = pb.start();
            p.waitFor();
            cStart.add(Calendar.DAY_OF_MONTH, 1);

        }
    }

    public static String getCurrentPath() throws URISyntaxException {
        File f = new File(Submit.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        File dir = f.getAbsoluteFile().getParentFile();
        return  dir.toString();
    }
}
