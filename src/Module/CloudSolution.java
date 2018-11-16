package Module;

import jcolibri.cbrcore.Attribute;
import jcolibri.cbrcore.CaseComponent;

public class CloudSolution implements CaseComponent {
    String name;

    @Override
    public Attribute getIdAttribute() {
        return new Attribute("Name", this.getClass());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}