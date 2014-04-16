package info.interactivesystems.spade.crawler.temp;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import org.testng.annotations.Test;

@Slf4j
public class TempTest {
    private static final SimpleDateFormat YELP_DATE = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
    private final Pattern starPattern = Pattern.compile("\\d[.\\d]*");

    @Test
    public void f() throws ParseException {

        Date date = YELP_DATE.parse("August 1, 2013");
        Calendar calendar = new GregorianCalendar(2013, 8, 1);
        Date is = calendar.getTime();

        assertThat(date).isEqualTo(is);

    }

    @Test
    public void t() {
        System.out.println(System.getProperty("hostname"));
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
