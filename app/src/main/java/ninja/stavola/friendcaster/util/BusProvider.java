package ninja.stavola.friendcaster.util;

import com.squareup.otto.Bus;

public class BusProvider {
    private BusProvider() {
        bus = new Bus();
    }

    private static BusProvider INSTANCE;
    private Bus bus;

    public static BusProvider getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BusProvider();
        }
        return INSTANCE;
    }

    public Bus provideBus() {
        return bus;
    }
}
