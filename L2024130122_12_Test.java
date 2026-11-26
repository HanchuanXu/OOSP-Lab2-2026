import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * 题目12：字符串相乘（Solution12.java 中的 Solution.multiply）的单元测试类。
 *
 * 测试用例设计的总体原则：
 * 1. 等价类划分：
 *    - 有效等价类A：两个操作数都为 "0" 以外的正整数，且乘积在 long 范围内（普通乘法）；
 *    - 有效等价类B：至少一个操作数为 "0"（乘积为 "0" 的特殊分支）；
 *    - 有效等价类C：乘积超出 long 范围的大整数乘法（题目要求不能转换为整数计算）；
 *    - 有效等价类D：操作数长度不同（num1 长、num2 短，或者反过来）。
 * 2. 边界值分析：
 *    - 最短输入：一位数乘一位数（"1"、"9"）；
 *    - 进位边界：9×9、99×99、999×999 等每一位都产生进位的情况；
 *    - 乘数为 "1"（单位元）以及含有中间 0 的数（如 "101"、"1000"）。
 * 3. 对比验证：使用 java.math.BigInteger 只作为“测试预言”(test oracle) 计算期望结果，
 *    被测代码本身不使用 BigInteger，符合题目约束。
 * 4. 同时对辅助方法 addStrings 做独立测试，保证乘法依赖的加法正确。
 */
public class L2024130122_12_Test {

    private Solution solution;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    /**
     * 测试目的：验证题目给出的两个示例（有效等价类A）。
     * 测试用例：("2","3") -> "6"；("123","456") -> "56088"。
     */
    @Test
    public void testExamplesFromProblem() {
        assertEquals("6", solution.multiply("2", "3"));
        assertEquals("56088", solution.multiply("123", "456"));
    }

    /**
     * 测试目的：验证有一个或两个操作数为 "0" 时返回 "0"（有效等价类B）。
     * 原代码中 String ans = "0" 缺少分号、使用了非短路的 |，此用例覆盖该分支。
     * 测试用例：("0","0")、("0","12345")、("98765","0")。
     */
    @Test
    public void testZeroOperand() {
        assertEquals("0", solution.multiply("0", "0"));
        assertEquals("0", solution.multiply("0", "12345"));
        assertEquals("0", solution.multiply("98765", "0"));
    }

    /**
     * 测试目的：边界值——一位数相乘，包括最小非零值和最大一位数。
     * 测试用例：("1","1")->"1"；("1","9")->"9"；("9","9")->"81"；("5","2")->"10"。
     */
    @Test
    public void testSingleDigit() {
        assertEquals("1", solution.multiply("1", "1"));
        assertEquals("9", solution.multiply("1", "9"));
        assertEquals("81", solution.multiply("9", "9"));
        assertEquals("10", solution.multiply("5", "2"));
    }

    /**
     * 测试目的：边界值——每一位都产生进位的情况，检查进位处理（add 变量）是否正确。
     * 测试用例：("99","99")->"9801"；("999","999")->"998001"；("9999","9")->"89991"。
     */
    @Test
    public void testAllCarries() {
        assertEquals("9801", solution.multiply("99", "99"));
        assertEquals("998001", solution.multiply("999", "999"));
        assertEquals("89991", solution.multiply("9999", "9"));
    }

    /**
     * 测试目的：乘数为单位元 "1" 以及含中间 0、末尾 0 的数，检查补零逻辑和中间 0 的处理。
     * 测试用例：("123456789","1")；("1","123456789")；("101","101")->"10201"；("1000","1000")->"1000000"。
     */
    @Test
    public void testOneAndInnerZeros() {
        assertEquals("123456789", solution.multiply("123456789", "1"));
        assertEquals("123456789", solution.multiply("1", "123456789"));
        assertEquals("10201", solution.multiply("101", "101"));
        assertEquals("1000000", solution.multiply("1000", "1000"));
    }

    /**
     * 测试目的：两个操作数长度不同（有效等价类D），并验证乘法交换律 a*b == b*a。
     * 测试用例：("12345","67")、("67","12345") -> "827115"。
     */
    @Test
    public void testDifferentLengthsAndCommutative() {
        assertEquals("827115", solution.multiply("12345", "67"));
        assertEquals("827115", solution.multiply("67", "12345"));
    }

    /**
     * 测试目的：乘积超出 long 范围的大整数（有效等价类C），这是题目的核心要求。
     * 测试用例：("123456789123456789","987654321987654321")，期望值由 BigInteger 计算；
     *          ("99999999999999999999","99999999999999999999")。
     */
    @Test
    public void testLargeNumbers() {
        String a = "123456789123456789";
        String b = "987654321987654321";
        assertEquals(new BigInteger(a).multiply(new BigInteger(b)).toString(),
                solution.multiply(a, b));
        String c = "99999999999999999999";
        assertEquals("9999999999999999999800000000000000000001", solution.multiply(c, c));
    }

    /**
     * 测试目的：随机测试，覆盖大量普通输入，与 BigInteger 的结果对比。
     * 测试用例：固定随机种子生成 200 组 1~30 位、不含前导零的正整数对。
     */
    @Test
    public void testRandomAgainstBigInteger() {
        Random random = new Random(2024130122L);
        for (int k = 0; k < 200; k++) {
            String a = randomNumber(random, 1 + random.nextInt(30));
            String b = randomNumber(random, 1 + random.nextInt(30));
            String expected = new BigInteger(a).multiply(new BigInteger(b)).toString();
            assertEquals("a=" + a + ", b=" + b, expected, solution.multiply(a, b));
        }
    }

    /**
     * 测试目的：独立测试辅助方法 addStrings（字符串加法），包括进位和长度不同的情况。
     * 测试用例：("0","0")->"0"；("1","9")->"10"；("999","1")->"1000"；("123","4567")->"4690"。
     */
    @Test
    public void testAddStrings() {
        assertEquals("0", solution.addStrings("0", "0"));
        assertEquals("10", solution.addStrings("1", "9"));
        assertEquals("1000", solution.addStrings("999", "1"));
        assertEquals("4690", solution.addStrings("123", "4567"));
    }

    /** 生成指定长度、首位不为 0 的随机十进制数字字符串。 */
    private static String randomNumber(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        sb.append((char) ('1' + random.nextInt(9)));
        for (int i = 1; i < length; i++) {
            sb.append((char) ('0' + random.nextInt(10)));
        }
        return sb.toString();
    }
}
