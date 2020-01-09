package interfaces;

import enums.Mime;

import java.nio.file.Path;
import java.util.List;

public interface Selector {

    List<Path> select();
    void setFileType(Mime fileType);
}
