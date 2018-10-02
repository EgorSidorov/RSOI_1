import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Created by Егор on 11.09.2018.
 */

class UserInfo
{
    private Map<String, ArrayList<String> > message;
    private Map<String, String> users;
    private Semaphore obj_mutex;

    private static final UserInfo instance = new UserInfo();

    private UserInfo()
    {
        obj_mutex = new Semaphore(1,true);
        message = new HashMap<String, ArrayList<String> >();
        users = new HashMap<String, String>();
    }

    public static UserInfo getInstance(){
        return instance;
    }

    public boolean Create_User(String name, String password)
    {
        try{
            obj_mutex.acquire();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if(users.containsKey(name))
        {
            obj_mutex.release();
            return false;
        }
        users.put(name,password);
        ArrayList<String> messages = new ArrayList<String>();
        message.put(name, messages);
        obj_mutex.release();
        return true;
    }

    public boolean Insert_message(String name, String password ,String string_message)
    {
        try{
            obj_mutex.acquire();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        boolean result = false;
        if(users.containsKey(name)  && users.get(name).equals(password))
        {
            ArrayList<String> copy_message = message.get(name);
            copy_message.add(string_message);
            message.replace(name,copy_message);
            result = true;
        }
        obj_mutex.release();
        return result;
    }

    public ArrayList<String> Get_messages(String name,String password)
    {
        ArrayList<String> value_return;
        try{
            obj_mutex.acquire();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        if(users.containsKey(name) && users.get(name).equals(password))
            value_return = message.get(name);
        else
        {
            value_return = new ArrayList<String>();
            value_return.add("Нет сообщений");
        }
        obj_mutex.release();
        return value_return;
    }
}

@WebServlet(name = "Main")
public class Main extends HttpServlet {
    static String response_string =
            "<form name=\"test\" method=\"post\" action=\"/\">"+
            "  <p><b>Тип запроса:</b><Br>\n" +
            "   <input type=\"radio\" name=\"types\" value=\"insert\"> Добавить сообщение<Br>\n" +
            "   <input type=\"radio\" name=\"types\" value=\"show\"> Показать сообщения<Br>\n" +
            "   <input type=\"radio\" name=\"types\" value=\"create\"> Создать пользователя<Br>\n" +
            "  </p>\n" +
            "<p><b>Имя:</b><br>\n" +
            "   <input type=\"text\" name=\"name\" size=\"40\">\n" +
            "  </p>\n" +
            "    <p><b>Пароль:</b><br>\n" +
            "   <input type=\"text\" name=\"password\" size=\"40\">\n" +
            "  </p>\n" +
            "        <p><b>Сообщение:</b><br>\n" +
            "   <input type=\"textarea\" name=\"message\" cols=\"40\" row=\"3\">\n" +
            "  </p>"+
                    "<p><input type=\"submit\" value=\"Отправить\">"+
                    "<input type=\"reset\" value=\"Очистить\"></p>"+
                    "</form>";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(response_string);
        if(request.getParameter("name") != null && request.getParameter("password") != null)
        {
            if (request.getParameter("types").indexOf("insert") != -1)
            {
                if (UserInfo.getInstance().Insert_message(request.getParameter("name"), request.getParameter("password"), request.getParameter("message")))
                    response.getWriter().write("Сообщение добавлено");
                else
                    response.getWriter().write("Пользователя не найдено");

            }
            else if (request.getParameter("types").indexOf("show") != -1)
            {
                response.getWriter().write(UserInfo.getInstance().Get_messages(request.getParameter("name"), request.getParameter("password")).toString());
            }
            else if (request.getParameter("types").indexOf("create") != -1)
            {
                if (UserInfo.getInstance().Create_User(request.getParameter("name"), request.getParameter("password")))
                    response.getWriter().write("Пользователь добавлен");
                else
                    response.getWriter().write("Такой пользователь уже есть");

            }
            else response.getWriter().write("Введите тип запроса"+request.getParameter("types"));
        }
        else response.getWriter().write("Заполните обязательные поля логина или пароля");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(response_string);
    }
}
