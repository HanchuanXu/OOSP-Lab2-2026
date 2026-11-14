import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

/*
 * 总体原则：用等价类划分覆盖不足两个元素、重复值和普通无序数组；
 * 用边界值分析覆盖两元素、末尾元素、十进制整次幂、最大元素值和最大长度。
 * 另外在固定种子的随机数据上，与独立的比较排序基准进行对照。
 */
class L2024113197_4_Test {
    private final Solution4 solution = new Solution4();

    // 目的：覆盖不足两个元素的返回分支。用例：[] 和 [10]。
    @Test
    void returnsZeroForFewerThanTwoElements() {
        assertEquals(0, solution.maximumGap(new int[0]));
        assertEquals(0, solution.maximumGap(new int[] {10}));
    }

    // 目的：验证最短有效输入的两个元素都被处理。用例：[9, 1]。
    @Test
    void handlesExactlyTwoElements() {
        assertEquals(8, solution.maximumGap(new int[] {9, 1}));
    }

    // 目的：验证题目示例的两个并列最大间距。用例：[3, 6, 9, 1]。
    @Test
    void matchesTheProblemExample() {
        assertEquals(3, solution.maximumGap(new int[] {3, 6, 9, 1}));
    }

    // 目的：验证扫描包含原数组的最后一个元素。用例：[2, 5, 7, 100]。
    @Test
    void includesTheLastInputElement() {
        assertEquals(93, solution.maximumGap(new int[] {2, 5, 7, 100}));
    }

    // 目的：验证最高位等于位权时仍执行该轮。用例：[10, 2, 1]、[100, 99, 80]。
    @Test
    void processesHighestDigitAtPowersOfTen() {
        assertEquals(8, solution.maximumGap(new int[] {10, 2, 1}));
        assertEquals(19, solution.maximumGap(new int[] {100, 99, 80}));
    }

    // 目的：验证多位数按正确位权稳定排序。用例：[21, 12, 11, 1]。
    @Test
    void sortsMultipleDigitsStably() {
        assertEquals(10, solution.maximumGap(new int[] {21, 12, 11, 1}));
    }

    // 目的：覆盖全零、相同非零值及混合重复值。用例：[0, 0, 0]、[7, 7, 7]、[8, 4, 4, 0]。
    @Test
    void handlesZeroAndDuplicateValues() {
        assertEquals(0, solution.maximumGap(new int[] {0, 0, 0}));
        assertEquals(0, solution.maximumGap(new int[] {7, 7, 7}));
        assertEquals(4, solution.maximumGap(new int[] {8, 4, 4, 0}));
    }

    // 目的：覆盖题设的长度和值域上界。用例：十万个元素，除末尾为 10^9 外均为 0。
    @Test
    void handlesMaximumLengthAndValue() {
        int[] nums = new int[100_000];
        nums[nums.length - 1] = 1_000_000_000;
        assertEquals(1_000_000_000, solution.maximumGap(nums));
    }

    // 目的：与排序基准作差分对照。用例：固定种子生成 500 组，交替使用小值域和全值域。
    @Test
    void agreesWithSortingOracle() {
        Random random = new Random(4);
        for (int trial = 0; trial < 500; trial++) {
            int[] nums = new int[random.nextInt(101)];
            int bound = trial % 2 == 0 ? 32 : 1_000_000_001;
            for (int i = 0; i < nums.length; i++) {
                nums[i] = random.nextInt(bound);
            }

            int[] sorted = nums.clone();
            Arrays.sort(sorted);
            int expected = 0;
            for (int i = 1; i < sorted.length; i++) {
                expected = Math.max(expected, sorted[i] - sorted[i - 1]);
            }
            assertEquals(expected, solution.maximumGap(nums), "随机用例序号 " + trial);
        }
    }
}
