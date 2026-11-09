<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Medico</title>
<link rel="stylesheet" href="/estilos.css">
</head>
<body>
<div class="menu"><a href="/logout">Cerrar sesion</a></div>
<h2>Consultas asignadas a <%= request.getRemoteUser() %></h2>

<table>
    <tr><th>Id</th><th>Inicio</th><th>Fin</th><th>Consultorio</th><th>Paciente</th><th>Motivo</th></tr>
    <tbody id="tabla"></tbody>
</table>

<script>
fetch("/api/consultas").then(res => {
    if (!res.ok) { location = "/sinpermiso.jsp"; return; }
    res.json().then(lista => {
        let html = "";
        for (let c of lista) {
            html += "<tr><td>" + c.id + "</td><td>" + c.horaInicio + "</td><td>" + c.horaFin + "</td><td>" + c.consultorio
                + "</td><td>" + c.paciente + "</td><td>" + c.motivo + "</td></tr>";
        }
        document.getElementById("tabla").innerHTML = html || "<tr><td colspan='6'>No tiene consultas asignadas</td></tr>";
    });
});
</script>
</body>
</html>
