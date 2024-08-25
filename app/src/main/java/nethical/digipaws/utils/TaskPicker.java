package nethical.digipaws.utils;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class TaskPicker {

    private final Random random = new Random();
    private final Context context;

    public TaskPicker(Context context) {
        this.context = context;
    }

    public String getRandomTask() throws IOException {
        long lineCount = getFileLineCount();
        int randomLine = random.nextInt((int) lineCount);

        return fetchTaskFromLine(randomLine);
    }

    private long getFileLineCount() throws IOException {
        try (InputStream is = context.getAssets().open("tasks.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().count();
        }
    }

    private String fetchTaskFromLine(int lineNumber) throws IOException {
        try (InputStream is = context.getAssets().open("tasks.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String task = null;
            for (int i = 0; i <= lineNumber; i++) {
                task = reader.readLine();  // Read until the random line
            }
            return task;
        }
    }
}
