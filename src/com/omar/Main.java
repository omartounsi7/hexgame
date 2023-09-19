package com.omar;

import com.omar.models.map.MapSize;
import com.omar.models.map.World;

public class Main {
    public static void main(String[] args) {
        World world1 = new World(MapSize.SMALL);
        System.out.println(world1);
        World world2 = new World(MapSize.MEDIUM);
        System.out.println(world2);
        World world3 = new World(MapSize.LARGE);
        System.out.println(world3);
    }
}