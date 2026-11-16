import java.util.Arrays;
import java.util.Random;

/*
 * 测试用例设计总体原则：
 * 1. 等价类划分：覆盖全部满足、部分满足、全部不满足，以及重复和不重复元素。
 * 2. 边界值分析：覆盖长度为 1、长度为 100000、元素及 target 的合法边界，并检查最小值与最大值之和等于 target 时的包含关系
 * 3. 回归测试：针对循环遗漏末元素、错误取余、幂初值错误和二分不收敛设计用例
 * 4. 暴力对照：以固定随机种子生成小数组，枚举所有非空子序列核对结果
 */
public class L2024112876_5_Test {
    private static int checks = 0;

    public static void main(String[] args) {
        testExamples();
        testSingleElement();
        testEquivalentClasses();
        testThresholdAndInputBounds();
        testBinarySearch();
        testMaximumLengthAndModulo();
        testRepeatedCalls();
        testAgainstBruteForce();
        System.out.println("PASS: 8 test methods, " + checks + " assertions.");
    }

    //测试目的：核对题目示例；用例：三个示例的预期结果分别为 4、6、61
    public static void testExamples() {
        check(new int[]{3, 5, 6, 7}, 9, 4);
        check(new int[]{3, 3, 6, 8}, 10, 6);
        check(new int[]{2, 3, 3, 4, 6, 7}, 12, 61);
    }

    //测试目的：检查最小长度、末元素遍历和 2^0 初值；用例：[1]，target 为 1、2、3，预期 0、1、1
    public static void testSingleElement() {
        check(new int[]{1}, 1, 0);
        check(new int[]{1}, 2, 1);
        check(new int[]{1}, 3, 1);
    }

    //测试目的：覆盖各等价类及排序；用例：全部满足、全部不满足、部分满足、重复元素、乱序数组
    public static void testEquivalentClasses() {
        check(new int[]{1, 2, 3}, 6, 7);
        check(new int[]{4, 5, 6}, 7, 0);
        check(new int[]{2, 3, 4}, 6, 5);
        check(new int[]{2, 2, 2}, 4, 7);
        check(new int[]{7, 3, 6, 5}, 9, 4);
    }

    //测试目的：检查阈值两侧和输入数值边界；用例：[2,3] 配 target=4、5、6；包含 1000000 的数组
    public static void testThresholdAndInputBounds() {
        check(new int[]{2, 3}, 4, 1);
        check(new int[]{2, 3}, 5, 2);
        check(new int[]{2, 3}, 6, 3);
        check(new int[]{1, 1000000}, 1000000, 1);
        check(new int[]{500000}, 1000000, 1);
        check(new int[]{1000000}, 1000000, 0);
    }

    //测试目的：验证二分返回第一个大于 target 的位置及收敛；用例：[2,3,3,5]，阈值为 1、2、3、4、5、6
    public static void testBinarySearch() {
        Solution5 solution = new Solution5();
        int[] nums = {2, 3, 3, 5};
        int[] targets = {1, 2, 3, 4, 5, 6};
        int[] expected = {0, 1, 3, 3, 4, 4};
        for (int i = 0; i < targets.length; i++) {
            assertEquals("binarySearch target=" + targets[i], expected[i],
                    solution.binarySearch(nums, targets[i]));
        }
    }

    //测试目的：验证最大长度、幂数组边界及大结果取余；用例：100000 个 1，target=2，预期 (2^100000-1) mod P
    public static void testMaximumLengthAndModulo() {
        int[] nums = new int[100000];
        Arrays.fill(nums, 1);
        int expected = java.math.BigInteger.TWO.pow(nums.length)
                .subtract(java.math.BigInteger.ONE)
                .mod(java.math.BigInteger.valueOf(1000000007)).intValue();
        check(nums, 2, expected);
    }

    //测试目的：验证同一对象重复调用不会残留错误状态；用例：依次测试 [1,1]、[5]、[1]，预期 3、0、1
    public static void testRepeatedCalls() {
        Solution5 solution = new Solution5();
        assertEquals("first call", 3, solution.numSubseq(new int[]{1, 1}, 2));
        assertEquals("second call", 0, solution.numSubseq(new int[]{5}, 2));
        assertEquals("third call", 1, solution.numSubseq(new int[]{1}, 2));
    }

    //测试目的：以独立枚举核对组合计数；用例：种子 5，200 组长度 1~10、元素 1~15、target 1~30 的数组
    public static void testAgainstBruteForce() {
        Random random = new Random(5);
        for (int trial = 0; trial < 200; trial++) {
            int[] nums = new int[1 + random.nextInt(10)];
            for (int i = 0; i < nums.length; i++) {
                nums[i] = 1 + random.nextInt(15);
            }
            int target = 1 + random.nextInt(30);
            int expected = 0;
            for (int mask = 1; mask < (1 << nums.length); mask++) {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (int i = 0; i < nums.length; i++) {
                    if ((mask & (1 << i)) != 0) {
                        min = Math.min(min, nums[i]);
                        max = Math.max(max, nums[i]);
                    }
                }
                if (min + max <= target) {
                    expected++;
                }
            }
            check(nums, target, expected);
        }
    }

    private static void check(int[] nums, int target, int expected) {
        assertEquals(Arrays.toString(nums) + ", target=" + target, expected,
                new Solution5().numSubseq(nums.clone(), target));
    }

    private static void assertEquals(String description, int expected, int actual) {
        checks++;
        if (expected != actual) {
            throw new AssertionError(description + ": expected=" + expected + ", actual=" + actual);
        }
    }
}
