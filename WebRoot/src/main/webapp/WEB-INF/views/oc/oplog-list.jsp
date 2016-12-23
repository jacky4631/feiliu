<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/datepicker/datepicker3.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-file-o" sysname="${sysname}" title="操作日志"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">操作人</div>
              <input type="text" class="form-control input-sm" id="filter_EQ_username" placeholder="操作人">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <input type="text" class="form-control input-sm" name="filter_GED_created" id="filter_GED_created" placeholder="起始时间...">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <input type="text" class="form-control input-sm" name="filter_LED_created" id="filter_LED_created" placeholder="结束时间...">
            </div>
          </div>
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">内容</div>
              <input type="text" class="form-control input-sm" style="width:200px;" id="filter_LIKE_description" placeholder="内容(模糊查询)">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary btn-sm">查询</button>
            <button type="button" id="reset" class="btn btn-primary btn-sm">全部</button>
          </div>
        </form>
      </div>
      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th style="width:180px;">时间</th>
              <th style="width:120px;">操作人</th>
              <th>说明</th>
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

<%@ include file="/WEB-INF/include/oc/script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script src="${cdn}/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="${cdn}/js/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript">
  $(function () {
    activeMenu("mc-oplog");
    $.fn.dataTable.ext.legacy.ajax = true;

    $('#filter_GED_created, #filter_LED_created').datepicker({
      todayBtn: "linked",
      keyboardNavigation: false,
      forceParse: false,
      autoclose: true,
      language: 'zh-CN',
      format: "yyyy-mm-dd"
    });

    var dt = $('#dt1').addClass('nowrap').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "stateSave": true,
      "ajax": {
        "url": "${ctx}/oc/oplog/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_username": $("#filter_EQ_username").val(),
            "filter_LIKE_description": $("#filter_LIKE_description").val(),
            "filter_GED_created": $("#filter_GED_created").val(),
            "filter_LED_created": $("#filter_LED_created").val()
          });
        }
      },
      "order": [0, "desc"],
      "columns": [
        {"data": "created"},
        {"data": "username", sortable: false},
        {"data": "description", sortable: false},
      ],
      "filter": false,

      "columnDefs": [],
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      $("#filter_EQ_accountingSubject").find("option:first").prop("selected", 'selected');
      dt.draw();
    });

  })
</script>
</body>
</html>
