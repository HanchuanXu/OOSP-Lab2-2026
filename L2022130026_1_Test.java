import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例设计的总体原则：
 * 1. 等价类划分：按结果类型划分为 整除 / 有限小数 / 无限循环小数；
 *    按符号划分为 正数 / 负数 / 零。
 * 2. 边界值分析：分子为0、分子分母为 int 最小值等边界情况。
 * 3. 每个等价类至少选取一个代表性测试用例。
 */
public class L2022130026_1_Test {

    private final Solution1 solution = new Solution1();

    /**
     * 测试目的：验证能整除时只返回整数部分
     * 测试用例：(2, 1) -> "2"
     */
    @Test
    public void testDivisible() {
        assertEquals("2", solution.fractionToDecimal(2, 1));
    }

    /**
     * 测试目的：验证有限小数的计算结果
     * 测试用例：(1, 2) -> "0.5"
     */
    @Test
    public void testFiniteDecimal() {
        assertEquals("0.5", solution.fractionToDecimal(1, 2));
    }

    /**
     * 测试目的：验证从小数点后第一位开始循环的情况
     * 测试用例：(4, 333) -> "0.(012)"
     */
    @Test
    public void testRepeatingFromStart() {
        assertEquals("0.(012)", solution.fractionToDecimal(4, 333));
    }

    /**
     * 测试目的：验证循环节不从第一位开始的情况
     * 测试用例：(1, 6) -> "0.1(6)"
     */
    @Test
    public void testRepeatingFromMiddle() {
        assertEquals("0.1(6)", solution.fractionToDecimal(1, 6));
    }

    /**
     * 测试目的：验证负数结果的符号处理
     * 测试用例：(-50, 8) -> "-6.25"，(1, -3) -> "-0.(3)"
     */
    @Test
    public void testNegative() {
        assertEquals("-6.25", solution.fractionToDecimal(-50, 8));
        assertEquals("-0.(3)", solution.fractionToDecimal(1, -3));
    }

    /**
     * 测试目的：验证分子为0的边界情况
     * 测试用例：(0, -5) -> "0"
     */
    @Test
    public void testZeroNumerator() {
        assertEquals("0", solution.fractionToDecimal(0, -5));
    }

    /**
     * 测试目的：验证 int 最小值时不发生溢出
     * 测试用例：(-2147483648, 1) -> "-2147483648"
     */
    @Test
    public void testIntMinValue() {
        assertEquals("-2147483648", solution.fractionToDecimal(Integer.MIN_VALUE, 1));
    }
}