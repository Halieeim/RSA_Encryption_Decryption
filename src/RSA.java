import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Scanner;
import java.util.ArrayList;
public class RSA {

    public String binToHex(String input) {
        String output = "";
        int n = input.length() / 4;
        ArrayList<String> blocks = splitText(input,4);
        for(String block : blocks){
            output = output.concat(Long.toHexString(Long.parseUnsignedLong(block,2)));
        }
        while (output.length() < n)
            output = "0" + output;
        return output;
    }
    public String convertStringToBinary(String data) { // data is either (a block from the plain text) or (a key)
        StringBuilder result = new StringBuilder();
        char[] chars = data.toCharArray();
        for (char c : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(c))   // char -> int, auto-cast
                            .replaceAll(" ", "0")     // zero padding
            );
        }
        return result.toString();
    }

    public String hexToString(String hex){
        byte[] bytes = HexFormat.of().parseHex(hex);
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }


    public ArrayList<String> splitText(String text, int size){
        int i, j;
        ArrayList<String> plainText = new ArrayList<>();
        for(i = 0; i < text.length(); i++){
            String temp = "";
            for(j = i; j < (i+size); j++){
                if(j < text.length()) {
                    temp += text.charAt(j);
                }else {
                    break;
                }
            }
            if(temp.length() < size){
                for(int k = temp.length(); k < size; k++){
                    temp = temp.concat("#");
                }
            }
            plainText.add(temp);
            i = j-1;
        }
        return plainText;
    }

    public void printMatrix(short[][] state, String name){
        System.out.println("\n" + name + "=\n");
        for (short[] shorts : state) {
            for (int j = 0; j < state[0].length; j++) {
                System.out.print(" " + shorts[j]);
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        /* **************************************************************Input_Handling************************************************************** */
        System.out.println("Enter Plaintext to encrypt:");
        Scanner input = new Scanner(System.in);
        String plainText = input.nextLine();
        String key = "";
        while(key.length() != 16) {
            System.out.println("Please Enter new key of length 16 characters");
            key = input.nextLine();
        }
        RSA Rsa = new RSA();
        ArrayList<String> text = new ArrayList<>();
        if(plainText.length() < 16){
            for(int i = plainText.length(); i < 16; i++){
                plainText = plainText.concat("#");
            }
        }
        if (plainText.length() >= 16) {
            text = Rsa.splitText(plainText, 16);
        }
        System.out.println(text);
        /* **************************************************************Key_Generation************************************************************** */

        /* ****************************************************************Encryption**************************************************************** */

        /* ****************************************************************Decryption**************************************************************** */

    }
}