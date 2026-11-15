import java.util.Arrays;

/**
 * 给定一个无序数组 nums，返回数组排序后相邻元素之间的最大差值。
 * 如果数组元素个数小于 2，则返回 0。
 *
 * 使用 LSD 基数排序：
 * 时间复杂度：O(n)
 * 额外空间复杂度：O(n)
 */
class Solution4 {

    public int maximumGap(int[] nums) {
        int n = nums.length;

        if (n < 2) {
            return 0;
        }

        int maxVal = Arrays.stream(nums).max().getAsInt();
        int[] buf = new int[n];

        // exp 表示当前处理的十进制位：1、10、100……
        long exp = 1;

        while (maxVal >= exp) {
            int[] count = new int[10];

            // 统计当前位上每个数字出现的次数
            for (int value : nums) {
                int digit = (int) ((value / exp) % 10);
                count[digit]++;
            }

            // 计算前缀和，确定每个数字在结果数组中的结束位置
            for (int i = 1; i < 10; i++) {
                count[i] += count[i - 1];
            }

            // 从后向前遍历，保证计数排序的稳定性
            for (int i = n - 1; i >= 0; i--) {
                int digit = (int) ((nums[i] / exp) % 10);
                buf[--count[digit]] = nums[i];
            }

            // 将本轮排序结果复制回原数组
            System.arraycopy(buf, 0, nums, 0, n);

            // 处理下一位
            exp *= 10;
        }

        int maxGap = 0;

        // 数组已经完成排序，计算相邻元素的最大差值
        for (int i = 1; i < n; i++) {
            maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
        }

        return maxGap;
    }
}