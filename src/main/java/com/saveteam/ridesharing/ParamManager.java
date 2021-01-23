package com.saveteam.ridesharing;

/**
 * Where controlling parameter or algorithm of program
 */
public class ParamManager {
    private static ParamManager instance;

    private long timeGetOfferRide = 20000;
    private long timeGetSearchRide = 3000;

    private ParamManager() {

    }

    public static ParamManager getInstance() {
        if (instance != null) {
            synchronized (ParamManager.class) {
                if (instance != null) {
                    instance = new ParamManager();
                }
            }
        }

        return instance;
    }

    public long getTimeGetOfferRide() {
        return timeGetOfferRide;
    }

    public void setTimeGetOfferRide(long timeGetOfferRide) {
        this.timeGetOfferRide = timeGetOfferRide;
    }

    public long getTimeGetSearchRide() {
        return timeGetSearchRide;
    }

    public void setTimeGetSearchRide(long timeGetSearchRide) {
        this.timeGetSearchRide = timeGetSearchRide;
    }
}
