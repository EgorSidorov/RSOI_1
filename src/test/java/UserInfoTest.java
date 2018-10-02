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

    @Test
    public void TestGetMessages() {
        String name = "name_test3";
        String password = "qwerty";
        Assert.assertEquals(true,UserInfo.getInstance().Create_User(name,password));
        Assert.assertEquals(true,UserInfo.getInstance().Insert_message(name,password,"some string1"));
        Assert.assertEquals(true,UserInfo.getInstance().Insert_message(name,password,"some string2"));
        Assert.assertEquals(true,UserInfo.getInstance().Get_messages(name,password).contains("some string1"));
    }

    @Test
    public void TestValidPassword() {
        String name = "name_test4";
        String password = "qwerty";
        Assert.assertEquals(true,UserInfo.getInstance().Create_User(name,password));
        Assert.assertEquals(false,UserInfo.getInstance().Insert_message(name,"123","some string1"));
    }


}