<%@ page import="com.jiam365.flow.oc.dto.TChannleProduct, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%
  List<TChannleProduct> channleProductList = (List<TChannleProduct>) request.getAttribute("channleProductList");
  for (TChannleProduct cp : channleProductList) {
    out.println("<span id='" + cp.getId() + "' class='tag'><span>" +
      cp.getName() + "</span> <a title='删除' class='channel-del' href='#'>x</a></span>");
  }
%>