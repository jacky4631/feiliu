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
    <tags:content_header icon="fa-file-text-o" sysname="${sysname}" title="平均回调时间分析"/>

    <div class="wrapper wrapper-content animated fadeInRight">
      <div class="ibox float-e-margins">
        <div class="ibox-title">
          <div class="col-sm-2">
            <h5>平均回调时间分析</h5>
          </div>

          <div class="col-sm-2">
            <select name="channelId" id="channelId" class="form-control" style="font-size:12px; height:22px; padding: 2px 10px;">
              <c:forEach var="channel" items="${channels}">
                <option value="${channel.id}">${channel.name}</option>
              </c:forEach>
            </select>
          </div>

          <div class="col-sm-2">
            <select name="provider" id="provider" class="form-control" style="font-size:12px; height:22px; padding: 2px 10px;">
              <option value="CMCC">中国移动</option>
              <option value="UNICOM">中国联通</option>
              <option value="TELECOM">中国电信</option>
            </select>
          </div>

          <div class="col-sm-2">
            <select name="recentMinutes" id="recentMinutes" class="form-control" style="font-size:12px; height:22px; padding: 2px 10px;">
              <option value="5">最近5分钟</option>
              <option value="10">最近10分钟</option>
              <option value="30">最近30分钟</option>
              <option value="60">最近1小时</option>
              <option value="120">最近2小时</option>
              <option value="360" selected="selected">最近6小时</option>
              <option value="720">最近12小时</option>
              <option value="1440">最近1天</option>
              <option value="2880">最近2天</option>
            </select>
          </div>

          <div class="col-sm-1">
            <button type="button" id="refresh" class="btn btn-info btn-sm" style="padding:0 18px;">开始分析</button>
          </div>
        </div>
        <div class="ibox-content">
          <div class="row">
            <div class="col-xs-12">
              <table class="table table-bordered table-striped">
                <thead>
                <tr>
                  <th>#</th>
                  <th>运营商</th>
                  <th>地区</th>
                  <th>平均回调耗时(秒)</th>
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
<script type="text/javascript">
  $(function () {
    activeMenu("mb-timeconsuming");

    function loading() {
      $('#content').html('<tr style="text-align:center;"><td colspan="4"><img src="${ctx}/static/img/loading.gif"/></td></tr>');
    }

    function loadContent() {
      var provider = $("#provider").val();
      var recentMinutes = $("#recentMinutes").val();
      var channelId = $("#channelId").val();
      $.ajax({
        url: "${ctx}/oc/statis/timeconsuming-state-content",
        beforeSend: loading,
        data: {
          "provider": provider,
          "channelId": channelId,
          "recentMinutes": recentMinutes
        },
        success: function (data) {
          $("#content").html(data);
        }
      });
    }

    $("#refresh").on('click', function () {
      loadContent();
    });

  });
</script>
</body>
</html>
