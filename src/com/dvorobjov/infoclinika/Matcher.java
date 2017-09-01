package com.dvorobjov.infoclinika;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Matcher {
    public static final int RECTANGLE_COLLECTION_SIZE = 1024*1024;

    private static final int RECTANGLE_COORDINATES_SCALE = 100;
    private static final float RECTANGLE_SIZE_SCALE = 10.0f;

    public static final int[] rectangleUpperLeftCornerX;
    public static final int[] rectangleUpperLeftCornerY;
    public static final int[] rectangleLowerRightCornerX;
    public static final int[] rectangleLowerRightCornerY;

    private static int rectangleAreaUpperLeftCornerX;
    private static int rectangleAreaUpperLeftCornerY;
    private static int rectangleAreaLowerRightCornerX;
    private static int rectangleAreaLowerRightCornerY;

    static {
        rectangleUpperLeftCornerX = new int[RECTANGLE_COLLECTION_SIZE];
        rectangleUpperLeftCornerY = new int[RECTANGLE_COLLECTION_SIZE];
        rectangleLowerRightCornerX = new int[RECTANGLE_COLLECTION_SIZE];
        rectangleLowerRightCornerY = new int[RECTANGLE_COLLECTION_SIZE];

        initRectangleCollection(RECTANGLE_COLLECTION_SIZE, RECTANGLE_COORDINATES_SCALE, RECTANGLE_SIZE_SCALE);
        calculateOptimizationParameters();
    }

    public List<Point> getMatchedPoints(List<Point> pointList) {
        return pointList.parallelStream().filter(this::isPointMatch).collect(Collectors.toList());
    }

    private boolean isPointMatch(Point point) {
        //check is point outside of whole rectangle area
        if (point.x < rectangleAreaUpperLeftCornerX || point.x > rectangleAreaLowerRightCornerX
            || point.y < rectangleAreaUpperLeftCornerY || point.y > rectangleAreaLowerRightCornerY) {
            return false;
        }
        //TODO check point in the cache

        for (int i = 0; i < RECTANGLE_COLLECTION_SIZE; i++) {
            if (point.x >= rectangleUpperLeftCornerX[i] && point.x <= rectangleLowerRightCornerX[i]
                    && point.y >= rectangleUpperLeftCornerY[i] && point.y <= rectangleLowerRightCornerY[i]) {
                //TODO add point to cache
                return true;
            }
        }
        return false;
    }

    private static void initRectangleCollection(int collectionSize, int rectangleCoordinatesScale, float rectanglesSizeScale) {
        final int collectionDimension = (int)Math.round(Math.floor(Math.sqrt(collectionSize)));
        final int initialUpperLeftX = (int)Math.round(Math.random() * rectangleCoordinatesScale);

        int x = initialUpperLeftX;
        int y = (int) Math.round(Math.random() * rectangleCoordinatesScale);

        for (int i = 0; i < collectionSize; i++) {
            rectangleUpperLeftCornerX[i] = x;
            rectangleUpperLeftCornerY[i] = y;
            int side = (int) Math.round(1+Math.random() * rectanglesSizeScale);
            rectangleLowerRightCornerX[i] = x + side;
            rectangleLowerRightCornerY[i] =  y + side;
            x += rectangleCoordinatesScale;
            if (i % collectionDimension == 0 && i > 0) {
                x = initialUpperLeftX;
                y = y + rectangleCoordinatesScale;
            }
        }
    }

    private static void calculateOptimizationParameters() {
        //TODO for future optimization to find min and max simultaneously
        rectangleAreaUpperLeftCornerX = IntStream.of(rectangleUpperLeftCornerX).parallel().min().getAsInt();
        rectangleAreaUpperLeftCornerY = IntStream.of(rectangleUpperLeftCornerY).parallel().min().getAsInt();
        rectangleAreaLowerRightCornerX = IntStream.of(rectangleLowerRightCornerX).parallel().max().getAsInt();
        rectangleAreaLowerRightCornerY = IntStream.of(rectangleLowerRightCornerY).parallel().max().getAsInt();
    }

}