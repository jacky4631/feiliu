<%@ page import="com.jiam365.flow.oc.dto.UserInfo, com.jiam365.flow.server.utils.DoubleUtils, java.text.DecimalFormat, java.util.List, com.jiam365.flow.server.channel.FlowChannel" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
  <link href="${cdn}/css/plugins/daterangepicker/daterangepicker.css" rel="stylesheet">
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-statis.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-file-text-o" sysname="${sysname}" title="供应商/通道余额一览表"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <div class="col-sm-3">
            <h5>供应商/通道余额一览表
            </h5>
          </div>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <table class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th>#</th>
                  <th>通道名称</th>
                  <th>通道编号</th>
                  <th>机构代码</th>
                  <th>余额（元）</th>
                </tr>
                </thead>
                <tbody id="content">
                <%
                  List<FlowChannel> flowChannelList = (List<FlowChannel>) request.getAttribute("flowChannelList");
                  if (flowChannelList != null) {
                    int idx = 0;
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    double total = 0;
                    for (FlowChannel channel : flowChannelList) {
                      out.println("<tr>");
                      out.println("<td>" + ++idx + "</td>");
                      out.println("<td>" + channel.getName() + "</td>");
                      out.println("<td>" + channel.getId() + "</td>");
                      out.println("<td>" + channel.getOrgcode() + "</td>");
                      String balance = df.format(channel.getBalance());
                      out.println("<td>" + balance + "</td>");
                      out.println("</tr>");

                      total = DoubleUtils.add(total, channel.getBalance());
                    }

                    out.println("<tr style='font-weight:bold;'>");
                    out.println("<td>" + ++idx + "</td>");
                    out.println("<td style='text-align:center;' colspan='3'>合计余额</td>");

                    out.println("<td>" + df.format(total) + "</td>");
                    out.println("</tr>");
                  }
                %>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>


    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/daterangepicker/moment.min.js"></script>
<script src="${cdn}/js/plugins/daterangepicker/daterangepicker.js"></script>
<script src="${ctx}/static/js/daterangehelp.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("mb-balancechannels");
  });
</script>
</body>
</html>
