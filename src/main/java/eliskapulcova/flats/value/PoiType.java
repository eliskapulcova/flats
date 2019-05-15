package eliskapulcova.flats.value;

public enum PoiType {
    UNDERGROUND("Metro"),
    TRAM("Tram"),
    BUS("Bus MHD"),
    TRAIN("Vlak"),
    ATM("Bankomat"),
    POST("Pošta"),
    PHARMACY("Lékárna"),
    SPORT_FIELD("Sportoviště"),
    RESTAURANT("Restaurace"),
    SHOP("Obchod"),
    SCHOOL("Škola");


    private final String sRealityPoiName;

    PoiType(String sRealityPoiName) {

        this.sRealityPoiName = sRealityPoiName;
    }

    public static PoiType fromString(String value) {
        for (PoiType poiType : PoiType.values()) {
            if (poiType.sRealityPoiName.equalsIgnoreCase(value)) {
                return poiType;
            }
        }

        throw new IllegalArgumentException();

    }

    public String getsRealityPoiName() {
        return sRealityPoiName;
    }
}

