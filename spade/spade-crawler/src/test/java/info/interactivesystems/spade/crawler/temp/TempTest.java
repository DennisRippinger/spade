package info.interactivesystems.spade.crawler.temp;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.testng.annotations.Test;

public class TempTest {
    private static final SimpleDateFormat YELP_DATE = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void f() throws ParseException {

        Date date = YELP_DATE.parse("7/6/2013");
        Calendar calendar = new GregorianCalendar(2013, 7, 6);
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
