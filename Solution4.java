import java.util.Arrays;

/**
 * @description:
 *
 * 给定一个无序的数组 nums，返回 数组在排序之后，相邻元素之间最大的差值 。如果数组元素个数小于 2，则返回 0 。
 *
 * 您必须编写一个在「线性时间」内运行并使用「线性额外空间」的算法。
 *
 *
 *
 * 示例 1:
 *
 * 输入: nums = [3,6,9,1]
 * 输出: 3
 * 解释: 排序后的数组是 [1,3,6,9], 其中相邻元素 (3,6) 和 (6,9) 之间都存在最大差值 3。
 * 示例 2:
 *
 * 输入: nums = [10]
 * 输出: 0
 * 解释: 数组元素个数小于 2，因此返回 0。
 *
 *
 * 提示:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 *
 */
class Solution4 {
    public int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }

        int maxVal = Arrays.stream(nums).max().getAsInt();
        int[] buf = new int[n];

        // 从个位到最高位进行稳定计数排序。
        for (long exp = 1; exp <= maxVal; exp *= 10) {
            int place = (int) exp;
            int[] counts = new int[10];
            for (int num : nums) {
                counts[(num / place) % 10]++;
            }
            for (int digit = 1; digit < counts.length; digit++) {
                counts[digit] += counts[digit - 1];
            }
            // 逆序取元素，保持同一数位的元素在上一轮中的相对顺序。
            for (int i = n - 1; i >= 0; i--) {
                int digit = (nums[i] / place) % 10;
                buf[--counts[digit]] = nums[i];
            }
            System.arraycopy(buf, 0, nums, 0, n);
        }

        int gap = 0;
        for (int i = 1; i < n; i++) {
            gap = Math.max(gap, nums[i] - nums[i - 1]);
        }
        return gap;
    }
}
