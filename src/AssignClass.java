import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AssignClass {
    public static Random rand = new Random();
    public static List<ParticipantPair> pairings = new ArrayList<>();
    public static List<ParticipantPair> covens = new ArrayList<>();
    public static List<ParticipantPair> chivalry = new ArrayList<>();

    public static void main(String[] args){

        // populate pairings list
        pairings.addAll(AssignPairings.getPairings());

        // separate pairings into covens and chivalry and either
        List<ParticipantPair> covenPreferredPairs = new ArrayList<>();
        List<ParticipantPair> chivalryPreferredPairs = new ArrayList<>();
        List<ParticipantPair> noPreferencePairs = new ArrayList<>();

        for (ParticipantPair pair : pairings) {
            if (pair.preferredClass == CardClass.COVEN)
                covenPreferredPairs.add(pair);
            else if (pair.preferredClass == CardClass.CHIVALRY )
                chivalryPreferredPairs.add(pair);
            else
                noPreferencePairs.add(pair);
        }

        ParticipantPair tempPair = null;

        /*
            If there are less than 40 coven-preferring pairs: add them all to coven
            else (40 or more):
                + randomly select from highest priority pairings (both participants want Coven)
                  until high priority list is empty or covens fills up
                + randomly select from medium priority pairings (one participant wants Coven, the other has no preference)
                  until medium priority list is empty or covens fills up
         */
        if (covenPreferredPairs.size() <= 39){
            covens.addAll(covenPreferredPairs);
            covenPreferredPairs.clear();
        } else {
            List<ParticipantPair> firstPriority = Utilities.getPairsByPriority(covenPreferredPairs, 1);
            List<ParticipantPair> secondPriority = Utilities.getPairsByPriority(covenPreferredPairs, 2);

            while (!firstPriority.isEmpty() || !secondPriority.isEmpty()) {
                if (covens.size() == 39) break;

                if (!firstPriority.isEmpty())
                    tempPair = firstPriority.remove(rand.nextInt(firstPriority.size()));
                else
                    tempPair = secondPriority.remove(rand.nextInt(secondPriority.size()));

                covenPreferredPairs.remove(tempPair);
                covens.add(tempPair);
            }
        }

        /*
            If there are less than 40 chivalry-preferring pairs: add them all to chivalry
            else (40 or more):
                + randomly select from highest priority pairings (both participants want Chivalry)
                  until high priority list is empty or chivalry fills up
                + randomly select from medium priority pairings (one participant wants Chivalry, the other has no preference)
                  until medium priority list is empty or chivalry fills up
        */
        if (chivalryPreferredPairs.size() <= 39){
            chivalry.addAll(chivalryPreferredPairs);
            chivalryPreferredPairs.clear();
        } else {
            List<ParticipantPair> firstPriority = Utilities.getPairsByPriority(chivalryPreferredPairs, 1);
            List<ParticipantPair> secondPriority = Utilities.getPairsByPriority(chivalryPreferredPairs, 2);

            while (!firstPriority.isEmpty() || !secondPriority.isEmpty()) {
                if (chivalry.size() == 39) break;

                if (!firstPriority.isEmpty())
                    tempPair = firstPriority.remove(rand.nextInt(firstPriority.size()));
                else
                    tempPair = secondPriority.remove(rand.nextInt(secondPriority.size()));

                chivalryPreferredPairs.remove(tempPair);
                chivalry.add(tempPair);
            }
        }

        // assign remainders
        while (covens.size() < 39) {
            if (noPreferencePairs.isEmpty()) break;
            tempPair = noPreferencePairs.remove(rand.nextInt(noPreferencePairs.size()));
            covens.add(tempPair);
        }
        while (chivalry.size() < 39) {
            if (noPreferencePairs.isEmpty()) break;
            tempPair = noPreferencePairs.remove(rand.nextInt(noPreferencePairs.size()));
            chivalry.add(tempPair);
        }

       printStatistics();

    }

    public static void printStatistics() {
        int fullMatches = 0;
        int halfMatches = 0;
        int noMatches = 0;
        int math = 0;

        for (ParticipantPair pair : covens) {
            if (pair.artist.preferredCardClass != CardClass.CHIVALRY) ++math;
            if (pair.writer.preferredCardClass != CardClass.CHIVALRY) ++math;

            if (math == 2) fullMatches++;
            else if (math == 1) halfMatches++;
            else noMatches++;

            math = 0;
        }

        for (ParticipantPair pair : chivalry) {
            if (pair.artist.preferredCardClass != CardClass.COVEN) ++math;
            if (pair.writer.preferredCardClass != CardClass.COVEN) ++math;

            if (math == 2) fullMatches++;
            else if (math == 1) halfMatches++;
            else noMatches++;

            math = 0;
        }

        /* Print */
        System.out.println("*********** CLASS ASSIGNMENT STATISTICS ***********");
        System.out.println("Note: Card assignments where the participant indicated they were okay with either card class counts as a match.\n");
        System.out.println("Artist and writer both got card class they wanted: " + fullMatches);
        System.out.println("Only one of the participants got the card class they wanted: " + halfMatches);
        System.out.println("Artist and writer both got opposite card class of what they wanted: " + noMatches);
    }
}
