<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Iniciar sesion</title>
<link rel="stylesheet" href="/estilos.css">
</head>
<body>
<h2>Consultas medicas - Iniciar sesion</h2>
<% if (request.getParameter("error") != null) { %>
<p style="color:red">Usuario o contraseña incorrectos</p>
<% } %>
<form method="post" action="/login">
    <p>Usuario<br><input name="username" required></p>
    <p>Contraseña<br><input name="password" type="password" required></p>
    <button type="submit">Entrar</button>
</form>
</body>
</html>
