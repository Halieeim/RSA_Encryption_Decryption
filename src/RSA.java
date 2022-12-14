import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class RSA {
    long p,q;
    public boolean fermatTest(long number){
        return (square_Multiply(2,number-1,number) == 1);
    }

    public boolean isPrime(long number){
        return fermatTest(number);
    }

    public long GCD(long a, long b){
        long temp = 0;
        while (b != 0){
            temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }

    public long EEA(long a, long b){
        long inverse, temp = a;
        for (int i = 2;;i++){
            if ((temp % b) != 1){
                temp = a * i;
            } else {
                inverse = (i-1) % b;
                break;
            }
        }
        return inverse;
    }
    public ArrayList<long[]> keyGeneration() {
        long[] res1 = new long[2];
        long[] res2 = new long[2];
        ArrayList<long[]> Result = new ArrayList<>();
        long n, r, e, gcd, d;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("Enter prime values for p & q respectively");
            p = input.nextLong();
            q = input.nextLong();
        } while ((!(isPrime(p) && isPrime(q))) || (p == q));

        n = p*q;
        System.out.println("p = " + p + " q = " + q);
        System.out.println("RSA Modulus -> n = " + n);

        r = (p - 1) * (q - 1);
        System.out.println("Euler's Toitent -> r = " + r);

        Random rand = new Random();
        do {
            e = rand.nextLong(2,r);
            gcd = GCD(e,r);
        } while (gcd != 1);

        System.out.println("CoPrime e : " + e);

        d = EEA(e,r);

        res1[0] = e;
        res1[1] = n;
        Result.add(res1);

        res2[0] = d;
        res2[1] = n;
        Result.add(res2);
        return Result;
    }
    public long square_Multiply(long a, long b, long n){
        long result = 1;
        while (b != 0){
            if ((b & 1) == 1){
                result = (result * a) % n;
            }
            a = (a * a) % n;
            b = b >> 1;
        }
        return result;
    }
    public long[] encrypt(String text, long[] publicKey){
        long key, n;
        long[] cipher = new long[text.length()];
        key = publicKey[0];
        n = publicKey[1];

        //key = 3;
        for (int i = 0; i < text.length(); i++){
            cipher[i] = square_Multiply(text.charAt(i),key,n);
        }
        return cipher;
    }

    public long[] decrypt(long[] cipher, long[] privateKey){
        long key, n, xp, xq, dp, dq, cp, cq;
        long[] plainText = new long[cipher.length];
        key = privateKey[0];
        n = privateKey[1];

        //key = 16971;

        dp = key % (p - 1);
        dq = key % (q - 1);

        cp = EEA(q,p);
        cq = EEA(p,q);
        for (int i = 0; i < cipher.length; i++) {
            xp = cipher[i] % p;
            xq = cipher[i] % q;
            long yp = square_Multiply(xp,dp,p);
            long yq = square_Multiply(xq,dq,q);
            plainText[i] = ((q*cp*yp) + (p*cq*yq)) % n;
        }
        return plainText;
    }
    public static void main(String[] args) {
        /* **************************************************************Input_Handling************************************************************** */
        System.out.println("Enter Plaintext to encrypt:");
        Scanner input = new Scanner(System.in);
        String plainText = input.nextLine();
        long[] t = new long[plainText.length()];

        /* **************************************************************Key_Generation************************************************************** */
        RSA Rsa = new RSA();
        ArrayList<long[]> keys = Rsa.keyGeneration();
        long[] publicKey = keys.get(0);
        long[] privateKey = keys.get(1);

        System.out.println("publicKey = " + publicKey[0] + " " + publicKey[1]);
        System.out.println("privateKey = " + privateKey[0] + " " + privateKey[1]);
        /* ****************************************************************Encryption**************************************************************** */
        System.out.println("\nYour plain text:");
        for (int i = 0; i < plainText.length(); i++){
            t[i] =  plainText.charAt(i);
            System.out.print(t[i] + " ");
        }

        long[] encryptedText = Rsa.encrypt(plainText,publicKey);
        System.out.println("\n\nEncryption");
        for (int i = 0; i < encryptedText.length; i++) {
            System.out.print(encryptedText[i] + " ");
        }
        /* ****************************************************************Decryption**************************************************************** */
        char letter;
        String text = "";
        System.out.println("\n\nDecryption:");
        long[] decrypted = Rsa.decrypt(encryptedText, privateKey);
        for (int i = 0; i < decrypted.length; i++){
            System.out.print(decrypted[i] + " ");
        }

        for (int i = 0; i < decrypted.length; i++){
            letter = (char) decrypted[i];
            text = text.concat(String.valueOf(letter));
        }
        System.out.println("\n" + text);
    }
}