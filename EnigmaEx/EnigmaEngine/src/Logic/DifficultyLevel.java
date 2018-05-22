package Logic;

public enum DifficultyLevel {
    Easy('E'),
    Medium('M'),
    Hard('H'),
    Impossible('I');

    private Character value;

    DifficultyLevel(Character ch) {
        value = ch;
    }



    public int getIntValue()
    {
        return value;
    }

    public static boolean isDifficultyLevel(String str)
    {
        if(str.length() != 1)
            return false;
        else
        {
            for (DifficultyLevel difficulty : DifficultyLevel.values()) {
                if (difficulty.value == str.charAt(0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static DifficultyLevel getDifficultyLevelByCharVal(Character val) {
        for (DifficultyLevel difficulty : DifficultyLevel.values()) {
            if (difficulty.value == val) {
                return difficulty;
            }
        }
        return null;
    }
}
