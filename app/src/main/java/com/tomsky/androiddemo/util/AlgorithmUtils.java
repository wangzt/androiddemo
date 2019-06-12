package com.tomsky.androiddemo.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AlgorithmUtils {

    public static void testMergePoints() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(3, 5));
        points.add(new Point(1, 10));
        points.add(new Point(12, 15));
        points.add(new Point(12, 20));
        points.add(new Point(13, 15));
        points.add(new Point(15, 18));
        points.add(new Point(19, 22));
        List<Point> mergedPoints = mergePoints(points);
        for (Point point: mergedPoints) {
            Log.d("wzt-merge", point.toString());
        }
    }

    public static List<Point> mergePoints(List<Point> points) {
        Collections.sort(points, new PointComparator());

        List<Point> result = new ArrayList<>();
        Point current = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point next = points.get(i);
            if (current.end < next.start) { // 不重叠
                result.add(current);
                current = next;
            } else if (current.end >= next.end) { // 前面包含后面
                continue;
            } else if (current.end < next.end) {
                if (current.start== next.start) { // 后面包含前面
                    current = next;
                } else { // 交叉
                    result.add(current);
                    current = next;
                }
            }
        }
        result.add(current);

        return result;
    }

    private static final class Point {
        public Point(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start +
                    ", " + end +
                    ']';
        }

        int start;
        int end;
    }

    private static final class PointComparator implements Comparator<Point> {

        @Override
        public int compare(Point o1, Point o2) {
            if (o1 == null || o2 == null) return 0;

            if (o1.start > o2.start) {
                return 1;
            } else if (o1.start < o2.start) {
                return -1;
            }

            return 0;
        }
    }
}
