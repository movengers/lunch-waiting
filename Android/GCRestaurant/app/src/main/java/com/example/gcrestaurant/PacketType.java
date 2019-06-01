package com.example.gcrestaurant;

public class PacketType {
    public static final int Login = 1;

    public static final int Debug = 1000;

    public static final int RestaurantInfo = 1001;

    public static final int Message = 1002;

    public static final int RestaurantWaitingList = 1003;

    public static final int GetRestaurantID = 1004;

    public static int PositionUpdate = 2000;

    public static final int RequestWaitingToServer = 14000;
    public static final int RequestWaitingToUser = 14001;
    public static final int ContainsWaitingListener = 14002;
}
