public class TarotCard {
    String cardName;
    CardClass cardClass;
    Artist artist;
    Writer writer;

    public TarotCard(String name, CardClass cclass) {
        this.cardName = name;
        this.cardClass = cclass;
        this.artist = null;
        this.writer = null;
    }

}
