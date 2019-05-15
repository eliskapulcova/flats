package eliskapulcova.flats.value;

public enum ParamType {
    AREA("Užitná plocha"),
    FLOOR("Podlaží"),
    BALCONY("Balkón"),
    ELEVATOR("Výtah"),
    CELLAR("Sklep"),
    GARAGE("Garáž"),
    ACCOMMODATION("Vybavení");


    private final String sRealityName;

    ParamType(String sRealityName) {

        this.sRealityName = sRealityName;
    }

    public static ParamType fromString(String value) {
        for (ParamType paramType : ParamType.values()) {
            if (paramType.sRealityName.equalsIgnoreCase(value)) {
                return paramType;
            }
        }

        throw new IllegalArgumentException();

    }

    public String getsRealityName() {
        return sRealityName;
    }
}
