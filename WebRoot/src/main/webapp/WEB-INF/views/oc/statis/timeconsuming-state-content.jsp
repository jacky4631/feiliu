<%@ page import="com.jiam365.flow.server.dto.TChannelProductTimeConsuming, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<TChannelProductTimeConsuming> consumings = (List<TChannelProductTimeConsuming>) request.getAttribute("timeConsumings");
  if (consumings != null) {
    int idx = 0;
    for (TChannelProductTimeConsuming consuming : consumings) {
      out.println("<tr>");
      out.println("<td>" + ++idx + "</td>");
      out.println("<td>" + consuming.getTelco().getName() + "</td>");
      out.println("<td>" + consuming.getStateName() + "</td>");
      out.println("<td>" + consuming.getTimeConsuming() + "</td>");
      out.println("</tr>");
    }
  }
%>