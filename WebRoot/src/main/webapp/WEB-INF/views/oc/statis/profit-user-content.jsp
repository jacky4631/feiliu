<%@ page import="com.jiam365.flow.server.dto.TProfitByUser, com.jiam365.flow.server.utils.DoubleUtils, java.text.DecimalFormat, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<TProfitByUser> profitByUsers = (List<TProfitByUser>) request.getAttribute("profitByUsers");
  if (profitByUsers != null) {
    int idx = 0;
    DecimalFormat df = new DecimalFormat("#,##0.00");
    double totalAmount = 0, totalCost = 0, totalProfit = 0;
    for (TProfitByUser profit : profitByUsers) {
      totalAmount = DoubleUtils.add(totalAmount, profit.getAmount());
      totalCost = DoubleUtils.add(totalCost, profit.getCost());
      totalProfit = DoubleUtils.add(totalProfit, profit.getProfit());
    }

    for (TProfitByUser profit : profitByUsers) {
      out.println("<tr>");
      out.println("<td>" + ++idx + "</td>");
      out.println("<td>" + profit.getDisplayName() + "(" + profit.getUsername() + ")</td>");
      out.println("<td>" + profit.getCompany() + "</td>");
      String cost = df.format(profit.getCost());
      out.println("<td>" + cost + "</td>");

      String amount = df.format(profit.getAmount());
      out.println("<td>" + amount + "</td>");

      String sProfit = df.format(profit.getProfit());
      out.println("<td>" + sProfit + "</td>");

      double profitRatio = 0;
      if (profit.getAmount() > 0) {
        profitRatio = profit.getProfit() / profit.getAmount();
      }
      out.println("<td>" + DoubleUtils.asPercent(profitRatio, 2) + "</td>");

      double percent = 0;
      if (totalAmount > 0) {
        percent = profit.getProfit() / totalProfit;
      }
      out.println("<td>" + DoubleUtils.asPercent(percent, 2) + "</td>");

      out.println("</tr>");
    }

    out.println("<tr style='font-weight:bold;'>");
    out.println("<td>" + ++idx + "</td>");
    out.println("<td colspan='2'>合计</td>");

    out.println("<td>" + df.format(totalCost) + "</td>");
    out.println("<td>" + df.format(totalAmount) + "</td>");
    out.println("<td>" + df.format(totalProfit) + "</td>");
    double totalRatio = totalCost > 0 ? totalProfit / totalCost : 0;
    out.println("<td>" + DoubleUtils.asPercent(totalRatio, 2) + "</td>");
    out.println("<td>100%</td>");
    out.println("</tr>");
  }
%>