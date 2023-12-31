import java.util.*;

public class Graph {
    private class Node{
        private String label;

        public Node(String label){
            this.label = label;
        }

        @Override
        public String toString() {
            return  label;
        }
    }

    // private List<Node> nodes = new ArrayList<>();
    // using List would result in O(n) time complexity while adding an edge or a node instead we should
    // make use of Map
    private Map<String, Node> nodes = new HashMap<>();
    private Map<Node, List<Node>> adjacencyList = new HashMap<>();

    public void addNode(String label){
        var node = new Node(label);
        nodes.putIfAbsent(label, node);
        adjacencyList.putIfAbsent(node, new ArrayList<>()); // adding node to adjacencyList with empty ArrayList to
        // track number of nodes it is connected to
    }

    public void addEdge(String from, String to){
        var fromNode = nodes.get(from);
        if(fromNode == null) throw new IllegalArgumentException();

        var toNode = nodes.get(to);
        if(toNode == null) throw new IllegalArgumentException();

        //uni-directed graph
        adjacencyList.get(fromNode).add(toNode);

        // bi-directed graph
        // adjacencyList.get(fromNode).add(toNode);
        // adjacencyList.get(toNode).add(fromNode);
    }

    public void removeNode(String label){
        var node = nodes.get(label);
        if(node == null) return;

        for(var n : adjacencyList.keySet())
            adjacencyList.get(n).remove(node);

        adjacencyList.remove(node);
        nodes.remove(node);
    }

    public void removeEdge(String from, String to){
        var fromNode = nodes.get(from);
        var toNodes = nodes.get(to);

        if(fromNode == null || toNodes == null) return;

        adjacencyList.get(fromNode).remove(toNodes);
    }

    public void traverseDepthFirst(String root){
        var node = nodes.get(root);
        if(node == null) return;

        traverseDepthFirst(node, new HashSet<>());
    }

    private void traverseDepthFirst(Node root, Set<Node> visited){
        System.out.println(root);
        visited.add(root);

        for(var node: adjacencyList.get(root))
            if(!visited.contains(node)) traverseDepthFirst(node, visited);

    }

    public void traverseDepthFirstIterative(String root){
        var node = nodes.get(root);
        if(node == null) return;
        traverseDepthFirstIterative(node, new Stack<>());
    }

    private void traverseDepthFirstIterative(Node root, Stack<Node> stack){
        Set<Node> visited = new HashSet<>();
        stack.push(root);

        while(!stack.isEmpty()){
            var curr = stack.pop();
            if(!visited.contains(curr)){
                System.out.println(curr);
                visited.add(curr);
                for(var node : adjacencyList.get(curr))
                    if(!visited.contains(node)) stack.push(node); // if not visited any neighbour
            }
        }
    }

    public void traverseBreadthFirst(String root){
        var node = nodes.get(root);
        if(node == null) return;

        traverseBreadthFirst(node, new ArrayDeque<>());
    }
    private void traverseBreadthFirst(Node root, Queue<Node> queue){
        Set<Node> visited = new HashSet<>();
        queue.add(root);

        while(!queue.isEmpty()) {
            var curr = queue.remove();
            if (!visited.contains(curr)) {
                System.out.println(curr);
                visited.add(curr);

                for (var node : adjacencyList.get(curr))
                    if (!visited.contains(node)) queue.add(node);
            }
        }

    }

    public List<String> topoLogicalSort(){
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        for (var node : nodes.values())
            topoLogicalSort(node, visited, stack);

        List<String> sorted = new ArrayList<>();
        while(!stack.isEmpty())
            sorted.add(stack.pop().label);

        return sorted;
    }
    private void topoLogicalSort(Node node, Set<Node> visited, Stack<Node> stack){
        if(visited.contains(node))
            return;

        visited.add(node);
        for (var neighbour : adjacencyList.get(node))
                topoLogicalSort(neighbour, visited, stack);

        stack.push(node);
    }

    public boolean hasCycle(){
        Set<Node> all = new HashSet<>(nodes.values());
        Set<Node> visiting = new HashSet<>();
        Set<Node> visited = new HashSet<>();

        while(!all.isEmpty()){
            // inefficient approach as it takes more space to allocate an entire array to get an element from array
            // var curr = all.toArray(new Node[0])[0]; // toArray method returns array of Object to return Node object we need
            // to pass new Node[0] as parameter
            var curr = all.iterator().next();
            if(hasCycle(curr, all, visiting, visited)) return true;
        }

        return false;
    }

    private boolean hasCycle(Node node, Set<Node> all, Set<Node> visiting, Set<Node> visited){
        all.remove(node);
        visiting.add(node);

        for (var neighbour: adjacencyList.get(node)){
            if(visited.contains(neighbour)) continue;

            if(visiting.contains(neighbour)) return true; // a way back to node which is still in
            // visiting set and is not visited

            if(hasCycle(neighbour, all, visiting, visited)) return true; // otherwise first we need to visit the
            // neighbours of that neighbour
        }
        visiting.remove(node);
        visited.add(node);

        return false;
    }
    public void print(){
        for(var source: adjacencyList.keySet()){
            var targets = adjacencyList.get(source);
            if(!targets.isEmpty())
                System.out.println(source + " is connected to " + targets);
        }
    }
}
