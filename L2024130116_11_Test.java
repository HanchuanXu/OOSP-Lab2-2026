import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * 题目11：三数之和（Solution11.java 中的 Solution.threeSum）的单元测试类。
 *
 * 测试用例设计的总体原则：
 * 1. 等价类划分：
 *    - 有效等价类A：存在一个或多个和为0的三元组（普通情况）；
 *    - 有效等价类B：不存在和为0的三元组（结果为空列表）；
 *    - 有效等价类C：数组中有大量重复元素（检验去重逻辑）；
 *    - 有效等价类D：全为正数或全为负数（不可能有解）。
 * 2. 边界值分析：
 *    - 数组长度为 0、1、2（不足三个元素）以及恰好为 3；
 *    - 全为 0 的数组（[0,0,0]、[0,0,0,0]）；
 *    - 元素取值较大（如 ±100000）的情况。
 * 3. 结果比较原则：题目说明输出顺序和三元组内部顺序都不重要，
 *    因此比较时先将每个三元组排序，再把结果放入集合进行比较；
 *    同时检查结果中没有重复的三元组。
 * 4. 随机测试：用暴力三重循环 O(n^3) 作为“测试预言”(test oracle)，
 *    与被测方法在随机数组上的结果对比。
 */
public class L2024130116_11_Test {

    private Solution solution;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    /**
     * 测试目的：验证题目示例1（有效等价类A，含重复元素）。
     * 测试用例：[-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]。
     */
    @Test
    public void testExample1() {
        assertSameTriplets(Arrays.asList(Arrays.asList(-1, -1, 2), Arrays.asList(-1, 0, 1)),
                solution.threeSum(new int[] {-1, 0, 1, 2, -1, -4}));
    }

    /**
     * 测试目的：验证题目示例2，不存在满足条件的三元组（有效等价类B）。
     * 测试用例：[0,1,1] -> []。
     */
    @Test
    public void testExample2NoSolution() {
        assertTrue(solution.threeSum(new int[] {0, 1, 1}).isEmpty());
    }

    /**
     * 测试目的：验证题目示例3以及全为0的边界情况，结果只能出现一次 [0,0,0]。
     * 测试用例：[0,0,0] -> [[0,0,0]]；[0,0,0,0] -> [[0,0,0]]。
     */
    @Test
    public void testAllZeros() {
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(0, 0, 0));
        assertSameTriplets(expected, solution.threeSum(new int[] {0, 0, 0}));
        assertSameTriplets(expected, solution.threeSum(new int[] {0, 0, 0, 0}));
    }

    /**
     * 测试目的：边界值——数组长度小于3时不可能有三元组，且不能抛出异常。
     * 原代码第33行 nums.length() 导致编译错误，此用例同时覆盖长度计算。
     * 测试用例：[]、[0]、[1,-1]。
     */
    @Test
    public void testTooShortArrays() {
        assertTrue(solution.threeSum(new int[] {}).isEmpty());
        assertTrue(solution.threeSum(new int[] {0}).isEmpty());
        assertTrue(solution.threeSum(new int[] {1, -1}).isEmpty());
    }

    /**
     * 测试目的：全为正数或全为负数时没有解（有效等价类D）。
     * 测试用例：[1,2,3,4] -> []；[-5,-3,-1] -> []。
     */
    @Test
    public void testAllPositiveOrAllNegative() {
        assertTrue(solution.threeSum(new int[] {1, 2, 3, 4}).isEmpty());
        assertTrue(solution.threeSum(new int[] {-5, -3, -1}).isEmpty());
    }

    /**
     * 测试目的：大量重复元素时结果不重复（有效等价类C），检查第一、二层循环的去重逻辑。
     * 测试用例：[-2,0,0,2,2,-2,0,1,-1,-1,1] -> [[-2,0,2],[-2,1,1],[-1,-1,2],[-1,0,1],[0,0,0]]。
     */
    @Test
    public void testManyDuplicates() {
        assertSameTriplets(Arrays.asList(
                        Arrays.asList(-2, 0, 2), Arrays.asList(-2, 1, 1), Arrays.asList(-1, -1, 2),
                        Arrays.asList(-1, 0, 1), Arrays.asList(0, 0, 0)),
                solution.threeSum(new int[] {-2, 0, 0, 2, 2, -2, 0, 1, -1, -1, 1}));
    }

    /**
     * 测试目的：指针相遇（second == third）时应正确退出，且多组解都能找到。
     * 测试用例：[-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6] 与暴力解法结果对比。
     */
    @Test
    public void testMultipleSolutionsAgainstBruteForce() {
        int[] nums = {-4, -2, -2, -2, 0, 1, 2, 2, 2, 3, 3, 4, 4, 6, 6};
        assertSameTriplets(bruteForce(nums), solution.threeSum(nums.clone()));
    }

    /**
     * 测试目的：较大数值时不发生错误。
     * 测试用例：[-100000,50000,50000,1,2] -> [[-100000,50000,50000]]。
     */
    @Test
    public void testLargeValues() {
        assertSameTriplets(Arrays.asList(Arrays.asList(-100000, 50000, 50000)),
                solution.threeSum(new int[] {-100000, 50000, 50000, 1, 2}));
    }

    /**
     * 测试目的：随机测试，与暴力解法（测试预言）对比。
     * 测试用例：固定种子生成 300 个长度 0~25、元素范围 [-10,10] 的随机数组。
     */
    @Test
    public void testRandomAgainstBruteForce() {
        Random random = new Random(2024130116L);
        for (int k = 0; k < 300; k++) {
            int[] nums = new int[random.nextInt(26)];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = random.nextInt(21) - 10;
            }
            assertSameTriplets(bruteForce(nums), solution.threeSum(nums.clone()));
        }
    }

    /** 暴力 O(n^3) 求所有不重复的三元组，作为期望结果。 */
    private static List<List<Integer>> bruteForce(int[] nums) {
        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        int[] t = {nums[i], nums[j], nums[k]};
                        Arrays.sort(t);
                        set.add(Arrays.asList(t[0], t[1], t[2]));
                    }
                }
            }
        }
        return new ArrayList<>(set);
    }

    /** 忽略顺序比较两个三元组列表，并检查实际结果中没有重复三元组。 */
    private static void assertSameTriplets(List<List<Integer>> expected, List<List<Integer>> actual) {
        Set<List<Integer>> exp = normalize(expected);
        Set<List<Integer>> act = normalize(actual);
        assertEquals("结果中存在重复的三元组", actual.size(), act.size());
        assertEquals(exp, act);
    }

    private static Set<List<Integer>> normalize(List<List<Integer>> triplets) {
        Set<List<Integer>> set = new HashSet<>();
        for (List<Integer> t : triplets) {
            List<Integer> copy = new ArrayList<>(t);
            copy.sort(null);
            set.add(copy);
        }
        return set;
    }
}
