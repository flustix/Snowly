package flustix.fluxifyed.components;

public class Autocomplete {
    private final String name;
    private final String value;

    public Autocomplete(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Autocomplete autocomplete) {
            return autocomplete.getName().equals(name) && autocomplete.getValue().equals(value);
        }
        return false;
    }
}
