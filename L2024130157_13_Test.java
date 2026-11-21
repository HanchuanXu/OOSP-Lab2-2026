
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class L2024130157_13_Test {

    /*
     * 测试用例设计原则：
     * 采用等价类划分和边界值分析。
     * 分别测试混合颜色、单一颜色、已排序、
     * 逆序排列、空数组和单元素数组。
     */

    // 测试目的：验证混合颜色数组能否正确排序
    @Test
    public void testMixedColors() {
        int[] nums = {2, 0, 2, 1, 1, 0};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
    }

    // 测试目的：验证三种颜色各出现一次
    @Test
    public void testThreeColors() {
        int[] nums = {2, 0, 1};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{0, 1, 2}, nums);
    }

    // 测试目的：验证全部为0的情况
    @Test
    public void testAllZeros() {
        int[] nums = {0, 0, 0};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{0, 0, 0}, nums);
    }

    // 测试目的：验证全部为1的情况
    @Test
    public void testAllOnes() {
        int[] nums = {1, 1, 1};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{1, 1, 1}, nums);
    }

    // 测试目的：验证全部为2的情况
    @Test
    public void testAllTwos() {
        int[] nums = {2, 2, 2};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{2, 2, 2}, nums);
    }

    // 测试目的：验证已经有序的数组
    @Test
    public void testAlreadySorted() {
        int[] nums = {0, 0, 1, 1, 2, 2};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
    }

    // 测试目的：验证逆序排列的数组
    @Test
    public void testReverseOrder() {
        int[] nums = {2, 2, 1, 1, 0, 0};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, nums);
    }

    // 测试目的：验证空数组的边界情况
    @Test
    public void testEmptyArray() {
        int[] nums = {};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{}, nums);
    }

    // 测试目的：验证单元素数组的边界情况
    @Test
    public void testSingleElement() {
        int[] nums = {1};
        new Solution().sortColors(nums);
        assertArrayEquals(new int[]{1}, nums);
    }
}
