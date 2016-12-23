<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <link href="${cdn}/css/plugins/dataTables/dataTables.bootstrap.css" rel="stylesheet">
  <link href="${cdn}/css/plugins/dataTables/dataTables.responsive.css" rel="stylesheet">
  <%@ include file="/WEB-INF/include/oc/header.jsp" %>
</head>
<body>
<div>
  <%@ include file="/WEB-INF/include/oc/nav-admin.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/oc/banner.jsp" %>
    <tags:content_header icon="fa-flag" sysname="${sysname}" title="地区定义"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox-content m-b-sm border-bottom">
        <form class="form-inline">
          <div class="form-group">
            <div class="input-group">
              <div class="input-group-addon">地区代码</div>
              <input type="text" class="form-control" id="filter_EQ_code" placeholder="地区代码">
            </div>
            <div class="input-group">
              <div class="input-group-addon">地区名称</div>
              <input type="text" class="form-control" id="filter_EQ_name" placeholder="地区名称">
            </div>
          </div>
          <div class="btn-group" role="group">
            <button type="button" id="search" class="btn btn-primary">查询</button>
            <button type="button" id="reset" class="btn btn-primary">全部</button>
          </div>
        </form>
      </div>

      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>地区名</th>
              <th>地区代码</th>
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
<script type="text/javascript">
  $(function () {
    activeMenu("mc-dic-state");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "ajax": {
        "url": "${ctx}/oc/state/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
            "filter_EQ_name": $("#filter_EQ_name").val(),
            "filter_EQ_code": $("#filter_EQ_code").val()
          });
        }
      },
      "columns": [
        {"data": "name"},
        {"data": "code"}
      ],
      "filter": false,
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });

    $("#search").on('click', function () {
      dt.draw();
    });
    $("#reset").on('click', function () {
      $("input[id^='filter_']").val("");
      dt.draw();
    });
  });
</script>
</body>
</html>
