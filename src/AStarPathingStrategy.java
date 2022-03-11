import java.util.*;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        int key = 0;
        Node cur;
        boolean begin = true;
        double g;
        Comparator<Node> comp = (n1,n2) -> Double.compare(n1.f, n2.f);
        List<Point> path = new LinkedList<Point>();
        PriorityQueue<Node> openList = new PriorityQueue<>(comp);
        HashMap<Point, Double> q = new HashMap();
        HashSet<Point> closedList = new HashSet<>();
        Node node = null;
        //g = distance from start
        //h = distance from goal
        //f = g+h
        cur = new Node(start, null,0, distance(start,end), distance(start,end));
        openList.add(cur);
        q.put(cur.pos, cur.g);
        while(!openList.isEmpty()){
            List<Point> adj = potentialNeighbors.apply(cur.pos).toList();
            for (Point point : adj) {
                if (canPassThrough.test(point) && begin || canPassThrough.test(point) && !closedList.contains(point)) {
                    if (begin) {
                        g = distance(point,start);
                    } else {
                        g = distance(cur.pos, point) + distance(point,start) + key;
                    }
                    key++;
                    double h = distance(point, end);
                    double f = g + h;
                    node = new Node(point, cur, g, h, f);
                    if (q.containsKey(node.pos)) {
                        if (Double.compare(q.get(node.pos), node.g) > 0 ) {
                            openList.remove(node);
                            q.remove(node.pos);
                            openList.add(node);
                            q.put(node.pos, node.g);
                        }
                    } else {
                        openList.add(node);
                        q.put(node.pos, node.g);
                    }

                }
            }
            begin = false;
            closedList.add(cur.pos);
            q.remove(cur.pos);
            openList.remove(cur);
            cur = openList.peek();
            if( cur != null && withinReach.test(cur.pos,end)){
                buildPath(cur, path);
                return path;
            }
        }
        return path;
    }

    public void buildPath(Node cur, List<Point> path){
        if(cur.prev != null){
            path.add(0,cur.pos);
            buildPath(cur.prev, path);
        }
    }

    public double distance(Point p1 ,Point p2){
        return Math.sqrt(Math.pow(p1.x - p2.x,2)+Math.pow(p1.y - p2.y,2));
        //return Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y);
    }

    public class Node implements Comparable<Node>
    {
        Point pos;
        Node prev;
        double g;
        double h;
        double f;

        public Node(Point pos, Node prev, double g, double h, double f) {
            this.pos = pos;
            this.prev = prev;
            this.g = g;
            this.h = h;
            this.f = f;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "pos=" + pos +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return pos.equals(node.pos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pos);
        }

        @Override
        public int compareTo(Node o) {
            if(f < o.f){
                return -1;
            }
            else if(f > o.f){
                return 1;
            }
            else{
                return 0;
            }
        }
    }
}
