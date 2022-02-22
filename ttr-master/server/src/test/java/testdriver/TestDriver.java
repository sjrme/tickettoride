package testdriver;

public class TestDriver {

    public static void main(String[] args){

        org.junit.runner.JUnitCore.main(
                "clientproxy.ClientProxyTest"
        );
    }
}