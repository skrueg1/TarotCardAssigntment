import java.util.ArrayList;
import java.util.List;

public class ParticipantPair {
    public Artist artist;
    public Writer writer;
    public CardClass preferredClass;
    public List<TarotCard> avoiders;
    public TarotCard assignedCard;
    int priority;

    public ParticipantPair(Writer writer, Artist artist) {
        this.artist = artist;
        this.writer = writer;
        this.preferredClass = null;
        this.avoiders = new ArrayList<>();
        this.assignedCard = null;
        this.priority = 0;
    }

    public ParticipantPair(Writer writer, Artist artist, CardClass preferredClass) {
        this.artist = artist;
        this.writer = writer;
        this.preferredClass = preferredClass;
        this.avoiders = new ArrayList<>();
        this.assignedCard = null;
        this.priority = 0;
    }

    public void setAvoiders() {

        for (TarotCard card : artist.avoiders) {
            this.avoiders.add(card);
        }
        for (TarotCard card : writer.avoiders) {
            if (!this.avoiders.contains(card)) this.avoiders.add(card);
        }

    }

    public void setCard(TarotCard card){
        this.assignedCard = card;
        this.artist.assignedCard = card;
        this.writer.assignedCard = card;
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

    @Override
    public String toString(){
        // artist,writer,PREFERENCE,"avoiders",assigned card,priority
        return  this.artist.name + "," +
                this.writer.name + "," +
                Utilities.preferenceToString(this.preferredClass) + ",\"" +
                Utilities.avoidersToString(this.avoiders) + "\"," +
                Utilities.cardNameToString(this.assignedCard) + "," +
                this.priority;
    }

}
