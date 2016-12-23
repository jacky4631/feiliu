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
    <tags:content_header icon="fa-file-text-o" sysname="${sysname}" title="利润表-按用户"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <div class="col-sm-3">
            <h5>利润表-按用户
              <small>用户利润分析表</small>
            </h5>
          </div>
          <div class="col-sm-3" id="reportrange" style="cursor: pointer;">
            <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
            <span></span><b class="caret"></b>
          </div>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <table class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th>#</th>
                  <th>用户名</th>
                  <th>公司</th>
                  <th>成本（元）</th>
                  <th>销售额（元）</th>
                  <th>利润（元）</th>
                  <th>利润率</th>
                  <th>占比</th>
                </tr>
                </thead>
                <tbody id="content"></tbody>
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
    activeMenu("mb-profituser");

    function loading() {
      $('#content').html('<tr style="text-align:center;"><td colspan="8"><img src="${ctx}/static/img/loading.gif"/></td></tr>');
    }

    function loadContent() {
      $.ajax({
        url: "${ctx}/oc/statis/profit-user-content",
        beforeSend: loading,
        data: {
          "startDate": picker.data('daterangepicker').startDate.format("YYYY-MM-DD"),
          "endDate": picker.data('daterangepicker').endDate.format("YYYY-MM-DD")
        },
        success: function (data) {
          $("#content").html(data);
        }
      });
    }

    loadContent();
    picker.on('apply.daterangepicker', function (ev, picker) {
      loadContent();
    });
  });
</script>
</body>
</html>
