import java.util.ArrayList;

public class Writer extends Participant {
    Artist partner1;
    Artist partner2;

    public Writer() {
        this.name = null;
        this.preferredCardClass = null;
        this.partner1 = null;
        this.partner2 = null;
        this.avoiders = new ArrayList<>();
        this.assignedCard = null;
    }

    public Writer (String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString(){
        // name,PREFERENCE,partner1,partner2,"avoiders",assigned card
        return  this.name + "," +
                Utilities.preferenceToString(this.preferredCardClass) + "," +
                partnerNameToString(this.partner1) + "," +
                partnerNameToString(this.partner2) + ",\"" +
                Utilities.avoidersToString(this.avoiders) + "\"," +
                Utilities.cardNameToString(this.assignedCard);
    }
}
