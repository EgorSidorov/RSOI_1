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

@javax.servlet.annotation.WebServlet(name = "Registration")
public class Registration extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        if(request.getParameter("name") != null && request.getParameter("password") != null)
        {
            if (request.getParameter("types").indexOf("insert") != -1)
            {
                if (UserInfo.getInstance().Insert_message(request.getParameter("name"), request.getParameter("password"), request.getParameter("message")))
                    request.setAttribute("name", "Сообщение добавлено");
                else
                    request.setAttribute("name", "Пользователя не найдено");
            }
            else if (request.getParameter("types").indexOf("show") != -1)
            {
                request.setAttribute("name", UserInfo.getInstance().Get_messages(request.getParameter("name"), request.getParameter("password")).toString());
            }
            else if (request.getParameter("types").indexOf("create") != -1)
            {
                if (UserInfo.getInstance().Create_User(request.getParameter("name"), request.getParameter("password")))
                    request.setAttribute("name", "Пользователь добавлен");
                else
                request.setAttribute("name", "Такой пользователь уже есть");
            }
            else request.setAttribute("name", "Введите тип запроса"+request.getParameter("types"));
        }
        else request.setAttribute("name", "Заполните обязательные поля логина или пароля");
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.getRequestDispatcher("/registration.jsp").forward(request, response);
    }
}
