package interfaces;

import java.nio.file.Path;
import java.util.List;

public interface Memoriser {

    Ranger memorise();
    void setPathList(List<Path> pathList);
    void setLimit(long limit);
}
