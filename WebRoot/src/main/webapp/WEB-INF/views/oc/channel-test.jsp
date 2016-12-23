<%@ page import="com.jiam365.flow.server.product.FlowPackage, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<div class="col-xs-12">
  <form id="balanceForm" class="form-horizontal">
    <div class="form-group">
      <label class="col-sm-3 control-label" for="testMobile">测试手机号</label>

      <div class="col-sm-9">
        <input type="text" name="testMobile" id="testMobile" value="" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label" for="testMobile">测试可选包</label>

      <div class="col-sm-9">
        <table class="table table-striped table-hover">
          <tr>
            <th></th>
            <th>名称</th>
            <th>产品ID</th>
            <th>供方ID</th>
          </tr>
          <%
            List<FlowPackage> packages = (List<FlowPackage>) request.getAttribute("packages");
            int count = 0;
            if (packages != null) {
              for (FlowPackage flowPackage : packages) {
                count++;
                if (flowPackage.getSize() < 30) {
                  out.println("<tr>");
                  out.println("<td><input type='radio' name='products' value='" +
                    flowPackage.getId() + "'" +
                    (count == 1 ? " checked" : "") + "></td>");
                  out.println("<td>" + flowPackage.getTitle() + "</td>");
                  out.println("<td>" + flowPackage.getProductId() + "</td>");
                  out.println("<td>" + flowPackage.getOrigiProductId() + "</td>");
                  out.println("</tr>");
                }
              }
            }
          %>
        </table>
      </div>
    </div>
    <div class="form-group">
      <label class="col-sm-3 control-label">说明</label>

      <div class="col-sm-9">
        <span>测试的时候, 系统不会对任何数据进行校验判断,即手机号码和充值产品不匹配问题,手机号码合法性问题均
        不作处理,以方便进行故意的失败测试. 同时,测试时,通道的状态是否为启用无关紧要.
        </span>
      </div>
    </div>
  </form>
</div>
