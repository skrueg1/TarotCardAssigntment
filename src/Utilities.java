import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utilities {
    public static HashMap<String, TarotCard> deck;

    public static CardClass determinePairPreference(Writer writer, Artist artist){
        CardClass writerPref = writer.preferredCardClass;
        CardClass artistPref = artist.preferredCardClass;

        // participants agree on class preference -> return it
        if (writerPref == artistPref) return writerPref;

        // writer has no preference, artist has preference -> return artist's preference
        if ((writerPref == null) && (artistPref != null)) return artistPref;

        // writer has preference, artist does not -> return writer's preference
        if ((writerPref != null) && (artistPref == null)) return writerPref;

        // neither participants have preference -> return no preference (null)
        return null;
    }

    public static List<ParticipantPair> sortPairsByPriority(List<ParticipantPair> pairs){
        List<ParticipantPair> first = new ArrayList<>();
        List<ParticipantPair> second = new ArrayList<>();
        List<ParticipantPair> third = new ArrayList<>();

        for (ParticipantPair pair : pairs){
            if (pair.priority == 1) first.add(pair);
            else if (pair.priority == 2) second.add(pair);
            else if (pair.priority == 3) third.add(pair);
        }

        first.addAll(second);
        first.addAll(third);

        return first;
    }

    public static List<ParticipantPair> getPairsByPriority(List<ParticipantPair> pairs, int priorityLevel){
        List<ParticipantPair> newlist = new ArrayList<>();
        for (ParticipantPair pair : pairs){
            if (pair.priority == priorityLevel) newlist.add(pair);
        }
        return newlist;
    }

    public static HashMap<String, TarotCard> getTarotDeck() {
        deck = new HashMap<>();
        String file = "data/deck.csv";
        String line;

        CardClass cardClass = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values[1].equals("Coven")) cardClass = CardClass.COVEN;
                else cardClass = CardClass.CHIVALRY;

                deck.put(values[0],new TarotCard(values[0], cardClass));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Confirming deck size: " + deck.size());
        return deck;
    }

    /****** TOSTRING METHODS ******/

    public static String avoidersToString(List<TarotCard> avoiders) {
        if (avoiders == null || avoiders.isEmpty()) return "null";
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (TarotCard card : avoiders) {
            sb.append(card.cardName);
            ++i;
            if (card != avoiders.getLast()) sb.append(",");
        }
        return sb.toString();
    }

    public static String cardNameToString(TarotCard card) {
        if (card == null) return "null";
        return card.cardName;
    }

    public static String preferenceToString(CardClass preference) {
        if (preference == null) return "null";
        return preference.toString();
    }

}
