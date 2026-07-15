import java.util.ArrayList;
import java.util.List;

public class ParticipantPair {
    Artist artist;
    Writer writer;
    CardClass preferredClass;
    List<TarotCard> avoiders;
    TarotCard assignedCard;
    int priority;

    public ParticipantPair(Writer writer, Artist artist) {
        this.artist = artist;
        this.writer = writer;
        this.preferredClass = null;
        this.avoiders = null;
        this.assignedCard = null;
        this.priority = 0;
    }

    public ParticipantPair(Writer writer, Artist artist, CardClass preferredClass) {
        this.artist = artist;
        this.writer = writer;
        this.preferredClass = preferredClass;
        this.avoiders = null;
        this.assignedCard = null;
        this.priority = 0;
    }

    public List<TarotCard> setAvoiders() {
        List<TarotCard> avoiders = new ArrayList<>();

        for (TarotCard card : artist.avoiders) {
            if (card == null) break;
            avoiders.add(card);
        }
        for (TarotCard card : writer.avoiders) {
            if (card == null) break;
            if (avoiders.contains(card)) continue;
            avoiders.add(card);
        }
    }

    public void setCard(TarotCard card){
        this.assignedCard = card;
        artist.assignedCard = card;
        writer.assignedCard = card;
    }

    public void setPriority(){

    }

}
