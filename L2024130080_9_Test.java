import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Solution9（可能的二分法，possibleBipartition）的单元测试类。
 *
 * 学号：2024130080    姓名：ALZAHRANI MUTEB SAEED A    题号：9
 *
 * 测试用例设计的总体原则：
 * 1. 等价类划分：把 n 个人看作图的顶点，dislikes 看作无向边，问题等价于判断图是否为二分图。
 *    据此把输入划分为两个有效等价类：
 *    (1) 可以二分（图中不含奇数长度的环）——期望返回 true；
 *    (2) 不能二分（图中至少含有一个奇数长度的环）——期望返回 false。
 *    每个等价类再按图的结构细分：无边、单条边、链、星形、偶环、完全二分图、多个连通分量等。
 * 2. 边界值分析：针对题目约束的边界取值，包括 n = 1（最小规模）、dislikes 为空（最少边数）、
 *    n = 2000（最大规模）、编号为 n 的人（最大编号）参与关系等。
 * 3. 错误推测：针对原代码中发现的缺陷专门设计用例，例如编号为 n 的人出现在 dislikes 中
 *    （检验邻接表是否完整初始化）、需要依赖反向边才能发现冲突的输入（检验双向建边）。
 * 4. 随机对照测试：以固定随机种子生成大量随机图，与基于 BFS 染色的参考实现比较结果，
 *    作为对上述人工设计用例的补充。
 */
public class L2024130080_9_Test {

    private Solution9 solution;

    @Before
    public void setUp() {
        solution = new Solution9();
    }

    /**
     * 测试目的：验证题目给出的示例 1（可以二分的一般情况）。
     * 测试用例：n = 4, dislikes = [[1,2],[1,3],[2,4]]，期望输出 true。
     */
    @Test
    public void testExample1() {
        int[][] dislikes = {{1, 2}, {1, 3}, {2, 4}};
        assertTrue(solution.possibleBipartition(4, dislikes));
    }

    /**
     * 测试目的：验证题目给出的示例 2（含长度为 3 的奇环，不能二分）。
     * 测试用例：n = 3, dislikes = [[1,2],[1,3],[2,3]]，期望输出 false。
     */
    @Test
    public void testExample2() {
        int[][] dislikes = {{1, 2}, {1, 3}, {2, 3}};
        assertFalse(solution.possibleBipartition(3, dislikes));
    }

    /**
     * 测试目的：验证题目给出的示例 3（含长度为 5 的奇环，不能二分）。
     * 测试用例：n = 5, dislikes = [[1,2],[2,3],[3,4],[4,5],[1,5]]，期望输出 false。
     */
    @Test
    public void testExample3() {
        int[][] dislikes = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}};
        assertFalse(solution.possibleBipartition(5, dislikes));
    }

    /**
     * 测试目的：边界值——最小规模 n = 1 且没有任何 dislike 关系。
     * 测试用例：n = 1, dislikes = []，期望输出 true。
     */
    @Test
    public void testSinglePersonNoDislikes() {
        assertTrue(solution.possibleBipartition(1, new int[0][]));
    }

    /**
     * 测试目的：边界值——dislikes 为空时，任何人数都可以分组。
     * 测试用例：n = 10, dislikes = []，期望输出 true。
     */
    @Test
    public void testNoDislikes() {
        assertTrue(solution.possibleBipartition(10, new int[0][]));
    }

    /**
     * 测试目的：错误推测——只有一条边且涉及编号最大的人 n，检验 g[n] 是否被初始化。
     * 测试用例：n = 2, dislikes = [[1,2]]，期望输出 true。
     */
    @Test
    public void testSinglePairWithLastPerson() {
        int[][] dislikes = {{1, 2}};
        assertTrue(solution.possibleBipartition(2, dislikes));
    }

    /**
     * 测试目的：有效等价类（可二分）——链状关系。
     * 测试用例：n = 6, dislikes = [[1,2],[2,3],[3,4],[4,5],[5,6]]，期望输出 true。
     */
    @Test
    public void testChain() {
        int[][] dislikes = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}};
        assertTrue(solution.possibleBipartition(6, dislikes));
    }

    /**
     * 测试目的：有效等价类（可二分）——长度为 4 的偶环。
     * 测试用例：n = 4, dislikes = [[1,2],[2,3],[3,4],[1,4]]，期望输出 true。
     */
    @Test
    public void testEvenCycle() {
        int[][] dislikes = {{1, 2}, {2, 3}, {3, 4}, {1, 4}};
        assertTrue(solution.possibleBipartition(4, dislikes));
    }

    /**
     * 测试目的：有效等价类（可二分）——星形关系，1 号与其他所有人互相不喜欢。
     * 测试用例：n = 5, dislikes = [[1,2],[1,3],[1,4],[1,5]]，期望输出 true。
     */
    @Test
    public void testStar() {
        int[][] dislikes = {{1, 2}, {1, 3}, {1, 4}, {1, 5}};
        assertTrue(solution.possibleBipartition(5, dislikes));
    }

    /**
     * 测试目的：有效等价类（可二分）——完全二分图 K(3,3)，{1,2,3} 与 {4,5,6} 两两不喜欢。
     * 测试用例：n = 6，共 9 条边，期望输出 true。
     */
    @Test
    public void testCompleteBipartite() {
        List<int[]> edges = new ArrayList<>();
        for (int a = 1; a <= 3; a++) {
            for (int b = 4; b <= 6; b++) {
                edges.add(new int[]{a, b});
            }
        }
        assertTrue(solution.possibleBipartition(6, edges.toArray(new int[0][])));
    }

    /**
     * 测试目的：错误推测——冲突只能通过反向边发现，检验邻接表是否双向建边。
     * 测试用例：n = 3, dislikes = [[1,3],[2,3],[1,2]]（3 号只作为 bi 出现），期望输出 false。
     */
    @Test
    public void testConflictFoundThroughReverseEdges() {
        int[][] dislikes = {{1, 3}, {2, 3}, {1, 2}};
        assertFalse(solution.possibleBipartition(3, dislikes));
    }

    /**
     * 测试目的：多个连通分量且都可以二分。
     * 测试用例：n = 7, dislikes = [[1,2],[3,4],[4,5],[6,7]]，期望输出 true。
     */
    @Test
    public void testMultipleComponentsAllBipartite() {
        int[][] dislikes = {{1, 2}, {3, 4}, {4, 5}, {6, 7}};
        assertTrue(solution.possibleBipartition(7, dislikes));
    }

    /**
     * 测试目的：多个连通分量，其中一个分量含奇环（无效等价类出现在后面的分量中）。
     * 测试用例：n = 7, dislikes = [[1,2],[2,3],[5,6],[6,7],[5,7]]，期望输出 false。
     */
    @Test
    public void testMultipleComponentsOneOddCycle() {
        int[][] dislikes = {{1, 2}, {2, 3}, {5, 6}, {6, 7}, {5, 7}};
        assertFalse(solution.possibleBipartition(7, dislikes));
    }

    /**
     * 测试目的：边界值——最大规模 n = 2000 的长链，可以二分。
     * 测试用例：n = 2000, dislikes = [[1,2],[2,3],...,[1999,2000]]，期望输出 true。
     */
    @Test(timeout = 2000)
    public void testMaxSizeLongChain() {
        int n = 2000;
        int[][] dislikes = new int[n - 1][];
        for (int i = 1; i < n; i++) {
            dislikes[i - 1] = new int[]{i, i + 1};
        }
        assertTrue(solution.possibleBipartition(n, dislikes));
    }

    /**
     * 测试目的：边界值——规模接近上限的奇环（1999 个人首尾相连），不能二分。
     * 测试用例：n = 1999，链 1-2-...-1999 再加边 [1,1999]，期望输出 false。
     */
    @Test(timeout = 2000)
    public void testLargeOddCycle() {
        int n = 1999;
        int[][] dislikes = new int[n][];
        for (int i = 1; i < n; i++) {
            dislikes[i - 1] = new int[]{i, i + 1};
        }
        dislikes[n - 1] = new int[]{1, n};
        assertFalse(solution.possibleBipartition(n, dislikes));
    }

    /**
     * 测试目的：随机对照测试——用 BFS 染色的参考实现验证大量随机输入。
     * 测试用例：固定随机种子 2024130080，生成 500 组 n ∈ [1, 30]、边数随机的图（满足 ai < bi 且不重复）。
     */
    @Test
    public void testRandomAgainstReference() {
        Random random = new Random(2024130080L);
        for (int round = 0; round < 500; round++) {
            int n = 1 + random.nextInt(30);
            int[][] dislikes = randomDislikes(n, random);
            boolean expected = referenceBipartition(n, dislikes);
            assertEquals("round " + round + ", n = " + n + ", dislikes = " + Arrays.deepToString(dislikes),
                    expected, solution.possibleBipartition(n, dislikes));
        }
    }

    /** 生成满足题目约束（ai < bi、每对不重复）的随机 dislikes。 */
    private static int[][] randomDislikes(int n, Random random) {
        int maxEdges = n * (n - 1) / 2;
        int edgeCount = maxEdges == 0 ? 0 : random.nextInt(Math.min(maxEdges, 2 * n) + 1);
        Set<Long> used = new HashSet<>();
        List<int[]> edges = new ArrayList<>();
        while (edges.size() < edgeCount) {
            int a = 1 + random.nextInt(n);
            int b = 1 + random.nextInt(n);
            if (a == b) {
                continue;
            }
            int lo = Math.min(a, b);
            int hi = Math.max(a, b);
            if (used.add((long) lo * 10000 + hi)) {
                edges.add(new int[]{lo, hi});
            }
        }
        return edges.toArray(new int[0][]);
    }

    /** 参考实现：BFS 二染色判断二分图。 */
    private static boolean referenceBipartition(int n, int[][] dislikes) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] d : dislikes) {
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);
        }
        int[] color = new int[n + 1];
        for (int start = 1; start <= n; start++) {
            if (color[start] != 0) {
                continue;
            }
            color[start] = 1;
            Deque<Integer> queue = new ArrayDeque<>();
            queue.add(start);
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : graph.get(u)) {
                    if (color[v] == 0) {
                        color[v] = -color[u];
                        queue.add(v);
                    } else if (color[v] == color[u]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
