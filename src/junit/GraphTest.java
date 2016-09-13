package junit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import graph.MyGraph;
import graph.MyNode;

public class GraphTest {

	@Test
	public void testaddNode() {
		MyGraph g = new MyGraph();
		assertEquals(g.getNumNodes(), 0);
		MyNode n1 = new MyNode(0, 0, 0, 1.0, true, "porto");
		MyNode n2 = new MyNode(1, 0, 1, 1.0, true, "coimbra");
		MyNode n3 = new MyNode(2, 3, 0, 1.0, true, "lisboa");
		MyNode n4 = new MyNode(3, 0, 4, 1.0, true, "faro");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		
		assertEquals(g.getNumNodes(), 4);
		g.addNode(n4);
		assertEquals(g.getNumNodes(), 4);
		
		MyNode n5 = new MyNode(4, 0, 5, 1.0, true, "andorra");
		g.addNode(n5);
		assertEquals(g.getNumNodes(), 5);
	}

	@Test
	public void testremoveNode() {
		MyGraph g = new MyGraph();
		assertEquals(g.getNumNodes(), 0);
		MyNode n1 = new MyNode(0, 0, 0, 1.0, true, "porto");
		MyNode n2 = new MyNode(1, 0, 1, 1.0, true, "coimbra");
		MyNode n3 = new MyNode(2, 3, 0, 1.0, true, "lisboa");
		MyNode n4 = new MyNode(3, 0, 4, 1.0, true, "faro");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addEdge(n1, n2, 1, 1, 1);
		g.addEdge(n3, n1, 2, 1, 1);
		g.addEdge(n1, n4, 2, 1, 1);
		assertEquals(g.getNumNodes(), 4);
		MyNode n5 = new MyNode(4, 0, 5, 1.0, true, "porto");
		g.addNode(n5);
		assertEquals(g.getNumNodes(), 5);
		g.removeNode(n5);
		assertEquals(g.getNumNodes(), 4);
		assertEquals(n4.getOutEdges().size(), 0);
		assertEquals(n1.getOutEdges().size(), 2);
		g.removeNode(n4);
		assertEquals(g.getNumNodes(), 3);
		assertEquals(n1.getOutEdges().size(), 1);
		g.removeNode(n1);
		assertEquals(n3.getOutEdges().size(), 0);
	}

	@Test
	public void testAddEdge() {
		MyGraph g = new MyGraph();
		assertEquals(g.getNumNodes(), 0);
		MyNode n1 = new MyNode(0, 0, 0, 1.0, true, "porto");
		MyNode n2 = new MyNode(1, 0, 1, 1.0, true, "coimbra");
		MyNode n3 = new MyNode(2, 3, 0, 1.0, true, "lisboa");
		MyNode n4 = new MyNode(3, 0, 4, 1.0, true, "faro");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);

		assertEquals(n1.getOutEdges().size(), 0);
		assertEquals(n1.getIndegree(), 0);

		g.addEdge(n1, n2, 1, 1, 1);
		g.addEdge(n1, n3, 2, 1, 1);
		g.addEdge(n4, n1, 2, 1, 1);

		assertEquals(n1.getOutEdges().size(), 2);
		assertEquals(n1.getIndegree(), 1);
	}

	@Test
	public void testRemoveEdge() {
		MyGraph g = new MyGraph();
		assertEquals(g.getNumNodes(), 0);
		MyNode n1 = new MyNode(0, 0, 0, 1.0, true, "porto");
		MyNode n2 = new MyNode(1, 0, 1, 1.0, true, "coimbra");
		MyNode n3 = new MyNode(2, 3, 0, 1.0, true, "lisboa");
		MyNode n4 = new MyNode(3, 0, 4, 1.0, true, "faro");
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addEdge(n1, n2, 1, 1, 1);
		g.addEdge(n1, n3, 2, 1, 1);
		g.addEdge(n4, n1, 2, 1, 1);

		assertEquals(n1.getOutEdges().size(), 2);
		assertEquals(n1.getIndegree(), 1);

		g.removeEdge(n4, n1);
		assertEquals(n1.getIndegree(), 0);

		assertEquals(g.removeEdge(n2, n2), false);
		assertEquals(g.removeEdge(n2, n3), false);

		assertEquals(g.removeEdge(n1, n2), true);
		assertEquals(g.removeEdge(n1, n3), true);

		assertEquals(n1.getOutEdges().size(), 0);
	}

	@Test
	public void testgetNumNodes() {
		MyGraph g = new MyGraph();
		assertEquals(g.getNumNodes(), 0);
		MyNode n1 = new MyNode(0, 0, 0, 1.0, true, "porto");
		g.addNode(n1);
		assertEquals(g.getNumNodes(), 1);
		g.removeNode(n1);
		assertEquals(g.getNumNodes(), 0);
	}
}
