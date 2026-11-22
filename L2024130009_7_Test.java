import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for Solution7 (Smallest String With Swaps).
 *
 * Test case design principles:
 * 1. Equivalence partitioning: inputs are divided into classes, namely
 *    no pairs, one pair, several pairs forming one connected group, and
 *    several pairs forming separate groups. One or more cases represent each class.
 * 2. Boundary value analysis: empty pair list, a single pair, a string of
 *    length 1, a pair that uses the last index, and a very large input
 *    (length 100000, the upper bound in the problem statement).
 * 3. Error guessing: duplicate pairs, a pair with equal indices [i, i],
 *    repeated characters, and checks on the length and content of the output.
 */
public class L2024130009_7_Test {

    private final Solution7 solution = new Solution7();

    private static List<List<Integer>> p(int[][] arr) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] a : arr) {
            list.add(Arrays.asList(a[0], a[1]));
        }
        return list;
    }

    /** Purpose: problem example 1, two separate groups. Case: s="dcab", pairs=[[0,3],[1,2]], expected "bacd". */
    @Test
    public void testExample1() {
        assertEquals("bacd", solution.smallestStringWithSwaps("dcab", p(new int[][]{{0, 3}, {1, 2}})));
    }

    /** Purpose: problem example 2, all indices in one group. Case: s="dcab", pairs=[[0,3],[1,2],[0,2]], expected "abcd". */
    @Test
    public void testExample2() {
        assertEquals("abcd", solution.smallestStringWithSwaps("dcab", p(new int[][]{{0, 3}, {1, 2}, {0, 2}})));
    }

    /** Purpose: problem example 3, a chain of pairs. Case: s="cba", pairs=[[0,1],[1,2]], expected "abc". */
    @Test
    public void testExample3() {
        assertEquals("abc", solution.smallestStringWithSwaps("cba", p(new int[][]{{0, 1}, {1, 2}})));
    }

    /** Purpose: boundary, exactly ONE pair must still be applied. Case: s="cba", pairs=[[0,1]], expected "bca". */
    @Test
    public void testSinglePair() {
        assertEquals("bca", solution.smallestStringWithSwaps("cba", p(new int[][]{{0, 1}})));
    }

    /** Purpose: boundary, empty pair list returns the string unchanged. Case: s="dcab", pairs=[], expected "dcab". */
    @Test
    public void testEmptyPairs() {
        assertEquals("dcab", solution.smallestStringWithSwaps("dcab", p(new int[][]{})));
    }

    /** Purpose: boundary, string of length 1. Case: s="a", pairs=[], expected "a". */
    @Test
    public void testSingleCharacter() {
        assertEquals("a", solution.smallestStringWithSwaps("a", p(new int[][]{})));
    }

    /** Purpose: a pair that uses the LAST index must not go out of bounds. Case: s="dcba", pairs=[[2,3]], expected "dcab". */
    @Test
    public void testPairWithLastIndex() {
        assertEquals("dcab", solution.smallestStringWithSwaps("dcba", p(new int[][]{{2, 3}})));
    }

    /** Purpose: the result must contain only the original characters (no extra spaces). Case: s="dcab", pairs=[[0,3],[1,2],[0,2]], expected length 4. */
    @Test
    public void testResultLengthAndNoSpaces() {
        String result = solution.smallestStringWithSwaps("dcab", p(new int[][]{{0, 3}, {1, 2}, {0, 2}}));
        assertEquals(4, result.length());
        assertEquals(-1, result.indexOf(' '));
    }

    /** Purpose: duplicate pairs and a pair with equal indices are harmless. Case: s="cba", pairs=[[0,1],[0,1],[1,1]], expected "bca". */
    @Test
    public void testDuplicateAndSelfPairs() {
        assertEquals("bca", solution.smallestStringWithSwaps("cba", p(new int[][]{{0, 1}, {0, 1}, {1, 1}})));
    }

    /** Purpose: two separate groups and one untouched index. Case: s="zyxwv", pairs=[[0,1],[3,4]], expected "yzxvw". */
    @Test
    public void testSeparateGroupsAndUntouchedIndex() {
        assertEquals("yzxvw", solution.smallestStringWithSwaps("zyxwv", p(new int[][]{{0, 1}, {3, 4}})));
    }

    /** Purpose: a long chain connects all indices, so the whole string is sorted. Case: s="edcba", pairs=[[0,1],[1,2],[2,3],[3,4]], expected "abcde". */
    @Test
    public void testLongChain() {
        assertEquals("abcde", solution.smallestStringWithSwaps("edcba", p(new int[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}})));
    }

    /** Purpose: repeated characters. Case: s="bbaa", pairs=[[0,3]], expected "abab". */
    @Test
    public void testRepeatedCharacters() {
        assertEquals("abab", solution.smallestStringWithSwaps("bbaa", p(new int[][]{{0, 3}})));
    }

    /** Purpose: upper-bound size (10^5) must work and be fast. Case: 100000 characters in descending pattern, chain pairs, expected sorted string. */
    @Test
    public void testLargeInput() {
        int n = 100000;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append((char) ('z' - (i % 26)));
        }
        String s = sb.toString();
        int[][] arr = new int[n - 1][2];
        for (int i = 0; i < n - 1; i++) {
            arr[i][0] = i;
            arr[i][1] = i + 1;
        }
        char[] expected = s.toCharArray();
        Arrays.sort(expected);
        assertEquals(new String(expected), solution.smallestStringWithSwaps(s, p(arr)));
    }
}
