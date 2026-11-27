import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/*
 * 测试用例设计总体原则（Test Case Design Principles）：
 * 1. 等价类划分原则（Equivalence Class Partitioning）：
 *    - 有效等价类：长度为 4~12 的纯数字字符串，可划分为 4 个 [0, 255] 范围内的合法段且无前导零（单个 '0' 除外）。
 *    - 无效等价类：长度过短（< 4）或过长（> 12）的字符串、无法凑成 4 段的数字串、某一段数值超过 255 的字符串。
 * 2. 边界值与特殊情况原则（Boundary & Edge Cases）：
 *    - 全零输入（如 "0000"）、包含多个前导零的情况（如 "010010"）。
 *    - 单段边界值 0 与 255、字符串最短/最长有效长度边界。
 *    - 同一 Solution 实例连续多次调用 restoreIpAddresses，验证状态无残留。
 */
public class L2024130417_14_Test {

    /*
     * 测试目的：验证常见合法输入及示例场景下能否正确还原所有有效 IP 地址。
     * 用到的测试用例：
     * - s = "25525511135" -> ["255.255.11.135", "255.255.111.35"]
     * - s = "101023" -> ["1.0.10.23", "1.0.102.3", "10.1.0.23", "10.10.2.3", "101.0.2.3"]
     */
    @Test
    void testValidIpAddresses() {
        Solution solution = new Solution();

        List<String> res1 = solution.restoreIpAddresses("25525511135");
        assertEquals(2, res1.size());
        assertTrue(res1.contains("255.255.11.135"));
        assertTrue(res1.contains("255.255.111.35"));

        List<String> res2 = solution.restoreIpAddresses("101023");
        assertEquals(5, res2.size());
        assertTrue(res2.containsAll(List.of(
                "1.0.10.23",
                "1.0.102.3",
                "10.1.0.23",
                "10.10.2.3",
                "101.0.2.3"
        )));
    }

    /*
     * 测试目的：验证前导零处理规则——单独的 "0" 可作为合法段，但含前导零的多位数（如 "01"）不合法。
     * 用到的测试用例：
     * - s = "0000" -> ["0.0.0.0"]
     * - s = "010010" -> ["0.10.0.10", "0.100.1.0"]
     */
    @Test
    void testLeadingZeros() {
        Solution solution = new Solution();

        assertEquals(List.of("0.0.0.0"), solution.restoreIpAddresses("0000"));

        List<String> res = solution.restoreIpAddresses("010010");
        assertEquals(2, res.size());
        assertTrue(res.contains("0.10.0.10"));
        assertTrue(res.contains("0.100.1.0"));
    }

    /*
     * 测试目的：验证无效输入或无法形成合法 4 段 IP 地址时返回空列表。
     * 用到的测试用例：
     * - s = ""（空字符串） -> []
     * - s = "123"（长度小于 4） -> []
     * - s = "9999999999999"（段数值超过 255 且长度超限） -> []
     */
    @Test
    void testInvalidAndOutOfRangeInputs() {
        Solution solution = new Solution();

        assertTrue(solution.restoreIpAddresses("").isEmpty());
        assertTrue(solution.restoreIpAddresses("123").isEmpty());
        assertTrue(solution.restoreIpAddresses("9999999999999").isEmpty());
    }

    /*
     * 测试目的：验证同一 Solution 对象连续调用多次时结果相互独立，不会累加历史结果。
     * 用到的测试用例：
     * - 第一次调用 s = "0000"，第二次调用 s = "25525511135"
     */
    @Test
    void testRepeatedCallsDoNotAccumulateResults() {
        Solution solution = new Solution();

        List<String> first = solution.restoreIpAddresses("0000");
        List<String> second = solution.restoreIpAddresses("25525511135");

        assertEquals(List.of("0.0.0.0"), first);
        assertEquals(List.of("255.255.11.135", "255.255.111.35"), second);
    }
}
