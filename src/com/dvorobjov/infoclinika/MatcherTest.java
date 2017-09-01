package com.dvorobjov.infoclinika;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatcherTest {

    public static void main(String[] args) {
        Matcher matcher = new Matcher();
        long startTime = System.currentTimeMillis();
        List<Point> matchedPointList = matcher.getMatchedPoints(generateMassiveTestPointList(1024*1024));
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Matched points count: " + matchedPointList.size());
        System.out.printf("Duration %s ms", duration);
    }

    public static List<Point> generateMassiveTestPointList(int listSize) {
        return Stream.generate(() -> new Point((int)Math.round(Math.random() * 9999999), (int)Math.round(Math.random() * 9999999)))
                .limit(listSize).collect(Collectors.toList());
    }


    public static List<Point> generateAccurateTestPointList(Matcher matcher) {
        List<Point> pointList = new ArrayList<Point>();

        //out of match
        pointList.add(new Point(matcher.rectangleUpperLeftCornerX[0] - 1, matcher.rectangleUpperLeftCornerY[0] - 1));
        //matched on first rectangle border
        pointList.add(new Point(matcher.rectangleUpperLeftCornerX[0], matcher.rectangleUpperLeftCornerY[0]));
        //matched on first rectangle border
        pointList.add(new Point(Math.round((matcher.rectangleUpperLeftCornerX[1] + matcher.rectangleLowerRightCornerX[1]) / 2),
                Math.round((matcher.rectangleUpperLeftCornerY[1] + matcher.rectangleLowerRightCornerY[1]) / 2)));
        //out of match
        pointList.add(new Point(999999999, 999999999));

        return pointList;
    }
}
