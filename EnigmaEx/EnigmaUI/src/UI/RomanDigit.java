package UI;

public enum RomanDigit {
    I (1),
    II (2),
    III (3),
    IV (4),
    V(5);

    private int value;

    RomanDigit(int i) {
        value = i;
    }



    public int getIntValue()
    {
        return value;
    }

    public static boolean isRomanDig(String str)
    {
        for(RomanDigit romanDig : RomanDigit.values()) {
            if (romanDig.name().compareTo(str) == 0)
                return true;
        }
        return false;
    }

    public static RomanDigit getRomanDigByIntVal(int val) {
        for (RomanDigit romanDig : RomanDigit.values()) {
            if (romanDig.value == val) {
                return romanDig;
            }
        }
        return null;
    }
}
