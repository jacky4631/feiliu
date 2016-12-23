<%@ page import="com.jiam365.flow.server.channel.FlowChannel, com.jiam365.flow.server.dto.ProductGroup, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%!
  String td(String content) {
    return "<td>" + content + "</td>";
  }
%>
<div>
  <%
    FlowChannel channel = (FlowChannel) request.getAttribute("channel");
    out.println("<input type='hidden' id='curChannelName' value='" + channel.getName() + "'>");
    out.println("<input type='hidden' id='curChannelId' value='" + channel.getId() + "'>");
    out.println("<button type='button' class='btn btn-primary btn-sm btn-outline btn-addall'>" +
      "整通道加入组, 注意此方式加入不包含受保护通道, 如果需要, 请单独加入以授权保护通道可用</button>");
  %>
</div>
<table class="table table-striped">
  <thead>
  <tr>
    <th style="width:50px;">操作</th>
    <th>名称</th>
    <th>漫游</th>
    <th>保护</th>
    <th>统付替换</th>
    <th>包</th>
  </tr>
  </thead>
  <tbody id="channel-products-list">
  <%
    List<ProductGroup> productGroups = (List<ProductGroup>) request.getAttribute("productGroups");
    if (productGroups != null && productGroups.size() > 0) {
      for (ProductGroup group : productGroups) {
        out.println("<tr>");
        out.println(td("<button class='btn-primary btn btn-xs btn-add' data-code='" +
          group.getGroupCode() + "' data-name='" + group.getName() + "'>加入</button>"));
        out.println(td(group.getName()));
        out.println(td(group.isRoamable() ? "可漫游" : "不漫游"));
        out.println(td(group.isNeedProtected() ? "保护" : "开放"));
        String canReplace;
        if (group.isNA()) {
          canReplace = "全网包";
        } else if (group.isCanReplaceNA()) {
          canReplace = "参与";
        } else {
          canReplace = "不参与";
        }
        out.println(td(canReplace));
        out.println(td(group.getPackagesCount() + "包"));
        out.println("</tr>");
      }
    } else {
      out.println("<tr>");
      out.println("<td colspan=6>通道下暂无具体产品</td>");
      out.println("</tr>");
    }
  %>
  </tbody>
</table>