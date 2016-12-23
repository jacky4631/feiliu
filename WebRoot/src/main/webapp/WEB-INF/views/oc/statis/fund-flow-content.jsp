<%@ page import="com.jiam365.flow.server.utils.DoubleUtils, java.text.DecimalFormat, java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  Map<Integer, Double> totalTransfer = (Map<Integer, Double>) request.getAttribute("totalTransfer");
  if (totalTransfer != null) {
    int idx = 0;
    double v0 = 0;
    double v1 = 0;
    for (Map.Entry entry : totalTransfer.entrySet()) {
      out.println("<tr>");
      Integer key = (Integer) entry.getKey();
      Double total = (Double) entry.getValue();
      if (key.equals(0)) {
        v0 = total;
      } else {
        v1 = total;
      }
      out.println("<td>" + ++idx + "</td>");
      out.println("<td>" + (key.equals(0) ? "收款" : "付款") + "</td>");
      DecimalFormat df = new DecimalFormat("#,##0.00");
      String sTotal = df.format(total);
      out.println("<td>" + sTotal + "</td>");
      out.println("</tr>");
    }

    out.println("<tr style='font-weight:bold;'>");
    out.println("<td>" + ++idx + "</td>");
    out.println("<td>收支盈余</td>");
    DecimalFormat df = new DecimalFormat("#,##0.00");
    String sBalance = df.format(DoubleUtils.sub(v0, v1));
    out.println("<td>" + sBalance + "</td>");
    out.println("</tr>");
  }
%>