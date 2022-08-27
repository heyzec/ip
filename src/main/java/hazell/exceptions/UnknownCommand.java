package hazell.exceptions;

public class UnknownCommand extends HazellException {
    @Override
    public String toString() {
        return "☹ OOPS!!! I'm sorry, but I don't know what that means :-(";
    }
}
