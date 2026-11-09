<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Iniciar sesion</title>
<link rel="stylesheet" href="/estilos.css">
</head>
<body>
<h2>Ingreso al sistema de consultas</h2>
<form method="post" action="/login">
    <table>
        <tr><td>Usuario</td><td><input name="username" required></td></tr>
        <tr><td>Contraseña</td><td><input name="password" type="password" required></td></tr>
    </table>
    <button type="submit">Ingresar</button>
</form>
<% if (request.getParameter("error") != null) { %>
<p style="color:red">Usuario o contraseña incorrectos</p>
<% } %>
</body>
</html>
