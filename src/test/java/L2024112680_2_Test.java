import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试类：L2024112680_2_Test
 * 测试用例设计总体原则：
 * 1. 等价类划分：将输入划分为「有重复且可弹出」「有重复但不可弹出」「无重复」「全重复」四类；
 * 2. 边界值分析：覆盖长度 1、字母 'z'、26 个字母全出现等边界；
 * 3. 典型示例：使用题目给出的两个示例作为基准用例；
 * 4. 分支覆盖：分别覆盖 while 弹出条件成立与不成立两条路径。
 */
public class L2024112680_2_Test {

    private final Solution2 solution = new Solution2();

    /**
     * 测试目的：验证示例 1，覆盖「后续有更小字母时弹出栈顶」的分支。
     * 测试用例：s = "bcabc"，期望 "abc"。
     */
    @Test
    public void testExample1() {
        assertEquals("abc", solution.removeDuplicateLetters("bcabc"));
    }

    /**
     * 测试目的：验证示例 2，覆盖多次弹出与跳过重复字符。
     * 测试用例：s = "cbacdcbc"，期望 "acdb"。
     */
    @Test
    public void testExample2() {
        assertEquals("acdb", solution.removeDuplicateLetters("cbacdcbc"));
    }

    /**
     * 测试目的：验证单字符边界（长度最小值 1）。
     * 测试用例：s = "a"，期望 "a"。
     */
    @Test
    public void testSingleCharacter() {
        assertEquals("a", solution.removeDuplicateLetters("a"));
    }

    /**
     * 测试目的：验证全部字符相同时只保留一个。
     * 测试用例：s = "aaaa"，期望 "a"。
     */
    @Test
    public void testAllSameCharacters() {
        assertEquals("a", solution.removeDuplicateLetters("aaaa"));
    }

    /**
     * 测试目的：验证已按字典序递增时无需弹出。
     * 测试用例：s = "abc"，期望 "abc"。
     */
    @Test
    public void testIncreasingOrder() {
        assertEquals("abc", solution.removeDuplicateLetters("abc"));
    }

    /**
     * 测试目的：验证严格递减且每个字母只出现一次时无法弹出（剩余次数为 0）。
     * 测试用例：s = "zyx"，期望 "zyx"。
     */
    @Test
    public void testDecreasingOrder() {
        assertEquals("zyx", solution.removeDuplicateLetters("zyx"));
    }

    /**
     * 测试目的：验证字母 'z' 的数组下标不越界（原代码 25 长度数组会异常）。
     * 测试用例：s = "zaaz"，期望 "az"。
     */
    @Test
    public void testLetterZBoundary() {
        assertEquals("az", solution.removeDuplicateLetters("zaaz"));
    }

    /**
     * 测试目的：验证 26 个小写字母全部出现且不重复的边界情况。
     * 测试用例：s = "zyxwvutsrqponmlkjihgfedcba"，期望与原串相同。
     */
    @Test
    public void testAll26Letters() {
        assertEquals("zyxwvutsrqponmlkjihgfedcba",
                solution.removeDuplicateLetters("zyxwvutsrqponmlkjihgfedcba"));
    }
}
