public class DemoClass {

    public void publicMethod1(String s) {
        System.out.println("publicMethod1: " + s);
    }

    public void publicMethod2(double x) {
        System.out.println("publicMethod2: " + x);
    }

    public void publicMethod3(double x, float y) {
        System.out.println("publicMethod3: " + x + " " + y);
    }

    @RunCount(3)
    protected void protectedMethod1(String s) {
        System.out.println("protectedMethod1: " + s);
    }

    @RunCount(2)
    protected void protectedMethod2(double x, float y) {
        System.out.println("protectedMethod2: " + x + " " + y);
    }

    @RunCount(4)
    private void privateMethod1(String s) {
        System.out.println("privateMethod1: " + s);
    }

    @RunCount(1)
    private void privateMethod2(int x) {
        System.out.println("privateMethod2: " + x);
    }

    @RunCount(5)
    private void privateMethod3() {
        System.out.println("privateMethod3:");
    }

}