<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/artDialog/ui-dialog.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-user" sysname="${sysname}" title="账户列表"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <c:if test="${not empty message}">
          <div id="message" class="alert alert-success">
            <button data-dismiss="alert" class="close">×</button>
              ${message}</div>
        </c:if>
        <div class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">用户名</div>
              <input type="text" class="form-control" style="width:120px;" id="filter_EQ_username" placeholder="输入用户名">
            </div>
            <div class="input-group">
              <div class="input-group-addon">显示名</div>
              <input type="text" class="form-control" style="width:120px;" id="filter_EQ_displayName" placeholder="输入显示名">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
          <div class="btn-group" role="group">
            <shiro:hasRole name="admin">
              <a class="btn btn-info" href="${ctx}/oc/users/create">注册</a>
            </shiro:hasRole>
            <button type="button" id="btn_passwd" class="btn btn-info">改密</button>
            <button type="button" id="btn_disable" class="btn btn-info">禁用</button>
            <button type="button" id="btn_enable" class="btn btn-info">启用</button>
            <shiro:hasRole name="admin">
              <button type="button" id="btn_remove" class="btn btn-info">删除</button>
            </shiro:hasRole>
          </div>
        </div>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width:25px;padding-right:0;">选</th>
              <th>显示名</th>
              <th>用户名</th>
              <th>公司名</th>
              <th>余额</th>
              <th>授信</th>
              <th>联系人</th>
              <th>手机</th>
              <th style="width:30px; padding-right: 0;">状态</th>
              <shiro:hasAnyRoles name="admin,financial">
                <th style="width:115px; padding-right: 0;">操作</th>
              </shiro:hasAnyRoles>
              <shiro:hasAnyRoles name="operator">
                <th style="width:55px; padding-right: 0;">操作</th>
              </shiro:hasAnyRoles>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/oc/footer.jsp" %>
  </div>
</div>
<div id="dlg" style="height:160px;width:400px;display:none;"></div>
<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-min.js"></script>
<script src="${cdn}/js/plugins/artDialog/dialog-plus-min.js"></script>
<script src="${cdn}/js/plugins/form/jquery.form.js"></script>
<script src="${cdn}/js/plugins/toastr/toastr.min.js"></script>
<script src="${ctx}/static/js/oc/user-list.js"></script>
<script>
  $(function () {
    activeMenu("mc-user");
    $.fn.dataTable.ext.legacy.ajax = true;
    dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "responsive": true,
      "stateSave": true,
      "ajax": {
        "url": $ctx + "/oc/users/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_userType": 1,
            "filter_EQ_username": $("#filter_EQ_username").val(),
            "filter_EQ_displayName": $("#filter_EQ_displayName").val()
          });
        }
      },
      "columns": [
        {"data": "id"},
        {"data": "displayName"},
        {"data": "username"},
        {"data": "company"},
        {"data": "balance"},
        {"data": "creditLine"},
        {"data": "linkman"},
        {"data": "mobile"},
        {"data": "status"},
        {"data": "id", sortable: false}
      ],
      "order": [2, "asc"],
      "filter": false,
      "columnDefs": [
        {
          "render": function (data, type, row) {
            return "<input name='ids' type='checkbox' value='" + data + "'>";
          },
          "targets": 0
        },
        {
          "render": function (data, type, row) {
            return "<a href='" + $ctx + "/oc/users/update/" + row.id + "'>" + data + "</a>";
          },
          targets: 1
        },
        {
          orderable: false,
          targets: [0, 1, 3, 4, 5, 6, 7, 8]
        },
        {
          "render": function (data, type, row) {
            if (data == '0')
              return "<small class='label label-primary'>正常</small>";
            else
              return "<small class='label'>禁用</small>";
          },
          "targets": 8
        },
        {
          "render": function (data, type, row) {
            return '<div class="btn-group">' +
              "<a type='button' class='btn-white btn btn-xs' href='" + $ctx + "/oc/users/update/" + data + "'> 编辑 </a>"
              <shiro:hasAnyRoles name="admin,financial">
              +
              "<input type='button' class='btn-white btn btn-xs' value='加款' onclick='setBalance(" + row.id + ");'>" +
              "<input type='button' class='btn-white btn btn-xs' value='授信' onclick='setCredit(" + row.id + ");'>"
              </shiro:hasAnyRoles>
              +
              "</div>";
          },
          "targets": 9
        },
        {targets: [4, 5], className: 'dt-body-right'}
      ],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    })
  });
</script>
</body>
</html>
