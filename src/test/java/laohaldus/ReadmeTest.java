package laohaldus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class ReadmeTest {

    // Markdown image: ![alt](path)
    private static final Pattern IMAGE = Pattern.compile("!\\[[^\\]]*\\]\\(([^)\\s]+)\\)");

    private List<String> imagePaths() throws IOException {
        String readme = Files.readString(Path.of("README.md"));
        Matcher matcher = IMAGE.matcher(readme);
        List<String> paths = new ArrayList<>();
        while (matcher.find()) {
            paths.add(matcher.group(1));
        }
        return paths;
    }

    @Test
    void readmeShowsScreenshots() throws IOException {
        assertFalse(imagePaths().isEmpty(), "README ei sisalda ühtegi pilti");
    }

    @Test
    void everyReadmeImageExists() throws IOException {
        for (String path : imagePaths()) {
            assertTrue(Files.isRegularFile(Path.of(path)), "README viitab puuduvale pildile: " + path);
        }
    }
}
