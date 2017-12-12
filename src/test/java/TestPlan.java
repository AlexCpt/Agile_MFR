import fr.insalyon.agile.modele.Plan;
import fr.insalyon.agile.modele.Point;
import fr.insalyon.agile.modele.Troncon;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestPlan {
    @Test
    public final void TestPlan() {
        List<Point> points = Arrays.asList(
                new Point("0", 1, 2),
                new Point("1", 2, 3),
                new Point("2", 3, 4)
        );

        List<Troncon> troncons = Arrays.asList(
                new Troncon(points.get(0), points.get(1), 1, "a"),
                new Troncon(points.get(0), points.get(2), 1, "a")
        );

        Plan plan = new Plan(points, troncons);

        HashMap<Point, List<Troncon>> graph = plan.getGraph();

        HashMap<Point, List<Troncon>> ourGraph = new HashMap<>();

        LinkedList<Troncon> arretes = new LinkedList<>(Arrays.asList(troncons.get(0), troncons.get(1)));

        ourGraph.put(points.get(0), arretes);

        assertEquals(graph, ourGraph);
    }
}
