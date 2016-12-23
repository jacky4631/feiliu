<%@ page import="com.jiam365.flow.server.dto.ProductGroup, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<div class="panel-body">
  <div class="ibox float-e-margins" style="margin-bottom: 0;">
    <div class="ibox-title">
      <h5>产品组列表(绿色表示有包开启)&nbsp;&nbsp;
        <div class="btn-group">
          <a title="返回" href="${ctx}/oc/channel" class="btn btn-sm btn-outline btn-info" type="button"><i class="fa fa-chevron-left"></i> </a>
          <button id="btn_add_group" title="增加产品组" class="btn btn-sm btn-outline btn-info" type="button"><i class="fa fa-plus"></i></button>
        </div>

      </h5>
      <div class="ibox-tools">
        <a class="collapse-link">
          <i class="fa fa-chevron-up"></i>
        </a>
      </div>
      <div class="pull-right legend">
        <span><i class="fa fa-lock" aria-hidden="true"></i> 不参与统付替换</span>
        <span><i class="fa fa-get-pocket" aria-hidden="true"></i> 通道保护</span>
        <span><i class="fa fa-usd" aria-hidden="true"></i> 不漫游</span>
        <span><i class="fa fa-delicious" aria-hidden="true"></i> 有地区限制</span>
      </div>
    </div>
    <div class="ibox-content" style="padding: 15px 20px 10px;">
      <%
        List<ProductGroup> productGroups = (List<ProductGroup>) request.getAttribute("productGroups");
        if (productGroups != null) {
          for (ProductGroup group : productGroups) {
      %>
      <div class="btn-group">
        <button class="btn btn-primary <% if(group.getEnabledCount() == 0) out.print("btn-outline"); %> btn-setting" data-group='<% out.print(group.getGroupCode()); %>' type="button">
          <%
            out.println(group.getName() + "&nbsp;" + group.getPackagesCount() + "包&nbsp;");
            out.println("<span style='font-size:13px;'>");
            if(!group.isCanReplaceNA()) out.print(" <i class='fa fa-lock'></i> ");
            if(group.isNeedProtected()) out.print(" <i class='fa fa-get-pocket'></i> ");
            if(group.isHasRestrict()) out.print(" <i class='fa fa-delicious'></i> ");
            out.println("<span>");
          %>
        </button>
        <button data-toggle="dropdown" class="btn btn-primary btn-outline dropdown-toggle" type="button">
          <span class="caret"></span>
          <span class="sr-only">Toggle Dropdown</span>
        </button>
        <ul role="menu" class="dropdown-menu">
          <li><a class="btn-setprice" data-group='<% out.print(group.getGroupCode()); %>'><i class="fa fa-jpy"></i> 统一调价</a></li>
          <li><a class="btn-setp" data-group='<% out.print(group.getGroupCode()); %>'><i class="fa fa-sort-numeric-desc"></i> 统一调优先级</a></li>
          <li><a class="btn-group-enable" data-group='<% out.print(group.getGroupCode()); %>'><i class="fa fa-toggle-on"></i> 全部启用</a></li>
          <li><a class="btn-group-disable" data-group='<% out.print(group.getGroupCode()); %>'><i class="fa fa-toggle-off"></i> 全部禁用</a></li>
          <li><a class="btn-group-del" data-group='<% out.print(group.getGroupCode()); %>'><i class="fa fa-remove"></i> 产品组删除</a></li>
        </ul>
      </div>
      <%
          }
        }
      %>
    </div>
  </div>
  <div id="tab-prd"></div>
</div>