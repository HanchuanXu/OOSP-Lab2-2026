import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Solution15Test {

    @Test
    void leadingZerosIgnored() {
        Solution15 s = new Solution15();
        assertEquals(0, s.compareVersion("1.01", "1.001"));
    }

    @Test
    void missingRevisionIsZero() {
        Solution15 s = new Solution15();
        assertEquals(0, s.compareVersion("1.0", "1.0.0"));
    }

    @Test
    void firstRevisionSmaller() {
        Solution15 s = new Solution15();
        assertEquals(-1, s.compareVersion("0.1", "1.1"));
    }

    @Test
    void firstRevisionGreater() {
        Solution15 s = new Solution15();
        assertEquals(1, s.compareVersion("1.2", "1.1"));
    }

    @Test
    void longerVersionGreater() {
        Solution15 s = new Solution15();
        assertEquals(1, s.compareVersion("1.0.1", "1.0"));
    }

    @Test
    void identicalVersions() {
        Solution15 s = new Solution15();
        assertEquals(0, s.compareVersion("2.5.33", "2.5.33"));
    }
}
