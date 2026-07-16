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

    public void setAvoiders() {
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

        this.avoiders = avoiders;
    }

    public void setCard(TarotCard card){
        this.assignedCard = card;
        artist.assignedCard = card;
        writer.assignedCard = card;
    }

    public void setPriority(){
        CardClass aPref = artist.preferredCardClass;
        CardClass wPref = writer.preferredCardClass;

        if ((wPref != null && aPref != null) && (wPref == aPref))
            this.priority = 1;
        else if ((wPref == null && aPref != null) || (wPref != null && aPref == null))
            this.priority = 2;
        else
            this.priority = 3;
    }

}
