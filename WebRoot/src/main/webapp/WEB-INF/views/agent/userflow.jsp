<%@ page import="com.jiam365.flow.agent.dto.TProductStatus" %>
<%@ page import="com.jiam365.flow.agent.dto.TFlowProduct" %>
<%@ page import="com.jiam365.modules.telco.Telco, java.io.IOException, java.util.List, java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<%!
  void showTFlowpackage(JspWriter out, List<TFlowProduct> flowProducts) throws IOException {
    if (flowProducts.size() > 0) {
      for (TFlowProduct tFlowProduct : flowProducts) {
        out.println("<tr><td class='text-navy'>" +
          tFlowProduct.getId() + "</td><td>" +
          tFlowProduct.getName() + "</td><td>" + "￥" +
          tFlowProduct.getPrice() + "</td><td>" +
          tFlowProduct.getDiscountPercent() + "</td></tr>");
      }
    } else {
      out.println("<tr><td colspan='4'>&nbsp;</td></tr>");
    }
  }
%>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/agent/header.jsp" %>
  <link href="${cdn}/css/plugins/footable/footable.core.css" rel="stylesheet">
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-laptop" sysname="${sysname}" title="产品与折扣"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="row">
        <div class="col-md-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5><i class="fa fa-star-o"></i> 中国移动</h5>
            </div>
            <div class="ibox-content">
              <input type="text" class="form-control input-sm m-b-xs" id="filter" placeholder="搜索产品">
              <table class="footable table" data-page-size="12" data-filter=#filter>
                <thead>
                <tr>
                  <th>产品ID</th>
                  <th>产品名</th>
                  <th>价格</th>
                  <th>折扣</th>
                </tr>
                </thead>
                <tbody>
                <%
                  Map<Telco, TProductStatus> overviews = (Map<Telco, TProductStatus>) request.getAttribute("overviews");
                  showTFlowpackage(out, overviews.get(Telco.CMCC).getPackages());
                  showTFlowpackage(out, overviews.get(Telco.CMCC).getStatePackages());
                %>
                </tbody>
                <tfoot>
                <tr>
                  <td colspan="4">
                    <ul class="pagination pull-right"></ul>
                  </td>
                </tr>
                </tfoot>
              </table>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5><i class="fa fa-star-o"></i> 中国电信</h5>
            </div>
            <div class="ibox-content">
              <input type="text" class="form-control input-sm m-b-xs" id="filter2" placeholder="搜索产品">
              <table class="footable table" data-page-size="12" data-filter=#filter2>
                <thead>
                <tr>
                  <th>产品ID</th>
                  <th>产品名</th>
                  <th>价格</th>
                  <th>折扣</th>
                </tr>
                </thead>
                <tbody>
                <%
                  showTFlowpackage(out, overviews.get(Telco.TELECOM).getPackages());
                  showTFlowpackage(out, overviews.get(Telco.TELECOM).getStatePackages());
                %>
                </tbody>
                <tfoot>
                <tr>
                  <td colspan="4">
                    <ul class="pagination pull-right"></ul>
                  </td>
                </tr>
                </tfoot>
              </table>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="ibox float-e-margins">
            <div class="ibox-title">
              <h5><i class="fa fa-star-o"></i> 中国联通</h5>
            </div>
            <div class="ibox-content">
              <input type="text" class="form-control input-sm m-b-xs" id="filter3" placeholder="搜索产品">
              <table class="footable table" data-page-size="12" data-filter=#filter3>
                <thead>
                <tr>
                  <th>产品ID</th>
                  <th>产品名</th>
                  <th>价格</th>
                  <th>折扣</th>
                </tr>
                </thead>
                <tbody>
                <%
                  showTFlowpackage(out, overviews.get(Telco.UNICOM).getPackages());
                  showTFlowpackage(out, overviews.get(Telco.UNICOM).getStatePackages());
                %>
                </tbody>
                <tfoot>
                <tr>
                  <td colspan="4">
                    <ul class="pagination pull-right"></ul>
                  </td>
                </tr>
                </tfoot>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/footable/footable.all.min.js"></script>
<script type="text/javascript">
  $(function () {
    $("#m-flowpacks").addClass("active");
    $('.footable').footable();
  });
</script>
</body>
</html>
