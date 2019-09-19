import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author flsh
 * @version 1.0
 * @date 2019-09-19
 * @since Jdk 1.8
 */
@RunWith(JUnit4.class)
public class RegexTest {

    @Test
    public void testRegexCode(){
        String code = "000045";
        System.out.println(code.matches("^(((002|000|200|300|600|900)[\\d]{3})|60[\\d]{4})$"));
    }
}
