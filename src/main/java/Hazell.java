import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Hazell {
    public static void reply(String msg) {
        String DIVIDER = "\t____________________________________________________________";
        System.out.println(DIVIDER);
        for (String line : msg.split("\n")) {
            System.out.println("\t" + line);
        }
        System.out.println(DIVIDER);
    }
    public static void main(String[] args) {
        String logo = "  _    _               _ _ \n"
                + " | |  | |             | | |\n"
                + " | |__| | __ _ _______| | |\n"
                + " |  __  |/ _` |_  / _ \\ | |\n"
                + " | |  | | (_| |/ /  __/ | |\n"
                + " |_|  |_|\\__,_/___\\___|_|_|\n";
        System.out.println(logo);

        List<String> store = new ArrayList<>();

        reply("Hello, I am Hazell!\nWhat can I do for you?");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String userinput = scanner.nextLine().strip();
            if (userinput.equals("bye")) {
                reply("Bye. Hope to see you again soon!");
                System.exit(0);
            } else if (userinput.equals("list")) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < store.size(); i++) {
                    String item = store.get(i);
                    sb.append(String.format("%d. %s", i + 1, item));
                    if (i != store.size() - 1) sb.append("\n");
                }
                reply(sb.toString());
            } else {
                store.add(userinput);
                reply(String.format("added: %s", userinput));
            }
        }
    }
}
