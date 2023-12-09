import service.IPGTrapReceiver;
import service.IQHCOTrapReceiver;

public class Main {
    public static void main(String[] args) {
        IQHCOTrapReceiver trapReceiver = new IQHCOTrapReceiver();
        IPGTrapReceiver ipgTrapReceiver = new IPGTrapReceiver();
        trapReceiver.run();
        ipgTrapReceiver.run();
    }
}