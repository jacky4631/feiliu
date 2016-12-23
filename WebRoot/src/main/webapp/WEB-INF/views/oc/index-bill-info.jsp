<%@ page import="com.jiam365.flow.oc.dto.UserBillInfo, com.jiam365.flow.server.utils.DoubleUtils, java.text.DecimalFormat, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  List<UserBillInfo> userBillInfoList = (List<UserBillInfo>) request.getAttribute("userBillInfoList");
  String sAmount = "";
  double amount = 0.0;
  int idx = 0;
  for (UserBillInfo info : userBillInfoList) {
    out.println("<tr>");
    out.println("<td>" + ++idx + "</td>");
    out.println("<td>" + info.getDisplayName() + "</td>");
    out.println("<td>" + info.getUsername() + "</td>");
    out.println("<td>" + info.getCompany() + "</td>");
    out.println("<td>" + info.getLinkman() + "</td>");
    out.println("<td>" + info.getBillAmount() + "</td>");
    out.println("<td>" + info.getBalance() + "</td>");
    out.println("</tr>");
    amount = DoubleUtils.add(info.getBillAmount(), amount);
    DecimalFormat df = new DecimalFormat("#,##0.00");
    sAmount = df.format(amount);
  }
  out.println("<script>$('#total').val('" + sAmount + "');</script>");
%>

