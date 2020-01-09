import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TxtSelectorTest {

    @Test
    public void testSelector() {

        final String F1 = "tst1.txt";
        final String F2 = "tst2.txt";
        final String DIR = "testFiles";
        final String SEPARATOR = File.separator;
        final String PATH_TO_DIR = "." + SEPARATOR + DIR + SEPARATOR;
        final Path path1 = Paths.get(PATH_TO_DIR + F1);
        final Path path2 = Paths.get(PATH_TO_DIR + F2);
        Path file1 = null;
        Path file2 = null;

        try {
            Files.createDirectories(path1.getParent());
            Files.createDirectories(path2.getParent());
            file1 = Files.createFile(path1);
            file2 = Files.createFile(path2);

            final String[] path = {PATH_TO_DIR};
            List<Path> select = new TxtSelector(path).select();
            assert select.size() == 2;

            TxtSelector selector = new TxtSelector(path);
            selector.setFileType(Mime.TEXT_CSV);
            select = selector.select();
            assert select.size() == 0;

            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(file1 != null) Files.deleteIfExists(file1);
                if(file2 != null) Files.deleteIfExists(file2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(expected = AssertionError.class)
    public void testSelectorNullArray() {

        new TxtSelector(null);
    }
}