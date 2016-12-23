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
  <%@ include file="/WEB-INF/include/agent/nav.jsp" %>
  <div id="page-wrapper" class="gray-bg">
    <%@ include file="/WEB-INF/include/agent/banner.jsp" %>
    <tags:content_header icon="fa-rmb" sysname="${sysname}" title="加扣款记录"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox">
        <div class="ibox-content">
          <table id="dt1" class="table table-striped table-hover">
            <thead>
            <tr>
              <th>操作时间</th>
              <th>到款金额</th>
              <th>当时余额</th>
              <th>备注</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/include/agent/footer.jsp" %>
  </div>
</div>
<%@ include file="/WEB-INF/include/http-script.jsp" %>
<script src="${cdn}/js/plugins/dataTables/jquery.dataTables.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.bootstrap.js"></script>
<script src="${cdn}/js/plugins/dataTables/dataTables.responsive.js"></script>
<script type="text/javascript">
  $(function () {
    $("#m-utransfer").addClass("active");
    $.fn.dataTable.ext.legacy.ajax = true;
    var dt = $('#dt1').DataTable({
      "autoWidth": false,
      "processing": true,
      "serverSide": true,
      "ajax": {
        "url": $ctx + "/agent/usertransfer/data",
        "method": "post",
        "data": function (d) {
          return $.extend({}, d, {
          });
        }
      },
      "columns": [
        {"data": "operateTime"},
        {"data": "amount"},
        {"data": "balance", sortable: false},
        {"data": "remark", sortable: false}
      ],
      "order": [0, "desc"],
      "columnDefs": [

      ],
      "filter": false,
      "language": {
        "url": $cdn + "/js/plugins/dataTables/i18n/Chinese.json"
      }
    });
  });
</script>
</body>
</html>
