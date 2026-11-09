<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administrador</title>
<link rel="stylesheet" href="/estilos.css">
</head>
<body>
<div class="menu"><a href="/swagger-ui.html">Doc swag</a> | <a href="/logout">Cerrar sesion</a></div>
<h2>Panel del administrador (<%= request.getRemoteUser() %>)</h2>

<h3>Consultas</h3>
<p>Medico <select id="cMedico"></select>
   Consultorio <input id="cConsultorio" type="number"></p>
<p>Paciente (usuario) <input id="cPaciente" maxlength="40"></p>
<p>Motivo <input id="cMotivo" maxlength="100" size="50"></p>
<p>Desde <input id="cInicio" size="6"> hasta <input id="cFin" size="6"></p>
<p><button onclick="guardarConsulta()">Guardar consulta</button> <button onclick="limpiarConsulta()">Cancelar</button></p>
<table>
    <tr><th>Id</th><th>Medico</th><th>Consultorio</th><th>Paciente</th><th>Motivo</th><th>Inicio</th><th>Fin</th><th></th></tr>
    <tbody id="tablaConsultas"></tbody>
</table>

<h3>Medicos</h3>
<p>Nombre <input id="mNombre">
   Usuario <input id="mUsuario">
   <button onclick="guardarMedico()">Guardar medico</button>
   <button onclick="limpiarMedico()">Cancelar</button></p>
<table>
    <tr><th>Id</th><th>Nombre</th><th>Usuario</th><th></th></tr>
    <tbody id="tablaMedicos"></tbody>
</table>

<script>
let medicos = [], consultas = [], idMedico = null, idConsulta = null;

function el(id) { return document.getElementById(id); }

function enviar(url, metodo, datos, ok) {
    fetch(url, { method: metodo, headers: { "Content-Type": "application/json" }, body: datos ? JSON.stringify(datos) : null })
        .then(res => {
            if (res.ok) ok();
            else if (res.status == 400) res.text().then(t => alert("Revise los datos:\n" + t));
            else location = "/sinpermiso.jsp";
        });
}

function cargarMedicos() {
    fetch("/api/medicos").then(r => r.json()).then(lista => {
        medicos = lista;
        let html = "", ops = "<option value=''>-- Seleccione --</option>";
        for (let m of lista) {
            html += "<tr><td>" + m.id + "</td><td>" + m.nombre + "</td><td>" + m.usuario + "</td><td>"
                + "<button onclick='editarMedico(" + m.id + ")'>Editar</button> <button onclick='eliminarMedico(" + m.id + ")'>Eliminar</button></td></tr>";
            ops += "<option value='" + m.id + "'>" + m.nombre + "</option>";
        }
        el("tablaMedicos").innerHTML = html;
        el("cMedico").innerHTML = ops;
    });
}

function guardarMedico() {
    let datos = { nombre: el("mNombre").value, usuario: el("mUsuario").value };
    enviar(idMedico ? "/api/medicos/" + idMedico : "/api/medicos", idMedico ? "PUT" : "POST", datos, () => { limpiarMedico(); cargarMedicos(); cargarConsultas(); });
}

function editarMedico(id) {
    let m = medicos.find(x => x.id == id);
    idMedico = id;
    el("mNombre").value = m.nombre;
    el("mUsuario").value = m.usuario;
}

function eliminarMedico(id) {
    if (confirm("¿Eliminar el medico " + id + "?")) enviar("/api/medicos/" + id, "DELETE", null, cargarMedicos);
}

function limpiarMedico() {
    idMedico = null;
    el("mNombre").value = "";
    el("mUsuario").value = "";
}

function cargarConsultas() {
    fetch("/api/consultas").then(r => r.json()).then(lista => {
        consultas = lista;
        let html = "";
        for (let c of lista) {
            html += "<tr><td>" + c.id + "</td><td>" + c.medico.nombre + "</td><td>" + c.consultorio + "</td><td>" + c.paciente + "</td><td>" + c.motivo
                + "</td><td>" + c.horaInicio + "</td><td>" + c.horaFin + "</td><td>"
                + "<button onclick='editarConsulta(" + c.id + ")'>Editar</button> <button onclick='eliminarConsulta(" + c.id + ")'>Eliminar</button></td></tr>";
        }
        el("tablaConsultas").innerHTML = html;
    });
}

function guardarConsulta() {
    let datos = {
        paciente: el("cPaciente").value,
        motivo: el("cMotivo").value,
        consultorio: el("cConsultorio").value ? Number(el("cConsultorio").value) : null,
        horaInicio: el("cInicio").value,
        horaFin: el("cFin").value,
        medico: el("cMedico").value ? { id: Number(el("cMedico").value) } : null
    };
    enviar(idConsulta ? "/api/consultas/" + idConsulta : "/api/consultas", idConsulta ? "PUT" : "POST", datos, () => { limpiarConsulta(); cargarConsultas(); });
}

function editarConsulta(id) {
    let c = consultas.find(x => x.id == id);
    idConsulta = id;
    el("cPaciente").value = c.paciente;
    el("cMotivo").value = c.motivo;
    el("cConsultorio").value = c.consultorio;
    el("cInicio").value = c.horaInicio;
    el("cFin").value = c.horaFin;
    el("cMedico").value = c.medico.id;
}

function eliminarConsulta(id) {
    if (confirm("¿Eliminar la consulta " + id + "?")) enviar("/api/consultas/" + id, "DELETE", null, cargarConsultas);
}

function limpiarConsulta() {
    idConsulta = null;
    for (let id of ["cPaciente", "cMotivo", "cConsultorio", "cInicio", "cFin", "cMedico"]) el(id).value = "";
}

cargarMedicos();
cargarConsultas();
</script>
</body>
</html>
