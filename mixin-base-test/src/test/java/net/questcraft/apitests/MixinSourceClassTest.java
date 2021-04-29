package net.questcraft.apitests;

public class MixinSourceClassTest {
    private final String testString;

    public MixinSourceClassTest(String testString) {
        this.testString = testString;
    }

    public void shadowMethod() {
        System.out.println("A shadow method has been called!");
    }


    public void printTheString(int integer, float otherNum) {
        System.out.println(testString);
    }


}
