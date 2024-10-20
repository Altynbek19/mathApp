//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        System.out.println("isPrime(13): " + MathUtil.isPrime(13));
        System.out.println("gcd(15, 100): " + MathUtil.gcd(15, 100));
        System.out.println("lcm(15, 100): " + MathUtil.lcm(15, 100));
        System.out.println("fibonacci(13): " + MathUtil.fibonacci(13));
        System.out.println("factorial(n): " + MathUtil.factorial(13));

        MathUtil mathUtil = new MathUtil();
        System.out.println("isPerfectNumber(28): " + mathUtil.isPerfectNumber(28));
        System.out.println("sumOfDigits(1234): " + mathUtil.sumOfDigits(1234));
        System.out.println("reverseNumber(1234): " + mathUtil.reverseNumber(1234));
        System.out.println("isArmstrongNumber(153): " + mathUtil.isArmstrongNumber(153));
        System.out.println("nextPrime(17): " + mathUtil.nextPrime(17));

    }


}