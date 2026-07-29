import java.util.ArrayList;

public class Artist extends Participant {
    Writer partner;

    public Artist() {
        this.name = null;
        this.preferredCardClass = null;
        this.partner = null;
        this.avoiders = new ArrayList<>();
        this.assignedCard = null;
    }

    public Artist (String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString(){
        // name,PREFERENCE,partner,"avoiders",assigned card
        return this.name + "," +
               Utilities.preferenceToString(this.preferredCardClass) + "," +
               partnerNameToString(this.partner) + ",\"" +
               Utilities.avoidersToString(this.avoiders) + "\"," +
               Utilities.cardNameToString(this.assignedCard);
    }

}
