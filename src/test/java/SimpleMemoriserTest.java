import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SimpleMemoriserTest {

    @Test (expected = IllegalArgumentException.class)
    public void testPathListNullability() {
        new SimpleMemoriser(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRangedListLengthNegation() {
        new SimpleMemoriser(new ArrayList<>(), - 10);
    }

    @Test
    public void rangerTest() {

        final String F1 = "tst1.txt";
        final String F2 = "tst2.txt";
        final String F3 = "tst3.txt";
        final String F4 = "tst4.txt";
        final String F5 = "tst5.txt";
        final String DIR = "testFiles";
        final String SEPARATOR = File.separator;
        final String PATH_TO_DIR = "." + SEPARATOR + DIR + SEPARATOR;
        final int LIMIT = 4;
        Path file1 = null;
        Path file2 = null;
        Path file3 = null;
        Path file4 = null;
        Path file5 = null;

        final Path path1 = Paths.get(PATH_TO_DIR + F1);
        final Path path2 = Paths.get(PATH_TO_DIR + F2);
        final Path path3 = Paths.get(PATH_TO_DIR + F3);
        final Path path4 = Paths.get(PATH_TO_DIR + F4);
        final Path path5 = Paths.get(PATH_TO_DIR + F5);
        final List<Path> pathList = new ArrayList<>(Arrays.asList(path1, path2, path3, path4, path5));

        try {
            Files.createDirectories(path1.getParent());
            Files.createDirectories(path2.getParent());
            Files.createDirectories(path3.getParent());
            Files.createDirectories(path4.getParent());
            Files.createDirectories(path5.getParent());
            file1 = Files.createFile(path1);
            file2 = Files.createFile(path2);
            file3 = Files.createFile(path3);
            file4 = Files.createFile(path4);
            file5 = Files.createFile(path5);

            try(BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1.toString(), false));
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2.toString(), false));
                    BufferedWriter bw3 = new BufferedWriter(new FileWriter(file3.toString(), false));
                    BufferedWriter bw4 = new BufferedWriter(new FileWriter(file4.toString(), false));
                    BufferedWriter bw5 = new BufferedWriter(new FileWriter(file5.toString(), false))){

                bw1.write("to be or not to be");
                bw2.write("to or not");
                bw3.write("or not");
                bw4.write("fsddfhfghjgh fdghgj");
                bw5.write("xcvdfdfgnfgn fdghgj");
            }

            Map<String, Integer> ranges = new SimpleMemoriser(pathList, LIMIT)
                    .memorise()
                    .range("to be or not to be");

            assert ranges.size() == LIMIT;

            ArrayList<Map.Entry<String, Integer>> entries = new ArrayList<>(ranges.entrySet());

            assert entries.get(0).getKey().equals(F1);
            assert entries.get(0).getValue() == 100;
            assert entries.get(1).getKey().equals(F2);
            assert entries.get(1).getValue() == 75;
            assert entries.get(2).getKey().equals(F3);
            assert entries.get(2).getValue() == 50;
            assert entries.get(3).getKey().equals(F4);
            assert entries.get(3).getValue() == 0;

            Files.deleteIfExists(file1);
            Files.deleteIfExists(file2);
            Files.deleteIfExists(file3);
            Files.deleteIfExists(file4);
            Files.deleteIfExists(file5);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(file1 != null) Files.deleteIfExists(file1);
                if(file2 != null) Files.deleteIfExists(file2);
                if(file3 != null) Files.deleteIfExists(file3);
                if(file4 != null) Files.deleteIfExists(file4);
                if(file5 != null) Files.deleteIfExists(file5);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}