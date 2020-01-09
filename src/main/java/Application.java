import interfaces.Memoriser;
import interfaces.Ranger;
import interfaces.Selector;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Application {

    private Memoriser memoriser;
    private Selector selector;
    private long LIMIT;

    public Application(@NotNull Memoriser memoriser) {

        if(memoriser == null) throw new IllegalArgumentException("Memoriser instance can't be null!");
        this.memoriser = memoriser;
    }

    public Application(@NotNull Memoriser memoriser, @NotNull Selector selector, long LIMIT) {

        if(memoriser == null) throw new IllegalArgumentException("Memoriser instance can't be null!");
        this.memoriser = memoriser;
        if(selector == null) throw new IllegalArgumentException("Selector instance can't be null!");
        this.selector = selector;
        if (LIMIT < 0) { throw new IllegalArgumentException("LIMIT can't be zero or negative"); }
        this.LIMIT = LIMIT;
    }

    public void start() {

        List<Path> pathsList = selector.select();

        if (pathsList.size() == 0) {

            ByteArrayOutputStream err = new ByteArrayOutputStream();
            System.setErr(new PrintStream(err));

            System.err.println("No text files found, please try again, exit application!");
            System.err.println("Out was: " + err.toString());
            return;
        }

        System.out.println("Files found for processing:");
        pathsList.forEach(p -> System.out.println(p.getFileName()));

        memoriser.setPathList(pathsList);
        memoriser.setLimit(LIMIT);

        Ranger ranger = memoriser.memorise();
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        System.out.println("Type the sequence and press Enter for searching or Ctrl+C to exit application");
        System.out.print("search > ");

        while (sc.hasNext()) {

            ranger.range(sc.nextLine()).forEach((k, v) -> sb.append(k).append(": ").append(v).append("\n"));
            System.out.println(sb.toString());
            System.out.print("search > ");
            sb.setLength(0);
        }
    }
}
