import java.util.ArrayList;
import java.util.List;

public class Participant {
    String name;
    CardClass preferredCardClass;
    List<TarotCard> avoiders;
    TarotCard assignedCard;

    public Participant() {
        this.name = null;
        this.preferredCardClass = null;
        this.assignedCard = null;
        this.avoiders = new ArrayList<>();
    }

    public String partnerNameToString(Participant partner) {
        if (partner == null) { return "null"; }
        return partner.name;
    }
}
