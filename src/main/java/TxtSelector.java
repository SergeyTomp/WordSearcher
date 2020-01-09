import interfaces.Selector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Demonstration example of Selector interface instantiation with Mime fileType set via initialisation.
 * For flexibility Mime fileType could be set after Selector instance is created */
public class TxtSelector implements Selector {

    private String[] args;
    @NotNull
    private Mime fileType = Mime.TEXT_PLAIN;

    public TxtSelector(@NotNull String[] args) {
        assert args != null;
        this.args = args;
    }

    public TxtSelector(@NotNull String[] args, @NotNull Mime fileType) {
        this.args = args;
        this.fileType = fileType;
    }

    public void setFileType(@NotNull Mime fileType) {
        this.fileType = fileType;
    }

    @Override
    public List<Path> select() {

        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        List<Path> pathsList = new ArrayList<>();
        Arrays.stream(args).forEach(p -> {
            Path path = FileSystems.getDefault().getPath(p);
            if (Files.exists(path, LinkOption.NOFOLLOW_LINKS) && Files.isDirectory(path)) {
                try {
                    Files.newDirectoryStream(path).forEach(f -> {
                        String type = fileNameMap.getContentTypeFor(f.toUri().toString());
                        if ((!Files.isDirectory(f)) && type != null && type.contains(fileType.getFileType())) {
                            pathsList.add(f);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return pathsList;
    }
}
