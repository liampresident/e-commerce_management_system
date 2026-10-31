package test;

public enum AccessoryType {
    dice,
    miniature,
    accessory_kit;

    @Override
    public String toString() {
        return switch (this) {
            case dice -> "dice";
            case miniature -> "miniature";
            case accessory_kit -> "accessory kit";
        };
    }
    public static AccessoryType fromString(String s) {
    	return switch (s) {
        case  "dice" -> dice;
        case  "miniature" -> miniature;
        case   "accessory kit" -> accessory_kit;
        default -> null;
    };
    }
}
