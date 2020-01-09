import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NioSearch {

//    public static void main(String[] args) throws IOException {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }
//        String grepfor = args[0];
//        Path path = Paths.get(args[1]);
//
//        String report = searchFor(grepfor, path);
//        System.out.println(report);
//
//    }

    private static final int MAPSIZE = 4 * 1024 ; // 4K - make this * 1024 to 4MB in a real system.

    private static String searchFor(String grepfor, Path path) throws IOException {

        final byte[] toSearch = grepfor.getBytes(StandardCharsets.UTF_8);
        StringBuilder report = new StringBuilder();
        int padding = 1; // need to scan 1 character ahead in case it is a word boundary.
        int lineCount = 0;
        int matchesCount = 0;
        boolean isInsideWord = false;
        boolean isLineOrWordEnd = false;

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {

            final long channelLength = channel.size();
            int channelPtrPosition = 0;

            while (channelPtrPosition < channelLength) {

                long remaining = channelLength - channelPtrPosition;
                // int conversion is safe because of a safe MAPSIZE.. Assume a reaosnably sized toSearch.
                int tryQuotSize = MAPSIZE + toSearch.length + padding;
                int quotSize = (int)Math.min(tryQuotSize, remaining);
                // different limits depending on whether we are the last mapped segment.
                int limit = tryQuotSize == quotSize ? MAPSIZE : (quotSize - toSearch.length);
                MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, channelPtrPosition, quotSize);
                System.out.println("Mapped from " + channelPtrPosition + " for " + quotSize);
                channelPtrPosition += (tryQuotSize == quotSize) ? MAPSIZE : quotSize;

                for (int i = 0; i < limit; i++) {

                    final byte b = buffer.get(i);
                    if (isLineOrWordEnd) {
                        if (b == '\n') {
                            isLineOrWordEnd = false;
                            isInsideWord = false;
                            lineCount ++;
                        }
                    } else if (b == '\n') {
                        lineCount++;
                        isInsideWord = false;
                    } else if (b == '\r' || b == ' ') {
                        isInsideWord = false;
                    } else if (!isInsideWord) {
                        if (wordMatch(buffer, i, quotSize, toSearch)) {
                            matchesCount++;
                            i += toSearch.length - 1;
                            if (report.length() > 0) {
                                report.append(", ");
                            }
                            report.append(lineCount);
                            isLineOrWordEnd = true;
                        } else {
                            isInsideWord = true;
                        }
                    }
                }
            }
        }
        return "Times found at--" + matchesCount + "\nWord found at--" + report;
    }

    private static boolean wordMatch(MappedByteBuffer buffer, int index, int quotSize, byte[] toSearch) {

        //assume at valid word start.
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i] != buffer.get(index + i)) {
                return false;
            }
        }
        byte byteToGo = (index + toSearch.length) == quotSize ? (byte)' ' : buffer.get(index + toSearch.length);
        return byteToGo == ' ' || byteToGo == '\n' || byteToGo == '\r';
    }
}
