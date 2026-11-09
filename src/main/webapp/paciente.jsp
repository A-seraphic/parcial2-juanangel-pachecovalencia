<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Paciente</title>
<link rel="stylesheet" href="/estilos.css">
</head>
<body>
<div class="menu"><a href="/logout">Cerrar sesion</a></div>
<h2>Citas de <%= request.getRemoteUser() %></h2>

<table>
    <tr><th>Id</th><th>Medico</th><th>Consultorio</th><th>Motivo</th><th>Inicio</th><th>Fin</th><th></th></tr>
    <tbody id="tabla"></tbody>
</table>

<h3>Cambiar horario</h3>
<p>Cita: <span id="cita">seleccione una cita de la tabla</span></p>
<p><label><input type="checkbox" id="habilitar" onchange="habilitar()" disabled> Habilitar cambio de horario</label></p>
<p>Desde <input id="inicio" size="6" disabled>
   hasta <input id="fin" size="6" disabled>
   <button id="guardar" onclick="guardar()" disabled>Guardar</button></p>

<script>
let lista = [], actual = null;

function el(id) { return document.getElementById(id); }

function cargar() {
    fetch("/api/consultas").then(res => {
        if (!res.ok) { location = "/sinpermiso.jsp"; return; }
        res.json().then(datos => {
            lista = datos;
            let html = "";
            for (let c of lista) {
                html += "<tr><td>" + c.id + "</td><td>" + c.medico.nombre + "</td><td>" + c.consultorio + "</td><td>" + c.motivo
                    + "</td><td>" + c.horaInicio + "</td><td>" + c.horaFin + "</td><td><button onclick='elegir(" + c.id + ")'>Editar</button></td></tr>";
            }
            el("tabla").innerHTML = html || "<tr><td colspan='7'>No tiene citas</td></tr>";
        });
    });
}

function elegir(id) {
    actual = lista.find(c => c.id == id);
    el("cita").innerText = "#" + actual.id + " " + actual.motivo + " (" + actual.horaInicio + " - " + actual.horaFin + ")";
    el("inicio").value = actual.horaInicio;
    el("fin").value = actual.horaFin;
    el("habilitar").disabled = false;
    el("habilitar").checked = false;
    habilitar();
}

// los campos solo se pueden editar con la casilla marcada
function habilitar() {
    let si = el("habilitar").checked;
    el("inicio").disabled = !si;
    el("fin").disabled = !si;
    el("guardar").disabled = !si;
}

function guardar() {
    let datos = { horaInicio: el("inicio").value, horaFin: el("fin").value };
    fetch("/api/consultas/" + actual.id + "/horario", {
        method: "PUT", headers: { "Content-Type": "application/json" }, body: JSON.stringify(datos)
    }).then(res => {
        if (res.ok) { alert("Horario actualizado"); el("habilitar").checked = false; habilitar(); cargar(); }
        else if (res.status == 400) res.text().then(t => alert("Revise los datos:\n" + t));
        else location = "/sinpermiso.jsp";
    });
}

cargar();
</script>
</body>
</html>
