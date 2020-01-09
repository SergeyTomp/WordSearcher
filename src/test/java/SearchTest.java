import org.junit.Test;

public class SearchTest {

    @Test(expected = IllegalArgumentException.class)
    public void testApplication() {
        new Application(null).start();
        new Application(new SimpleMemoriser(), null, 10).start();
        new Application(new SimpleMemoriser(), new TxtSelector(new String[10]), 0).start();
    }
}