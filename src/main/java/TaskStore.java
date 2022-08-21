import exceptions.NoSuchTask;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class that stores Tasks.
 */
public class TaskStore {
    private final List<Task> store;
    private final FileWriter writer;

    public TaskStore() {
        this.store = new ArrayList<>();
        try {
            this.writer = new FileWriter(getAndInitialiseFilePath().toFile(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a specified task using an index (1-indexed).
     * @param index
     * @return The `index`-th task
     */
    public Task getTask(int index) throws NoSuchTask {
        try {
            return this.store.get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchTask(this.size());
        }
    }

    private String getSummary() {
        return String.format("Now you have %d tasks in the list.", this.store.size());
    }

    public String addTask(Task task) {
        this.store.add(task);
        try {
            this.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Got it. I've added this task:\n\t%s", task.toString()));
        sb.append("\n");
        sb.append(this.getSummary());
        return sb.toString();
    }

    public String deleteTask(int index) throws NoSuchTask {
        Task task;
        try {
            task = this.store.remove(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchTask(this.size());
        }
        try {
            this.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Noted. I've removed this task:\n\t%s", task.toString()));
        sb.append("\n");
        sb.append(this.getSummary());
        return sb.toString();
    }

    public String markTaskAsDone(int index) throws NoSuchTask {
        Task task = getTask(index);
        task.markAsDone();
        try {
            this.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.format("Nice! I've marked this task as done:\n\t%s", task);
    }

    public String markTaskAsUndone(int index) throws NoSuchTask {
        Task task = getTask(index);
        task.markAsUndone();
        try {
            this.saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.format("OK, I've marked this task as not done yet:\n\t%s", task);
    }

    /**
     * Returns a Path object pointing to the file used to store Hazell data locally.
     *
     * Before doing so, it ensures that the folder exists.
     * @return Path to file used by Hazell chatbot
     * @throws IOException
     */
    private static Path getAndInitialiseFilePath() throws IOException {
        String currentDir = System.getProperty("user.dir");
        Path dataDir = Paths.get(currentDir, "data");
        if (!Files.exists(dataDir)) {
            Files.createDirectory(dataDir);
        }
        return Paths.get(currentDir, "data", "hazell.txt");
    }

    public void saveToFile() throws IOException {
        FileWriter writer = new FileWriter(getAndInitialiseFilePath().toFile(), false);
        for (Task task : this.store) {
            writer.write(task.serialise());
            writer.write("\n");
        }
        writer.close();
    }

    public static TaskStore createFromFile() throws IOException {
        Scanner sc = new Scanner(getAndInitialiseFilePath().toFile());
        TaskStore store = new TaskStore();
        while (sc.hasNextLine()) {
            Task task = Task.unserialise(sc.nextLine().strip());
            store.store.add(task);
        }
        return store;
    }

    /**
     * List detailed information about all tasks in this store.
     * @return String Details
     */
    @Override
    public String toString() {
        if (this.size() == 0) {
            return "You have no tasks, please create one!";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.store.size(); i++) {
            Task task = null;
            try {
                task = this.getTask(i);
            } catch (NoSuchTask e) {
                // This block will never be executed as we are looping within the size of store.
            }
            sb.append(String.format("%d. %s", i + 1, task.toString()));
            if (i != this.size() - 1) sb.append("\n");
        }
        return sb.toString();
    }

    public int size() {
        return this.store.size();
    }
}
