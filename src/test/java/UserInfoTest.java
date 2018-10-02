import org.junit.Assert;
import org.junit.Test;

public class UserInfoTest {

    @Test
    public void TestCreateUser() {
        Assert.assertEquals(true,UserInfo.getInstance().Create_User("name_test1","qwerty"));
    }

    @Test
    public void TestInsertMessage() {
        String name = "name_test2";
        String password = "qwerty";
        Assert.assertEquals(true,UserInfo.getInstance().Create_User(name,password));
        Assert.assertEquals(true,UserInfo.getInstance().Insert_message(name,password,"some string"));
    }
}