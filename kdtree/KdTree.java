import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final boolean X = true;
    private static final boolean LEFT = true;
    private Node root;
    private int size;

    public KdTree() {
    }

    /*
     *********************************************************************
     ********************** TESTING METHODS ******************************
     *********************************************************************
     */
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.createKdTree();
        kdTree.testDrillDown();
        kdTree.testSize();
        kdTree.testContains();
        kdTree.testRange();
        kdTree.testNearestNeighbor();
        kdTree.testNearestChild();
        kdTree.testIsNodeRecCloserToQuery();
        kdTree.testGetChild();
        System.out.println("All tests passed.");
    }

    private void createKdTree() {
        insert(new Point2D(7, 2));
        insert(new Point2D(5, 4));
        insert(new Point2D(2, 3));
        insert(new Point2D(4, 7));
        insert(new Point2D(9, 6));
        insert(new Point2D(9, 6));
    }

    private void testSize() {
        if (size() != 5)
            throw new IllegalArgumentException("testSize failed");
    }

    private void testNearestNeighbor() {
        // left
        Point2D query = new Point2D(6, 5);
        if (!nearestNeighbor(query).equals(root.getLeft().getVal()))
            throw new IllegalArgumentException("testNearestNeighbor failed");

        // left > left
        query = new Point2D(1, 1);
        if (!nearestNeighbor(query).equals(root.getLeft().getLeft().getVal()))
            throw new IllegalArgumentException("testNearestNeighbor failed");

        // right
        query = new Point2D(8, 7);
        if (!nearestNeighbor(query).equals(root.getRight().getVal()))
            throw new IllegalArgumentException("testNearestNeighbor failed");

        // root
        query = new Point2D(8, 3);
        if (!nearestNeighbor(query).equals(root.getVal()))
            throw new IllegalArgumentException("testNearestNeighbor failed");

        // left > right
        query = new Point2D(4, 8);
        if (!nearestNeighbor(query).equals(root.getLeft().getRight().getVal()))
            throw new IllegalArgumentException("testNearestNeighbor failed");
    }

    private void testRange() {
        Iterable<Point2D> range = range(new RectHV(3, 3, 6, 6));
        range.forEach(point2D -> {
            if (!point2D.equals(new Point2D(5, 4)))
                throw new IllegalArgumentException("testRange failed");
        });
    }

    private void testContains() {
        if (!contains(new Point2D(4, 7)))
            throw new IllegalArgumentException("testContains failed.");
        if (contains(new Point2D(4, 2)))
            throw new IllegalArgumentException("testContains failed.");
        if (contains(null))
            throw new IllegalArgumentException("testContains failed.");
    }

    private void testNearestChild() {
        Node nearestChild = getChild(root.getLeft(), new Point2D(2, 2), true);
        if (!nearestChild.equals(root.getLeft().getLeft()))
            throw new IllegalArgumentException("testNearestChild failed");

        nearestChild = getChild(root.getLeft(), new Point2D(10, 5), true);
        if (!nearestChild.equals(root.getLeft().getRight()))
            throw new IllegalArgumentException("testNearestChild failed");

        nearestChild = getChild(root.getLeft(), new Point2D(5, 4), true);
        if (!nearestChild.equals(root.getLeft().getRight()))
            throw new IllegalArgumentException("testNearestChild failed");
    }

    private void testIsNodeRecCloserToQuery() {
        Point2D query = new Point2D(1, 1);
        if (!isNodeRecCloserToQuery(query, root.getLeft().getLeft(), root.getLeft().getVal()))
            throw new IllegalArgumentException("testIsNodeRecCloserToQuery failed");

        query = new Point2D(6, 5);
        if (isNodeRecCloserToQuery(query, root.getLeft().getLeft(), root.getLeft().getVal()))
            throw new IllegalArgumentException("testIsNodeRecCloserToQuery failed");

        query = new Point2D(2, 3);
        if (!isNodeRecCloserToQuery(query, root.getLeft().getLeft(), root.getLeft().getVal()))
            throw new IllegalArgumentException("testIsNodeRecCloserToQuery failed");
    }

    private void testDrillDown() {
        Node leftLeft = drillDown(new Point2D(2, 3), root, LEFT);
        if (!leftLeft.equals(root.getLeft().getLeft()))
            throw new IllegalArgumentException("testDrillDown failed");

        Node leftRight = drillDown(new Point2D(4, 7), root, LEFT);
        if (!leftRight.equals(root.getLeft().getRight()))
            throw new IllegalArgumentException("testDrillDown failed");

        Node right = drillDown(new Point2D(9, 6), root, !LEFT);
        if (!right.equals(root.getRight()))
            throw new IllegalArgumentException("testDrillDown failed");
    }

    private void testGetChild() {
        Node nearestChild = getChild(root, new Point2D(5, 5), true);
        if (!nearestChild.equals(root.getLeft()))
            throw new IllegalArgumentException("testGetChild failed");

        nearestChild = getChild(root, new Point2D(10, 10), true);
        if (!nearestChild.equals(root.getRight()))
            throw new IllegalArgumentException("testGetChild failed");

        nearestChild = getChild(root, new Point2D(10, 10), false);
        if (!nearestChild.equals(root.getLeft()))
            throw new IllegalArgumentException("testGetChild failed");
    }

    /*
     *********************************************************************
     ********************** KDTREE IMPLEMENTATION ************************
     *********************************************************************
     */


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean contains(Point2D p) {
        if (p == null || isEmpty()) return false;
        Node node = traverse(p);
        return node.getVal() != null;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null point");
        if (contains(p)) return;
        if (root == null) {
            root = new Node(p, X);
            size++;
            return;
        }
        Node result = traverse(p);
        result.setVal(p);
        size++;
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> result = new ArrayList<>();
        searchNearest(rect, root, result);
        return result;
    }

    private Node traverse(Point2D point) {
        return traverse(point, root);
    }

    private Node traverse(Point2D point, Node currentNode) {
        int smaller = comparePointWithNode(point, currentNode);
        if (point.x() == currentNode.getVal().x() && point.y() == currentNode.getVal().y())
            return currentNode;
        return drillDown(point, currentNode, (smaller < 0) == LEFT);
    }

    private int comparePointWithNode(Point2D point, Node node) {
        return node.getAxis() == X ? Double.compare(point.x(), node.getVal().x()) :
               Double.compare(point.y(), node.getVal().y());
    }

    private Node drillDown(Point2D point, Node parent, boolean direction) {
        if (direction == LEFT) {
            if (parent.getLeft() == null || parent.getLeft().getVal() == null) {
                Node newNode = new Node(!parent.getAxis());
                parent.setLeft(newNode);
                return newNode;
            }
            return traverse(point, parent.getLeft());
        }
        else {
            if (parent.getRight() == null || parent.getRight().getVal() == null) {
                Node newNode = new Node(!parent.getAxis());
                parent.setRight(newNode);
                return newNode;
            }
            return traverse(point, parent.getRight());
        }
    }

    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.5);
        drawNodes(root);
    }

    private void drawNodes(Node node) {
        if (node == null || node.getVal() == null) return;
        drawNodes(node.getLeft());
        node.getVal().draw();
        drawNodes(node.getRight());
    }

    private void searchNearest(RectHV rect, Node node, List<Point2D> result) {
        if (node == null || node.getVal() == null) return;
        addIfEligible(rect, node, result);

        if (node.getAxis() == X) {
            if (rect.xmax() < node.getVal().x() && rect.xmin() <= node.getVal().x())
                searchNearest(rect, node.getLeft(), result);
            else if (rect.xmax() >= node.getVal().x() && rect.xmin() >= node.getVal().x())
                searchNearest(rect, node.getRight(), result);
            else {
                searchNearest(rect, node.getLeft(), result);
                searchNearest(rect, node.getRight(), result);
            }
        }
        else {
            if (rect.ymax() < node.getVal().y() && rect.ymin() <= node.getVal().y())
                searchNearest(rect, node.getLeft(), result);
            else if (rect.ymax() >= node.getVal().y() && rect.ymin() >= node.getVal().y())
                searchNearest(rect, node.getRight(), result);
            else {
                searchNearest(rect, node.getLeft(), result);
                searchNearest(rect, node.getRight(), result);
            }
        }
    }

    private void addIfEligible(RectHV rect, Node node, List<Point2D> result) {
        if (node.getVal().x() >= rect.xmin()
                && node.getVal().x() <= rect.xmax()
                && node.getVal().y() >= rect.ymin()
                && node.getVal().y() <= rect.ymax()) {
            result.add(node.getVal());
        }
    }

    public Point2D nearestNeighbor(Point2D query) {
        return nearestNeighbor(query, root, new Point2D(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    private Point2D nearestNeighbor(Point2D query, Node node, Point2D finalChamp) {
        if (node == null) return finalChamp;

        Node nearestChild = getChild(node, query, true);
        Node otherChild = getChild(node, query, false);

        if (Double.compare(node.getVal().distanceTo(query), finalChamp.distanceTo(query)) < 0)
            finalChamp = node.getVal();

        finalChamp = nearestNeighbor(query, nearestChild, finalChamp);
        if (isNodeRecCloserToQuery(query, node, finalChamp)) {
            finalChamp = nearestNeighbor(query, otherChild, finalChamp);
        }
        return finalChamp;
    }

    private boolean isNodeRecCloserToQuery(Point2D query, Node node, Point2D champion) {
        return node.getAxis() == X ?
               Math.abs(query.x() - node.getVal().y()) <= Math.abs(query.x() - champion.y()) :
               Math.abs(query.y() - node.getVal().x()) <= Math.abs(query.y() - champion.x());
    }

    private Node getChild(Node node, Point2D query, boolean nearestChild) {
        if (node.getAxis() == X) {
            if (nearestChild)
                return query.x() < node.getVal().x() ? node.getLeft() : node.getRight();
            return query.x() < node.getVal().x() ? node.getRight() : node.getLeft();
        }
        if (nearestChild)
            return query.y() < node.getVal().y() ? node.getLeft() : node.getRight();
        return query.y() < node.getVal().y() ? node.getRight() : node.getLeft();
    }


    /*
     *********************************************************************
     ********************** NODE CLASS ***********************************
     *********************************************************************
     */


    private static class Node {
        private Node left;
        private Node right;
        private Point2D val;
        private boolean axis;

        public Node(boolean axis) {
            this.axis = axis;
        }

        public Node(Point2D val, boolean axis) {
            this.val = val;
            this.axis = axis;
        }

        public boolean getAxis() {
            return axis;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Point2D getVal() {
            return val;
        }

        public void setVal(Point2D val) {
            this.val = val;
        }
    }
}
