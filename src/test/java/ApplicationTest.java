import enums.Mime;
import org.junit.Test;

public class ApplicationTest {

    @Test (expected = IllegalArgumentException.class)
    public void applicationCreation() {

        new Application(null);
        new Application(new SimpleMemoriser(), null, Mime.TEXT_PLAIN, 10);
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), Mime.TEXT_PLAIN, 0);
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), null, 10);
    }
}