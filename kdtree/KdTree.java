import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final boolean X = true;
    private Node root;
    private int size;

    public KdTree() {
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
        if (smaller < 0)
            return goLeft(point, currentNode, currentNode.getLeft());
        return goRight(point, currentNode, currentNode.getRight());
    }

    private int comparePointWithNode(Point2D point, Node node) {
        return node.getAxis() == X ? Double.compare(point.x(), node.getVal().x()) :
               Double.compare(point.y(), node.getVal().y());
    }

    private Node goLeft(Point2D point, Node parent, Node node) {
        if (node == null || node.getVal() == null) {
            Node newNode = new Node(!parent.getAxis());
            parent.setLeft(newNode);
            return newNode;
        }
        return traverse(point, node);
    }

    private Node goRight(Point2D point, Node parent, Node node) {
        if (node == null || node.getVal() == null) {
            Node newNode = new Node(!parent.getAxis());
            parent.setRight(newNode);
            return newNode;
        }
        return traverse(point, node);
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

    private Point2D nearestNeighbor(Point2D query, Node node, Point2D champion) {
        if (node == null) return champion;

        Node nearestChild = nearestChild(node, query);
        // if new champion, prune the other subtree
        if (node.getVal().distanceTo(query) < champion.distanceTo(query)) {
            return nearestNeighbor(query, nearestChild, node.getVal());
        }
        else {
            // if not champion, then we might have to search both subtrees
            Point2D nearestResult = nearestNeighbor(query, nearestChild, node.getVal());
            if (nearestResult.distanceTo(query) < champion.distanceTo(query)) {
                return nearestNeighbor(query, nearestChild, nearestResult);
            }
            else {
                // if rectangle containing the node is closer to the query than the champion, search it
                if (isRecCloserToQuery(query, node, champion))
                    return nearestNeighbor(query,
                                           nearestChild.equals(node.getLeft()) ? node.getLeft() :
                                           node.getRight(), champion);
            }
        }
        return champion;
    }

    private boolean isRecCloserToQuery(Point2D query, Node node, Point2D champion) {
        return node.getAxis() == X ?
               Math.abs(query.x() - node.getVal().x()) < Math.abs(query.x() - champion.x()) :
               Math.abs(query.y() - node.getVal().y()) < Math.abs(query.y() - champion.y());
    }

    private Node nearestChild(Node node, Point2D query) {
        return node.getAxis() == X ?
               query.x() < node.getVal().x() ? node.getLeft() : node.getRight() :
               query.y() < node.getVal().y() ? node.getLeft() : node.getRight();
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
