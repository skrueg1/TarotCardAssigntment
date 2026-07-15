import java.awt.List;

public class Participant {
    String name;
    CardClass preferredCardClass;
    List<TarotCard> avoiders;
    TarotCard assignedCard;

    public void setName(String name) {
        this.name = name;
    }

    public void setPreferredCardClass(CardClass preferredCardClass) {
        this.preferredCardClass = preferredCardClass;
    }

    public void setAvoidedCards(List avoidedCards) {
        this.avoiders = avoidedCards;
    }
}
