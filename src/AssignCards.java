import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignCards {
    public static Random rand = new Random();

    public static void main(String[] args){

        // load data file for pairings
        // create list object from pairings data file
        List<ParticipantPair> pairings = new ArrayList<>();

        // create TarotDeck deck object
        List<TarotCard> unassignedCards = new ArrayList<TarotCard>();

        /*
            separate pairings into covens and chivalry and either
        */
        List<ParticipantPair> covenPreferredPairs = new ArrayList<>();
        List<ParticipantPair> chivalryPreferredPairs = new ArrayList<>();
        List<ParticipantPair> noPreferencePairs = new ArrayList<>();

        for (ParticipantPair pair : pairings) {
            if (pair.preferredClass == CardClass.COVEN)
                covenPreferredPairs.add(pair);
            else if (pair.preferredClass == CardClass.CHIVALRY)
                chivalryPreferredPairs.add(pair);
            else
                noPreferencePairs.add(pair);
        }

        // create final lists for covens, chivalry, and either
        List<ParticipantPair> covens = new ArrayList<>();
        List<ParticipantPair> chivalry = new ArrayList<>();
        List<ParticipantPair> either = new ArrayList<>();


        if (covenPreferredPairs.size() <= 39){
            covens.addAll(covenPreferredPairs);
        } else {
            // sort by priority, add high priority ones into covens if they are less than 39
            // randomly select from there
        }


        if (chivalryPreferredPairs.size() <= 39){
            chivalry.addAll(chivalryPreferredPairs);
        } else {
            // sort by priority, add high priority ones into covens if they are less than 39
            // randomly select from there
        }




        for (ParticipantPair pair : chivalry) {
            assignBestCard(pair, unassignedCards);
        }
        for (ParticipantPair pair : covens) {
            assignBestCard(pair, unassignedCards);
        }


        ParticipantPair pair;
        while (!either.isEmpty()) {
            pair = either.get(rand.nextInt(either.size()));
        }


        // create list notAssignedYet with all pairings to start

        // for i < 38, assign coven cards
            // choose random card
                // loop selection until card is chosen that is not on pairing's avoid list
            // Assign artist and writer (or pairing object) to the TarotCard
            // Remove TarotCard from Deck
            // remove each pairing from notAssignedYet


        // while notAssignedYet !isEmpty, assign chivalry cards
            // choose random card
                // loop selection until card is chosen that is not on pairing's avoid list
            // remove each pairing from notAssignedYet



        /* print statistics */
        int fullMatches = 0;
        int halfMatches = 0;
        int noMatches = 0;
        int math = 0;
        for (TarotCard card : deck) {
            if (card.artist.preferredCardClass == null && card.writer.preferredCardClass == null) {
                fullMatches++;
            } else if (card.cardClass == card.artist.preferredCardClass) {
                if (card.cardClass == card.writer.preferredCardClass || card.writer.preferredCardClass == null ) {
                    fullMatches++;
                } else {
                    halfMatches++;
                }
            } else if (card.cardClass == card.writer.preferredCardClass) {
                halfMatches++;
            } else {
                noMatches++;
            }
        }
        System.out.println("*********** PAIRING STATISTICS ***********");
        System.out.println("Note: Card assignments where the participant indicated they were okay with either card class counts as a match.\n");
        System.out.println("Artist and writer both got card class they wanted: " + fullMatches);
        System.out.println("Only one of the participants got the card class they wanted: " + halfMatches);
        System.out.println("Artist and writer both got opposite card class of what they wanted: " + noMatches);

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
}
