<%--
  Created by IntelliJ IDEA.
  User: Егор
  Date: 27.09.2018
  Time: 1:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form name="test" method="post" action="/">
    <p><b>Тип запроса:</b><Br>
        <input type="radio" name="types" value="insert"> Добавить сообщение<Br>
        <input type="radio" name="types" value="show"> Показать сообщения<Br>
        <input type="radio" name="types" value="create"> Создать пользователя<Br>
    </p>
    <p><b>Имя:</b><br>
        <input type="text" name="name" size="40">
    </p>
    <p><b>Пароль:</b><br>
        <input type="text" name="password" size="40">
    </p>
    <p><b>Сообщение:</b><br>
        <input type="textarea" name="message" cols="40" row="3">
    </p>
    <p><input type="submit" value="Отправить">
        <input type="reset" value="Очистить"></p>
</form>
${name}

</body>
</html>

