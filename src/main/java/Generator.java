import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    private enum PasswordLength {SHORT,MEDIUM, LONG,SUPER,CRAZY}
    private static PasswordLength passwordLength;
    private static final int lowLen = 5, mediumLen = 10, highLen = 15, superLen = 20, crazyLen = 25;

    // ================================== Singleton =======================================
    // static variable single_instance of type Singleton
    private static Generator single_instance = null;
    // static method to create instance of Singleton class
    public static Generator getInstance()
    {
        if (single_instance == null) {
            single_instance = new Generator();
        }

        return single_instance;
    }
    // ====================================================================================
    public Generator(){
        setPasswordLen(1);
    }

    public String generateCode(String leadingCharacters){
        // leading characters option
        boolean leadingCharHuh = true;
        if(leadingCharacters.equals("(Optional)") || leadingCharacters.equals("")){
            leadingCharHuh = false;
        }
        // set up initial values
        int lenDiff,codeLen;
        String code;
        List<String> codeList = new LinkedList<>();

        code = NameBase.getInstance().getRandomName();
        // if needed, find a word that satisfies the leading character requirement first
        while(leadingCharHuh){
            if(code.substring(0,leadingCharacters.length()).equals(leadingCharacters)) leadingCharHuh = false;
            else code = NameBase.getInstance().getRandomName();
        }
        // add the first word to list
        codeList.add(code);
        codeLen = code.length();
        // loop until less than three characters are needed to fulfill the length requirement
        while((lenDiff = moreHuh(codeLen)) > 3){
            code = NameBase.getInstance().getRandomName();
            codeLen += code.length();
            codeList.add(code);
        }
        // if needed, add non-alphanumeric characters until fulfilling the length requirement
        while(lenDiff > 0){
            int randomNum = ThreadLocalRandom.current().nextInt(0,codeList.size());
            codeList.set(randomNum ,codeList.get(randomNum)+ NameBase.getInstance().getRandomNonAlphabet());
            lenDiff --;
        }
        // build the output back
        code = "";
        for (String aString : codeList){ code += aString; }
        return code;
    }

    private int moreHuh(int len){
        switch(passwordLength){
            case SHORT:
                return (len < lowLen)? lowLen - len : 0;
            case MEDIUM:
                return (len < mediumLen)? mediumLen - len : 0;
            case LONG:
                return (len < highLen)? highLen - len : 0;
            case SUPER:
                return (len < superLen)? superLen - len : 0;
            case CRAZY:
                return (len < crazyLen)? crazyLen - len : 0;
            default:
                return 3; // yaaaaaaayy! more!!!
        }
    }

    public void setPasswordLen(int passwordLen){
        switch(passwordLen){
            case 0:
                passwordLength = PasswordLength.SHORT;
                break;
            case 1:
                passwordLength = PasswordLength.MEDIUM;
                break;
            case 2:
                passwordLength = PasswordLength.LONG;
                break;
            case 3:
                passwordLength = PasswordLength.SUPER;
                break;
            default:
                passwordLength = PasswordLength.CRAZY;
        }
    }
}
