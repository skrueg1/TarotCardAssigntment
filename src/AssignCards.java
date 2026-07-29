import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AssignCards {
    public static Random rand = new Random();
    public static HashMap<String, TarotCard> deck;

    public static void run() {

        deck = Utilities.getTarotDeck();


    }

    public static void assignBestCard(ParticipantPair pair, List<TarotCard> unassignedCards){
        CardClass pref = pair.preferredClass;
        TarotCard card = null;
        boolean conflictFound = true;

        while (conflictFound) {
            card = unassignedCards.get(rand.nextInt(unassignedCards.size()));

            if(pref != null && card.cardClass != pref) continue;

            if (!(pair.avoiders.contains(card))) {
                conflictFound = false;
            }
        }

        card.artist = pair.artist;
        card.writer = pair.writer;
        pair.setCard(card);
        unassignedCards.remove(card);
    }

    public static void printStatistics() {
        int fullMatches = 0;
        int halfMatches = 0;
        int noMatches = 0;

        /*
        for (TarotCard card : deck) {
            if (card.artist.preferredCardClass == null && card.writer.preferredCardClass == null) {
                fullMatches++;
            } else if (card.cardClass == card.artist.preferredCardClass) {
                if (card.cardClass == card.writer.preferredCardClass || card.writer.preferredCardClass == null) {
                    fullMatches++;
                } else {
                    halfMatches++;
                }
            } else if (card.cardClass == card.writer.preferredCardClass) {
                halfMatches++;
            } else {
                noMatches++;
            }
        } */

        /* Print */
        System.out.println("*********** PAIRING STATISTICS ***********");
        System.out.println("Note: Card assignments where the participant indicated they were okay with either card class counts as a match.\n");
        System.out.println("Artist and writer both got card class they wanted: " + fullMatches);
        System.out.println("Only one of the participants got the card class they wanted: " + halfMatches);
        System.out.println("Artist and writer both got opposite card class of what they wanted: " + noMatches);
    }
}
