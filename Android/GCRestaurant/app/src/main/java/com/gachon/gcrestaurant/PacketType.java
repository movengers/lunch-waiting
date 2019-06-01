package com.gachon.gcrestaurant;

public class PacketType {
    public static final int Login = 1;

    public static final int Debug = 1000;

    public static final int RestaurantInfo = 1001;

    public static final int Message = 1002;

    public static final int RestaurantWaitingList = 1003;

    public static final int GetRestaurantID = 1004;
    public static final int RestaurantRankingList = 1100;

    public static final int PositionUpdate = 2000;

    public static final int ReadBoard = 3000;
    public static final int WriteBoardItem = 3001;
    public static final int DeleteBoardItem = 3002;
    public static final int ReadComments = 3003;

    public static final int GetLikes = 10000;
    public static final int ClickLikes = 10001;

    public static final int RequestWaitingToServer = 14000;
    public static final int RequestWaitingToUser = 14001;
    public static final int ContainsWaitingListener = 14002;
}
