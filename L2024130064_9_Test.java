/**
 * 测试用例设计总体原则：采用等价类划分、边界值分析和回归测试。
 * 有效输入按空图、可二分图、不可二分图、连通图和非连通图划分；
 * 选取 n=1、n=2000、dislikes.length=0 和 10000 等边界。
 * 通过题目示例、编号 n、奇偶环和并查集操作复现原程序的错误。
 * 穷举 n<=6 的全部简单无向图，以独立的 BFS 二染色算法作为判定依据，
 * 避免只使用与被测并查集算法相同的实现验证自身。
 * 输入均满足题目约束，不测试未定义的非法参数行为。
 */
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

public class L2024130064_9_Test {
    private final Solution9 solution = new Solution9();

    /** 测试目的：验证题目给出的三个示例。用例：可行分组、三角形和五边形。 */
    @Test
    void publishedExamples() {
        assertTrue(solution.possibleBipartition(4, new int[][] {{1, 2}, {1, 3}, {2, 4}}));
        assertFalse(solution.possibleBipartition(3, new int[][] {{1, 2}, {1, 3}, {2, 3}}));
        assertFalse(solution.possibleBipartition(5,
                new int[][] {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}}));
    }

    /** 测试目的：覆盖人数和空边集边界。用例：n=1 和 n=2000，均没有不喜欢关系。 */
    @Test
    void emptyGraphsAtBothPopulationBoundaries() {
        assertTrue(solution.possibleBipartition(1, new int[0][2]));
        assertTrue(solution.possibleBipartition(2000, new int[0][2]));
    }

    /** 测试目的：回归验证编号 n 的邻接表已初始化。用例：n=2，仅有边 [1,2]。 */
    @Test
    void singleEdgeUsesHighestNumberedPerson() {
        assertTrue(solution.possibleBipartition(2, new int[][] {{1, 2}}));
    }

    /** 测试目的：区分奇环与偶环。用例：四边形为 true，七边形为 false。 */
    @Test
    void evenAndOddCycles() {
        assertTrue(solution.possibleBipartition(4,
                new int[][] {{1, 2}, {2, 3}, {3, 4}, {1, 4}}));
        assertFalse(solution.possibleBipartition(7,
                new int[][] {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}, {1, 7}}));
    }

    /** 测试目的：覆盖多个连通分量和孤立点。用例：合法分量；后部另有三角形的非法分量。 */
    @Test
    void disconnectedComponentsAndIsolatedPeople() {
        assertTrue(solution.possibleBipartition(9,
                new int[][] {{1, 2}, {2, 3}, {4, 5}, {5, 6}, {6, 7}, {4, 7}}));
        assertFalse(solution.possibleBipartition(9,
                new int[][] {{1, 2}, {2, 3}, {5, 6}, {6, 7}, {5, 7}}));
    }

    /** 测试目的：覆盖高编号中心及最大人数。用例：以 2000 为中心的星形图，以及 2000 人链。 */
    @Test
    void maximumPopulationStarAndChain() {
        int[][] star = new int[1999][2];
        int[][] chain = new int[1999][2];
        for (int i = 0; i < 1999; i++) {
            star[i] = new int[] {i + 1, 2000};
            chain[i] = new int[] {i + 1, i + 2};
        }
        assertTrue(solution.possibleBipartition(2000, star));
        assertTrue(solution.possibleBipartition(2000, chain));
    }

    /** 测试目的：覆盖边数上界 10000。用例：K100,100 可二分；保留边数并加入同侧边后不可二分。 */
    @Test
    void maximumDislikeCount() {
        int[][] edges = new int[10000][2];
        int index = 0;
        for (int left = 1; left <= 100; left++) {
            for (int right = 101; right <= 200; right++) {
                edges[index++] = new int[] {left, right};
            }
        }
        assertTrue(solution.possibleBipartition(2000, edges));
        edges[edges.length - 1] = new int[] {1, 2};
        assertFalse(solution.possibleBipartition(2000, edges));
    }

    /** 测试目的：验证边的输入顺序不影响结果。用例：同一个可二分图的原序和逆序。 */
    @Test
    void edgeOrderDoesNotChangeResult() {
        int[][] forward = {{1, 4}, {1, 5}, {2, 4}, {2, 6}, {3, 5}, {3, 6}};
        int[][] reversed = {{3, 6}, {3, 5}, {2, 6}, {2, 4}, {1, 5}, {1, 4}};
        assertTrue(solution.possibleBipartition(6, forward));
        assertTrue(solution.possibleBipartition(6, reversed));
    }

    /** 测试目的：验证负值根节点和路径压缩。用例：1->2->3，根 3 大小为 3；另有独立根 4。 */
    @Test
    void findRootAndCompressPath() {
        int[] parents = {-1, 2, 3, -3, -1};
        assertEquals(3, solution.findFa(1, parents));
        assertEquals(3, parents[1]);
        assertEquals(3, parents[2]);
        assertEquals(4, solution.findFa(4, parents));
        assertEquals(-1, parents[4]);
    }

    /** 测试目的：验证按大小合并、连通性与重复合并。用例：大小 3 的集合与单点合并。 */
    @Test
    void unionBySizeConnectivityAndRepeatedUnion() {
        int[] parents = {-1, -3, 1, 1, -1, -1};
        solution.unit(4, 1, parents);
        assertEquals(1, solution.findFa(4, parents));
        assertEquals(-4, parents[1]);
        assertTrue(solution.isconnect(2, 4, parents));
        assertFalse(solution.isconnect(1, 5, parents));
        solution.unit(2, 4, parents);
        assertEquals(-4, parents[1]);
        solution.unit(1, 5, parents);
        assertEquals(1, solution.findFa(5, parents));
        assertEquals(-5, parents[1]);
    }

    /** 测试目的：全面检查小图结构。用例：n=1..6 的全部 33867 个简单无向图，逐一对照 BFS。 */
    @Test
    void allSmallGraphsMatchIndependentBfsReference() {
        int checked = 0;
        for (int n = 1; n <= 6; n++) {
            List<int[]> candidateEdges = new ArrayList<>();
            for (int a = 1; a <= n; a++) {
                for (int b = a + 1; b <= n; b++) {
                    candidateEdges.add(new int[] {a, b});
                }
            }
            for (int mask = 0; mask < (1 << candidateEdges.size()); mask++) {
                int[][] edges = new int[Integer.bitCount(mask)][2];
                int index = 0;
                for (int bit = 0; bit < candidateEdges.size(); bit++) {
                    if ((mask & (1 << bit)) != 0) {
                        edges[index++] = candidateEdges.get(bit);
                    }
                }
                boolean expected = bfsReference(n, edges);
                boolean actual = solution.possibleBipartition(n, edges);
                assertEquals(expected, actual, "n=" + n + ", edges=" + Arrays.deepToString(edges));
                checked++;
            }
        }
        assertEquals(33867, checked);
    }

    /** 独立判定器：每个连通分量通过 BFS 二染色，边的两端必须异色。 */
    private static boolean bfsReference(int n, int[][] dislikes) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : dislikes) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        int[] colors = new int[n + 1];
        Queue<Integer> queue = new ArrayDeque<>();
        for (int start = 1; start <= n; start++) {
            if (colors[start] != 0) {
                continue;
            }
            colors[start] = 1;
            queue.add(start);
            while (!queue.isEmpty()) {
                int current = queue.remove();
                for (int neighbor : graph.get(current)) {
                    if (colors[neighbor] == 0) {
                        colors[neighbor] = -colors[current];
                        queue.add(neighbor);
                    } else if (colors[neighbor] == colors[current]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
