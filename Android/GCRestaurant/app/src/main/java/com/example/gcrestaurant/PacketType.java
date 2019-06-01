package com.example.gcrestaurant;

public class PacketType {
    public static final int Login = 1;

    public static final int Debug = 1000;

    public static final int RestaurantInfo = 1001;

    public static final int Message = 1002;

    public static final int RestaurantWaitingList = 1003;

    public static final int GetRestaurantID = 1004;

    public static int PositionUpdate = 2000;

    public static final int ReadBoard = 3000;
    public static final int WriteBoardItem = 3001;
    public static final int DeleteBoardItem = 3002;

    public static final int GetLikes = 10000;
    public static final int ClickLikes = 10001;
}
