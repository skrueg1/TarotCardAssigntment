import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignCards {
    private static Random rand = new Random();
    private static List<TarotCard> unassignedCovens;
    private static List<TarotCard> unassignedChivalry;
    private static List<ParticipantPair> pairs;

    public static void main(String[] args) {

        boolean allCardsAssigned = true;
        int outerNum = 0; // for statistics

        // inner loop attempts to assign random cards to all pairs in the pairs list
        // if no card exists to assign to the pair, the 'null' card will be assigned
        // a null card will signal that it ran into a dead end
        // or a dead end is not found, and allCardsAssigned stays true
        // continues to outer loop, where we either try again or break
        do {
            System.out.println("Outer Loop iteration: " + ++outerNum);
            repopulateLists();

            for (ParticipantPair pair : pairs) {
                assignBestCard(pair);
                if (pair.assignedCard == null) {
                    allCardsAssigned = false;
                    break;
                }
            }
        } while (!allCardsAssigned);

        printAndSaveCardAssignments();

    }

    private static void assignBestCard(ParticipantPair pair) {

        // for troubleshooting
        int loopNum = 0;

        CardClass pref = pair.preferredClass;
        TarotCard card = null;
        List<TarotCard> appropriateList;
            if (pref == CardClass.COVEN) appropriateList = unassignedCovens;
            else appropriateList = unassignedChivalry;
        List<TarotCard> temp = new ArrayList<>();

        while (true) {
            ++loopNum;

            // select random card from appropriate list
            card = appropriateList.remove(rand.nextInt(appropriateList.size())); // remove so it is not considered again if it is on the avoiders list

            // removed card is not on avoiders list, so finalize assignment and break
            if (!pair.avoiders.contains(card)) {
                card.artist = pair.artist;
                card.writer = pair.writer;
                pair.setCard(card);
                pairs.remove(pair);
                appropriateList.addAll(temp);
                temp.clear();
                System.out.println("Loops needed for pair #" + (79 - (unassignedChivalry.size() + unassignedCovens.size())) + ": " + loopNum);
                break;
            }

            // removed card is on avoiders list, check if appropriate list is now empty
            // if so, dead end found, so assign card null and break
            if (appropriateList.isEmpty()) {
                pair.setCard(null);
                break;
            }

            // otherwise, add card to temp list and continue
            temp.add(card); // list will be readded to the appropriate unassigned card list when best card is found

        }

    }

    private static void repopulateLists(){

        // clear
        unassignedChivalry.clear();
        unassignedCovens.clear();
        pairs.clear();

        // repop pairings, unassigned decks
        pairs.addAll(AssignPairings.getPairings());
        for (TarotCard card : Utilities.getTarotDeck().values()) {
            if (card.cardClass == CardClass.COVEN) unassignedCovens.add(card);
            else unassignedChivalry.add(card);
        }

    }

    public static void printAndSaveCardAssignments() {
        // Print to terminal and save them as text file
        try (PrintWriter printer = new PrintWriter("data/finalCardAssignments.txt")) {
            printer.println("Card Name:");
            for (TarotCard card : Utilities.getTarotDeck().values()) {
                printer.print(card.cardName + ": " + card.artist.name + " & " + card.writer.name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
