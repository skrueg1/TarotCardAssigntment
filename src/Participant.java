import java.util.ArrayList;
import java.util.List;

public class Participant {
    String name;
    CardClass preferredCardClass;
    List<TarotCard> avoiders = new ArrayList<>();
    TarotCard assignedCard;

    public Participant() {
        this.name = null;
        this.preferredCardClass = null;
        this.assignedCard = null;
    }
}
