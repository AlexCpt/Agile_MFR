import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestPlan {
    @Test
    public final void TestPlan() {
        Point[] points = {
                new Point(1, 2),
                new Point(2, 3),
                new Point(3, 4)
        };

        Troncon[] troncons = {
                new Troncon(points[0], points[1], 1),
                new Troncon(points[0], points[2], 1)
        };

        Plan plan = new Plan(points, troncons);

        HashMap<Point, List<Troncon>> graph = plan.getGraph();

        HashMap<Point, List<Troncon>> ourGraph = new HashMap<>();

        LinkedList<Troncon> arretes = new LinkedList<>(Arrays.asList(troncons[0], troncons[1]));

        ourGraph.put(points[0], arretes);

        assertEquals(graph, ourGraph);
    }
}
