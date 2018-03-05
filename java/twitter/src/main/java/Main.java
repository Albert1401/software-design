import sd.ifmo.ru.twitter.api.TwitterApi;
import sd.ifmo.ru.twitter.api.TwitterApiImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class Main {
    public static void main(String[] args) {
        TwitterApi api = null;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("keys.txt"))) {
            api = new TwitterApiImpl(
                    reader.readLine().trim(),
                    reader.readLine().trim(),
                    reader.readLine().trim(),
                    reader.readLine().trim()
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        HotTags tags = new HotTags(api);
        if (args.length != 1) {
            System.out.println("expected hashtag as first argument");
            return;
        }
        int[] freqs = tags.getFrequencies(args[0]);
        System.out.println("Frequencies:");
        System.out.println(Arrays.toString(freqs));
    }
}
