<%-- 
    Document   : Inicio
    Created on : 14-oct-2020, 13:08:03
    Author     : isra9
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <link rel="stylesheet" type="text/css" href="./Estilos/Estilo1.css">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="./Cabecera.jsp"/>
        <h1>Hello USER!</h1>
         <form name="form" action="../Controlador.jsp" method="POST">
             <input type="submit" name="iniciarChat" id="iniciarChat" value="CHAT">
            <button type="submit" name="Return" class="btn btn-dark w-100">Volver</button>
        </form>
    </body>
</html>
