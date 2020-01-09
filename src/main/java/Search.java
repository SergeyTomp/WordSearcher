import interfaces.Memoriser;
import interfaces.Selector;

public class Search {

    public static void main(String[] args) {

        //TODO: constants below could be placed in Properties
        final Mime FILE_TYPE = Mime.TEXT_PLAIN;
        final long LIMIT = 10;

        if (args.length == 0) {
            System.err.println("No directories specified, please try again, exit application!");
            System.exit(0);
        }

        Selector selector = new TxtSelector(args, FILE_TYPE);
        Memoriser memoriser = new SimpleMemoriser();

        new Application(memoriser, selector, LIMIT).start();
    }
}
