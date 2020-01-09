import enums.Mime;
import org.junit.Test;

public class SearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void testApplication() {
        new Application(null).start();
        new Application(new SimpleMemoriser(), null, Mime.TEXT_PLAIN, 10).start();
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), Mime.TEXT_PLAIN, 0).start();
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), null, 10).start();
    }
}