package rikka.akashitoolkit.otto;

import com.squareup.otto.Bus;

/**
 * Created by Rikka on 2016/3/9.
 */
public class BusProvider {
    private static MainThreadBus bus;

    public static synchronized Bus instance() {
        if (bus == null) {
            bus = new MainThreadBus();
        }

        return bus;
    }
}
